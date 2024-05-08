import 'package:flutter/material.dart';
import 'package:potless/widgets/UI/ScreenSize.dart';

class button714_150 extends StatefulWidget {
  final String lable;
  final void Function()? onPressed;
  final Color? backgroundColor;
  final Matrix4? transform;

  const button714_150({
    super.key,
    required this.lable,
    this.onPressed,
    this.backgroundColor = const Color(0xff151C62), // Default color
    this.transform,
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
      backgroundColor: widget.backgroundColor,
      foregroundColor: const Color(0xffffffff),
      textStyle: const TextStyle(fontSize: 22),
      fixedSize: Size(
        UIhelper.scaleWidth(context) * 180,
        UIhelper.scaleHeight(context) * 65,
      ),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
    );
    return Transform(
      transform: widget.transform ?? Matrix4.identity(),
      child: ElevatedButton(
        onPressed: widget.onPressed,
        style: style,
        child: Text(widget.lable),
      ),
    );
  }
}
