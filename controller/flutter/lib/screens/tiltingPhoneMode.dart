import 'dart:async';
import 'dart:math';
import 'package:flutter/material.dart';
import 'package:openbot_controller/globals.dart';
import 'package:openbot_controller/utils/forwardSpeed.dart';
import 'package:sensors_plus/sensors_plus.dart';

class TiltingPhoneMode extends StatefulWidget {
  const TiltingPhoneMode({super.key});

  @override
  State<StatefulWidget> createState() {
    return TiltingPhoneModeState();
  }
}

class TiltingPhoneModeState extends State<TiltingPhoneMode> {
  Timer? timer;
  bool forward = false;
  bool reverse = false;


  Future<void> gyroscopeData() async {
    gyroscopeEvents.listen((GyroscopeEvent event) {
      print(event);
      //Output: [GyroscopeEvent (x: 0.08372224867343903, y: -0.09925820678472519, z: 0.21376553177833557)]
    });
  }

  @override
  void initState() {
    gyroscopeEvents.listen((GyroscopeEvent event) {
      // print("inside init state $event" );
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
                        timer = Timer.periodic(const Duration(milliseconds: 333),
                            (t) {
                          reverse = !reverse;
                          double decrementSpeed = ForwardSpeed.minNegative/3;
                          ForwardSpeed.decrementNegative(decrementSpeed);
                          print(ForwardSpeed.value);
                          print("client socket is : $clientSock");
                          clientSock?.write("hello sanjeev kya haal hai ");
                        });
                      });
                    },
                    onLongPressEnd: (detail) {
                      if (timer != null) {
                        ForwardSpeed.reset();
                        timer!.cancel();
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
                        timer = Timer.periodic(const Duration(milliseconds: 200),
                            (t) {
                          forward = !forward;
                          double incrementValue = (ForwardSpeed.max-ForwardSpeed.value)/5;
                          ForwardSpeed.increment(incrementValue);
                          print(ForwardSpeed.value);

                        });
                      });
                    },
                    onLongPressEnd: (detail) {
                      if (timer != null) {
                        ForwardSpeed.reset();
                        timer!.cancel();
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
