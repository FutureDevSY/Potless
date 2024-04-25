import 'package:flutter/material.dart';
import 'package:porthole24/widgets/UI/ScreenSize.dart';

class MainLarge extends StatefulWidget {
  final String label;
  final Widget? buttonImage;
  final void Function()? onPressed;

  const MainLarge({
    super.key,
    required this.label,
    this.buttonImage,
    this.onPressed,
  });

  @override
  State<MainLarge> createState() => MainLargeState();
}

class MainLargeState extends State<MainLarge> {
  @override
  Widget build(BuildContext context) {
    final ButtonStyle style = ElevatedButton.styleFrom(
      elevation: 5,
      padding: const EdgeInsets.all(20),
      backgroundColor: Colors.white,
      foregroundColor: Colors.black,
      textStyle: const TextStyle(fontSize: 24, fontWeight: FontWeight.w900),
      fixedSize: Size(
        UIhelper.deviceWidth(context) * .8,
        UIhelper.deviceHeight(context) * .15,
      ),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
    );

    return ElevatedButton(
      style: style,
      onPressed: widget.onPressed,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Column(
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  Text(
                    widget.label,
                  ),
                  const SizedBox(
                    height: 20,
                  )
                ],
              ),
              if (widget.buttonImage != null)
                Column(
                  mainAxisAlignment: MainAxisAlignment.end,
                  children: [
                    const SizedBox(
                      height: 20,
                    ),
                    widget.buttonImage!,
                  ],
                )
            ],
          ),
        ],
      ),
    );
  }
}
