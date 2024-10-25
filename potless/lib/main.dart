import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:camera/camera.dart';
import 'package:kakao_flutter_sdk/kakao_flutter_sdk.dart';
import 'package:kakao_map_plugin/kakao_map_plugin.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:potless/screens/Loading/LoadingScreen.dart';

late List<CameraDescription> cameras;

Future<void> main() async {
  AuthRepository.initialize(appKey: '97b4e3e8c18f71505423fdd035c848ab');

  KakaoSdk.init(
    nativeAppKey: '5f99dc12b71c122bea77a7d24d0f50e0',
    javaScriptAppKey: '97b4e3e8c18f71505423fdd035c848ab',
  );

  WidgetsFlutterBinding.ensureInitialized();
  DartPluginRegistrant.ensureInitialized();
  cameras = await availableCameras();
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
    Permission.location,
    Permission.ignoreBatteryOptimizations
  ].request();
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        theme: ThemeData(fontFamily: 'Pretendard'),
        themeMode: ThemeMode.system,
        // home: const LoginScreen(),
        home: const LoadingScreen());
  }
}
