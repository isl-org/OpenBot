import 'package:openbot_controller/globals.dart';
import 'package:openbot_controller/utils/forwardSpeed.dart';

class DriveCommandReducer {
  static double lastRight = 0;
  static double lastLeft = 0;
  static double withinRange = .02;

  static void filter(double rightValue, double leftValue) {
    if (isDifferent(rightValue, leftValue)) {
      lastLeft = leftValue;
      lastRight = rightValue;
      String msg =
          "{driveCmd: {r:${rightValue.toPrecision(2)}, l:${leftValue.toPrecision(2)}}}";
      clientSocket?.writeln(msg);
    }
  }

  static bool isDifferent(double right, double left) {
    if ((left - lastLeft).abs() <= withinRange &&
        (right - lastRight).abs() <= withinRange) {
      return false;
    }
    return true;
  }
}
