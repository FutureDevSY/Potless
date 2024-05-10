import 'package:flutter/material.dart';
import 'package:potless/models/pothole.dart';
import 'package:potless/screens/Works/WorkDetail2.dart';
import 'package:potless/widgets/UI/ScreenSize.dart';

class WorkBlock extends StatelessWidget {
  final int potholeId, severity;
  final double x, y, width;
  final String address, roadName, field, status, dType;
  final DateTime createdAt;
  final List<DamageImage> images;
  final VoidCallback onProjectUpdate;

  const WorkBlock({
    super.key,
    this.potholeId = 0,
    this.status = '작업 전',
    this.severity = 1,
    this.x = 0,
    this.y = 0,
    this.width = 0,
    this.address = '예제',
    this.roadName = '예제',
    this.field = '예제',
    required this.images,
    required this.dType,
    required this.createdAt,
    required this.onProjectUpdate,
  });

  Image? get primaryImage {
    final primaryImg = images.firstWhere((img) => img.order == 1);
    return Image.network(primaryImg.url, fit: BoxFit.cover);
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(0.2),
      child: GestureDetector(
        onTap: () {
          Navigator.push(
            context,
            MaterialPageRoute(
              builder: (context) => WorkDetailScreen(
                placeLatitude: y,
                placeLongitude: x,
                potholeId: potholeId,
                field: field,
                address: address,
                roadName: roadName,
                status: status,
                severity: severity,
                width: width,
                images: images,
                dType: dType,
                createdAt: createdAt,
                onProjectUpdate: onProjectUpdate,
              ),
            ),
          );
        },
        child: Container(
          decoration: BoxDecoration(
            color: status == '작업중' ? Colors.white : Colors.grey,
            borderRadius: BorderRadius.circular(10),
            border: Border.all(),
          ),
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
                        Text('파손번호: ${potholeId.toString()}'),
                        Text(roadName),
                      ],
                    ),
                    SizedBox(height: UIhelper.deviceHeight(context) * 0.02),
                    Row(
                      children: [Text(address)],
                    )
                  ],
                ),
              ),
              Stack(
                children: [
                  Container(
                    margin: const EdgeInsets.fromLTRB(10, 0, 0, 0),
                    decoration: BoxDecoration(
                      border: Border.all(),
                      borderRadius: BorderRadius.circular(5),
                    ),
                    height: 100,
                    width: 150,
                    child: ClipRRect(
                      borderRadius: BorderRadius.circular(5),
                      child: primaryImage,
                    ),
                  ),
                  Positioned(
                    bottom: 0,
                    right: 0,
                    child: Container(
                      padding: const EdgeInsets.all(5),
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(5),
                        border: Border.all(),
                        color: severity == 1
                            ? Colors.green
                            : (severity == 2 ? Colors.orange : Colors.red),
                      ),
                      child: Text(
                        severity == 1 ? '양호' : (severity == 2 ? '중간' : '심각'),
                        style: const TextStyle(color: Color(0xFFFFFFFF)),
                      ),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
