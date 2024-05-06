import 'package:flutter/material.dart';
import 'package:porthole24/widgets/tflite/screen.dart';

class Bbox extends StatelessWidget {
  const Bbox({
    super.key,
    required this.box,
    required this.name,
    required this.score,
  });
  final List<double> box;
  final String name;
  final double score;

  @override
  Widget build(BuildContext context) {
    final double screenWidth = MediaQuery.of(context).size.width;
    final double screenHeight = MediaQuery.of(context).size.height;

    // Calculating the bounding box dimensions and position
    final double width = box[2] * screenWidth;
    final double height = box[3] * screenHeight;
    final double left = (box[0] * screenWidth) - (width / 2);
    final double top = (box[1] * screenHeight) - (height / 2);

    final Color borderColor = name == 'good' ? Colors.green : Colors.red;

    return Positioned(
      left: left,
      top: top,
      width: width,
      height: height,
      child: Container(
        width: width,
        height: height,
        decoration: BoxDecoration(
          border: Border.all(color: borderColor, width: 1),
        ),
        child: Column(
          children: [
            Text(name),
            Text(score.toStringAsFixed(2)),
          ],
        ),
      ),
    );
  }
}
