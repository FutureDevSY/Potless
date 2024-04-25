import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'dart:convert';
import 'package:flutter/services.dart' show rootBundle;
import 'package:porthole24/models/pothole.dart';
import 'package:porthole24/widgets/UI/AppBar.dart';
import 'package:porthole24/widgets/UI/ScreenSize.dart';
import 'package:porthole24/widgets/blocks/work_block.dart';

class WorkListScreen extends StatefulWidget {
  const WorkListScreen({super.key});

  @override
  State<WorkListScreen> createState() => _WorkListScreenState();
}

class _WorkListScreenState extends State<WorkListScreen> {
  late Future<List<Pothole>> potholesFuture;
  List<Pothole> allPotholes = [];
  List<Pothole> filteredPotholes = [];
  int? filterStatus; // null for all, 0 for not done, 1 for done

  @override
  void initState() {
    super.initState();
    potholesFuture = fetchPotholes();
    potholesFuture.then((potholes) {
      setState(() {
        allPotholes = potholes;
        filteredPotholes = allPotholes; // Display all potholes by default
      });
    });
  }

  Future<List<Pothole>> fetchPotholes() async {
    final String response = await rootBundle
        .loadString('assets/dummy_data/dummy_pothole_data.json');
    final data = await json.decode(response) as List;
    return data.map((data) => Pothole.fromJson(data)).toList();
  }

  void filterPotholes(int? status) {
    setState(() {
      filterStatus = status;
      filteredPotholes = status == null
          ? allPotholes
          : allPotholes.where((pothole) => pothole.status == status).toList();
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
          child: FutureBuilder<List<Pothole>>(
            future: potholesFuture,
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
                              onPressed: () => filterPotholes(null),
                              child: const Text('모든 작업')),
                          ElevatedButton(
                              onPressed: () => filterPotholes(0),
                              child: const Text('작업 전')),
                          ElevatedButton(
                              onPressed: () => filterPotholes(1),
                              child: const Text('작업 후')),
                          Text('${filteredPotholes.length}개')
                        ],
                      ),
                    ),
                    Expanded(
                      child: ListView.builder(
                        itemCount: filteredPotholes.length,
                        itemBuilder: (context, index) {
                          final pothole = filteredPotholes[index];
                          return WorkBlock(
                            potholeId: pothole.potholeId,
                            field: pothole.field,
                            address: pothole.address,
                            roadName: pothole.roadName,
                            x: pothole.dirX,
                            y: pothole.dirY,
                            status: pothole.status,
                            severity: pothole.severity,
                            length: pothole.length,
                            image: Image.asset('assets/pothole/porthole1.jpg'),
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
