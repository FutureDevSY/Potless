import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class StorageService {
  final _storage = const FlutterSecureStorage();

  Future<void> saveToken(String token, int role, String name) async {
    await _storage.write(key: 'accessToken', value: token);
    await _storage.write(key: 'role', value: role.toString());
    await _storage.write(key: 'name', value: name);
  }

  Future<String?> getToken() async {
    return await _storage.read(key: 'accessToken');
  }

  Future<String?> getRole() async {
    return await _storage.read(key: 'role');
  }

  Future<String?> getName() async {
    return await _storage.read(key: 'name');
  }

  Future<void> deleteToken() async {
    await _storage.delete(key: 'accessToken');
    await _storage.delete(key: 'role');
    await _storage.delete(key: 'name');
  }
}
