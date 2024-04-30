import 'package:flutter/material.dart';
import 'package:porthole24/screens/Record/PotLess.dart';
import 'package:porthole24/screens/Record/RecordingMode2.dart';
import 'package:porthole24/screens/Works/WorkList.dart';
import 'package:porthole24/widgets/UI/ScreenSize.dart';
import 'package:porthole24/widgets/buttons/main_button.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
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
                style: TextStyle(fontSize: 32),
              ),
              const Text('도로파손 통합 관리 시스템'),
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
                label: '주행모드',
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => const CameraScreen(),
                    ),
                  );
                },
              ),
              SizedBox(
                height: UIhelper.deviceHeight(context) * 0.02,
              ),
              MainLarge(
                label: '작업목록',
                buttonImage: Image.asset('assets/icons/roller.png'),
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => const WorkListScreen(),
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
