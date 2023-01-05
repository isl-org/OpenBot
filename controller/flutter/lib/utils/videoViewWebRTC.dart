import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_webrtc/flutter_webrtc.dart';
import 'package:openbot_controller/globals.dart';

class VideoViewWebRTC extends StatefulWidget {
  final String type;
  final String sdp;
  final String id;
  final int label;
  final String candidate;

  const VideoViewWebRTC(
      this.sdp, this.type, this.id, this.label, this.candidate,
      {super.key});

  @override
  State<StatefulWidget> createState() {
    return VideoViewWebRTCState();
  }
}

class VideoViewWebRTCState extends State<VideoViewWebRTC> {
  final _remoteVideoRenderer = RTCVideoRenderer();
  RTCPeerConnection? _peerConnection;

  get remoteRenderer => _remoteVideoRenderer;

  @override
  void initState() {
    super.initState();
  }

  @override
  void didUpdateWidget(covariant oldWidget) {
    super.didUpdateWidget(oldWidget);
    handleWebRtcEvent();
    initRenderers();
    _createPeerConnection().then((pc) {
      // if (kDebugMode) {
      //   print(pc.iceConnectionState);
      //   print(pc.getConfiguration.toString());
      // }
      _peerConnection = pc;
    });
  }

  initRenderers() async {
    await _remoteVideoRenderer.initialize();
  }

  // Future<void> createOffer() async {
  //   _offer = true;
  //   RTCSessionDescription? description =
  //       await _peerConnection?.createOffer({'offerToReceiveVideo': 1});
  //   // var session = parse(description.sdp);
  //   // print(json.encode(session));
  //   // _offer = true;
  //   var roomDef = Firestore.instance.collection("rooms").document("test");
  //
  //   var data = {
  //     "offer": {
  //       'sdp': description?.sdp.toString(),
  //       'type': description?.type.toString(),
  //     }
  //   };
  //   await roomDef.setData(data, merge: true);
  //   await _peerConnection?.setLocalDescription(description!);
  //   Firestore.instance
  //       .collection("rooms")
  //       .document("test")
  //       .snapshots()
  //       .listen((event) {
  //     if (event.data["answer"] != null) {
  //       _setRemoteDescription(event.data["answer"]);
  //     }
  //   });
  // }

  void createAnswer() async {
    final Map<String, dynamic> offerSdpConstraints = {
      "mandatory": {
        "OfferToReceiveAudio": true,
        "OfferToReceiveVideo": true,
      },
      "optional": [],
    };

    RTCSessionDescription? description =
        await _peerConnection?.createAnswer(offerSdpConstraints);
    await _peerConnection?.setLocalDescription(description!);
    var data = {
      'type': 'answer',
      'sdp': description?.sdp.toString(),
    };
    sendMessage(data);
  }

  void handleWebRtcEvent() async {
    var description = {
      "type": widget.type,
      "sdp": widget.sdp,
    };
    if (description["type"] == "offer") {
      _setRemoteDescription(description);
    }
    if (description["type"] == "candidate") {
      var candidateValue = {
        "id": widget.id,
        "label": widget.label,
        "candidate": widget.candidate,
      };
      _addCandidate(candidateValue);
    }
  }

  void _setRemoteDescription(doc) async {
    RTCSessionDescription description =
        RTCSessionDescription(doc["sdp"], doc["type"]);
    await _peerConnection
        ?.setRemoteDescription(description)
        .whenComplete(() => createAnswer());
    // createAnswer();
  }

  void _addCandidate(candidateValue) async {
    dynamic candidate = RTCIceCandidate(candidateValue['candidate'],
        candidateValue['id'], candidateValue['label']);
    await _peerConnection?.addCandidate(candidate);
  }

  Future<RTCPeerConnection> _createPeerConnection() async {
    Map<String, dynamic> configuration = {
      "iceServers": [
        {"url": "stun:stun.l.google.com:19302"},
      ]
    };
    final Map<String, dynamic> offerSdpConstraints = {
      "mandatory": {
        "OfferToReceiveAudio": false,
        "OfferToReceiveVideo": true,
      },
      "optional": [],
    };

    RTCPeerConnection pc =
        await createPeerConnection(configuration, offerSdpConstraints);
    log("Connection state ${pc.connectionState}");
    pc.onIceCandidate = (e) {
      debugPrint("call onIceCandidate");
      if (e.candidate != null) {
        var temp = {
          'type': 'candidate',
          'candidate': e.candidate.toString(),
          'sdpMid': e.sdpMid.toString(),
          'sdpMLineIndex': e.sdpMLineIndex,
        };
        final message = jsonEncode(temp);
        sendMessage(message);
        // Firestore.instance.collection("callee").snapshots().listen((event) {
        //   event.documentChanges.forEach((element) {
        //     print(element.document.data);
        //     _addCandidate(element.document.data);
        //   });
        // });
      }
      // if (!_offer && e.candidate != null) {
      //   Firestore.instance.collection("callee").add({
      //     'candidate': e.candidate.toString(),
      //     'sdpMid': e.sdpMid.toString(),
      //     'sdpMLineIndex': e.sdpMLineIndex,
      //   });
      //   Firestore.instance.collection("caller").snapshots().listen((event) {
      //     event.documentChanges.forEach((element) {
      //       print(element.document.data);
      //       _addCandidate(element.document.data);
      //     });
      //   });
      // }
      // if (e.candidate != null) {
      //   debugPrint(json.encode({
      //     'candidate': e.candidate.toString(),
      //     'sdpMid': e.sdpMid.toString(),
      //     'sdpMLineIndex': e.sdpMLineIndex,
      //   }));
      // }
    };

    pc.onIceConnectionState = (e) {
      print("onIceConnectionState");
      print(e.toString());
      if (kDebugMode) {
        print(e);
      }
    };

    pc.onAddStream = (stream) {
      // debugPrint('addStream: ${stream.id}');
      _remoteVideoRenderer.srcObject = stream;
    };
    return pc;
  }

  void sendMessage(message) async {
    print("call send message");
    clientSocket?.writeln(message);
  }

  @override
  Widget build(BuildContext context) {
    if (widget.sdp.isEmpty) {
      return Container();
    }

    return RTCVideoView(_remoteVideoRenderer, mirror: true);
  }
}
