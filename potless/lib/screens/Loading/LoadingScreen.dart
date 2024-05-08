import 'package:flutter/material.dart';
import 'package:potless/API/api_request.dart';
import 'package:potless/screens/Home/HomeScreen.dart';
import 'package:potless/screens/Login/Login.dart';
import 'package:potless/widgets/UI/ScreenSize.dart';
import 'package:lottie/lottie.dart';

class LoadingScreen extends StatefulWidget {
  const LoadingScreen({super.key});

  @override
  State<LoadingScreen> createState() => _LoadingScreenState();
}

class _LoadingScreenState extends State<LoadingScreen> {
  final ApiService _apiService = ApiService();

  @override
  void initState() {
    super.initState();
    verifyToken();
  }

  void verifyToken() async {
    bool isValid = await _apiService.checkTokenValidity();
    if (isValid) {
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(builder: (context) => const HomeScreen()),
      );
    } else {
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(builder: (context) => const LoginScreen()),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(20),
      color: Colors.white,
      child: Column(
        children: [
          SizedBox(
            height: UIhelper.deviceHeight(context) * 0.3,
          ),
          const SizedBox(
              child: Text(
            '포트홀 24',
            style: TextStyle(
              fontFamily: 'Pretendard',
              color: Color(0xff000000),
            ),
          )),
          Lottie.asset('assets/lottie/loading_truck.json')
        ],
      ),
    );
  }
}
