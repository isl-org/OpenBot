import 'package:flutter/material.dart';

class OnScreenIcon extends StatefulWidget {
  const OnScreenIcon({super.key});

  @override
  State<StatefulWidget> createState() {
    return OnScreenIconState();
  }
}

class OnScreenIconState extends State<OnScreenIcon> {
  bool pause = false;
  bool speaker = false;
  bool camera = false;
  bool leftIndicator = false;
  bool rightIndicator = false;

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      child: Row(
        // crossAxisAlignment: CrossAxisAlignment.start,
        // mainAxisAlignment: MainAxisAlignment.end,
        children: [
          GestureDetector(
              onTap: () {
                setState(() {
                  pause = !pause;
                });
              }, // Image tapped
              child: Container(
                padding: const EdgeInsets.all(15),
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(45),
                  color: pause
                      ? const Color(0xFF0071C5).withOpacity(0.5)
                      : Colors.white.withOpacity(0.5),
                ),
                child: Image.asset(
                  pause
                      ? "images/pause_icon_white.png"
                      : "images/pause_icon_blue.png",
                  height: 23,
                  width: 23,
                ),
              )),
          const SizedBox(
            width: 15,
          ),
          GestureDetector(
              onTap: () {
                setState(() {
                  speaker = !speaker;
                });
              }, // Image tapped
              child: Container(
                // height: 50,
                // width: 50,
                padding: const EdgeInsets.all(15),
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(45),
                  color: speaker
                      ? const Color(0xFF0071C5).withOpacity(0.5)
                      : Colors.white.withOpacity(0.5),
                ),
                child: Image.asset(
                  speaker
                      ? "images/speaker_icon_white.png"
                      : "images/speaker_icon_blue.png",
                  height: 23,
                  width: 23,
                ),
              )),
          const SizedBox(
            width: 15,
          ),
          GestureDetector(
              onTap: () {
                print("{command: SWITCH_CAMERA}");
                setState(() {
                  camera = !camera;
                });
              }, // Image tapped
              child: Container(
                // height: 50,
                // width: 50,
                padding: const EdgeInsets.all(15),
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(45),
                  color: camera
                      ? const Color(0xFF0071C5).withOpacity(0.5)
                      : Colors.white.withOpacity(0.5),
                ),
                child: Image.asset(
                  camera
                      ? "images/camera_icon_white.png"
                      : "images/camera_icon_blue.png",
                  height: 23,
                  width: 23,
                ),
              )),
          const SizedBox(
            width: 15,
          ),
          GestureDetector(
              onTap: () {
                if(leftIndicator) {
                  print("{command: INDICATOR_STOP}");
                } else {
                  print("{command: INDICATOR_LEFT}");
                }
                setState(() {
                  leftIndicator = !leftIndicator;
                });
              }, // Image tapped
              child: Container(
                // height: 50,
                // width: 50,
                padding: const EdgeInsets.all(15),
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(45),
                  color: leftIndicator
                      ? const Color(0xFF0071C5).withOpacity(0.5)
                      : Colors.white.withOpacity(0.5),
                ),
                child: Image.asset(
                  leftIndicator
                      ? "images/left_indicator_icon_white.png"
                      : "images/left_indicator_icon_blue.png",
                  height: 23,
                  width: 23,
                ),
              )),
          const SizedBox(
            width: 15,
          ),
          GestureDetector(
              onTap: () {
                if(rightIndicator) {
                  print("{command: INDICATOR_STOP}");
                } else {
                  print("{command: INDICATOR_RIGHT}");
                }
                setState(() {
                  rightIndicator = !rightIndicator;
                });
              }, // Image tapped
              child: Container(
                // height: 50,
                // width: 50,
                padding: const EdgeInsets.all(15),
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(45),
                  color: rightIndicator
                      ? const Color(0xFF0071C5).withOpacity(0.5)
                      : Colors.white.withOpacity(0.5),
                ),
                child: Image.asset(
                  rightIndicator
                      ? "images/right_indicator_icon_white.png"
                      : "images/right_indicator_icon_blue.png",
                  height: 23,
                  width: 23,
                ),
              )),
        ],
      ),
    );
  }
}
