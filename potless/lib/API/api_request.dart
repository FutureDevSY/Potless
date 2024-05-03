import 'dart:convert';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:http/http.dart' as http;
import 'package:http_parser/http_parser.dart' as mime;
import 'package:permission_handler/permission_handler.dart';
import 'package:porthole24/API/login.dart';

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
}
