import 'package:openbot_controller/globals.dart';

class RightIndicator {

  void toRightIndicator() {
    clientSocket?.writeln("{command: INDICATOR_RIGHT}");
  }
}