import 'dart:io';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:path_provider/path_provider.dart';

class ImagePreviewScreen extends StatefulWidget {
  const ImagePreviewScreen({super.key});

  @override
  _ImagePreviewScreenState createState() => _ImagePreviewScreenState();
}

class _ImagePreviewScreenState extends State<ImagePreviewScreen> {
  List<File> _imageFiles = [];

  @override
  void initState() {
    super.initState();
    _getImageFiles();
  }

  Future<void> _getImageFiles() async {
    String formattedDate = DateFormat('yyyy-MM-dd').format(DateTime.now());
    Directory baseDir = Directory('storage/emulated/0/DCIM/$formattedDate');
    debugPrint('ImagePreview.dart 25 ${baseDir.path}');

    List<File> images = [];
    await for (var entity
        in baseDir.list(recursive: true, followLinks: false)) {
      if (entity is File && entity.path.toLowerCase().endsWith('.jpg')) {
        images.add(entity);
      }
    }

    setState(() {
      _imageFiles = images;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('Image Preview'),
        ),
        body: _imageFiles.isEmpty
            ? const Center(child: CircularProgressIndicator())
            : ListView.builder(
                itemCount: _imageFiles.length,
                itemBuilder: (context, index) {
                  return ListTile(
                    leading: Image.file(
                      File(_imageFiles[index].path),
                      width: 100,
                      height: 100,
                      fit: BoxFit.cover,
                    ),
                    title: Text(_imageFiles[index].path.split('/').last),
                    onTap: () {
                      // Handle image tap; perhaps open in a full-screen viewer or similar
                    },
                  );
                },
              ));
  }
}
