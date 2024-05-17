import 'package:flutter/material.dart';
import 'package:potless/API/api_request.dart';
import 'package:potless/API/login.dart';
import 'package:potless/screens/Login/Login.dart';
import 'package:potless/screens/Navi/NaviScreen.dart';
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
  final StorageService _storage = StorageService();
  bool isUser = false;
  String userName = '작업자';
  @override
  void initState() {
    super.initState();
    _checkRole();
    _checkName();
  }

  void _checkRole() async {
    try {
      String? role = await _storage.getRole();

      if (role == '3') {
        setState(() {
          isUser = true;
        });
      }
    } catch (E) {
      debugPrint('36: $E');
    }
  }

  void _checkName() async {
    try {
      String? name = await _storage.getName();

      if (name != null) {
        setState(() {
          userName = name;
        });
      }
    } catch (E) {
      debugPrint('52: $E');
    }
  }

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
                  if (!isUser) ...{
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
                  } else ...{
                    Column(
                      children: [
                        SizedBox(
                          height: UIhelper.deviceHeight(context) * 0.2,
                        ),
                        const Text('포트리스를 설치해주신 여러분께 감사드립니다'),
                        MainLarge(
                          label: '포트홀 안내받기',
                          onPressed: () {
                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (context) => const RouteFinderScreen(),
                              ),
                            );
                          },
                        ),
                      ],
                    )
                  },
                  SizedBox(
                    height: UIhelper.deviceHeight(context) * 0.02,
                  ),
                  MainLarge(
                    label: '탐지모드',
                    onPressed: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => const VideoPage(),
                        ),
                      );
                    },
                  ),
                ],
              ),
            ),
          ),
          Positioned(
            left: 30,
            top: 50,
            child: Text(
              '$userName님',
              style: const TextStyle(fontSize: 16),
            ),
          ),
          Positioned(
            right: 20,
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
