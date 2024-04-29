import 'dart:async';
import 'dart:io';

import 'package:camera/camera.dart'; // 카메라 플러그인
import 'package:external_path/external_path.dart';
import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:intl/intl.dart';
import 'package:porthole24/main.dart';
import 'package:porthole24/screens/Record/ImagePreview.dart';
import 'package:porthole24/widgets/functions/geolocator.dart';
import 'package:porthole24/widgets/tflite/bbox.dart';
import 'package:porthole24/widgets/tflite/detector.dart';
import 'package:porthole24/widgets/tflite/screen.dart';
import 'package:porthole24/widgets/UI/AppBar.dart';
import 'package:porthole24/widgets/UI/ScreenSize.dart'; // Flutter의 머티리얼 디자인 위젯

// YOLO 객체 감지 페이지를 위한 StatefulWidget
class VideoPage extends StatefulWidget {
  const VideoPage({super.key});

  @override
  _VideoPageState createState() => _VideoPageState();
}

// YoloPage의 상태 관리 클래스
class _VideoPageState extends State<VideoPage> with WidgetsBindingObserver {
  CameraController? _cameraController; // 카메라 컨트롤러
  get _controller => _cameraController; // 초기화되었을 때만 사용됨, null이 아님
  Detector? _detector; // 객체 감지기
  StreamSubscription? _subscription; // 객체 감지 결과 스트림의 구독
  final CameraLensDirection initialCameraLensDirection =
      CameraLensDirection.back; // 초기 카메라 렌즈 방향
  bool _isCapturing = false;

  List<String> classes = []; // 감지된 객체의 클래스
  List<List<double>> bboxes = []; // 감지된 객체의 경계 상자 좌표
  List<double> scores = []; // 감지된 객체의 점수

  List<XFile> imageSaveQueue = [];
  bool _isSaving = false;

  int? resultIndex;

  // 앱 생명주기 상태 변경 시 호출
  @override
  void didChangeAppLifecycleState(AppLifecycleState state) async {
    switch (state) {
      case AppLifecycleState.inactive:
        // 앱이 비활성 상태가 되면 리소스 해제
        _cameraController?.stopImageStream();
        _detector?.stop();
        _subscription?.cancel();
        break;
      case AppLifecycleState.resumed:
        // 앱이 다시 활성화되면 초기화
        _initStateAsync();
        break;
      default:
    }
  }

  Future<void> createTodayFolder() async {
    String formattedDate =
        DateFormat('yyyy-MM-dd').format(DateTime.now()); // Format today's date
    final String baseDir = await ExternalPath.getExternalStoragePublicDirectory(
        ExternalPath.DIRECTORY_DCIM);
    final Directory todayDir = Directory('$baseDir/$formattedDate');

    debugPrint(todayDir.path);
    if (!await todayDir.exists()) {
      await todayDir.create(
          recursive: true); // Create the directory if it doesn't exist
      debugPrint("폴더 생성함 55: ${todayDir.path}");
    } else {
      debugPrint("폴더 이미 있음 57: ${todayDir.path}");
    }
  }

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this); // 앱 생명주기 이벤트를 관찰하기 위해 등록
    _initStateAsync(); // 비동기 초기화 메서드 호출
    createTodayFolder();
  }

  // 카메라와 객체 감지기 초기화
  void _initStateAsync() async {
    _initializeCamera(); // 카메라 초기화
    // 새로운 isolate에서 객체 감지기 시작
    Detector.start().then((instance) {
      setState(() {
        _detector = instance;
        // 결과 스트림을 구독하여 상태를 업데이트
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
        await _controller
            .startImageStream(onLatestImageAvailable); // 카메라 이미지 스트림 시작
        setState(() {});
        ScreenParams.previewSize =
            _controller.value.previewSize!; // 화면 매개변수 업데이트
      });
  }

  // 새 카메라 이미지가 사용 가능할 때 호출되는 메서드
  void onLatestImageAvailable(CameraImage cameraImage) async {
    _detector?.processFrame(cameraImage); // 이미지를 객체 감지기로 전달하여 처리
  }

  // Future<void> capturePhoto() async {
  //   if (_cameraController != null &&
  //       _cameraController!.value.isInitialized &&
  //       !_cameraController!.value.isTakingPicture) {
  //     try {
  //       XFile picture = await _cameraController!.takePicture();
  //       savePhoto(picture); // Handle saving asynchronously
  //     } catch (e) {
  //       debugPrint("Error capturing image: $e");
  //     }
  //   }
  // }

  Future<void> capturePhoto() async {
    // Continuously attempt to capture without checking the save queue
    while (true) {
      if (_cameraController != null && _cameraController!.value.isInitialized) {
        if (!_cameraController!.value.isTakingPicture) {
          try {
            XFile picture = await _cameraController!.takePicture();
            savePhoto(
                picture); // Offload to queue and immediately ready for next capture
          } catch (e) {
            debugPrint("Error capturing image: $e");
          }
        }
      }
      await Future.delayed(const Duration(
          milliseconds: 100)); // Small delay to prevent hogging the main thread
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
      await _saveImageToFileSystem(imageToSave);
    }
    _isSaving = false;
  }

  Future<void> _saveImageToFileSystem(XFile image) async {
    String formattedDate =
        DateFormat('yyyy-MM-dd').format(DateTime.now()); // Format today's date
    String formattedTime =
        DateFormat('yyyy-MM-dd_ahhmm').format(DateTime.now());

    final String baseDir = await ExternalPath.getExternalStoragePublicDirectory(
        ExternalPath.DIRECTORY_DCIM);
    debugPrint('233 코드 제발 좀 뜨자 $baseDir');
    final String dirPath = '$baseDir/$formattedDate';
    final String filePath = '$dirPath/$formattedTime.jpg';

    setState(() {
      _isCapturing = true;
    });

    try {
      await Directory(dirPath).create(recursive: true);
      await image.saveTo(filePath);
      debugPrint("Photo saved to $filePath");
    } catch (e) {
      debugPrint("Error saving photo: $e");
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
