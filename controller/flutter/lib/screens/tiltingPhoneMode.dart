import 'package:flutter/material.dart';

class TiltingPhoneMode extends StatefulWidget {
  const TiltingPhoneMode({super.key});

  @override
  State<StatefulWidget> createState() {
    return TiltingPhoneModeState();
  }
}

class TiltingPhoneModeState extends State<TiltingPhoneMode> {
  bool forward = false;
  bool reverse = false;

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
                      setState(() {
                        forward = !forward;
                      });
                    }, // Image tapped
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
                        reverse = !reverse;
                      });
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
