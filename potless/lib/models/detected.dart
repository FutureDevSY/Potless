import 'dart:io';
import 'package:geolocator/geolocator.dart';

class QueuedImage {
  final File imageFile;
  final Position position;

  QueuedImage(
    this.imageFile,
    this.position,
  );
}
