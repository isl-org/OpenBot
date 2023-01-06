import 'dart:convert';
import 'dart:developer';
import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_webrtc/flutter_webrtc.dart';
import 'package:nsd/nsd.dart';
import 'package:openbot_controller/globals.dart';
import 'package:openbot_controller/screens/controlSelector.dart';
import 'package:openbot_controller/screens/tiltingPhoneMode.dart';
import 'package:openbot_controller/utils/videoViewWebRTC.dart';

import 'discoveringDevices.dart';
import 'onScreenMode.dart';

const String serviceTypeDiscover = '_1openbot._tcp';
const String serviceTypeRegister = '_1openbot._tcp';
const utf8encoder = Utf8Encoder();

class Controller extends StatefulWidget {
  const Controller({Key? key}) : super(key: key);

  @override
  State createState() => ControllerState();
}

class ControllerState extends State<Controller> {
  final discoveries = <Discovery>[];
  final registrations = <Registration>[];
  ServerSocket? _serverSocket;
  Stream<Uint8List>? _broadcast;
  String sdp = "";
  String type = "";
  String id = "";
  int label = 0;
  String candidate = "";
  bool isDeviceConnected = false;
  bool videoView = false;

  var _nextPort = 56360;

  int get nextPort => _nextPort++;

  //webRTC________________

  final _remoteVideoRenderer = RTCVideoRenderer();
  RTCPeerConnection? _peerConnection;

  get remoteRenderer => _remoteVideoRenderer;

  Future<void> videoConnection() async {
    initRenderers();
    _createPeerConnection().then((pc) {
      _peerConnection = pc;
    });
    handleWebRtcEvent();
  }

  initRenderers() async {
    await _remoteVideoRenderer.initialize();
  }

  void handleWebRtcEvent() async {
    var description = {
      "type": type,
      "sdp": sdp,
    };
    print("description is: $description");
    if (description["type"] == "offer") {
      _setRemoteDescription(description);
    }
    if (description["type"] == "candidate") {
      var candidateValue = {
        "id": id,
        "label": label,
        "candidate": candidate,
      };
      _addCandidate(candidateValue);
    }
  }

  void _setRemoteDescription(doc) async {
    print('remote description');
    RTCSessionDescription description =
        RTCSessionDescription(doc["sdp"], doc["type"]);
    print('remote description = ' + jsonEncode(doc));
    await _peerConnection
        ?.setRemoteDescription(description)
        .whenComplete(() => createAnswer());
  }

  void _addCandidate(candidateValue) async {
    dynamic candidate = RTCIceCandidate(candidateValue['candidate'],
        candidateValue['id'], candidateValue['label']);
    print("to string = ${candidateValue}");
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
    print("peer connection ${pc.iceConnectionState}");
    pc.onIceCandidate = (e) {
      print("on ice candidate");
      if (e.candidate != null) {
        var temp = {
          'type': 'candidate',
          'candidate': e.candidate.toString(),
          'sdpMid': e.sdpMid.toString(),
          'sdpMLineIndex': e.sdpMLineIndex,
        };
        final message = jsonEncode(temp);
        sendMessage(message);
      }
    };

    pc.onIceConnectionState = (e) {
      print("connection state = $e");
      print("connection state 2 = ${pc.connectionState}");
    };

    pc.onAddStream = (stream) {
      print("connection stream = ${stream.getVideoTracks()}");
      _remoteVideoRenderer.srcObject = stream;
      setState(() {
        videoView = true;
      });
    };
    return pc;
  }

