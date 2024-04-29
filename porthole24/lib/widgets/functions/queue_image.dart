import 'dart:async';
import 'dart:io';

import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:porthole24/models/detected.dart';

class ImageQueueManager {
  final _queue = <QueuedImage>[];
  bool _isProcessing = false;

  void enqueueImage(XFile imageFile, double x, double y) {
    _queue.add(QueuedImage(imageFile, x, y));
    if (!_isProcessing) {
      _processQueue();
    }
  }

  Future<void> _processQueue() async {
    _isProcessing = true;
    while (_queue.isNotEmpty) {
      var nextImage = _queue.removeAt(0);
      await _saveImageToFileSystem(nextImage);
    }
    _isProcessing = false;
  }

  Future<void> _saveImageToFileSystem(QueuedImage image) async {
    try {
      const directory = 'storage/emulated/0/DCIM/Camera';
      final path = '$directory/${DateTime.now().millisecondsSinceEpoch}.jpg';
      final file = File(path);

      // Copy the image file to the new path
      await image.imageFile.saveTo(path);
    } catch (e) {
      debugPrint('save failed $e');
    }
  }
}
