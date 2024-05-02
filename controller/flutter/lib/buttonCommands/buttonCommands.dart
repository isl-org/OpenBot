import '../globals.dart';

class ButtonCommands {
  static void toSwitchCamera() {
    clientSocket?.writeln("{command: SWITCH_CAMERA}");
  }

  static void toLeftIndicator() {
    clientSocket?.writeln("{command: INDICATOR_LEFT}");
  }

  static void toRightIndicator() {
    clientSocket?.writeln("{command: INDICATOR_RIGHT}");
  }

  static void toStopIndicator() {
    clientSocket?.writeln("{command: INDICATOR_STOP}");
  }
}
