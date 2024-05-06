import 'dart:async';
import 'dart:io';

import 'package:camera/camera.dart'; // 카메라 플러그인
import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:porthole24/API/api_request.dart';
import 'package:porthole24/main.dart';
import 'package:porthole24/models/detected.dart';
import 'package:porthole24/widgets/functions/geolocator.dart';
import 'package:porthole24/widgets/tflite/bbox.dart';
import 'package:porthole24/widgets/tflite/detector.dart';
import 'package:porthole24/widgets/tflite/screen.dart';
import 'package:porthole24/widgets/UI/AppBar.dart';
import 'package:porthole24/widgets/UI/ScreenSize.dart';

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
  final bool _isCapturing = false;
  final ApiService _apiService = ApiService();

  List<String> classes = [];
  List<List<double>> bboxes = [];
  List<double> scores = [];

  List<XFile> imageSaveQueue = [];

  int? resultIndex;

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

// 사진을 로컬에 저장 하기 위해 사용하던 코드
// 자동으로 오늘 날짜에 찍힌 사진을 저장할 폴더를 생성한다.
  // Future<void> createTodayFolder() async {
  //   String formattedDate = DateFormat('yyyy-MM-dd').format(DateTime.now());
  //   final String baseDir = await ExternalPath.getExternalStoragePublicDirectory(
  //       ExternalPath.DIRECTORY_DCIM);
  //   final Directory todayDir = Directory('$baseDir/$formattedDate');

  //   debugPrint(todayDir.path);
  //   if (!await todayDir.exists()) {
  //     await todayDir.create(recursive: true);
  //     debugPrint("폴더 생성함 55: ${todayDir.path}");
  //   } else {
  //     debugPrint("폴더 이미 있음 57: ${todayDir.path}");
  //   }
  // }

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);
    _initStateAsync();
    _uploadStreamController.stream.listen(_uploadImage);
    // createTodayFolder();
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
      ResolutionPreset.high,
      enableAudio: false,
    )..initialize().then((_) async {
        await _controller.startImageStream(onLatestImageAvailable);
        setState(() {});
        ScreenParams.previewSize = _controller.value.previewSize!;
      });
  }

  void onLatestImageAvailable(CameraImage cameraImage) async {
    _detector?.processFrame(cameraImage);
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

  // 감지된 객체 주위에 경계 상자를 그리는 위젯
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
    return Stack(children: bboxesWidgets); // 경계 상자 위젯을 스택으로 묶어 반환
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
        title: '하이',
      ),
      extendBodyBehindAppBar: true,
      extendBody: true,
      body: Stack(
        fit: StackFit.expand,
        children: [
          AspectRatio(
            aspectRatio: aspect,
            child: CameraPreview(_controller),
          ),
          AspectRatio(
            aspectRatio: aspect,
            child: _boundingBoxes(),
          ),
          Column(
            mainAxisAlignment: MainAxisAlignment.end,
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Container(
                    color: Colors.white,
                    width: UIhelper.deviceWidth(context) * 0.1,
                    height: UIhelper.deviceWidth(context) * 0.1,
                    child: _isCapturing
                        ? const CircularProgressIndicator()
                        : const Icon(Icons.camera_alt),
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
            ],
          ),
        ],
      ),
    );
  }
}
