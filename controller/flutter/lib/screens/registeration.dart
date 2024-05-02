import 'package:flutter/material.dart';
import 'package:nsd/nsd.dart';

import '../utils/constants.dart';

class RegistrationWidget extends StatelessWidget {
  final Registration registration;

  RegistrationWidget(this.registration) : super(key: ValueKey(registration.id));

  @override
  Widget build(BuildContext context) {
    final service = registration.service;
    return Card(
      margin: const EdgeInsets.fromLTRB(16, 4, 16, 4),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: <Widget>[
          ListTile(
            leading: const Icon(Icons.wifi_tethering),
            title: Text('Registration ${Constants.shorten(registration.id)}'),
            subtitle: Text(
              'Name: ${service.name} ▪️ '
              'Type: ${service.type} ▪️ '
              'Host: ${service.host} ▪️ '
              'Port: ${service.port}',
              style: const TextStyle(color: Colors.black, fontSize: 12),
            ),
          ),
          const SizedBox(
            height: 8,
          ),
        ],
      ),
    );
  }
}
