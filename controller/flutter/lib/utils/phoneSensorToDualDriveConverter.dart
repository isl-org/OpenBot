import 'package:openbot_controller/utils/forwardSpeed.dart';

class PhoneSensorToDualDriveConverter {
  static double g = 9.81;

  DualDriveValues convert(double azimuth, double pitch, double roll) {
    double leftSpeed = 0;
    double rightSpeed = 0;
    double forwardSpeed = 0;

    forwardSpeed = ForwardSpeed.value;
    leftSpeed = forwardSpeed + (pitch / (g / 2)) * forwardSpeed;
    rightSpeed = forwardSpeed - (pitch / (g / 2)) * forwardSpeed;
    return DualDriveValues(leftSpeed, rightSpeed);
  }
}

class DualDriveValues {
  double MAX = 1.0;
  double MIN = -1.0;
  double left = 0.0;
  double right = 0.0;

  DualDriveValues(double left, double right) {
    this.left = clean(left);
    this.right = clean(right);
  }

  double clean(double value) {
    double ret = value;
    if (value > MAX) {
      ret = MAX;
    }
    if (value < MIN) {
      ret = MIN;
    }
    return ret;
  }
}
