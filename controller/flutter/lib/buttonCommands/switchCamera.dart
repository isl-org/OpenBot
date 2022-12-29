import 'package:openbot_controller/globals.dart';

class SwitchCamera {

  void toSwitchCamera() {
    clientSocket?.writeln("{command: SWITCH_CAMERA}");
    print("{command: SWITCH_CAMERA}");
  }
}