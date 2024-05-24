import 'package:flutter/material.dart';
import 'package:flutter_webrtc/flutter_webrtc.dart';
import 'package:openbot_controller/buttonCommands/buttonCommands.dart';
import 'package:openbot_controller/screens/component/blinkingButton.dart';

class OnScreenIcon extends StatefulWidget {
  final dynamic updateMirrorView;
  final bool indicatorLeft;
  final bool indicatorRight;
  final RTCPeerConnection? peerConnection;

  const OnScreenIcon(this.updateMirrorView, this.indicatorLeft,
      this.indicatorRight, this.peerConnection,
      {super.key});

  @override
  State<StatefulWidget> createState() {
    return OnScreenIconState();
  }
}

class OnScreenIconState extends State<OnScreenIcon> {
  bool mirrorView = false;
  bool speaker = false;
  bool leftIndicator = false;
  bool rightIndicator = false;

  @override
  void didUpdateWidget(covariant OnScreenIcon oldWidget) {
    // TODO: implement didUpdateWidget
    setState(() {
      leftIndicator = widget.indicatorLeft;
      rightIndicator = widget.indicatorRight;
    });
    super.didUpdateWidget(oldWidget);
  }

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      child: Row(
        children: [
          GestureDetector(
              onTap: () {
                setState(() {
                  mirrorView = !mirrorView;
                  widget.updateMirrorView.call();
                });
              }, // Image tapped
              child: Container(
                padding: const EdgeInsets.all(15),
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(45),
                  color: mirrorView
                      ? const Color(0xFF0071C5).withOpacity(0.5)
                      : Colors.white.withOpacity(0.5),
                ),
                child: Image.asset(
                  mirrorView
                      ? "images/mirror_view_icon_white.png"
                      : "images/mirror_view_icon_blue.png",
                  height: 23,
                  width: 23,
                ),
              )),
          const SizedBox(
            width: 15,
          ),
          GestureDetector(
              onTap: () async {
                setState(() {
                  speaker = !speaker;
                });
                if (widget.peerConnection != null) {
                  List<RTCRtpReceiver> receivers =
                      await widget.peerConnection!.receivers;
                  RTCRtpReceiver firstReceiver = receivers[0];
                  if (receivers.isNotEmpty) {
                    if (speaker) {
                      firstReceiver.track!.enabled = false;
                    } else {
                      firstReceiver.track!.enabled = true;
                    }
                  }
                }
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
                ButtonCommands.toSwitchCamera();
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
                  ButtonCommands.toStopIndicator();
                  ButtonCommands.toLeftIndicator();
                } else {
                  if (leftIndicator) {
                    ButtonCommands.toStopIndicator();
                  } else {
                    ButtonCommands.toLeftIndicator();
                  }
                }
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
                  ButtonCommands.toStopIndicator();
                  ButtonCommands.toRightIndicator();
                } else {
                  if (rightIndicator) {
                    ButtonCommands.toStopIndicator();
                  } else {
                    ButtonCommands.toRightIndicator();
                  }
                }
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
