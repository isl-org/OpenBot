import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:nsd/nsd.dart';
import 'package:provider/provider.dart';

import 'app.dart';

class DiscoveryWidget extends StatefulWidget {
  final Discovery discovery;

  DiscoveryWidget(this.discovery) : super(key: ValueKey(discovery.id));

  @override
  State createState() => DiscoveryState();
}

class DiscoveryState extends State<DiscoveryWidget> {
  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.fromLTRB(16, 4, 16, 4),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: <Widget>[
          ListTile(
              leading: const Icon(Icons.wifi_outlined),
              title: Text('Discovery ${shorten(widget.discovery.id)}')),
          Padding(
              padding: const EdgeInsets.fromLTRB(24, 0, 24, 0),
              child: ChangeNotifierProvider.value(
                value: widget.discovery,
                child: Consumer<Discovery>(builder: buildDataTable),
              )),
          const SizedBox(
            height: 16,
          ),
        ],
      ),
    );
  }

  Widget buildDataTable(
      BuildContext context, Discovery discovery, Widget? child) {
    return DataTable(
      headingRowHeight: 24,
      dataRowHeight: 24,
      dataTextStyle: const TextStyle(color: Colors.black, fontSize: 12),
      columnSpacing: 8,
      horizontalMargin: 0,
      headingTextStyle: const TextStyle(
          color: Colors.black, fontSize: 12, fontWeight: FontWeight.w600),
      columns: <DataColumn>[
        buildDataColumn('Name'),
        buildDataColumn('Type'),
        buildDataColumn('Host'),
        buildDataColumn('Port'),
      ],
      rows: buildDataRows(discovery),
    );
  }

  DataColumn buildDataColumn(String name) {
    return DataColumn(
      label: Text(
        name,
      ),
    );
  }

  List<DataRow> buildDataRows(Discovery discovery) {
    return discovery.services
        .map((e) => DataRow(
              cells: <DataCell>[
                DataCell(Text(e.name ?? 'unknown'), onTap: () => connect(e)),
                DataCell(Text(e.type ?? 'unknown')),
                DataCell(Text(e.host ?? 'unknown')),
                DataCell(Text(e.port != null ? '${e.port}' : 'unknown'))
              ],
            ))
        .toList();
  }

  connect(e) async {
    try {
      var target = await resolve(e);
      final socket = await Socket.connect(
        target.host,
        target.port!,
      );
      final broadcast = socket.asBroadcastStream();
      socket.write("{command: \"CONNECTED\"}");
      // broadcast.
      // late StreamSubscription<String> sub;
      broadcast
          .map((bytes) => String.fromCharCodes(bytes))
          .listen((message) async {
        if (kDebugMode) {
          print(message);
        }
      });

      if (kDebugMode) {
        print("connect");
        print(socket.address);
      }
    } on SocketException catch (e) {
      if (kDebugMode) {
        print('This server is not available: $e');
      }
    }
  }
}
