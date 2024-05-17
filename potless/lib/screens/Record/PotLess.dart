import 'dart:async';
import 'dart:io';
import 'dart:typed_data';

import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:path_provider/path_provider.dart';
import 'package:potless/API/api_request.dart';
import 'package:potless/main.dart';
import 'package:potless/models/detected.dart';
import 'package:potless/widgets/functions/frame_rate.dart';
import 'package:potless/widgets/tflite/bbox.dart';
import 'package:potless/widgets/tflite/screen.dart';
import 'package:potless/widgets/UI/AppBar.dart';
import 'package:potless/widgets/UI/ScreenSize.dart';
import 'package:potless/widgets/tflite/detector.dart';

class VideoPage extends StatefulWidget {
  const VideoPage({super.key});

  @override
  _VideoPageState createState() => _VideoPageState();
}

class _VideoPageState extends State<VideoPage> with WidgetsBindingObserver {
  CameraController? _cameraController;
  get _controller => _cameraController;
  Detector? _detector;
  StreamSubscription? _subscription;
  final CameraLensDirection initialCameraLensDirection =
      CameraLensDirection.back;

  bool _previewOn = false;
  int imageTaken = 0;
  double _currentZoomLevel = 1.5;
  double _minZoomLevel = 1.0;
  double _maxZoomLevel = 1.5;

  int? resultIndex;
  List<String> classes = [];
  List<List<double>> bboxes = [];
  List<double> scores = [];
  List<XFile> imageSaveQueue = [];

  Map<String, dynamic>? stats;
  String? cls;
  String? box;
  String? score;

