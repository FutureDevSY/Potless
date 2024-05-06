import 'dart:math';
import 'package:flutter/material.dart';

class ScreenParams {
  static late Size previewSize;

  static double getPreviewRatio() {
    if (previewSize.width == 0 || previewSize.height == 0) {
      return 1.0;
    }
    return max(previewSize.height, previewSize.width) /
        min(previewSize.height, previewSize.width);
  }

  static Size screenPreviewSize(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    return Size(width, width * getPreviewRatio());
  }

  static void setPreviewSize(Size size) {
    previewSize = size;
  }
}
