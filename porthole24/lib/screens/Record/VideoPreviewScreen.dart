import 'dart:io';
import 'package:video_player/video_player.dart';
import 'package:flutter/material.dart';

class VideoPreviewScreen extends StatefulWidget {
  final String directoryPath;

  const VideoPreviewScreen({super.key, required this.directoryPath});

  @override
  _VideoPreviewScreenState createState() => _VideoPreviewScreenState();
}

class _VideoPreviewScreenState extends State<VideoPreviewScreen> {
  late List<File> videoFiles;
  VideoPlayerController? _controller;

  @override
  void initState() {
    super.initState();
    loadVideos();
  }

  @override
  void dispose() {
    _controller?.dispose();
    super.dispose();
  }

  void loadVideos() {
    final dir = Directory(widget.directoryPath);
    setState(() {
      videoFiles = dir.listSync().whereType<File>().toList();
    });
  }

  void initializeVideoPlayer(File videoFile) {
    _controller?.dispose();
    _controller = VideoPlayerController.file(videoFile)
      ..initialize().then((_) {
        setState(() {
          _controller!.play();
        });
      });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Video Previews')),
      body: Column(
        children: [
          Expanded(
            child: _controller != null && _controller!.value.isInitialized
                ? AspectRatio(
                    aspectRatio: _controller!.value.aspectRatio,
                    child: VideoPlayer(_controller!),
                  )
                : const Center(child: Text('Select a video to play')),
          ),
          Expanded(
            child: ListView.builder(
              itemCount: videoFiles.length,
              itemBuilder: (context, index) {
                return ListTile(
                  title: Text('Video ${index + 1}'),
                  onTap: () {
                    initializeVideoPlayer(videoFiles[index]);
                  },
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
