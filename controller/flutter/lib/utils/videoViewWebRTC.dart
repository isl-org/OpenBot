import 'package:flutter/material.dart';
import 'package:flutter_webrtc/flutter_webrtc.dart';

class VideoViewWebRTC extends StatefulWidget {
  const VideoViewWebRTC(RTCVideoRenderer remoteVideoRenderer, {super.key});

  @override
  State<StatefulWidget> createState() {
    return VideoViewWebRTCState();
  }
}

class VideoViewWebRTCState extends State<VideoViewWebRTC> {
  RTCVideoRenderer get remoteVideoRenderer => remoteVideoRenderer;

  @override
  Widget build(BuildContext context) {
    // if (widget.sdp.isEmpty) {
    //   return Container();
    // }

    return RTCVideoView(remoteVideoRenderer);
  }
}
