import 'package:flutter/material.dart';

class InputPage extends StatefulWidget {
  final TextEditingController controller;
  final String hintText;
  final String heroTag;

  const InputPage({
    super.key,
    required this.controller,
    required this.hintText,
    required this.heroTag,
  });

  @override
  State<InputPage> createState() => _InputPageState();
}

class _InputPageState extends State<InputPage> {
  late FocusNode _focusNode;
  bool _isKeyboardVisible = true;

  @override
  void initState() {
    super.initState();
    _focusNode = FocusNode();

    // Adding a delay to ensure the transition is complete before focusing
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (mounted) {
        FocusScope.of(context).requestFocus(_focusNode);
        setState(() {
          _isKeyboardVisible = true;
        });
      }
    });

    _focusNode.addListener(() {
      if (!_focusNode.hasFocus && _isKeyboardVisible) {
        Navigator.of(context).pop();
      }
    });
  }

  @override
  void dispose() {
    _focusNode.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Input"),
        backgroundColor: const Color(0xffffffff),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () {
            if (_focusNode.hasFocus) {
              _focusNode.unfocus();
            } else {
              Navigator.of(context).pop();
            }
          },
        ),
      ),
      body: Container(
        padding: const EdgeInsets.all(20),
        alignment: Alignment.topCenter,
        child: Hero(
          tag: widget.heroTag,
          child: Material(
            type: MaterialType.transparency,
            child: TextField(
              focusNode: _focusNode,
              controller: widget.controller,
              autofocus: _isKeyboardVisible,
              decoration: InputDecoration(
                filled: true,
                fillColor: Colors.white.withOpacity(0.2),
                border: const OutlineInputBorder(
                  borderRadius: BorderRadius.all(Radius.circular(10)),
                ),
                hintText: widget.hintText,
              ),
            ),
          ),
        ),
      ),
    );
  }
}
