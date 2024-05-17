import 'dart:async';
import 'dart:io';
import 'dart:isolate';
import 'package:camera/camera.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:image/image.dart' as image_lib;
import 'package:potless/widgets/tflite/image.dart';
import 'package:potless/widgets/tflite/nms.dart';
import 'package:tflite_flutter/tflite_flutter.dart';

enum _Codes { init, busy, ready, detect, result }

class _Command {
  const _Command(this.code, {this.args});

  final _Codes code;
  final List<Object>? args;
}

class Detector {
  static const String _modelPath = 'assets/ai_model/12_v8m_224.tflite';
  static const String _labelPath = 'assets/ai_model/train34.txt';

  Detector._(this._isolate, this._interpreter, this._labels);

  final Isolate _isolate;
  late final Interpreter _interpreter;
  late final List<String> _labels;

  late final SendPort _sendPort;

  bool _isReady = false;

  final StreamController<Map<String, dynamic>> resultsStream =
      StreamController<Map<String, dynamic>>();

  static Future<Detector> start() async {
    final ReceivePort receivePort = ReceivePort();
    final Isolate isolate =
        await Isolate.spawn(_DetectorServer._run, receivePort.sendPort);

    final Detector result = Detector._(
      isolate,
      await _loadModel(),
      await _loadLabels(),
    );
    receivePort.listen((message) {
      result._handleCommand(message as _Command);
    });
    return result;
  }

  static Future<Interpreter> _loadModel() async {
    final interpreterOptions = InterpreterOptions()..threads = 4;
    interpreterOptions.addDelegate(XNNPackDelegate());
    return Interpreter.fromAsset(
      _modelPath,
      options: interpreterOptions,
    );
  }

  static Future<List<String>> _loadLabels() async {
    return (await rootBundle.loadString(_labelPath)).split('\n');
  }

  void processFrame(CameraImage cameraImage) {
    try {
      if (_isReady) {
        _sendPort.send(_Command(_Codes.detect, args: [cameraImage]));
      }
    } catch (E) {
      print('detector 80: $E');
    }
  }

  void _handleCommand(_Command command) {
    switch (command.code) {
      case _Codes.init:
        _sendPort = command.args?[0] as SendPort;
        RootIsolateToken rootIsolateToken = RootIsolateToken.instance!;
        _sendPort.send(_Command(_Codes.init,
            args: [rootIsolateToken, _interpreter.address, _labels]));
        break;
      case _Codes.ready:
        _isReady = true;
        break;
      case _Codes.busy:
        _isReady = false;
        break;
      case _Codes.result:
        _isReady = true;
        resultsStream.add(command.args?[0] as Map<String, dynamic>);
        break;
      default:
        debugPrint('Detector unrecognized command: ${command.code}');
    }
  }

  void stop() {
    _isolate.kill();
  }
}

class _DetectorServer {
  int mlModelInputSize = 224;
  Interpreter? _interpreter;
  List<String>? _labels;

  _DetectorServer(this._sendPort);

  final SendPort _sendPort;

  static void _run(SendPort sendPort) {
    ReceivePort receivePort = ReceivePort();
    final _DetectorServer server = _DetectorServer(sendPort);
    receivePort.listen((message) async {
      final _Command command = message as _Command;
      await server._handleCommand(command);
    });

    sendPort.send(_Command(_Codes.init, args: [receivePort.sendPort]));
  }

  Future<void> _handleCommand(_Command command) async {
    switch (command.code) {
      case _Codes.init:
        RootIsolateToken rootIsolateToken =
            command.args?[0] as RootIsolateToken;
        BackgroundIsolateBinaryMessenger.ensureInitialized(rootIsolateToken);
        _interpreter = Interpreter.fromAddress(command.args?[1] as int);

        mlModelInputSize =
            _interpreter?.getInputTensors().first.shape[1] ?? 224;
        _labels = command.args?[2] as List<String>;
        _sendPort.send(const _Command(_Codes.ready));
      case _Codes.detect:
        _sendPort.send(const _Command(_Codes.busy));
        _convertCameraImage(command.args?[0] as CameraImage);
      default:
        debugPrint('_DetectorService unrecognized command ${command.code}');
    }
  }

