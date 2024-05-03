import 'package:camera/camera.dart';
import 'package:geolocator/geolocator.dart';

class QueuedImage {
  final XFile imageFile;
  final Position position;

  QueuedImage(
    this.imageFile,
    this.position,
  );
}
