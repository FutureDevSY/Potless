import 'dart:convert';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:http/http.dart' as http;
import 'package:http_parser/http_parser.dart' as mime;
import 'package:potless/API/login.dart';
import 'package:potless/models/pothole.dart';

class ApiService {
  // static const String _baseUrl = "http://192.168.117.165:7080/api";
  // static const String _baseUrl = "http://192.168.117.36:7080/api";
  static const String _baseUrl = "https://api.potless.co.kr/api";
  final StorageService _storageService = StorageService();

  Future<bool> signUp(String email, String password, String passwordConfirm,
      String memberName, String phone) async {
    try {
      var res = await http.post(Uri.parse('$_baseUrl/member/signup'),
          headers: {
            'Content-Type': 'application/json',
          },
          body: jsonEncode({
            'email': email,
            'password': password,
            'passwordConfirm': passwordConfirm,
            'memberName': memberName,
            'phone': phone,
          }));
      if (res.statusCode == 200) {
        login(email, password);
        return true;
      } else {
        debugPrint('APIservice 33: ${res.body}');
        return false;
      }
    } catch (E) {
      debugPrint('APIservice 36: $E');
      return false;
    }
  }

  Future<bool> login(String id, String password) async {
    try {
      var response = await http.post(
        Uri.parse('$_baseUrl/member/login-app'),
        headers: {
          'Content-Type': 'application/json',
        },
        body: jsonEncode({
          'email': id,
          'password': password,
        }),
      );

      if (response.statusCode == 200) {
        var res = jsonDecode(response.body);
        var res2 = jsonDecode(utf8.decode(response.bodyBytes));

        await _storageService.saveToken(
            res['data']['token'],
            res['data']['memberInfo']['role'],
            res2['data']['memberInfo']['memberName']);
        debugPrint('login 31 ${res['data']['token']}');
        return true;
      } else {
        debugPrint('APIservice 31: ${response.body}');
        return false;
      }
    } catch (e) {
      debugPrint('api service 34 로그인 에러: $e');
      return false;
    }
  }

  Future<bool> checkTokenValidity() async {
    String? token = await _storageService.getToken();
    debugPrint('APIservice 토큰 42 $token');
    if (token == null) return false;

    try {
      var response = await http.post(
        Uri.parse('$_baseUrl/member/refresh'),
        headers: {
          'Authorization': 'Bearer $token',
          'Content-Type': 'application/json',
        },
        body: jsonEncode({}),
      );
      debugPrint((response.body));
      debugPrint((response.statusCode.toString()));

      if (response.statusCode == 200) {
        var res = jsonDecode(utf8.decode(response.bodyBytes));
        var res2 = jsonDecode(utf8.decode(response.bodyBytes));
        if (res['token'] != null) {
          await _storageService.saveToken(
            res2['data']['memberInfo']['memberName'],
            res['token'],
            res['data']['memberInfo']['role'],
          );
        }
        return true;
      }
      return false;
    } catch (e) {
      debugPrint('APIservice 62: $e');
      return false;
    }
  }

  Future<bool> logout() async {
    String? token = await _storageService.getToken();
    if (token == null) return false;

    try {
      var response = await http.post(
        Uri.parse('$_baseUrl/member/logout-app'),
        headers: {
          'Authorization': 'Bearer $token',
          'Content-Type': 'application/json',
        },
        body: jsonEncode({}),
      );
      debugPrint((response.body));
      debugPrint((response.statusCode.toString()));

      if (response.statusCode == 200) {
        await _storageService.deleteToken();
        return true;
      }
      return false;
    } catch (e) {
      debugPrint('APIservice 62: $e');
      return false;
    }
  }

  Future<bool> damageSet(String dtype, double x, double y, File image) async {
    try {
      String? token = await _storageService.getToken();

      if (token == null) {
        debugPrint('No token found in secure storage.');
        return false;
      }

      var request =
          http.MultipartRequest('POST', Uri.parse('$_baseUrl/damage/set'))
            ..headers['Authorization'] = 'Bearer $token';

      request.fields['dtype'] = dtype;
      request.fields['x'] = x.toString();
      request.fields['y'] = y.toString();

      List<File> files = [image];

      for (File file in files) {
        request.files.add(
          await http.MultipartFile.fromPath(
            'files',
            file.path,
            contentType: mime.MediaType('image', 'jpg'),
          ),
        );
      }

      var response = await request.send();
      var res = await http.Response.fromStream(response);

      if (res.statusCode == 200) {
        return true;
      } else {
        debugPrint(res.statusCode.toString());
        debugPrint("APIservice 96: ${res.body}");
      }
      return false;
    } catch (e) {
      debugPrint('APIservice 100: $e');
      return false;
    }
  }

