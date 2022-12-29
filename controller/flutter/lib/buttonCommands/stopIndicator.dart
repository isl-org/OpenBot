import 'package:openbot_controller/globals.dart';

class StopIndicator {

  void toStopIndicator() {
    clientSocket?.writeln("{command: INDICATOR_STOP}");
    print("{command: INDICATOR_STOP}");
  }
}