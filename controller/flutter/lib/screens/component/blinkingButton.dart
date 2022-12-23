import 'package:flutter/material.dart';

class MyBlinkingButton extends StatefulWidget {
  final String indicator;

  const MyBlinkingButton(this.indicator, {super.key});

  @override
  _MyBlinkingButtonState createState() => _MyBlinkingButtonState();
}

class _MyBlinkingButtonState extends State<MyBlinkingButton>
    with SingleTickerProviderStateMixin {
  late AnimationController _animationController;

  @override
  void initState() {
    _animationController = AnimationController(
        vsync: this, duration: const Duration(milliseconds: 500));
    _animationController.repeat(reverse: true);
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return FadeTransition(
        opacity: _animationController,
        child: (widget.indicator == "LEFT")
            ? Image.asset(
                "images/left_indicator_icon_white.png",
                height: 23,
                width: 23,
              )
            : Image.asset(
                "images/right_indicator_icon_white.png",
                height: 23,
                width: 23,
              ));
  }

  @override
  void dispose() {
    _animationController.dispose();
    super.dispose();
  }
}
