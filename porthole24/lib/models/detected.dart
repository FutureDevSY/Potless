import 'package:camera/camera.dart';

class QueuedImage {
  final XFile imageFile;
  final double x, y;

  QueuedImage(this.imageFile, this.x, this.y);
}
