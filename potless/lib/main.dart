import 'package:flutter/material.dart';
import 'package:camera/camera.dart';
import 'package:kakao_map_plugin/kakao_map_plugin.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:porthole24/screens/Loading/LoadingScreen.dart';
import 'package:porthole24/screens/Login/Login.dart';

late List<CameraDescription> cameras;

Future<void> main() async {
  AuthRepository.initialize(appKey: '97b4e3e8c18f71505423fdd035c848ab');

  WidgetsFlutterBinding.ensureInitialized();
  cameras = await availableCameras();
  // await requestStoragePermission();
  await requestPermissions();
  runApp(const MyApp());
}

Future<bool> requestStoragePermission() async {
  var status = await Permission.storage.status;
  if (!status.isGranted) {
    status = await Permission.storage.request();
  }
  return status.isGranted;
}

Future<void> requestPermissions() async {
  await [
    Permission.camera,
    Permission.storage,
    // Add other permissions as needed
  ].request();
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        theme: ThemeData(fontFamily: 'Hanbit'),
        themeMode: ThemeMode.system,
        // home: const LoginScreen(),
        home: const LoadingScreen());
  }
}
