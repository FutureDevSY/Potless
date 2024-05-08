import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:potless/API/api_request.dart';
import 'package:potless/models/pothole.dart';
import 'package:potless/screens/Works/WorkList2.dart';
import 'package:potless/widgets/UI/AppBar.dart';

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
    _projects =
        _fetchProjects(); // Load the projects when the state is initialized
  }

  Future<List<Project>> _fetchProjects() async {
    return await _apiService.fetchProject();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: const CustomAppBar(title: '작업 지시서'),
      body: FutureBuilder<List<Project>>(
        future: _projects,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
          } else if (snapshot.hasData) {
            return ListView.builder(
              itemCount: snapshot.data!.length,
              itemBuilder: (context, index) {
                Project project = snapshot.data![index];
                return ListTile(
                  title: Text(
                    project.projectName,
                    style: const TextStyle(fontSize: 20),
                  ),
                  subtitle: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text('${project.projectId}번 지시서'),
                      const SizedBox(),
                      Text(
                          '일시: ${DateFormat('yyyy-MM-dd').format(project.createdDate)}'),
                      const SizedBox()
                    ],
                  ),
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) =>
                            WorkListScreen(damages: project.damages),
                      ),
                    );
                  },
                );
              },
            );
          } else {
            return const Center(child: Text('No projects found'));
          }
        },
      ),
    );
  }
}
