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
  List<Project> _projects = [];
  final ApiService _apiService = ApiService();
  bool _isLoading = true;
  bool _hasError = false;

  @override
  void initState() {
    super.initState();
    _fetchProjects();
  }

  Future<void> _fetchProjects() async {
    try {
      List<Project> projects = await _apiService.fetchProject();
      setState(() {
        _projects = projects;
        _isLoading = false;
        _hasError = false;
      });
    } catch (error) {
      setState(() {
        _isLoading = false;
        _hasError = true;
      });
      debugPrint('Error fetching projects: $error');
    }
  }

  void _refreshProjects() async {
    setState(() {
      _isLoading = true;
    });

    try {
      List<Project> updatedProjects = await _apiService.fetchProject();
      setState(() {
        _projects = updatedProjects;
        _isLoading = false;
        _hasError = false;
      });
      debugPrint('Project list updated');
    } catch (error) {
      setState(() {
        _isLoading = false;
        _hasError = true;
      });
      debugPrint('Error refreshing projects: $error');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: const CustomAppBar(title: '작업 지시서'),
      body: Container(
        color: const Color(0xffffffff),
        child: _isLoading
            ? const Center(child: CircularProgressIndicator())
            : _hasError
                ? const Center(child: Text('Error fetching projects'))
                : _projects.isEmpty
                    ? Center(
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Lottie.asset('./assets/lottie/check.json',
                                repeat: false),
                            const SizedBox(height: 16),
                            const Text('할당된 작업 지시서가 없습니다!'),
                          ],
                        ),
                      )
                    : ListView.builder(
                        itemCount: _projects.length,
                        itemBuilder: (context, index) {
                          Project project = _projects[index];
                          return ProjectBlock(
                            projectName: project.projectName,
                            projectId: project.projectId,
                            createdDate: project.createdDate,
                            damages: project.damages,
                            onProjectUpdate: _refreshProjects,
                          );
                        },
                      ),
      ),
    );
  }
}
