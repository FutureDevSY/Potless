import 'package:flutter/material.dart';
import 'package:lottie/lottie.dart';
import 'package:porthole24/screens/Home/HomeScreen.dart';
import 'package:porthole24/widgets/UI/ScreenSize.dart';
import 'package:porthole24/widgets/buttons/714_150button.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final TextEditingController _idController = TextEditingController();
  final TextEditingController _pwController = TextEditingController();

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
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => const HomeScreen(),
                    ),
                  );
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
}
