import 'package:openbot_controller/globals.dart';

class StopIndicator {

  void toStopIndicator() {
    clientSocket?.write("{command: INDICATOR_STOP}");
    print("{command: INDICATOR_STOP}");
  }
}