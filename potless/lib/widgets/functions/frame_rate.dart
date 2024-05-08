import 'dart:async';

class FrameRateTester {
  int frameCount = 0;
  Timer? timer;

  void startTesting(Function onFrame) {
    timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      print("FPS: $frameCount");
      frameCount = 0;
    });
  }

  void countFrame() {
    frameCount++;
  }

  void stopTesting() {
    timer?.cancel();
    print("Testing stopped");
  }
}
