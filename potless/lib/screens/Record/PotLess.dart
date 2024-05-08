import 'dart:async';
import 'dart:io';

import 'package:camera/camera.dart'; // 카메라 플러그인
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:geolocator/geolocator.dart';
import 'package:potless/API/api_request.dart';
import 'package:potless/main.dart';
import 'package:potless/models/detected.dart';
import 'package:potless/widgets/functions/frame_rate.dart';
import 'package:potless/widgets/functions/geolocator.dart';
import 'package:potless/widgets/tflite/bbox.dart';
import 'package:potless/widgets/tflite/detector.dart';
import 'package:potless/widgets/tflite/screen.dart';
import 'package:potless/widgets/UI/AppBar.dart';
import 'package:potless/widgets/UI/ScreenSize.dart';

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
  int? resultIndex;
  List<String> classes = [];
  List<List<double>> bboxes = [];
  List<double> scores = [];
  List<XFile> imageSaveQueue = [];

  final ApiService _apiService = ApiService();
  final FrameRateTester _frameRateTester = FrameRateTester();
  bool _isTestingFrameRate = false;
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
            classes = values['cls'];
            bboxes = values['box'];
            scores = values['conf'];
          });
          _handleDetectionResults();
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
      setState(() {
        ScreenParams.previewSize = _controller.value.previewSize!;
      });
    } catch (e) {
      debugPrint('Error initializing camera: $e');
    }
  }

  int frameCounter = 0;
  int skipFactor = 5;

  void onLatestImageAvailable(CameraImage cameraImage) async {
    if (_isTestingFrameRate) {
      _frameRateTester.countFrame();
    }
    if (frameCounter % (skipFactor + 1) == 0) {
      _detector?.processFrame(cameraImage);
    }
    frameCounter++;

    if (frameCounter > 1000) {
      frameCounter = 0;
    }
  }

  void _handleDetectionResults() {
    for (int i = 0; i < scores.length; i++) {
      if (scores[i] > 0.75) {
        debugPrint('Detection with high confidence, capturing photo...');
        capturePhoto();
        break;
      }
    }
  }

  Future<void> capturePhoto() async {
    if (_cameraController != null && _cameraController!.value.isInitialized) {
      if (!_cameraController!.value.isTakingPicture) {
        try {
          XFile picture = await _cameraController!.takePicture();
          Position position = await Geolocator.getCurrentPosition(
              desiredAccuracy: LocationAccuracy.high);
          _uploadStreamController.add(QueuedImage(picture, position));
          imageTaken = imageTaken + 1;
        } catch (e) {
          debugPrint("Error capturing image: $e");
        }
      }
    }
    await Future.delayed(
      const Duration(milliseconds: 500),
    );
  }

  Future<void> _uploadImage(QueuedImage queuedImage) async {
    try {
      File file = File(queuedImage.imageFile.path);
      double lat = queuedImage.position.latitude;
      double lng = queuedImage.position.longitude;

      bool success = await _apiService.damageSet('POTHOLE', lng, lat, file);

      if (success) {
        debugPrint('potless 207 업로드 올라감');
      } else {
        debugPrint('potless 209 업로드 실패');
      }
    } catch (e) {
      debugPrint('potless 212 에러 $e');
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
    _uploadStreamController.close();
    WidgetsBinding.instance.removeObserver(this);
    _cameraController?.dispose();
    _detector?.stop();
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
          ] else ...[
            Container(
              color: const Color(0xffffffff),
              height: UIhelper.deviceHeight(context),
              width: UIhelper.deviceWidth(context),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.end,
                children: [
                  const Text('AI 촬영의 성능을 위해 카메라 화면을 꺼두는 모드입니다.'),
                  SizedBox(
                    height: UIhelper.deviceHeight(context) * 0.3,
                  ),
                ],
              ),
            ),
          ],
          Column(
            mainAxisAlignment: MainAxisAlignment.end,
            children: [
              FloatingActionButton(
                onPressed: () {
                  setState(() {
                    _isTestingFrameRate = !_isTestingFrameRate;
                    if (_isTestingFrameRate) {
                      _frameRateTester
                          .startTesting(() => onLatestImageAvailable);
                    } else {
                      _frameRateTester.stopTesting();
                    }
                  });
                },
                child:
                    Icon(_isTestingFrameRate ? Icons.stop : Icons.play_arrow),
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
                        '저장 중: ${imageSaveQueue.length}',
                        style: const TextStyle(
                            color: Colors.white, fontWeight: FontWeight.bold),
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

  void togglePreview() {
    setState(() {
      _previewOn = !_previewOn;
    });
  }
}