  void _convertCameraImage(CameraImage cameraImage) {
    var preConversionTime = DateTime.now().millisecondsSinceEpoch;

    convertCameraImageToImage(cameraImage).then((image) {
      if (image != null) {
        if (Platform.isAndroid) {
          image = image_lib.copyRotate(image, angle: 90);
        }

        final results = analyseImage(image, preConversionTime);
        if (results['conf'].any((double c) => c > 0.20)) {
          Uint8List jpegImage = image_lib.encodeJpg(image, quality: 60);
          _sendPort.send(
            _Command(
              _Codes.result,
              args: [
                results,
                jpegImage,
              ],
            ),
          ); // Send both image and results
        } else {
          _sendPort.send(_Command(_Codes.result, args: [results]));
        }
      }
    });
  }

  Map<String, dynamic> analyseImage(
    image_lib.Image? image,
    int preConversionTime,
  ) {
    try {
      var conversionElapsedTime =
          DateTime.now().millisecondsSinceEpoch - preConversionTime;
      var preProcessStart = DateTime.now().millisecondsSinceEpoch;

      final imageInput = image_lib.copyResize(image!,
          width: mlModelInputSize, height: mlModelInputSize);

      final imageMatrix = List.generate(
        imageInput.height,
        (y) => List.generate(
          imageInput.width,
          (x) {
            final pixel = imageInput.getPixel(x, y);
            return [pixel.rNormalized, pixel.gNormalized, pixel.bNormalized];
          },
        ),
      );

      var preProcessElapsedTime =
          DateTime.now().millisecondsSinceEpoch - preProcessStart;
      var inferenceTimeStart = DateTime.now().millisecondsSinceEpoch;

      final output = runInference(imageMatrix);

      List<List<double>> rawOutput =
          (output.first as List).first as List<List<double>>;

      List<int> idx = [];
      List<String> cls = [];
      List<List<double>> box = [];
      List<double> conf = [];
      // 라벨 수 적용
      final numOfLabels = _labels?.length ?? 0;
      final count = numOfLabels + 4;
      (idx, box, conf) =
          nms(rawOutput, count, confidenceThreshold: 0.20, iouThreshold: 0.4);
      if (idx.isNotEmpty) {
        cls = idx.map((e) => _labels![e]).toList();
      }

      var inferenceElapsedTime =
          DateTime.now().millisecondsSinceEpoch - inferenceTimeStart;
      var totalElapsedTime =
          DateTime.now().millisecondsSinceEpoch - preConversionTime;

      bool hasHighConfidenceDetection = conf.any((double c) => c > 0.20);

      if (hasHighConfidenceDetection) {
        // Convert to JPEG format
        Uint8List jpegImage =
            image_lib.encodeJpg(image, quality: 85); // Adjust quality as needed
        return {
          "jpegImage": jpegImage,
          "cls": cls,
          "box": box,
          "conf": conf,
          "stats": {
            'Conversion time:': conversionElapsedTime.toString(),
            'Pre-processing time:': preProcessElapsedTime.toString(),
            'Inference time:': inferenceElapsedTime.toString(),
            'Total prediction time:': totalElapsedTime.toString(),
            'Frame': '${image.width} X ${image.height}',
          }
        };
      } else {
        return {
          "cls": cls,
          "box": box,
          "conf": conf,
          "stats": {
            'Conversion time:': conversionElapsedTime.toString(),
            'Pre-processing time:': preProcessElapsedTime.toString(),
            'Inference time:': inferenceElapsedTime.toString(),
            'Total prediction time:': totalElapsedTime.toString(),
            'Frame': '${image.width} X ${image.height}',
          }
        };
      }
    } catch (E, stackTrace) {
      debugPrint('detector.dart 270 : $E');
      debugPrint('detector.dart 271  stackTrace: $stackTrace');
      return {
        "error": E.toString(),
        "stackTrace": stackTrace.toString(),
        "stats": {
          'Error detail': 'An error occurred during the image analysis process.'
        }
      };
    }
  }

  List<Object> runInference(List<List<List<num>>> imageMatrix) {
    final input = [imageMatrix];
    final numOut = _interpreter?.getOutputTensors().first.shape[0] ?? 1;
    final numOut1 = _interpreter?.getOutputTensors().first.shape[1] ?? 1;
    final numOut2 = _interpreter?.getOutputTensors().first.shape[2] ?? 1;
    final outputs = List<num>.filled(numOut * numOut1 * numOut2, 0)
        .reshape([numOut, numOut1, numOut2]);
    var map = <int, Object>{};
    map[0] = outputs;
    _interpreter!.runForMultipleInputs([input], map);

    return map.values.toList();
  }
}
