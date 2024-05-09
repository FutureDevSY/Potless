import 'package:flutter/material.dart';
import 'package:potless/API/api_request.dart';
import 'package:potless/models/pothole.dart';
import 'package:potless/widgets/UI/AppBar.dart';
import 'package:potless/widgets/UI/ScreenSize.dart';
import 'package:potless/widgets/blocks/work_block.dart';

class WorkListScreen extends StatefulWidget {
  final List<DamageResponse> damages;

  const WorkListScreen({
    super.key,
    required this.damages,
  });

  @override
  State<WorkListScreen> createState() => _WorkListScreenState();
}

class _WorkListScreenState extends State<WorkListScreen> {
  List<DamageResponse> allDamages = [];
  List<DamageResponse> filteredDamages = [];
  String filterStatus = '작업중';
  final ApiService _apiService = ApiService();

  @override
  void initState() {
    super.initState();
    allDamages = widget.damages;
    filteredDamages = allDamages;
    filterDamages(filterStatus);
  }

  void filterDamages(String? status) {
    setState(() {
      if (status == null) {
        filteredDamages = allDamages;
      } else {
        filteredDamages = allDamages.where((damage) {
          return damage.status == status;
        }).toList();
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: const CustomAppBar(title: '파손 목록'),
      body: Container(
        color: const Color(0xffffffff),
        child: Column(
          children: [
            const Divider(),
            SizedBox(
              height: MediaQuery.of(context).size.height * 0.1,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: [
                  ElevatedButton(
                    style: ButtonStyle(
                      backgroundColor: MaterialStateProperty.all(
                        const Color(0xff151c62),
                      ),
                      foregroundColor: MaterialStateProperty.all(
                        const Color(0xffffffff),
                      ),
                    ),
                    onPressed: () => filterDamages(null),
                    child: const Text('모든 작업'),
                  ),
                  ElevatedButton(
                    onPressed: () => filterDamages('작업중'),
                    child: const Text('작업 전'),
                  ),
                  ElevatedButton(
                    onPressed: () => filterDamages('작업완료'),
                    child: const Text('작업 완료'),
                  ),
                  Text('총 ${filteredDamages.length}개')
                ],
              ),
            ),
            const Divider(),
            Expanded(
              child: ListView.builder(
                itemCount: filteredDamages.length,
                itemBuilder: (context, index) {
                  final damage = filteredDamages[index];
                  return WorkBlock(
                    potholeId: damage.id,
                    field: damage.area,
                    address: damage.address,
                    roadName: damage.location,
                    x: damage.dirX,
                    y: damage.dirY,
                    status: damage.status,
                    severity: damage.severity,
                    width: damage.width,
                    images: damage.images,
                    dType: damage.dtype,
                    createdAt: damage.createdDateTime,
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}
