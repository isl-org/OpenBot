import 'dart:convert';
import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:flutter_speed_dial/flutter_speed_dial.dart';
import 'package:nsd/nsd.dart';
import 'package:openbot_controller/screens/registeration.dart';

import 'discovery.dart';

const String serviceTypeDiscover = '_openbot._tcp';
const String serviceTypeRegister = '_openbot._tcp';
const utf8encoder = Utf8Encoder();

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State createState() => MyAppState();
}

class MyAppState extends State<MyApp> {
  final discoveries = <Discovery>[];
  final registrations = <Registration>[];

  var _nextPort = 56360;

  int get nextPort => _nextPort++;

  MyAppState() {
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
    final service = Service(
        name: 'Some Service',
        type: serviceTypeRegister,
        port: nextPort,
        txt: createTxt());

    final registration = await register(service);
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
        body: Column(
          children: <Widget>[
            Expanded(
              child: ListView.builder(
                controller: ScrollController(),
                itemBuilder: buildDiscovery,
                itemCount: discoveries.length,
              ),
            ),
            const Divider(
              height: 20,
              thickness: 4,
              indent: 0,
              endIndent: 0,
              color: Colors.blue,
            ),
            Expanded(
              child: ListView.builder(
                controller: ScrollController(),
                itemBuilder: buildRegistration,
                itemCount: registrations.length,
              ),
            ),
          ],
        ),
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
