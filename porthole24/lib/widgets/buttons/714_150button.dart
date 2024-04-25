import 'package:flutter/material.dart';
import 'package:porthole24/widgets/UI/ScreenSize.dart';

class button714_150 extends StatefulWidget {
  final String lable;
  final void Function()? onPressed;

  const button714_150({
    super.key,
    required this.lable,
    this.onPressed,
  });

  @override
  State<button714_150> createState() => _button714_150State();
}

class _button714_150State extends State<button714_150> {
  @override
  Widget build(BuildContext context) {
    final ButtonStyle style = ElevatedButton.styleFrom(
      elevation: 5,
      padding: const EdgeInsets.all(10),
      backgroundColor: Colors.white,
      foregroundColor: Colors.black,
      textStyle: const TextStyle(fontSize: 22),
      fixedSize: Size(
        UIhelper.scaleWidth(context) * 180,
        UIhelper.scaleHeight(context) * 65,
      ),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
    );
    return ElevatedButton(
      onPressed: widget.onPressed,
      style: style,
      child: Text(widget.lable),
    );
  }
}
