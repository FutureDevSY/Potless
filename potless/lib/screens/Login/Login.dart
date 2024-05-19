import 'package:flutter/material.dart';
import 'package:lottie/lottie.dart';
import 'package:potless/API/api_request.dart';
import 'package:potless/screens/Home/HomeScreen.dart';
import 'package:potless/screens/Login/SignUp.dart';
import 'package:potless/widgets/UI/ScreenSize.dart';
import 'package:potless/widgets/buttons/714_150button.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen>
    with SingleTickerProviderStateMixin {
  final TextEditingController _idController = TextEditingController();
  final TextEditingController _pwController = TextEditingController();
  final ApiService _apiService = ApiService();

  late AnimationController _animationController;
  late Animation<Color?> _colorAnimation;
  late Animation<double> _positionAnimation;

  @override
  void initState() {
    super.initState();
    _animationController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 500),
    );

    _colorAnimation =
        ColorTween(begin: const Color(0xff151C62), end: Colors.red).animate(
      CurvedAnimation(
          parent: _animationController,
          curve: const Interval(0.0, 0.5, curve: Curves.easeIn)),
    );

    _positionAnimation = Tween(begin: 0.0, end: 10.0).animate(
      CurvedAnimation(
          parent: _animationController, curve: Curves.easeInOutCubic),
    );

    _animationController.addListener(() {
      setState(() {});
    });

    _animationController.addStatusListener((status) {
      if (status == AnimationStatus.completed) {
        _animationController.reverse();
      } else if (status == AnimationStatus.dismissed) {
        _animationController.stop();
      }
    });
  }

  @override
  void dispose() {
    _animationController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        children: [
          Container(
            color: Colors.white,
            padding: const EdgeInsets.all(20),
            child: Center(
              child: Column(
                children: [
                  SizedBox(height: UIhelper.deviceHeight(context) * 0.1),
                  const Text('포트리스',
                      style:
                          TextStyle(fontSize: 30, fontWeight: FontWeight.bold)),
                  const Text(
                    'POTLESS',
                    style: TextStyle(fontSize: 20),
                  ),
                  Lottie.asset('assets/lottie/loading_truck.json'),
                  InkWell(
                    onTap: () {
                      _openInputModal(context, _idController, 'ID를 입력해주세요');
                    },
                    child: Container(
                      height: UIhelper.deviceHeight(context) * 0.08,
                      padding: const EdgeInsets.all(10),
                      decoration: BoxDecoration(
                        border: Border.all(color: Colors.blue),
                        borderRadius: BorderRadius.circular(10),
                      ),
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Expanded(
                            child: Text(
                              _idController.text.isEmpty
                                  ? 'ID를 입력해주세요'
                                  : _idController.text,
                              style: const TextStyle(fontSize: 16),
                            ),
                          ),
                          const Icon(Icons.edit, color: Colors.blue),
                        ],
                      ),
                    ),
                  ),
                  SizedBox(height: UIhelper.deviceHeight(context) * 0.02),
                  InkWell(
                    onTap: () {
                      _openInputModal(context, _pwController, 'PW를 입력해주세요');
                    },
                    child: Material(
                      type: MaterialType.transparency,
                      child: Container(
                        height: UIhelper.deviceHeight(context) * 0.08,
                        padding: const EdgeInsets.all(10),
                        decoration: BoxDecoration(
                          border: Border.all(color: Colors.blue),
                          borderRadius: BorderRadius.circular(10),
                        ),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Expanded(
                              child: Text(
                                _pwController.text.isEmpty
                                    ? 'PW를 입력해주세요'
                                    : '•' * _pwController.text.length,
                                style: const TextStyle(fontSize: 16),
                              ),
                            ),
                            const Icon(Icons.edit, color: Colors.blue),
                          ],
                        ),
                      ),
                    ),
                  ),
                  SizedBox(height: UIhelper.deviceHeight(context) * 0.02),
                  button714_150(
                    lable: '로그인',
                    backgroundColor: _colorAnimation.value,
                    onPressed: _handleLogin,
                    transform: Matrix4.translationValues(
                        _positionAnimation.value *
                            (_animationController.value <= 0.5 ? 1 : -1),
                        0,
                        0),
                  ),
                ],
              ),
            ),
          ),
          Positioned(
            right: 10,
            top: 40,
            child: GestureDetector(
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => const SignUpScreen(),
                  ),
                );
              },
              child: const Row(
                children: [
                  Icon(
                    Icons.logout,
                    color: Color(0xff151c62),
                    size: 30,
                  ),
                  Text('회원가입')
                ],
              ),
            ),
          )
        ],
      ),
    );
  }

  void _handleLogin() async {
    bool success =
        await _apiService.login(_idController.text, _pwController.text);
    if (success) {
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(
          builder: (context) => const HomeScreen(),
        ),
      );
    } else {
      _animationController.forward(from: 0);
    }
  }

  void _openInputModal(
      BuildContext context, TextEditingController controller, String hintText) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return Dialog(
          backgroundColor: Colors.transparent,
          insetPadding: const EdgeInsets.all(10),
          child: Center(
            child: Material(
              type: MaterialType.transparency,
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Container(
                    padding: const EdgeInsets.symmetric(
                        vertical: 20, horizontal: 30),
                    decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.circular(15),
                    ),
                    child: TextField(
                      autofocus: true,
                      controller: controller,
                      decoration: InputDecoration(
                        hintText: hintText,
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(10),
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(height: 20),
                  ElevatedButton(
                    onPressed: () => Navigator.of(context).pop(),
                    child: const Text('입력'),
                  )
                ],
              ),
            ),
          ),
        );
      },
    );
  }
}
