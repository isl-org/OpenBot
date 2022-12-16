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
              Image.asset("images/openbot_icon.png"),
              const Text("Searching for OpenBot..."),
              const Text("Select Phone as control mode in the bot app to Connect"),
            ],
          ),
        ));
  }
}
