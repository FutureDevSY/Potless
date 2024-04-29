import 'dart:convert';
import 'package:http/http.dart' as http;
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
        print('Login failed: ${response.body}');
        return false;
      }
    } catch (e) {
      print('Login error: $e');
      return false;
    }
  }

  // Future<bool> damageSet(String dtype, String x, String y, Xfile)
}
