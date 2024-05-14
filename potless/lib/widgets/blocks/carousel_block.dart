import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:potless/models/pothole.dart';

class CustomCarouselCard extends StatelessWidget {
  final DamageImage image;
  final VoidCallback onTap;

  const CustomCarouselCard({
    super.key,
    required this.image,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    Color backgroundColor =
        image.url.startsWith('http') || image.url.startsWith('https')
            ? const Color(0xff151C62)
            : Colors.white;
    Color textColor =
        image.url.startsWith('http') || image.url.startsWith('https')
            ? Colors.white
            : const Color(0xff151C62);

    String statusText() {
      switch (image.order) {
        case 1:
          return '작업 전';
        case 2:
          return '작업 중';
        case 3:
          return '작업 후';
        default:
          return '사진 추가';
      }
    }

    Widget imageContent() {
      if (image.url.startsWith('http') || image.url.startsWith('https')) {
        return Image.network(image.url, fit: BoxFit.cover);
      } else if (image.url.isNotEmpty) {
        return Image.file(File(image.url), fit: BoxFit.cover);
      } else {
        return Container(
          color: Colors.grey.shade300,
          alignment: Alignment.center,
          child: const Icon(Icons.photo, color: Colors.white, size: 50),
        );
      }
    }

    return GestureDetector(
      onTap: onTap,
      child: Container(
        width: MediaQuery.of(context).size.width * 0.85,
        margin: const EdgeInsets.symmetric(horizontal: 10),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(20),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(0.1),
              spreadRadius: 0,
              blurRadius: 10,
              offset: const Offset(0, 5),
            ),
          ],
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: <Widget>[
            Expanded(
              child: ClipRRect(
                borderRadius:
                    const BorderRadius.vertical(top: Radius.circular(20)),
                child: imageContent(),
              ),
            ),
            Container(
              color: backgroundColor,
              padding: const EdgeInsets.all(8.0),
              child: Text(
                statusText(),
                style: TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                    color: textColor),
                textAlign: TextAlign.center,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
