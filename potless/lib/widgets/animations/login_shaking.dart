import 'dart:math' as math;
import 'package:flutter/animation.dart';

class ShakeCurve extends Curve {
  final int shakes;
  const ShakeCurve({this.shakes = 3});

  @override
  double transform(double t) {
    // Amplitude decreases over time
    double decayFactor = 1 - t;
    // Sine function to create the oscillation
    double sineWave = math.sin(t * shakes * 2 * math.pi);
    // Decrease amplitude over time to ensure it ends at 0
    return decayFactor * sineWave * 0.5 + 0.5; // Normalize to range 0 to 1
  }
}
