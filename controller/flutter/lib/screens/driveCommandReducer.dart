import 'package:openbot_controller/globals.dart';
class DriveCommandReducer {
  static double lastRight = 0;
  static double lastLeft = 0;
  static double withinRange = .02;

  static void filter(double rightValue, double leftValue) {
    if (isDifferent(rightValue, leftValue)) {
      lastLeft = leftValue;
      lastRight = rightValue;
      String msg = "{driveCmd: {r:${rightValue}, l:${leftValue}}}";
      clientSocket?.write(msg);
      print(msg);
    }
  }

  static bool isDifferent(double right,double left){
  if ((left - lastLeft).abs() <= withinRange && (right - lastRight).abs() <= withinRange) {
  return false;
  }
  return true;
  }
}
