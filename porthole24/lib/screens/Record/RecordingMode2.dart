import 'dart:async';
import 'dart:io';

import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:porthole24/main.dart';
import 'package:porthole24/screens/Record/VideoPreviewScreen.dart';
import 'package:flutter/services.dart';
import 'package:porthole24/widgets/UI/AppBar.dart';

class CameraScreen extends StatefulWidget {
  const CameraScreen({super.key});
  @override
  State<CameraScreen> createState() => _CameraScreenState();
}

class _CameraScreenState extends State<CameraScreen> {
  late CameraController _controller;
  bool _isRecordingInProgress = false;
  Timer? _timer;
  String? videoDirectoryPath;
  bool _continuousRecording = false;
  bool _isPreviewOn = true;
  late CameraImage cameraImage;

  var cameraCount = 0;

  static const platform = MethodChannel('com.example.porthole24/mediastore');

  Future<void> notifyMediaStore(String path) async {
    try {
      final int result =
          await platform.invokeMethod('notifyMediaStore', {'path': path});
      debugPrint('aMedia store updated: $result');
    } on PlatformException catch (e) {
      debugPrint("Failed to notify media store: '${e.message}'.");
    }
  }

  @override
  void initState() {
    super.initState();
    _controller =
        CameraController(cameras[0], ResolutionPreset.max, enableAudio: false);
    _controller.initialize().then((_) {
      if (!mounted) return;
      setState(() {});
      prepareVideoDirectory();
    }).catchError((Object e) {
      if (e is CameraException) {
        debugPrint('Camera Exception: ${e.description}');
      }
    });
  }

  void toggleCameraPreview() {
    setState(() {
      _isPreviewOn = !_isPreviewOn;
    });
  }

  Future<void> prepareVideoDirectory() async {
    final Directory videosDir2 = Directory('/storage/emulated/0/DCIM/Videos');
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
                Container(
                  padding: const EdgeInsets.all(20),
                  child: FloatingActionButton(
                    heroTag: "togglePreviewButton",
                    onPressed: toggleCameraPreview,
                    child: Icon(
                        _isPreviewOn ? Icons.visibility_off : Icons.visibility),
                  ),
                ),
                Container(
                  padding: const EdgeInsets.all(20),
                  child: FloatingActionButton(
                    heroTag: "toggleRecordingButton",
                    onPressed: () => toggleRecordingMode(),
                    child: Icon(
                        _continuousRecording ? Icons.stop : Icons.videocam),
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
          ),
        ],
      ),
    );
  }

  void toggleRecordingMode() {
    setState(() {
      _continuousRecording = !_continuousRecording;
    });
    if (_continuousRecording) {
      if (!_isRecordingInProgress) {
        startVideoRecording();
      }
    } else {
      _timer?.cancel();
      if (_isRecordingInProgress) {
        stopVideoRecording();
      }
    }
  }

  void startVideoRecording() async {
    if (!_controller.value.isInitialized ||
        _controller.value.isRecordingVideo) {
      return;
    }

    try {
      await _controller.startVideoRecording();
      setState(() {
        _isRecordingInProgress = true;
      });
      _timer = Timer(const Duration(seconds: 10), () {
        if (_continuousRecording) {
          stopVideoRecording();
        }
      });
    } catch (e) {
      debugPrint('Error starting video recording: $e');
    }
  }

  void stopVideoRecording() async {
    if (!_controller.value.isRecordingVideo) {
      return;
    }

    try {
      XFile videoFile = await _controller.stopVideoRecording();
      setState(() {
        _isRecordingInProgress = false;
      });

      if (videoDirectoryPath != null) {
        final String videoPath =
            '$videoDirectoryPath/${DateTime.now().millisecondsSinceEpoch}.mp4';
        await videoFile.saveTo(videoPath);
        debugPrint('Video saved to $videoPath');

        notifyMediaStore(videoPath);
      }

      if (_continuousRecording) {
        startVideoRecording();
      }
    } catch (e) {
      debugPrint('Error stopping video recording: $e');
    }
  }

  @override
  void dispose() {
    _controller.dispose();
    _timer?.cancel();
    super.dispose();
  }
}
