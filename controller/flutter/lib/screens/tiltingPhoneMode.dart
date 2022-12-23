import 'dart:async';
import 'package:flutter/material.dart';
import 'package:openbot_controller/utils/forwardSpeed.dart';
import 'package:openbot_controller/utils/phoneSensorToDualDriveConvertor.dart';
import 'package:sensors_plus/sensors_plus.dart';

import 'driveCommandReducer.dart';

class TiltingPhoneMode extends StatefulWidget {
  const TiltingPhoneMode({super.key});

  @override
  State<StatefulWidget> createState() {
    return TiltingPhoneModeState();
  }
}

class TiltingPhoneModeState extends State<TiltingPhoneMode> {
  Timer? forwardSpeedTimer;
  Timer? backwardSpeedTimer;
  bool forward = false;
  bool reverse = false;
  late double azimuth;
  late double pitch;
  late double roll;
  var phoneAccelerometerToDualDriveConverted =
      PhoneSensorToDualDriveConvertor();
  double leftSpeedValue = 0;
  double rightSpeedValue = 0;

  @override
  void initState() {
    accelerometerEvents.listen((event) {
      print("object = , $event");
      azimuth = event.x;
      pitch = event.y;
      roll = event.z;
      var sliderValues =
          phoneAccelerometerToDualDriveConverted.convert(azimuth, pitch, roll);
      leftSpeedValue = sliderValues.left;
      rightSpeedValue = sliderValues.right;
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        alignment: Alignment.center,
        color: const Color(0xFF202020),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            GestureDetector(
                onTap: () {
                  Navigator.pop(context);
                },
                child: Container(
                  alignment: Alignment.topRight,
                  padding: const EdgeInsets.fromLTRB(0, 20, 20, 0),
                  child: Image.asset(
                    "images/cross_icon.png",
                    height: 55,
                    width: 55,
                  ),
                )),
            Expanded(
                child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                GestureDetector(
                    onTap: () {
                      print("pressed on reverse");
                      ForwardSpeed.reset();
                      setState(() {});
                    },
                    onLongPressStart: (detail) {
                      setState(() {
                        backwardSpeedTimer = Timer.periodic(
                            const Duration(milliseconds: 333), (t) {
                          reverse = !reverse;
                          double decrementSpeed = ForwardSpeed.minNegative / 3;
                          ForwardSpeed.decrementNegative(decrementSpeed);
                          DriveCommandReducer.filter(
                              rightSpeedValue, leftSpeedValue);
                        });
                      });
                    },
                    onLongPressEnd: (detail) {
                      if (backwardSpeedTimer != null) {
                        ForwardSpeed.reset();
                        DriveCommandReducer.filter(0, 0);
                        backwardSpeedTimer!.cancel();
                      }
                    },
                    // Image tapped
                    child: Container(
                      margin: const EdgeInsets.fromLTRB(20, 0, 0, 0),
                      // padding: const EdgeInsets.all(10),
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(5),
                        color: forward
                            ? const Color(0xFF292929).withOpacity(0.8)
                            : const Color(0xFF292929).withOpacity(0.3),
                      ),
                      child: Image.asset(
                        "images/reverse_icon.png",
                        height: 80,
                        width: 64,
                      ),
                    )),
                GestureDetector(
                    onTap: () {
                      setState(() {
                        forward = !forward;
                        print("forward tapped");
                        ForwardSpeed.reset();
                      });
                    },
                    onLongPressStart: (detail) {
                      setState(() {
                        forwardSpeedTimer = Timer.periodic(
                            const Duration(milliseconds: 200), (t) {
                          forward = !forward;
                          double incrementValue =
                              (ForwardSpeed.max - ForwardSpeed.value) / 5;
                          ForwardSpeed.increment(incrementValue);
                          DriveCommandReducer.filter(
                              rightSpeedValue, leftSpeedValue);
                        });
                      });
                    },
                    onLongPressEnd: (detail) {
                      if (forwardSpeedTimer != null) {
                        ForwardSpeed.reset();
                        DriveCommandReducer.filter(0, 0);
                        forwardSpeedTimer!.cancel();
                      }
                    }, // Image tapped
                    child: Container(
                      margin: const EdgeInsets.fromLTRB(0, 0, 20, 0),
                      // padding: const EdgeInsets.all(10),
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(5),
                        color: reverse
                            ? const Color(0xFF292929).withOpacity(0.8)
                            : const Color(0xFF292929).withOpacity(0.3),
                      ),
                      child: Image.asset(
                        "images/forward_icon.png",
                        height: 110,
                        width: 70,
                      ),
                    )),
              ],
            ))
          ],
        ),
      ),
    );
  }
}
