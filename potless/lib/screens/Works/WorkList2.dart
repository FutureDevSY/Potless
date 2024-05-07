import 'package:flutter/material.dart';
import 'package:flutter/services.dart' show rootBundle;
import 'package:porthole24/API/api_request.dart';
import 'package:porthole24/models/pothole.dart';
import 'dart:convert';

import 'package:porthole24/widgets/UI/AppBar.dart';

import 'package:porthole24/widgets/UI/ScreenSize.dart';
import 'package:porthole24/widgets/blocks/work_block.dart';

class WorkListScreen extends StatefulWidget {
  const WorkListScreen({super.key});

  @override
  State<WorkListScreen> createState() => _WorkListScreenState();
}

class _WorkListScreenState extends State<WorkListScreen> {
  late Future<List<DamageResponse>> damagesFuture;
  List<DamageResponse> allDamages = [];
  List<DamageResponse> filteredDamages = [];
  String filterStatus = '작업전';
  final ApiService _apiService = ApiService();

  @override
  void initState() {
    super.initState();
    damagesFuture = fetchDamages();
    damagesFuture.then((damages) {
      setState(() {
        allDamages = damages;
        filteredDamages = allDamages; // Display all damages by default
      });
    });
  }

  Future<List<DamageResponse>> fetchDamages() async {
    try {
      return await _apiService.fetchProject();
    } catch (e) {
      debugPrint('42 : $e');
      return [];
    }
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
      appBar: const CustomAppBar(title: '오늘의 작업'),
      body: Container(
        padding: const EdgeInsets.all(20),
        color: Colors.white,
        child: Center(
          child: FutureBuilder<List<DamageResponse>>(
            future: damagesFuture,
            builder: (context, snapshot) {
              if (snapshot.connectionState == ConnectionState.waiting) {
                return const CircularProgressIndicator();
              } else if (snapshot.hasError) {
                return Text('Error: ${snapshot.error}');
              } else {
                return Column(
                  children: [
                    Container(
                      height: UIhelper.deviceHeight(context) * 0.1,
                      decoration: BoxDecoration(border: Border.all()),
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        children: [
                          ElevatedButton(
                            onPressed: () => filterDamages(null),
                            child: const Text('모든 작업'),
                          ),
                          ElevatedButton(
                            onPressed: () => filterDamages('작업전'),
                            child: const Text('작업 전'),
                          ),
                          ElevatedButton(
                            onPressed: () => filterDamages('작업완료'),
                            child: const Text('작업 완료'),
                          ),
                          Text('${filteredDamages.length}개')
                        ],
                      ),
                    ),
                    Expanded(
                      child: ListView.builder(
                        itemCount: filteredDamages.length,
                        itemBuilder: (context, index) {
                          final damage = filteredDamages[index];
                          return WorkBlock(
                            potholeId: damage.id,
                            field:
                                damage.area, // Update field mapping if needed
                            address: damage.address,
                            roadName: damage
                                .location, // Update roadName mapping if needed
                            x: damage.dirX,
                            y: damage.dirY,
                            status: damage.status,
                            severity: damage.severity,
                            width: damage.width, // Update as needed
                            image: Image.network(damage.images.first
                                .url), // Assuming there's at least one image
                          );
                        },
                      ),
                    ),
                  ],
                );
              }
            },
          ),
        ),
      ),
    );
  }
}
