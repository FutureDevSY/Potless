import 'dart:async';
import 'dart:io';
import 'dart:isolate';
import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:intl/intl.dart';
import 'package:potless/main.dart';
import 'package:potless/screens/Record/VideoPreviewScreen.dart';
import 'package:flutter/services.dart';
import 'package:potless/widgets/UI/AppBar.dart';
import 'package:potless/widgets/functions/model_manager.dart';
import 'package:potless/widgets/tflite/screen.dart';
import 'package:video_player/video_player.dart';

class CameraScreen extends StatefulWidget {
  const CameraScreen({super.key});
  @override
  State<CameraScreen> createState() => _CameraScreenState();
}

class _CameraScreenState extends State<CameraScreen> {
  late CameraController _cameraController;
  get _controller => _cameraController;
  List<XFile> photoQueue = [];
  int captureInterval = 1;
  Timer? _timer;
  Timer? _captureTimer;
  final bool _isRecordingInProgress = false;
  String? videoDirectoryPath;
  final bool _continuousRecording = false;
  bool _isPreviewOn = false;

  late CameraImage cameraImage;
  var cameraCount = 0;

  @override
  void initState() {
    super.initState();
    _initializeCamera();
  }

  void toggleCameraPreview() {
    setState(() {
      _isPreviewOn = !_isPreviewOn;
    });
  }

  Future<void> _initializeCamera() async {
    _cameraController = CameraController(
      cameras[0],
      ResolutionPreset.medium,
      enableAudio: false,
    );

    try {
      await _cameraController.initialize();
      await _cameraController.setFlashMode(FlashMode.off);
      await _controller.startImageStream(onLatestImageAvailable);
      setState(() {
        ScreenParams.previewSize = _controller.value.previewSize!;
      });
      prepareVideoDirectory();
    } catch (e) {
      debugPrint('Error initializing camera: $e');
    }
  }

  Future<void> prepareVideoDirectory() async {
    final String today = DateFormat('yy-MM-dd').format(DateTime.now());
    final Directory videosDir2 = Directory('/storage/emulated/0/DCIM/$today');
    if (await videosDir2.exists()) {
      debugPrint(videosDir2.path);
    } else {
      debugPrint(videosDir2.path);
      await videosDir2.create(recursive: true);
    }
    videoDirectoryPath = videosDir2.path;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: const CustomAppBar(
        title: '주행모드',
      ),
      body: Stack(
        children: [
          _isPreviewOn
              ? SizedBox(
                  height: double.infinity, child: CameraPreview(_controller))
              : const SizedBox.expand(),
          Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                Text(photoQueue.length.toString()),
                Row(
                  children: [
                    Container(
                      padding: const EdgeInsets.all(20),
                      child: FloatingActionButton(
                        heroTag: "togglePreviewButton",
                        onPressed: toggleCameraPreview,
                        child: Icon(_isPreviewOn
                            ? Icons.visibility_off
                            : Icons.visibility),
                      ),
                    ),
                    Container(
                      padding: const EdgeInsets.all(20),
                      child: FloatingActionButton(
                        onPressed: () {
                          if (_captureTimer == null ||
                              !_captureTimer!.isActive) {
                            startPhotoCapture();
                          } else {
                            stopPhotoCapture();
                          }
                        },
                        child: Icon(
                            _captureTimer == null || !_captureTimer!.isActive
                                ? Icons.camera_alt
                                : Icons.stop),
                      ),
                    ),
                    Container(
                      padding: const EdgeInsets.all(20),
                      child: FloatingActionButton(
                        heroTag: "openFolderButton",
                        onPressed: () {
                          if (videoDirectoryPath != null) {
                            Navigator.of(context).push(
                              MaterialPageRoute(
                                builder: (context) => VideoPreviewScreen(
                                    directoryPath: videoDirectoryPath!),
                              ),
                            );
                          }
                        },
                        child: const Icon(Icons.folder_open),
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  void startPhotoCapture() {
    _captureTimer = Timer.periodic(Duration(seconds: captureInterval), (timer) {
      capturePhoto();
    });
  }

  Future<void> capturePhoto() async {
    if (!_controller.value.isInitialized) {
      return;
    }
    try {
      final XFile photo = await _controller.takePicture();
      setState(() {
        photoQueue.add(photo);
        if (photoQueue.length >= 10) {
          processPhotos(photoQueue);
          photoQueue.clear();
        }
      });
    } catch (e) {
      debugPrint('Error capturing photo: $e');
    }
  }

  void stopPhotoCapture() {
    _captureTimer?.cancel();
  }

  Future<void> processPhotos(List<XFile> photos) async {
    final modelManager = ModelManager();

    try {
      await modelManager.loadModel();
      debugPrint('Number of photos to process: ${photos.length}');
    } catch (e) {
      debugPrint('Error before processing photos: $e');
    }
    for (XFile photo in photos) {
      debugPrint('188 Processing photo: ${photo.path}');
      File imageFile = File(photo.path);
      Uint8List imageBytes = await imageFile.readAsBytes();

      List<dynamic>? results = await modelManager.runInference(imageBytes);

      if (results != null && results.isNotEmpty && results[0][0] > 0.5) {
        debugPrint(
            '198 Object detected in ${photo.path} with confidence ${results[0][0]}');
      } else {
        debugPrint('201 Not detected ${photo.path}');
      }
    }

    modelManager.dispose();
  }

  @override
  void dispose() {
    _controller.dispose();
    _captureTimer?.cancel();
    _timer?.cancel();
    super.dispose();
  }
}
