import 'package:flutter/material.dart';
import 'package:lottie/lottie.dart';
import 'package:potless/API/api_request.dart';
import 'package:potless/models/pothole.dart';
import 'package:potless/widgets/UI/AppBar.dart';
import 'package:potless/widgets/blocks/project_block.dart';

class ProjectListScreen extends StatefulWidget {
  const ProjectListScreen({super.key});

  @override
  State<ProjectListScreen> createState() => _ProjectListScreenState();
}

class _ProjectListScreenState extends State<ProjectListScreen> {
  late Future<List<Project>> _projects;
  final ApiService _apiService = ApiService();
  @override
  void initState() {
    super.initState();
    _projects = _fetchProjects();
  }

  void _refreshProjects() async {
    setState(() {
      _projects = _apiService.fetchProject();
    });
    debugPrint('list project update 31');
  }

  Future<List<Project>> _fetchProjects() async {
    return await _apiService.fetchProject();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: const CustomAppBar(title: '작업 지시서'),
      body: Container(
        color: const Color(0xffffffff),
        child: FutureBuilder<List<Project>>(
          future: _projects,
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return const Center(child: CircularProgressIndicator());
            } else if (snapshot.hasError) {
              return Center(child: Text('Error: ${snapshot.error}'));
            } else if (snapshot.hasData) {
              if (snapshot.data!.isEmpty) {
                return Center(
                    child: Column(
                  children: [
                    Lottie.asset('./assets/lottie/check.json', repeat: false),
                    const Text('할당된 작업 지시서가 없습니다!'),
                  ],
                ));
              } else {
                return ListView.builder(
                  itemCount: snapshot.data!.length,
                  itemBuilder: (context, index) {
                    Project project = snapshot.data![index];
                    return ProjectBlock(
                      projectName: project.projectName,
                      projectId: project.projectId,
                      createdDate: project.createdDate,
                      damages: project.damages,
                      onProjectUpdate: _refreshProjects,
                    );
                  },
                );
              }
            } else {
              return const Center(child: Text('할당된 프로젝트가 없습니다'));
            }
          },
        ),
      ),
    );
  }
}
