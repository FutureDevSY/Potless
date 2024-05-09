import 'dart:async';

class FrameRateTester {
  int frameCount = 0;
  Timer? timer;
  Function(int)? onUpdate; // Callback for UI update

  FrameRateTester({this.onUpdate});

  void startTesting() {
    timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      onUpdate?.call(frameCount); // Update the UI
      frameCount = 0;
    });
  }

  void countFrame() {
    frameCount++;
  }

  void stopTesting() {
    timer?.cancel();
    onUpdate?.call(0); // Update UI on stop
  }
}
