import 'package:openbot_controller/globals.dart';

class RightIndicator {

  void toRightIndicator() {
    clientSocket?.write("{command: INDICATOR_RIGHT}");
    print("{command: INDICATOR_RIGHT}");
  }
}