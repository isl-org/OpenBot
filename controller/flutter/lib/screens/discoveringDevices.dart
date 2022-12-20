import 'package:flutter/material.dart';
import 'package:openbot_controller/screens/controlSelector.dart';

class DiscoveringDevice extends StatefulWidget {
  final bool isDeviceConnected;

  DiscoveringDevice(this.isDeviceConnected);

  @override
  State<StatefulWidget> createState() {
    return DiscoveringDeviceState();
  }
}

class DiscoveringDeviceState extends State<DiscoveringDevice> {
  bool selectController = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
          alignment: Alignment.center,
          child: widget.isDeviceConnected
              ? const ControlSelector()
              : Column(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Image.asset("images/openbot_icon.png"),
                    const Text("Searching for OpenBot..."),
                    const Text(
                        "Select Phone as control mode in the bot app to Connect"),
                  ],
                )),
    );
  }
}
