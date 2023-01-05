import 'dart:convert';
import 'dart:developer';
import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:nsd/nsd.dart';
import 'package:openbot_controller/globals.dart';
import 'package:openbot_controller/screens/controlSelector.dart';
import 'package:openbot_controller/screens/tiltingPhoneMode.dart';

import 'discoveringDevices.dart';
import 'onScreenMode.dart';

const String serviceTypeDiscover = '_openbot._tcp';
const String serviceTypeRegister = '_openbot._tcp';
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

  var _nextPort = 56360;

  int get nextPort => _nextPort++;

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
        name: 'OPEN_BOT_CONTROLLER',
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
            log(message);
            var msgInObject;
            try {
              var jsonArr = message.split("\n");
              for (var element in jsonArr) {
                var jsonMsg = json.encode(element);
                if (jsonMsg.isNotEmpty && jsonMsg != "\"\"") {
                  msgInObject = json.decode(json.decode(jsonMsg));
                  if (msgInObject["status"] != null) {
                    processMessageFromBot(msgInObject["status"]);
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
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      initialRoute: '/',
      routes: {
        '/': (context) => DiscoveringDevice(
            isDeviceConnected, sdp, type, id, label, candidate),
        '/ControlSelector': (context) => const ControlSelector(),
        '/OnScreenMode': (context) => const OnScreenMode(),
        '/TiltingPhoneMode': (context) => const TiltingPhoneMode(),
      },
    );
  }

  void processMessageFromBot(items) {
    print(items);
    if (items["CONNECTION_ACTIVE"] != null) {
      setDeviceConnected(items["CONNECTION_ACTIVE"]);
    }

    if (items["WEB_RTC_EVENT"] != null) {
      var webRTCResponse = json.decode(items["WEB_RTC_EVENT"]);
      if (webRTCResponse["type"].toString() == "offer") {
        type = webRTCResponse["type"];
        sdp = webRTCResponse["sdp"];
        setState(() {});
      }
      if (webRTCResponse["type"].toString() == "candidate") {
        id = webRTCResponse["id"];
        label = webRTCResponse["label"];
        candidate = webRTCResponse["candidate"].toString();
        setState(() {});
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
