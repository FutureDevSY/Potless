import 'package:flutter/material.dart';
import 'package:potless/API/api_request.dart';
import 'package:potless/screens/Login/Login.dart';
import 'package:potless/screens/Record/PotLess.dart';
import 'package:potless/screens/Works/ProjectList.dart';
import 'package:potless/widgets/UI/ScreenSize.dart';
import 'package:potless/widgets/buttons/main_button.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final ApiService _apiService = ApiService();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        children: [
          Container(
            padding: const EdgeInsets.all(20),
            color: Colors.white,
            child: Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  SizedBox(
                    height: UIhelper.deviceHeight(context) * 0.05,
                  ),
                  const Text(
                    'POTLESS',
                    style: TextStyle(
                      fontSize: 38,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const Text(
                    '도로파손 통합 관리 시스템',
                    style: TextStyle(fontSize: 16),
                  ),
                  SizedBox(
                    height: UIhelper.deviceHeight(context) * 0.2,
                  ),
                  MainLarge(
                    label: 'ai test',
                    onPressed: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => const VideoPage(),
                        ),
                      );
                    },
                  ),
                  SizedBox(
                    height: UIhelper.deviceHeight(context) * 0.02,
                  ),
                  MainLarge(
                    label: '작업목록',
                    onPressed: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => const ProjectListScreen(),
                        ),
                      );
                    },
                  ),
                ],
              ),
            ),
          ),
          Positioned(
            right: 10,
            top: 40,
            child: GestureDetector(
              onTap: () => _openLogoutModal(context),
              child: const Row(
                children: [
                  Icon(
                    Icons.logout,
                    color: Color(0xff151c62),
                    size: 30,
                  ),
                  Text('로그아웃')
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  void _openLogoutModal(
    BuildContext context,
  ) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return Dialog(
          backgroundColor: Colors.transparent,
          insetPadding: const EdgeInsets.all(10),
          child: Center(
            child: Material(
              type: MaterialType.transparency,
              child: Container(
                padding:
                    const EdgeInsets.symmetric(vertical: 20, horizontal: 30),
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(15),
                ),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    SizedBox(height: UIhelper.deviceHeight(context) * 0.03),
                    const Text(
                      '로그아웃 하시겠습니까?',
                      style: TextStyle(fontSize: 18),
                    ),
                    SizedBox(height: UIhelper.deviceHeight(context) * 0.05),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        ElevatedButton(
                          onPressed: () => Navigator.of(context).pop(),
                          style: ButtonStyle(
                            backgroundColor: MaterialStateProperty.all(
                              const Color(0xff151c62),
                            ),
                            foregroundColor: MaterialStateProperty.all(
                              const Color(0xffffffff),
                            ),
                          ),
                          child: const Text('취소'),
                        ),
                        SizedBox(
                          width: UIhelper.deviceWidth(context) * 0.1,
                        ),
                        ElevatedButton(
                          onPressed: () {
                            logout();
                          },
                          style: ButtonStyle(
                            backgroundColor: MaterialStateProperty.all(
                              const Color(0xffFF6D6D),
                            ),
                            foregroundColor: MaterialStateProperty.all(
                              const Color(0xffffffff),
                            ),
                          ),
                          child: const Text('로그아웃'),
                        ),
                      ],
                    )
                  ],
                ),
              ),
            ),
          ),
        );
      },
    );
  }

  void logout() async {
    bool loggedOut = await _apiService.logout();
    if (loggedOut) {
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(builder: (context) => const LoginScreen()),
      );
    }
  }
}
