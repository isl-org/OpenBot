import 'package:blinking_text/blinking_text.dart';
import 'package:flutter/material.dart';

class DiscoveringDevice extends StatefulWidget {
  const DiscoveringDevice({super.key});

  @override
  State<StatefulWidget> createState() {
    return DiscoveringDeviceState();
  }
}

class DiscoveringDeviceState extends State<DiscoveringDevice> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
          alignment: Alignment.center,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Image.asset(
                "images/openbot_icon.png",
                height: 130,
                width: 130,
              ),
              const Padding(
                  padding: EdgeInsets.all(10),
                  child: BlinkText("Searching for OpenBot...",
                      style: TextStyle(fontSize: 25, color: Colors.black),
                      beginColor: Colors.black,
                      endColor: Colors.white,
                      // times: 10,
                      duration: Duration(milliseconds: 500))),
              const Text(
                "Select Phone as control mode in the bot app to Connect",
                style: TextStyle(fontSize: 20),
              ),
            ],
          )),
    );
  }
}
