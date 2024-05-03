import 'dart:async';
import 'dart:io';

import 'package:camera/camera.dart'; // 카메라 플러그인
import 'package:external_path/external_path.dart';
import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:http/http.dart';
import 'package:intl/intl.dart';
import 'package:porthole24/API/api_request.dart';
import 'package:porthole24/main.dart';
import 'package:porthole24/screens/Record/ImagePreview.dart';
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
  bool _isCapturing = false;
  final ApiService _apiService = ApiService();

  List<String> classes = [];
  List<List<double>> bboxes = [];
  List<double> scores = [];

  List<XFile> imageSaveQueue = [];
  bool _isSaving = false;

  int? resultIndex;

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

  Future<void> createTodayFolder() async {
    String formattedDate = DateFormat('yyyy-MM-dd').format(DateTime.now());
    final String baseDir = await ExternalPath.getExternalStoragePublicDirectory(
        ExternalPath.DIRECTORY_DCIM);
    final Directory todayDir = Directory('$baseDir/$formattedDate');

    debugPrint(todayDir.path);
    if (!await todayDir.exists()) {
      await todayDir.create(recursive: true);
      debugPrint("폴더 생성함 55: ${todayDir.path}");
    } else {
      debugPrint("폴더 이미 있음 57: ${todayDir.path}");
    }
  }

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);
    _initStateAsync();
    createTodayFolder();
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

  void _handleDetectionResults() {
    for (int i = 0; i < scores.length; i++) {
      if (scores[i] > 0.75) {
        capturePhoto();
        break;
      }
    }
  }

  // 카메라 초기화
  Future<void> _initializeCamera() async {
    _cameraController = CameraController(
      cameras[0],
      ResolutionPreset.max,
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

  Future<void> capturePhoto() async {
    while (true) {
      if (_cameraController != null && _cameraController!.value.isInitialized) {
        if (!_cameraController!.value.isTakingPicture) {
          try {
            XFile picture = await _cameraController!.takePicture();
            savePhoto(picture);
          } catch (e) {
            debugPrint("Error capturing image: $e");
          }
        }
      }
      await Future.delayed(
        const Duration(milliseconds: 100),
      );
    }
  }

  Future<void> savePhoto(XFile picture) async {
    imageSaveQueue.add(picture);
    if (!_isSaving) {
      _processImageQueue();
    }
  }

  Future<void> _processImageQueue() async {
    _isSaving = true;
    while (imageSaveQueue.isNotEmpty) {
      final XFile imageToSave = imageSaveQueue.removeAt(0);
      await _uploadImage(imageToSave);
    }
    _isSaving = false;
  }

  Future<void> _uploadImage(XFile image) async {
    Position position = await Geolocator.getCurrentPosition(
        desiredAccuracy: LocationAccuracy.high);
    double lat = position.latitude;
    double lng = position.longitude;

    setState(() {
      _isCapturing = true;
    });

    debugPrint(lat.toString());
    debugPrint(lng.toString());

    try {
      File file = File(image.path);

      bool success = await _apiService.damageSet('POTHOLE', lng, lat, file);

      if (success) {
        debugPrint('potless 207 업로드 올라감');
      } else {
        debugPrint('potless 209 업로드 실패');
      }
    } catch (e) {
      debugPrint('potless 212 에러 $e');
    } finally {
      setState(() {
        _isCapturing = false;
      });
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
    WidgetsBinding.instance.removeObserver(this);
    _cameraController?.dispose();
    _detector?.stop();
    _subscription?.cancel();
    super.dispose();
  }

  // UI 구성
  @override
  Widget build(BuildContext context) {
    if (_cameraController == null || !_controller.value.isInitialized) {
      return const SizedBox.shrink(); // 카메라가 초기화되지 않은 경우 빈 위젯 반환
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
            child: CameraPreview(_controller), // 카메라 미리보기
          ),
          AspectRatio(
            aspectRatio: aspect,
            child: _boundingBoxes(), // 경계 상자 위젯
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
                        ? const CircularProgressIndicator() // Shown when capturing
                        : const Icon(Icons
                            .camera_alt), // Empty container or any other widget when not capturing
                  ),
                  Container(
                    padding: const EdgeInsets.all(20),
                    child: FloatingActionButton(
                      heroTag: "openFolderButton",
                      onPressed: () {
                        Navigator.of(context).push(
                          MaterialPageRoute(
                            builder: (context) => const ImagePreviewScreen(),
                          ),
                        );
                      },
                      child: const Icon(Icons.folder_open),
                    ),
                  ),
                  Container(
                    padding: const EdgeInsets.all(8),
                    decoration: BoxDecoration(
                      color: Colors.black54, // Semi-transparent black
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