  Future<bool> damageDuring(String damageId, File image) async {
    try {
      String? token = await _storageService.getToken();

      if (token == null) {
        debugPrint('No token found in secure storage.');
        return false;
      }

      var request = http.MultipartRequest(
          'POST', Uri.parse('$_baseUrl/damage/set/during'))
        ..headers['Authorization'] = 'Bearer $token';

      request.fields['damageId'] = damageId;

      List<File> files = [image];

      for (File file in files) {
        request.files.add(
          await http.MultipartFile.fromPath(
            'files',
            file.path,
            contentType: mime.MediaType('image', 'jpg'),
          ),
        );
      }

      var response = await request.send();
      var res = await http.Response.fromStream(response);

      if (res.statusCode == 200) {
        return true;
      } else {
        debugPrint(res.statusCode.toString());
        debugPrint("APIservice 96: ${res.body}");
      }
      return false;
    } catch (e) {
      debugPrint('APIservice 100: $e');
      return false;
    }
  }

  Future<bool> damageAfter(String damageId, File image) async {
    try {
      String? token = await _storageService.getToken();

      if (token == null) {
        debugPrint('No token found in secure storage.');
        return false;
      }

      var request =
          http.MultipartRequest('POST', Uri.parse('$_baseUrl/damage/set/after'))
            ..headers['Authorization'] = 'Bearer $token';

      request.fields['damageId'] = damageId;

      List<File> files = [image];

      for (File file in files) {
        request.files.add(
          await http.MultipartFile.fromPath(
            'files',
            file.path,
            contentType: mime.MediaType('image', 'jpg'),
          ),
        );
      }

      var response = await request.send();
      var res = await http.Response.fromStream(response);

      if (res.statusCode == 200) {
        return true;
      } else {
        debugPrint(res.statusCode.toString());
        debugPrint("APIservice 96: ${res.body}");
      }
      return false;
    } catch (e) {
      debugPrint('APIservice 100: $e');
      return false;
    }
  }

  Future<bool> workDone(int damageId) async {
    String? token = await _storageService.getToken();

    if (token == null) {
      debugPrint('No token found in secure storage.');
      return false;
    }

    try {
      var response = await http.post(
        Uri.parse('$_baseUrl/damage/workDone'),
        headers: {
          'Authorization': 'Bearer $token',
          'Content-Type': 'application/json',
        },
        body: jsonEncode({"damageId": damageId}),
      );

      if (response.statusCode == 200) {
        debugPrint('api252 : done');
        return true;
      } else {
        debugPrint(response.statusCode.toString());
        debugPrint(response.body);
      }
      return false;
    } catch (e) {
      debugPrint('APIservice 256: $e');
      return false;
    }
  }

  Future<bool> projectDone(int projectId) async {
    String? token = await _storageService.getToken();

    if (token == null) {
      debugPrint('No token found in secure storage.');
      return false;
    }

    try {
      var response = await http.post(
        Uri.parse('$_baseUrl/project/workDone'),
        headers: {
          'Authorization': 'Bearer $token',
          'Content-Type': 'application/json',
        },
        body: jsonEncode({"projectId": projectId}),
      );

      if (response.statusCode == 200) {
        debugPrint('api317 : done');
        return true;
      } else {
        debugPrint(response.statusCode.toString());
        debugPrint(response.body);
      }
      return false;
    } catch (e) {
      debugPrint('APIservice 325: $e');
      return false;
    }
  }

  Future<List<Project>> fetchProject() async {
    String? token = await _storageService.getToken();

    if (token == null) {
      debugPrint('No token found in secure storage.');
      return [];
    }

    try {
      var res = await http.get(
        Uri.parse('$_baseUrl/member/task'),
        headers: {
          'Authorization': 'Bearer $token',
          'Content-Type': 'application/json',
        },
      );

      if (res.statusCode == 200) {
        var resBody = utf8.decode(res.bodyBytes);
        var data = json.decode(resBody) as Map<String, dynamic>;

        List<dynamic> projectsJson = data['data'];
        List<Project> projects = projectsJson
            .map((projectsJson) => Project.fromJson(projectsJson))
            .toList();

        return projects;
      } else {
        debugPrint('Failed to load data: ${res.statusCode}');
        return [];
      }
    } catch (e) {
      debugPrint('APIservice 290: $e');
      return [];
    }
  }

  Future<Map<String, dynamic>?> findRoute(
      double startX, double startY, double endX, double endY) async {
    String? token = await _storageService.getToken();

    final response = await http.post(
      Uri.parse('$_baseUrl/flutter/find_route'),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
      body: jsonEncode({
        'startX': startX,
        'startY': startY,
        'endX': endX,
        'endY': endY,
      }),
    );

    if (response.statusCode == 200) {
      final responseData = jsonDecode(response.body)['data'];
      return responseData;
    } else {
      print('Error: ${response.statusCode}');
      return null;
    }
  }
}
