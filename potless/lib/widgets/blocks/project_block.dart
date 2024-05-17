import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:intl/intl.dart';
import 'package:potless/API/api_request.dart';
import 'package:potless/models/pothole.dart';
import 'package:potless/screens/Works/WorkList2.dart';
import 'package:potless/widgets/UI/ScreenSize.dart'; // Ensure you have the intl package added in your pubspec.yaml

class ProjectBlock extends StatefulWidget {
  final String projectName;
  final int projectId;
  final DateTime createdDate;
  final List<DamageResponse> damages;

  final Function onProjectUpdate;

  const ProjectBlock({
    super.key,
    required this.projectName,
    required this.projectId,
    required this.createdDate,
    required this.damages,
    required this.onProjectUpdate,
  });

  @override
  State<ProjectBlock> createState() => _ProjectBlockState();
}

class _ProjectBlockState extends State<ProjectBlock> {
  final ApiService _apiService = ApiService();

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.symmetric(vertical: 8.0, horizontal: 16.0),
      child: Row(
        children: [
          Expanded(
            flex: 5,
            child: GestureDetector(
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => WorkListScreen(
                      damages: widget.damages,
                      onProjectUpdate: () => widget.onProjectUpdate(),
                    ),
                  ),
                );
              },
              child: Container(
                padding: const EdgeInsets.all(16.0),
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(8.0),
                  boxShadow: const [
                    BoxShadow(
                      color: Colors.black12,
                      offset: Offset(0, 2),
                      blurRadius: 6.0,
                    ),
                  ],
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      widget.projectName,
                      style: const TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    const SizedBox(height: 8.0),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text('${widget.projectId}번 지시서'),
                        Text(
                          '일시: ${DateFormat('yyyy-MM-dd').format(widget.createdDate)}',
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          ),
          const SizedBox(width: 8.0),
          Expanded(
            flex: 1,
            child: Container(
              height: 90,
              padding: const EdgeInsets.all(8.0),
              decoration: BoxDecoration(
                color: const Color(0xff151C62),
                borderRadius: BorderRadius.circular(8.0),
                boxShadow: const [
                  BoxShadow(
                    color: Colors.black12,
                    offset: Offset(0, 2),
                    blurRadius: 6.0,
                  ),
                ],
              ),
              child: GestureDetector(
                onTap: () {
                  showDialog(
                    context: context,
                    builder: (BuildContext context) {
                      return AlertDialog(
                        backgroundColor: const Color(
                            0xffFFFFFF), // Set dialog background color to white
                        title: Text(
                          '${widget.projectId}번 작업 지시서',
                          style: const TextStyle(
                            color: Color(
                                0xff151c62), // Set title text color to blue
                          ),
                        ),
                        content: const Text(
                          '완료하시겠습니까?',
                          style: TextStyle(
                            color: Color(0xff151c62),
                          ),
                        ),
                        actions: <Widget>[
                          TextButton(
                            child: const Text(
                              '취소',
                              style: TextStyle(
                                color: Color(0xff151c62),
                              ),
                            ),
                            onPressed: () {
                              Navigator.of(context).pop();
                            },
                          ),
                          TextButton(
                            child: const Text(
                              '확인',
                              style: TextStyle(
                                color: Color(
                                    0xff151c62), // Set button text color to blue
                              ),
                            ),
                            onPressed: () {
                              Navigator.of(context).pop(); // Close the dialog
                              _apiService.projectDone(
                                  widget.projectId); // Perform the action
                            },
                          ),
                        ],
                      );
                    },
                  );
                },
                child: Container(
                  alignment: Alignment.center,
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(8.0),
                  ),
                  child: const Text(
                    '완료',
                    style: TextStyle(
                      color: Colors.white,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
