import 'dart:io';

import 'package:camera/camera.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:porthole24/widgets/UI/AppBar.dart';

class ImagePreview extends StatefulWidget {
  XFile file;

  ImagePreview(this.file, {super.key});

  @override
  State<ImagePreview> createState() => _ImagePreviewState();
}

class _ImagePreviewState extends State<ImagePreview> {
  @override
  Widget build(BuildContext context) {
    File picture = File(widget.file.path);

    return Scaffold(
      appBar: const CustomAppBar(
        title: '이미지',
      ),
      body: Center(
        child: Image.file(picture),
      ),
    );
  }
}
