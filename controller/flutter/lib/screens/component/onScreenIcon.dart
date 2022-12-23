import 'package:flutter/material.dart';
import 'package:openbot_controller/buttonCommands/leftIndicator.dart';
import 'package:openbot_controller/buttonCommands/rightIndicator.dart';
import 'package:openbot_controller/buttonCommands/stopIndicator.dart';
import 'package:openbot_controller/buttonCommands/switchCamera.dart';
import 'package:openbot_controller/screens/component/blinkingButton.dart';

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
  bool leftIndicator = false;
  bool rightIndicator = false;

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      child: Row(
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
          InkWell(
              borderRadius: const BorderRadius.all(Radius.circular(45)),
              onTap: () {
                SwitchCamera().toSwitchCamera();
              }, // Image tapped
              child: Container(
                padding: const EdgeInsets.all(15),
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(45),
                  color: Colors.white.withOpacity(0.5),
                ),
                child: Image.asset(
                  "images/camera_icon_blue.png",
                  height: 23,
                  width: 23,
                ),
              )),
          const SizedBox(
            width: 15,
          ),
          GestureDetector(
              onTap: () {
                if (rightIndicator) {
                  StopIndicator().toStopIndicator();
                  LeftIndicator().toLeftIndicator();
                } else {
                  if (leftIndicator) {
                    StopIndicator().toStopIndicator();
                  } else {
                    LeftIndicator().toLeftIndicator();
                  }
                }
                setState(() {
                  if (rightIndicator) {
                    rightIndicator = false;
                    leftIndicator = true;
                  } else {
                    if (leftIndicator) {
                      leftIndicator = false;
                    } else {
                      leftIndicator = true;
                    }
                  }
                });
              }, // Image tapped
              child: Container(
                padding: const EdgeInsets.all(15),
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(45),
                  color: leftIndicator
                      ? const Color(0xFF0071C5).withOpacity(0.5)
                      : Colors.white.withOpacity(0.5),
                ),
                child: leftIndicator
                    ? const MyBlinkingButton("LEFT")
                    : Image.asset(
                        "images/left_indicator_icon_blue.png",
                        height: 23,
                        width: 23,
                      ),
              )),
          const SizedBox(
            width: 15,
          ),
          GestureDetector(
              onTap: () {
                if (leftIndicator) {
                  StopIndicator().toStopIndicator();
                  RightIndicator().toRightIndicator();
                } else {
                  if (rightIndicator) {
                    StopIndicator().toStopIndicator();
                  } else {
                    RightIndicator().toRightIndicator();
                  }
                }
                setState(() {
                  if (leftIndicator) {
                    leftIndicator = false;
                    rightIndicator = true;
                  } else {
                    if (rightIndicator) {
                      rightIndicator = false;
                    } else {
                      rightIndicator = true;
                    }
                  }
                });
              }, // Image tapped
              child: Container(
                padding: const EdgeInsets.all(15),
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(45),
                  color: rightIndicator
                      ? const Color(0xFF0071C5).withOpacity(0.5)
                      : Colors.white.withOpacity(0.5),
                ),
                child: rightIndicator
                    ? const MyBlinkingButton("RIGHT")
                    : Image.asset(
                        "images/right_indicator_icon_blue.png",
                        height: 23,
                        width: 23,
                      ),
              )),
        ],
      ),
    );
  }
}
