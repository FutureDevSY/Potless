import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:lottie/lottie.dart';
import 'package:potless/API/api_request.dart';
import 'package:potless/screens/Home/HomeScreen.dart';
import 'package:potless/widgets/UI/ScreenSize.dart';
import 'package:potless/widgets/buttons/714_150button.dart';

class SignUpScreen extends StatefulWidget {
  const SignUpScreen({super.key});

  @override
  State<SignUpScreen> createState() => _SignUpScreenState();
}

class _SignUpScreenState extends State<SignUpScreen> {
  final ApiService _apiService = ApiService();

  final TextEditingController _idController = TextEditingController();
  final TextEditingController _pwController = TextEditingController();
  final TextEditingController _confirmController = TextEditingController();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _phoneController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        color: Colors.white,
        padding: const EdgeInsets.all(20),
        child: Center(
          child: Column(
            children: [
              SizedBox(height: UIhelper.deviceHeight(context) * 0.1),
              const Text('회원가입', style: TextStyle(fontSize: 26)),
              InkWell(
                onTap: () {
                  _openInputModal(context, _idController, 'ID를 입력해주세요');
                },
                child: Container(
                  height: UIhelper.deviceHeight(context) * 0.08,
                  padding: const EdgeInsets.all(10),
                  decoration: BoxDecoration(
                    border: Border.all(color: Colors.blue),
                    borderRadius: BorderRadius.circular(10),
                  ),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Expanded(
                        child: Text(
                          _idController.text.isEmpty
                              ? 'ID를 입력해주세요'
                              : _idController.text,
                          style: const TextStyle(fontSize: 16),
                        ),
                      ),
                      const Icon(Icons.edit, color: Colors.blue),
                    ],
                  ),
                ),
              ),
              SizedBox(height: UIhelper.deviceHeight(context) * 0.02),
              InkWell(
                onTap: () {
                  _openInputModal(context, _pwController, '비밀번호를 입력해주세요');
                },
                child: Material(
                  type: MaterialType.transparency,
                  child: Container(
                    height: UIhelper.deviceHeight(context) * 0.08,
                    padding: const EdgeInsets.all(10),
                    decoration: BoxDecoration(
                      border: Border.all(color: Colors.blue),
                      borderRadius: BorderRadius.circular(10),
                    ),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Expanded(
                          child: Text(
                            _pwController.text.isEmpty
                                ? '비밀번호를 입력해주세요'
                                : '•' * _pwController.text.length,
                            style: const TextStyle(fontSize: 16),
                          ),
                        ),
                        const Icon(Icons.edit, color: Colors.blue),
                      ],
                    ),
                  ),
                ),
              ),
              SizedBox(height: UIhelper.deviceHeight(context) * 0.02),
              InkWell(
                onTap: () {
                  _openInputModal(
                      context, _confirmController, '비밀번호를 다시 입력해주세요');
                },
                child: Material(
                  type: MaterialType.transparency,
                  child: Container(
                    height: UIhelper.deviceHeight(context) * 0.08,
                    padding: const EdgeInsets.all(10),
                    decoration: BoxDecoration(
                      border: Border.all(color: Colors.blue),
                      borderRadius: BorderRadius.circular(10),
                    ),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Expanded(
                          child: Text(
                            _confirmController.text.isEmpty
                                ? '비밀번호를 다시 입력해주세요'
                                : '•' * _confirmController.text.length,
                            style: const TextStyle(fontSize: 16),
                          ),
                        ),
                        const Icon(Icons.edit, color: Colors.blue),
                      ],
                    ),
                  ),
                ),
              ),
              SizedBox(height: UIhelper.deviceHeight(context) * 0.02),
              InkWell(
                onTap: () {
                  _openInputModal(context, _nameController, '이름을 입력해주세요');
                },
                child: Material(
                  type: MaterialType.transparency,
                  child: Container(
                    height: UIhelper.deviceHeight(context) * 0.08,
                    padding: const EdgeInsets.all(10),
                    decoration: BoxDecoration(
                      border: Border.all(color: Colors.blue),
                      borderRadius: BorderRadius.circular(10),
                    ),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Expanded(
                          child: Text(
                            _nameController.text.isEmpty
                                ? '이름을 입력해주세요'
                                : _nameController.text,
                            style: const TextStyle(fontSize: 16),
                          ),
                        ),
                        const Icon(Icons.edit, color: Colors.blue),
                      ],
                    ),
                  ),
                ),
              ),
              SizedBox(height: UIhelper.deviceHeight(context) * 0.02),
              InkWell(
                onTap: () {
                  _openInputModal(context, _phoneController, '핸드폰 번호를 입력해주세요');
                },
                child: Material(
                  type: MaterialType.transparency,
                  child: Container(
                    height: UIhelper.deviceHeight(context) * 0.08,
                    padding: const EdgeInsets.all(10),
                    decoration: BoxDecoration(
                      border: Border.all(color: Colors.blue),
                      borderRadius: BorderRadius.circular(10),
                    ),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Expanded(
                          child: Text(
                            _phoneController.text.isEmpty
                                ? '핸드폰 번호를 입력해주세요'
                                : _phoneController.text,
                            style: const TextStyle(fontSize: 16),
                          ),
                        ),
                        const Icon(Icons.edit, color: Colors.blue),
                      ],
                    ),
                  ),
                ),
              ),
              SizedBox(height: UIhelper.deviceHeight(context) * 0.02),
              button714_150(
                lable: '회원가입',
                onPressed: _handleSignUp,
              ),
            ],
          ),
        ),
      ),
    );
  }

  void _handleSignUp() async {
    bool success = await _apiService.signUp(
      _idController.text,
      _pwController.text,
      _confirmController.text,
      _nameController.text,
      _phoneController.text,
    );

    if (success) {
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(
          builder: (context) => const HomeScreen(),
        ),
      );
    }
  }

  void _openInputModal(
      BuildContext context, TextEditingController controller, String hintText) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return Dialog(
          backgroundColor: Colors.transparent,
          insetPadding: const EdgeInsets.all(10),
          child: Center(
            child: Material(
              type: MaterialType.transparency,
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Container(
                    padding: const EdgeInsets.symmetric(
                        vertical: 20, horizontal: 30),
                    decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.circular(15),
                    ),
                    child: TextField(
                      autofocus: true,
                      controller: controller,
                      decoration: InputDecoration(
                        hintText: hintText,
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(10),
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(height: 20),
                  ElevatedButton(
                    onPressed: () => Navigator.of(context).pop(),
                    child: const Text('입력'),
                  )
                ],
              ),
            ),
          ),
        );
      },
    );
  }
}
