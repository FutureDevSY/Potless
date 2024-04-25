import 'package:flutter/material.dart';
import 'package:porthole24/screens/Home/HomeScreen.dart';
import 'package:porthole24/widgets/UI/ScreenSize.dart';

class LoadingScreen extends StatefulWidget {
  const LoadingScreen({super.key});

  @override
  State<LoadingScreen> createState() => _LoadingScreenState();
}

class _LoadingScreenState extends State<LoadingScreen> {
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
          const SizedBox(child: Text('포트홀 24')),
          ElevatedButton(
              onPressed: () {
                Navigator.pushReplacement(
                  context,
                  MaterialPageRoute(
                    builder: (context) => const HomeScreen(),
                  ),
                );
              },
              child: const Text(
                '폰트 변경 왜 안돼',
                style: TextStyle(fontSize: 20, fontFamily: 'Pretendard'),
              ))
        ],
      ),
    );
  }
}
