import 'dart:convert';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:http/http.dart' as http;
import 'package:http_parser/http_parser.dart' as mime;
import 'package:permission_handler/permission_handler.dart';
import 'package:potless/API/login.dart';
import 'package:potless/models/pothole.dart';

class ApiService {
  static const String _baseUrl = "https://api.potless.co.kr/api";
  final StorageService _storageService = StorageService();

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
        await _storageService.saveToken(res['data']['token']);
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
        var res = jsonDecode(response.body);
        if (res['token'] != null) {
          await _storageService.saveToken(res['token']);
        }
        return true;
      }
      // debugPrint(jsonDecode(response.body));
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

        // Correct access to the list
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
}
