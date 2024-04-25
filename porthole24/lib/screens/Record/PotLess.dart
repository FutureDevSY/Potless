import 'dart:async';
import 'dart:io';
import 'dart:math';
import 'package:camera/camera.dart'; // 카메라 플러그인
import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/widgets.dart';
import 'package:geolocator/geolocator.dart';
import 'package:porthole24/main.dart';
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

  List<String> classes = []; // 감지된 객체의 클래스
  List<List<double>> bboxes = []; // 감지된 객체의 경계 상자 좌표
  List<double> scores = []; // 감지된 객체의 점수

  int? resultIndex;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this); // 앱 생명주기 이벤트를 관찰하기 위해 등록
    _initStateAsync(); // 비동기 초기화 메서드 호출
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
        // Assuming 0.8 is the confidence threshold
        capturePhoto();
        break; // Avoid multiple captures per detection event
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

  // 새 카메라 이미지가 사용 가능할 때 호출되는 메서드
  void onLatestImageAvailable(CameraImage cameraImage) async {
    _detector?.processFrame(cameraImage); // 이미지를 객체 감지기로 전달하여 처리
  }

  Future<void> capturePhoto() async {
    if (!_cameraController!.value.isInitialized) {
      debugPrint("Controller is not initialized.");
      return;
    }

    if (_cameraController!.value.isTakingPicture) {
      debugPrint("Capture is already in progress.");
      return;
    }

    try {
      final XFile picture = await _cameraController!.takePicture();
      const String dirPath = 'storage/emulated/0/DCIM/Videos';
      await Directory(dirPath).create(recursive: true);
      final String filePath =
          '$dirPath/${DateTime.now().millisecondsSinceEpoch}.jpg';

      Position position = await getCurrentLocation();
      debugPrint('${position.latitude}, ${position.longitude}');

      await picture.saveTo(filePath);
      debugPrint("Picture saved to $filePath");
    } catch (e) {
      debugPrint("Error capturing image: $e");
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
        ],
      ),
    );
  }
}

// // 사진을 표시하는 화면
// class DisplayPictureScreen extends StatelessWidget {
//   final String imagePath;
//   final int? resultIndex;

//   const DisplayPictureScreen({
//     super.key,
//     required this.imagePath,
//     this.resultIndex,
//   });

//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//       appBar: AppBar(
//         centerTitle: true,
//         title: const Text('촬영한 사진 보기'),
//       ),
//       body: Center(
//         child: Column(
//           children: [
//             const SizedBox(
//               height: 125,
//             ),
//             // 이미지 회전 전에
//             Transform.rotate(
//               angle: pi / 2,
//               child: SizedBox(
//                 height: 300,
//                 child: Image.network(
//                   imagePath,
//                   fit: BoxFit.fill,
//                 ),
//               ),
//             ),
//             const SizedBox(
//               height: 100,
//             ),
//             const Text('뒤로가기 버튼을 눌러 추가 촬영을 진행하거나'),
//             const Text('분석 버튼을 눌러 촬영한 사진의 분석 결과를 확인하세요.'),
//           ],
//         ),
//       ),
//     );
//   }
// }
