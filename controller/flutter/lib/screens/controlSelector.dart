import 'package:flutter/material.dart';
import 'package:openbot_controller/screens/tiltingPhoneMode.dart';

import 'onScreenMode.dart';

class ControlSelector extends StatefulWidget {
  const ControlSelector({super.key});

  @override
  State<StatefulWidget> createState() {
    return ControlSelectorState();
  }
}

class ControlSelectorState extends State<ControlSelector> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: const Color(0xFF202020),
        body: Center(
            child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Row(
              crossAxisAlignment: CrossAxisAlignment.center,
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                GestureDetector(
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (context) => const OnScreenMode()),
                    );
                  },
                  child: Container(
                    height: 180,
                    width: 180,
                    padding: const EdgeInsets.all(20),
                    margin: const EdgeInsets.all(20),
                    decoration: const BoxDecoration(
                        borderRadius: BorderRadius.all(Radius.circular(10)),
                        color: Color(0xFF292929),
                        boxShadow: [
                          BoxShadow(
                            blurRadius: 14,
                            color: Color(0xFF000000),
                            offset: Offset(
                              0,
                              4,
                            ),
                          )
                        ]),
                    child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        mainAxisAlignment: MainAxisAlignment.start,
                        children: [
                          Image.asset(
                            "images/controller_icon.png",
                            height: 18,
                            width: 18,
                          ),
                          const SizedBox(
                            height: 20,
                          ),
                          const Text(
                            "Use On-Screen Controls to Drive",
                            style: TextStyle(
                              fontSize: 15,
                              fontWeight: FontWeight.w500,
                              color: Color(0xFFffffff),
                            ),
                          ),
                          const SizedBox(
                            height: 60,
                          ),
                          Container(
                            alignment: Alignment.bottomRight,
                            child: Image.asset(
                              "images/arrow_icon.png",
                              width: 43,
                            ),
                          ),
                        ]),
                  ),
                ),
                GestureDetector(
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (context) => const TiltingPhoneMode()),
                    );
                  },
                  child: Container(
                    height: 180,
                    width: 180,
                    padding: const EdgeInsets.all(20),
                    margin: const EdgeInsets.all(20),
                    decoration: const BoxDecoration(
                        borderRadius: BorderRadius.all(Radius.circular(10)),
                        color: Color(0xFF292929),
                        boxShadow: [
                          BoxShadow(
                            blurRadius: 14,
                            color: Color(0xFF000000),
                            offset: Offset(
                              0,
                              4,
                            ),
                          )
                        ]),
                    child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        mainAxisAlignment: MainAxisAlignment.start,
                        children: [
                          Image.asset(
                            "images/tilting_phone_icon.png",
                            height: 18,
                            width: 18,
                          ),
                          const SizedBox(
                            height: 20,
                          ),
                          const Text(
                            "Drive by tilting\nthe phone",
                            style: TextStyle(
                              fontSize: 15,
                              fontWeight: FontWeight.w500,
                              color: Color(0xFFffffff),
                            ),
                          ),
                          const SizedBox(
                            height: 60,
                          ),
                          Container(
                            alignment: Alignment.bottomRight,
                            child: Image.asset(
                              "images/arrow_icon.png",
                              width: 43,
                            ),
                          ),
                        ]),
                  ),
                ),
              ],
            ),
            Row(
              crossAxisAlignment: CrossAxisAlignment.center,
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                Container(
                  height: 30,
                  width: 85,
                  margin: const EdgeInsets.all(20),
                  decoration: const BoxDecoration(
                    borderRadius: BorderRadius.all(Radius.circular(3)),
                    color: Color(0xFF0071C5),
                  ),
                  child: const Center(
                    child: Text(
                      "Logs",
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontSize: 13,
                        color: Color(0xFFffffff),
                      ),
                    ),
                  ),
                ),
                Container(
                  height: 30,
                  width: 85,
                  margin: const EdgeInsets.all(20),
                  decoration: const BoxDecoration(
                    borderRadius: BorderRadius.all(Radius.circular(3)),
                    color: Color(0xFF0071C5),
                  ),
                  child: const Center(
                    child: Text(
                      "Noise",
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontSize: 13,
                        color: Color(0xFFffffff),
                      ),
                    ),
                  ),
                ),
                Container(
                  height: 30,
                  width: 85,
                  margin: const EdgeInsets.all(20),
                  decoration: const BoxDecoration(
                    borderRadius: BorderRadius.all(Radius.circular(3)),
                    color: Color(0xFF0071C5),
                  ),
                  child: const Center(
                    child: Text(
                      "Network",
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontSize: 13,
                        color: Color(0xFFffffff),
                      ),
                    ),
                  ),
                ),
                Container(
                  height: 30,
                  width: 85,
                  margin: const EdgeInsets.all(20),
                  decoration: const BoxDecoration(
                    borderRadius: BorderRadius.all(Radius.circular(3)),
                    color: Color(0xFF0071C5),
                  ),
                  child: const Center(
                    child: Text(
                      "Game",
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontSize: 13,
                        color: Color(0xFFffffff),
                      ),
                    ),
                  ),
                )
              ],
            )
          ],
        ) // color: const Color(0xFF292929),
            ));
  }
}
