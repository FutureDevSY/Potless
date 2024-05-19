import 'dart:async';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:http/http.dart' as http;
import 'package:kakao_flutter_sdk/kakao_flutter_sdk.dart';
import 'package:potless/API/api_request.dart';
import 'package:flutter_background/flutter_background.dart';
import 'package:flutter_tts/flutter_tts.dart';

class RouteFinderScreen extends StatefulWidget {
  const RouteFinderScreen({super.key});

  @override
  State<RouteFinderScreen> createState() => _RouteFinderScreenState();
}

class _RouteFinderScreenState extends State<RouteFinderScreen> {
  final TextEditingController _addressController = TextEditingController();

  Position? _currentPosition;
  final String kakaoApiKey = 'b48ac6c3c645f21c0649b4cd7d438496';
  final FocusNode _focusNode = FocusNode();
  StreamSubscription<Position>? _positionStreamSubscription;
  List<Map<String, dynamic>> _potholes = [];
  List _searchResults = [];

  final ApiService _apiService = ApiService();
  final FlutterTts flutterTts = FlutterTts();

  @override
  void initState() {
    super.initState();
    _initializeBackground();
  }

  @override
  void dispose() {
    _focusNode.dispose();
    _positionStreamSubscription?.cancel();
    super.dispose();
  }
  Future<void> _initializeBackground() async {
    final androidConfig = FlutterBackgroundAndroidConfig(
      notificationTitle: "백그라운드 위치 추적",
      notificationText: "앱이 백그라운드에서 위치를 추적하고 있습니다.",
      notificationImportance: AndroidNotificationImportance.Default,
      enableWifiLock: true,
    );

    await FlutterBackground.initialize(androidConfig: androidConfig);

    _getCurrentLocation();
  }

  Future<void> _getCurrentLocation() async {
    bool serviceEnabled;
    LocationPermission permission;

    serviceEnabled = await Geolocator.isLocationServiceEnabled();
    if (!serviceEnabled) {
      print("Location services are not enabled");
      return;
    }

    permission = await Geolocator.checkPermission();
    if (permission == LocationPermission.denied) {
      permission = await Geolocator.requestPermission();
      if (permission == LocationPermission.denied) {
        print("Location permissions are denied");
        return;
      }
    }

    if (permission == LocationPermission.deniedForever) {
      print("Location permissions are permanently denied");
      return;
    }

    permission = await Geolocator.requestPermission(); // 백그라운드 권한 요청 추가

    if (permission == LocationPermission.whileInUse || permission == LocationPermission.always) {
      Position position = await Geolocator.getCurrentPosition();
      setState(() {
        _currentPosition = position;
      });
      print("Current location: $_currentPosition");
    }
  }

  Future<void> _searchAddress() async {
    final query = _addressController.text;
    if (query.isEmpty) {
      return;
    }
    FocusScope.of(context).unfocus();

    final response = await http.get(
      Uri.https(
          'dapi.kakao.com', '/v2/local/search/keyword.json', {'query': query}),
      headers: {'Authorization': 'KakaoAK $kakaoApiKey'},
    );

    if (response.statusCode == 200) {
      setState(() {
        _searchResults = json.decode(response.body)['documents'];
      });
    } else {
      // Handle error
      print('Error 89: ${response.statusCode}');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Route Finder')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              controller: _addressController,
              focusNode: _focusNode,
              decoration: InputDecoration(
                labelText: '주소 입력',
                suffixIcon: IconButton(
                  icon: const Icon(Icons.search),
                  onPressed: _searchAddress,
                ),
              ),
            ),
            const SizedBox(height: 20),
            Expanded(
              child: ListView.builder(
                itemCount: _searchResults.length,
                itemBuilder: (context, index) {
                  final result = _searchResults[index];
                  return ListTile(
                    title: Text(result['place_name']),
                    subtitle: Text(result['address_name']),
                    onTap: () => _showConfirmationDialog(
                      result['place_name'],
                      double.parse(result['x']),
                      double.parse(result['y']),
                    ),
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }

  void _showPotholeAlert(int potholeCount) {
    flutterTts.speak('근처에 포트홀이 있습니다. 주의하세요!');
  }

  void _showModalDialog(String name, double x, double y, int potholeCount, String message) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text('알림'),
          content: Text('감지된 포트홀 : $potholeCount 개\n$message\n\n길안내를 시작할까요?'),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.of(context).pop();
                // Dismiss keyboard if it's open
                _focusNode.unfocus();
              },
              child: Text('아니요'),
            ),
            TextButton(
              onPressed: () async {
                Navigator.of(context).pop();
                // Dismiss keyboard if it's open
                _focusNode.unfocus();
                if (await NaviApi.instance.isKakaoNaviInstalled()) {
                  await NaviApi.instance.navigate(
                    destination: Location(name: name, x: x.toString(), y: y.toString()),
                    option: NaviOption(coordType: CoordType.wgs84, routeInfo: true),
                  );
                } else {
                  launchBrowserTab(Uri.parse(NaviApi.webNaviInstall));
                }
                _startLocationUpdates();
              },
              child: Text('네'),
            ),
          ],
        );
      },
    );
  }

  void _startLocationUpdates() async {
    await FlutterBackground.enableBackgroundExecution();

    const double alertDistance = 20.0; // meters

    LocationSettings locationSettings = LocationSettings(
      accuracy: LocationAccuracy.best,
      distanceFilter: 10, // meters
    );

    _positionStreamSubscription = Geolocator.getPositionStream(
      locationSettings: locationSettings,
    ).listen((Position position) {
      for (var pothole in _potholes) {
        double distance = Geolocator.distanceBetween(
          position.latitude,
          position.longitude,
          pothole['dirY'],
          pothole['dirX'],
        );

        if (distance <= alertDistance) {
          _showPotholeAlert(_potholes.length);
          break;
        }
      }
    });

  }

  Future<void> _confirmSearch(String name, double x, double y) async {
    if (_currentPosition == null) {
      print("Current position is null");
      return;
    }

    final startX = _currentPosition!.longitude;
    final startY = _currentPosition!.latitude;

    try {
      final res = await _apiService.findRoute(startX, startY, x, y);
      if (res != null) {
        final kakaoResponse = jsonDecode(res['kakaoResponse']);
        setState(() {
          _potholes = List<Map<String, dynamic>>.from(res['results']);
        });

        int potholeCount = _potholes.length;
        String message;
        if (potholeCount == 0) {
          message = '포트홀이 존재하지 않습니다.';
        } else if (potholeCount <= 10) {
          message = '경로에 포트홀이 있습니다.';
        } else {
          message = '포트홀이 다수 존재합니다.';
        }
        _showModalDialog(name, x, y, potholeCount, message);
      }
    } catch (E) {
      debugPrint('Err247: $E');
    }
  }

  void _showConfirmationDialog(String name, double x, double y) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text('검색 확인'),
          content: Text('목적지: $name\n검색하시겠습니까?'),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.of(context).pop();
                // Dismiss keyboard if it's open
                _focusNode.unfocus();
              },
              child: Text('아니요'),
            ),
            TextButton(
              onPressed: () {
                Navigator.of(context).pop();
                _confirmSearch(name, x, y);
                // Dismiss keyboard if it's open
                _focusNode.unfocus();
              },
              child: Text('네'),
            ),
          ],
        );
      },
    );
  }
}
