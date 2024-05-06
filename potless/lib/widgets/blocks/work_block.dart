import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:porthole24/screens/Works/WorkDetail.dart';
import 'package:porthole24/widgets/UI/ScreenSize.dart';

class WorkBlock extends StatelessWidget {
  final int potholeId, status, severity;
  final double x, y, length;
  final String address, roadName, field;
  final Image? image;
  const WorkBlock({
    super.key,
    this.potholeId = 0,
    this.status = 1,
    this.severity = 1,
    this.x = 0,
    this.y = 0,
    this.length = 0,
    this.address = '예제',
    this.roadName = '예제',
    this.field = '예제',
    this.image,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        debugPrint('click');
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => WorkDetailScreen(
              placeLatitude: x,
              placeLongitude: y,
              potholeId: potholeId,
              field: field,
              address: address,
              roadName: roadName,
              status: status,
              severity: severity,
              length: length,
            ),
          ),
        );
      },
      child: Container(
        decoration: BoxDecoration(
            color: status == 0 ? Colors.white : Colors.grey,
            borderRadius: BorderRadius.circular(10),
            border: Border.all()),
        padding: const EdgeInsets.all(15),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Expanded(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(field),
                      Text(roadName),
                    ],
                  ),
                  SizedBox(height: UIhelper.deviceHeight(context) * 0.02),
                  Row(
                    children: [
                      Text(address),
                    ],
                  )
                ],
              ),
            ),
            Stack(children: [
              Container(
                margin: const EdgeInsets.fromLTRB(10, 0, 0, 0),
                decoration: BoxDecoration(
                  border: Border.all(),
                  borderRadius: BorderRadius.circular(5),
                ),
                height: 100,
                width: 150,
                child: image,
              ),
              Positioned(
                bottom: 0,
                right: 0,
                child: Container(
                  padding: const EdgeInsets.all(5),
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(5),
                    border: Border.all(),
                    // Change the color based on the severity
                    color: severity == 1
                        ? Colors.green
                        : (severity == 2 ? Colors.orange : Colors.red),
                  ),
                  child: Text(
                    // Change the text based on the severity
                    severity == 1 ? '양호' : (severity == 2 ? '중간' : '심각'),
                    style: const TextStyle(
                      color: Color(0xFFFFFFFF), // White color for text
                    ),
                  ),
                ),
              ),
            ])
          ],
        ),
      ),
    );
  }
}