  void createAnswer() async {
    print("create answer call");
    final Map<String, dynamic> offerSdpConstraints = {
      "mandatory": {
        "OfferToReceiveAudio": "false",
        "OfferToReceiveVideo": "false",
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
    print("message = $data");
    sendMessage(data);
  }

  void sendMessage(message) async {
    print("send message chala");
    var newMessage = jsonEncode(message);
    clientSocket?.writeln(newMessage);
    clientSocket?.writeln("newMessage");
  }

  //________________webRTC

  ControllerState() {
    enableLogging(LogTopic.calls);
  }

  @override
  void initState() {
    super.initState();
    registerNewService();
  }

  Future<void> registerNewService() async {
    var port = nextPort;
    final service = Service(
        name: 'OPEN_BOT_CONTROLLER_SANJEEV',
        host: InternetAddress.anyIPv4.address,
        type: serviceTypeRegister,
        port: port,
        txt: createTxt());

    final registration = await register(service);
    _serverSocket = await ServerSocket.bind(service.host, port);
    _serverSocket?.listen((socket) {
      print('Connection from'
          ' ${socket.remoteAddress.address}:${socket.remotePort}');
      if (clientSocket != null) {
        socket.close();
      } else {
        clientSocket = socket;
        _broadcast = clientSocket?.asBroadcastStream();
        //
        // clientSocket!.write("{driveCmd: {r:0.0, l:0.26}}");
        // clientSocket!.write("{command: SWITCH_CAMERA}");

        _broadcast?.map((data) => String.fromCharCodes(data)).listen(
          (message) {
            // log(message);
            var msgInObject;
            try {
              var jsonArr = message.split("\n");
              for (var element in jsonArr) {
                var jsonMsg = json.encode(element);
                if (jsonMsg.isNotEmpty && jsonMsg != "\"\"") {
                  msgInObject = json.decode(json.decode(jsonMsg));
                  if (msgInObject["status"] != null) {
                    processMessageFromBot(msgInObject["status"]);
                    handleWebRtcEvent();
                  }
                  // log(msgInObject.toString() + "_____");
                  // setDeviceConnected();
                }
              }
            } catch (e) {
              // log("res: $e : $msgInObject");
            }
          },
          onDone: () {
            if (kDebugMode) {
              print('client left');
            }
            setState(() {
              isDeviceConnected = false;
            });
            socket.destroy();
            socket.close();
            clientSocket?.destroy();
            clientSocket = null;
            setState(() {});
          },
        );
      }
    });
    setState(() {
      registrations.add(registration);
    });
  }

  setDeviceConnected(status) {
    if (status == "true") {
      setState(() {
        isDeviceConnected = true;
      });
    } else if (status == "false") {
      setState(() {
        isDeviceConnected = false;
      });
    }
  }

  Future<void> dismissRegistration(Registration registration) async {
    setState(() {
      /// remove fast, without confirmation, to avoid "onDismissed" error.
      registrations.remove(registration);
    });

    await unregister(registration);
  }

  @override
  Widget build(BuildContext context) {
    if (videoView) {
      return RTCVideoView(_remoteVideoRenderer);
    } else {
      return MaterialApp(
        debugShowCheckedModeBanner: false,
        initialRoute: '/',
        routes: {
          '/': (context) => DiscoveringDevice(isDeviceConnected),
          '/ControlSelector': (context) => const ControlSelector(),
          '/OnScreenMode': (context) => const OnScreenMode(),
          '/TiltingPhoneMode': (context) => const TiltingPhoneMode(),
        },
      );
    }
  }

  void processMessageFromBot(items) {
    print(items);
    if (items["CONNECTION_ACTIVE"] != null) {
      setDeviceConnected(items["CONNECTION_ACTIVE"]);
    }
    print("item is = ${items["CONNECTION_ACTIVE"]}, ${items["VIDEO_COMMAND"]}");

    if (items["VIDEO_COMMAND"].toString() == "START") {
      print("call Video connection");
      videoConnection();
    }

    if (items["WEB_RTC_EVENT"] != null) {
      var webRTCResponse = json.decode(items["WEB_RTC_EVENT"]);
      if (webRTCResponse["type"].toString() == "offer") {
        setState(() {
          type = webRTCResponse["type"];
          sdp = webRTCResponse["sdp"];
        });
      }
      if (webRTCResponse["type"].toString() == "candidate") {
        setState(() {
          type = webRTCResponse["type"];
          id = webRTCResponse["id"];
          label = webRTCResponse["label"];
          candidate = webRTCResponse["candidate"].toString();
        });
      }
    }
  }
}

/// Shortens the id for display on-screen.
String shorten(String? id) {
  return id?.toString().substring(0, 4) ?? 'unknown';
}

/// Creates a txt attribute object that showcases the most common use cases.
Map<String, Uint8List?> createTxt() {
  return <String, Uint8List?>{
    'a-string': utf8encoder.convert('κόσμε'),
    'a-blank': Uint8List(0),
    'a-null': null,
  };
}