  final ApiService _apiService = ApiService();
  final FrameRateTester _frameRateTester = FrameRateTester();
  final bool _isTestingFrameRate = false;
  final StreamController<QueuedImage> _uploadStreamController =
      StreamController<QueuedImage>();

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) async {
    switch (state) {
      case AppLifecycleState.inactive:
        _cameraController?.stopImageStream();
        _detector?.stop();
        _subscription?.cancel();
        break;
      case AppLifecycleState.resumed:
        _initStateAsync();
        break;
      default:
    }
  }

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);
    _initStateAsync();
    _uploadStreamController.stream.listen(_uploadImage);
  }

  void _initStateAsync() async {
    _initializeCamera();
    Detector.start().then((instance) {
      setState(() {
        _detector = instance;
        _subscription = instance.resultsStream.stream.listen((values) {
          setState(() {
            if (values.containsKey('jpegImage')) {
              Uint8List imageData = values['jpegImage'];
              _handleDetectedImage(imageData);
            }
            classes = values['cls'];
            bboxes = values['box'];
            scores = values['conf'];

            stats = values['stats'];
          });
        });
      });
    });
  }

  // 카메라 초기화
  Future<void> _initializeCamera() async {
    _cameraController = CameraController(
      cameras[0],
      ResolutionPreset.medium,
      enableAudio: false,
    );

    try {
      await _cameraController!.initialize();
      await _cameraController!.setFlashMode(FlashMode.off);
      await _controller.startImageStream(onLatestImageAvailable);
      _maxZoomLevel = await _cameraController!.getMaxZoomLevel();
      _minZoomLevel = await _cameraController!.getMinZoomLevel();
      await _setZoomLevel(_currentZoomLevel);
      setState(() {
        ScreenParams.previewSize = _controller.value.previewSize!;
      });
    } catch (e) {
      debugPrint('Error initializing camera: $e');
    }
  }

  Future<void> _setZoomLevel(double zoomLevel) async {
    if (_cameraController != null && _cameraController!.value.isInitialized) {
      final double maxZoom = await _cameraController!.getMaxZoomLevel();
      final double minZoom = await _cameraController!.getMinZoomLevel();

      if (zoomLevel >= minZoom && zoomLevel <= maxZoom) {
        await _cameraController!.setZoomLevel(zoomLevel);
      } else {
        debugPrint('Zoom level out of bounds: $zoomLevel');
      }
    }
  }

  int frameCounter = 0;
  int skipFactor = 9;

  void onLatestImageAvailable(CameraImage cameraImage) async {
    // debugPrint('125: FPS 측정좀');
    _frameRateTester.countFrame();
    if (frameCounter % (skipFactor + 1) == 0) {
      _detector?.processFrame(cameraImage);
    }
    frameCounter++;

    if (frameCounter > 1000) {
      // debugPrint('125: FPS 초기화');

      frameCounter = 0;
    }
  }

  Future<void> _handleDetectedImage(Uint8List imageData) async {
    String directory = (await getApplicationDocumentsDirectory()).path;
    String fileName = '${DateTime.now().millisecondsSinceEpoch}.jpg';
    File imageFile = File('$directory/$fileName');
    await imageFile.writeAsBytes(imageData);

    Position position = await Geolocator.getCurrentPosition(
        desiredAccuracy: LocationAccuracy.best);
    try {
      _uploadStreamController.add(QueuedImage(imageFile, position));
      imageTaken = imageTaken + 1;
    } catch (E) {
      debugPrint('Potless 146: $E');
    }
  }

  Future<void> _uploadImage(QueuedImage queuedImage) async {
    try {
      File file = File(queuedImage.imageFile.path);
      double lat = queuedImage.position.latitude;
      double lng = queuedImage.position.longitude;

      bool success = await _apiService.damageSet('POTHOLE', lng, lat, file);
    } catch (e) {
      debugPrint('potless 212: $e');
    }
  }

  Widget _boundingBoxes() {
    List<Bbox> bboxesWidgets = [];
    for (int i = 0; i < bboxes.length; i++) {
      bboxesWidgets.add(
        Bbox(
          box: bboxes[i],
          name: classes[i],
          score: scores[i],
        ),
      );
    }
    return Stack(children: bboxesWidgets);
  }

  @override
  void dispose() {
    _detector?.stop();
    WidgetsBinding.instance.removeObserver(this);
    _frameRateTester.stopTesting();
    _uploadStreamController.close();
    _cameraController?.dispose();
    _subscription?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    if (_cameraController == null || !_controller.value.isInitialized) {
      return const SizedBox.shrink();
    }
    var aspect = 1 / _controller.value.aspectRatio;
    return Scaffold(
      appBar: const CustomAppBar(
        title: '주행모드',
      ),
      extendBodyBehindAppBar: true,
      extendBody: true,
      body: Stack(
        fit: StackFit.expand,
        children: [
          if (_previewOn) ...[
            AspectRatio(
              aspectRatio: aspect,
              child: CameraPreview(_controller),
            ),
            AspectRatio(
              aspectRatio: aspect,
              child: _boundingBoxes(),
            ),
            Positioned(
              right: 10,
              top: UIhelper.deviceHeight(context) * 0.4,
              child: RotatedBox(
                quarterTurns: 3,
                child: Slider(
                  value: _currentZoomLevel,
                  min: _minZoomLevel,
                  max: _maxZoomLevel,
                  onChanged: (value) {
                    setState(() {
                      _currentZoomLevel = value;
                    });
                    _setZoomLevel(value);
                  },
                ),
              ),
            )
          ] else ...[
            Container(
              color: const Color(0xffffffff),
              height: UIhelper.deviceHeight(context),
              width: UIhelper.deviceWidth(context),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.end,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text('AI 촬영의 성능을 위해 카메라 화면을 꺼두는 모드입니다.'),
                  if (stats != null) ...[
                    Text('프레임 당 탐지 시간 : ${stats!['Total prediction time:']}'),
                  ],
                  SizedBox(height: UIhelper.deviceHeight(context) * 0.1),
                  const Text('촬영개수 : 탐지 이후 실제로 서버에 업로드를 요청한 횟수입니다'),
                  const Text('아이콘 : 촬영되고 있는 화면을 끄고 키는 버튼입니다'),
                  const Text('프레임 : 현재 프레임 값에 도달할 때마다'),
                  const Text('하나의 프레임을 ai 모델에 넣습니다'),
                  const Text('영상 스트리밍 시 평균적으로 30 의 FPS 값을 갖고 있습니다.'),
                  const Text('따라서, 프레임 값이 10일 때, 초당 3개의 프레임을 ai 모델에 삽입합니다.'),
                  SizedBox(height: UIhelper.deviceHeight(context) * 0.2),
                ],
              ),
            ),
          ],
          Column(
            mainAxisAlignment: MainAxisAlignment.end,
            children: [
              Container(
                color: const Color(0xff151c62),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.end,
                  children: [
                    ElevatedButton(
                      onPressed: incFrame,
                      style: ButtonStyle(
                        backgroundColor: MaterialStateProperty.all(
                          const Color(0xffFFFFFF),
                        ),
                      ),
                      child: const Text('+'),
                    ),
                    ElevatedButton(
                      onPressed: decFrame,
                      style: ButtonStyle(
                        backgroundColor: MaterialStateProperty.all(
                          const Color(0xffFFFFFF),
                        ),
                      ),
                      child: const Text('-'),
                    ),
                  ],
                ),
              ),
              Container(
                color: const Color(0xff151c62),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Container(
                      padding: const EdgeInsets.all(8),
                      height: UIhelper.deviceWidth(context) * 0.1,
                      child: Text(
                        '촬영 개수: ${imageTaken.toString()}',
                        style: const TextStyle(
                          color: Color(0xffffffff),
                        ),
                      ),
                    ),
                    SizedBox(
                      width: UIhelper.deviceWidth(context) * 0.1,
                      height: UIhelper.deviceWidth(context) * 0.1,
                      child: IconButton(
                        icon: Icon(
                          _previewOn ? Icons.visibility_off : Icons.visibility,
                          color: const Color(0xffffffff),
                        ),
                        onPressed: togglePreview,
                      ),
                    ),
                    Container(
                      padding: const EdgeInsets.all(8),
                      decoration: BoxDecoration(
                        color: Colors.black54,
                        borderRadius: BorderRadius.circular(10),
                      ),
                      child: Text(
                        '프레임: ${skipFactor + 1}',
                        style: const TextStyle(
                          color: Colors.white,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }

  void incFrame() {
    setState(() {
      skipFactor = skipFactor + 5;
    });
  }

  void decFrame() {
    setState(() {
      skipFactor = skipFactor - 5;
    });
  }

  void togglePreview() {
    setState(() {
      _previewOn = !_previewOn;
    });
  }
}
