import 'package:openbot_controller/globals.dart';

class LeftIndicator {

  void toLeftIndicator() {
    clientSocket?.writeln("{command: INDICATOR_LEFT}");
    print("{command: INDICATOR_LEFT}");
  }
}