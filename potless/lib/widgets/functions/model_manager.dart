import 'dart:io';
import 'dart:typed_data';
import 'package:tflite_flutter/tflite_flutter.dart';
import 'package:image/image.dart' as img;

class ModelManager {
  late Interpreter interpreter;

  Future<void> loadModel() async {
    interpreter = await Interpreter.fromAsset('assets/ai_model/train34.tflite');
    print('Model loaded successfully');
  }

  Future<List<dynamic>?> runInference(Uint8List imageBytes) async {
    try {
      print('model_manager 16: running interference');
      var inputSize = 416;
      var input = img.decodeImage(imageBytes);
      input = img.copyResize(input!, width: inputSize, height: inputSize);
      print('model_manager 20: resizing');

      List inputList = input.getBytes();
      List outputList = List.filled(1 * 2, 0)
          .reshape([1, 2]); // Adjust based on your model output

      interpreter.run(inputList, outputList);
      return outputList;
    } catch (E) {
      print('29 model : $E');
    }
    return null;
  }

  void dispose() {
    interpreter.close();
  }
}
