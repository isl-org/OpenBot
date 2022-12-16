import 'dart:convert';
import 'dart:io';
import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:flutter_speed_dial/flutter_speed_dial.dart';
import 'package:nsd/nsd.dart';
import 'package:openbot_controller/screens/registeration.dart';

import 'discoveringDevices.dart';
import 'discovery.dart';

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
  Socket? _opponentSocket;
  Stream<Uint8List>? _broadcast;

  var _nextPort = 56360;

  int get nextPort => _nextPort++;

  ControllerState() {
    enableLogging(LogTopic.calls);
  }

  Future<void> addDiscovery() async {
    final discovery = await startDiscovery(serviceTypeDiscover);
    setState(() {
      discoveries.add(discovery);
    });
  }

  Future<void> dismissDiscovery(Discovery discovery) async {
    setState(() {
      /// remove fast, without confirmation, to avoid "onDismissed" error.
      discoveries.remove(discovery);
    });

    await stopDiscovery(discovery);
  }

  Future<void> addRegistration() async {
    var port = nextPort;
    final service = Service(
        name: 'OPEN_BOT_CONTROLLER_FLUTTER',
        host: InternetAddress.anyIPv4.address,
        type: serviceTypeRegister,
        port: port,
        txt: createTxt());

    final registration = await register(service);
    _serverSocket = await ServerSocket.bind(service.host, port);
    _serverSocket?.listen((socket) {
      if (_opponentSocket != null) {
        socket
          ..write('error:already_in_game')
          ..close();
      } else {
        _opponentSocket = socket;
        _broadcast = socket.asBroadcastStream();

        socket.write("{driveCmd: {r:0.0, l:0.26}}");
        socket.write("{command: SWITCH_CAMERA}");

        _broadcast?.map((data) => String.fromCharCodes(data)).listen(
          (message) {
            print(message);
          },
          onDone: () {
            socket.destroy();
            socket.close();
            _opponentSocket?.destroy();
            _opponentSocket = null;
            setState(() {});
          },
        );
      }
    });
    setState(() {
      registrations.add(registration);
    });
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
      home: Scaffold(
        floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
        floatingActionButton: SpeedDial(
          icon: Icons.add,
          spacing: 10,
          spaceBetweenChildren: 5,
          children: [
            SpeedDialChild(
              elevation: 2,
              child: const Icon(Icons.wifi_tethering),
              label: 'Register Service',
              onTap: () async => addRegistration(),
            ),
            SpeedDialChild(
              elevation: 2,
              child: const Icon(Icons.wifi_outlined),
              label: 'Start Discovery',
              onTap: () async => addDiscovery(),
            ),
          ],
        ),
        // body: Column(
        //   children: <Widget>[
        //     Expanded(
        //       child: ListView.builder(
        //         controller: ScrollController(),
        //         itemBuilder: buildDiscovery,
        //         itemCount: discoveries.length,
        //       ),
        //     ),
        //     const Divider(
        //       height: 20,
        //       thickness: 4,
        //       indent: 0,
        //       endIndent: 0,
        //       color: Colors.blue,
        //     ),
        //     Expanded(
        //       child: ListView.builder(
        //         controller: ScrollController(),
        //         itemBuilder: buildRegistration,
        //         itemCount: registrations.length,
        //       ),
        //     ),
        //   ],
        // ),
        body: const DiscoveringDevice(),
      ),
    );
  }

  Widget buildDiscovery(context, index) {
    final discovery = discoveries.elementAt(index);
    return Dismissible(
        key: ValueKey(discovery.id),
        onDismissed: (_) async => dismissDiscovery(discovery),
        child: DiscoveryWidget(discovery));
  }

  Widget buildRegistration(context, index) {
    final registration = registrations.elementAt(index);
    return Dismissible(
        key: ValueKey(registration.id),
        onDismissed: (_) async => dismissRegistration(registration),
        child: RegistrationWidget(registration));
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
