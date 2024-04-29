import 'package:flutter/material.dart';
import 'package:lottie/lottie.dart';
import 'package:porthole24/API/api_request.dart';
import 'package:porthole24/screens/Home/HomeScreen.dart';
import 'package:porthole24/widgets/UI/ScreenSize.dart';
import 'package:porthole24/widgets/animations/login_shaking.dart';
import 'package:porthole24/widgets/buttons/714_150button.dart';

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
        parent: _animationController,
        curve: Curves.easeInOutCubic,
      ),
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
      body: Container(
        color: Colors.white,
        padding: const EdgeInsets.all(20),
        child: Center(
          child: Column(
            children: [
              SizedBox(height: UIhelper.deviceHeight(context) * 0.1),
              const Text(
                '포트리스',
                style: TextStyle(fontSize: 26),
              ),
              const Text('Pot-Less'),
              Lottie.asset('assets/lottie/loading_truck.json'),
              TextField(
                controller: _idController,
                decoration: InputDecoration(
                  filled: true,
                  fillColor: const Color(0xffffffff).withOpacity(0.2),
                  border: const OutlineInputBorder(
                    borderRadius: BorderRadius.all(
                      Radius.circular(10),
                    ),
                  ),
                  hintText: 'ID를 입력해주세요',
                  contentPadding: EdgeInsets.symmetric(
                    vertical: UIhelper.deviceHeight(context) * 0.02,
                    horizontal: UIhelper.deviceWidth(context) * 0.04,
                  ),
                ),
              ),
              SizedBox(height: UIhelper.deviceHeight(context) * 0.02),
              TextField(
                controller: _pwController,
                decoration: InputDecoration(
                  filled: true,
                  fillColor: const Color(0xffffffff).withOpacity(0.2),
                  border: const OutlineInputBorder(
                    borderRadius: BorderRadius.all(
                      Radius.circular(10),
                    ),
                  ),
                  hintText: 'PW를 입력해주세요',
                  contentPadding: EdgeInsets.symmetric(
                    vertical: UIhelper.deviceHeight(context) * 0.02,
                    horizontal: UIhelper.deviceWidth(context) * 0.04,
                  ),
                ),
              ),
              SizedBox(height: UIhelper.deviceHeight(context) * 0.02),
              button714_150(
                lable: '로그인',
                backgroundColor: _colorAnimation.value,
                onPressed: () {
                  _handleLogin();
                },
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
    );
  }

  void _handleLogin() async {
    bool success =
        await _apiService.login(_idController.text, _pwController.text);
    if (success) {
      Navigator.push(
          context, MaterialPageRoute(builder: (context) => const HomeScreen()));
    } else {
      _animationController.forward(from: 0);
    }
  }
}
