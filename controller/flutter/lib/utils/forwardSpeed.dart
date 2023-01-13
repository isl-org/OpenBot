import 'dart:math';

class ForwardSpeed {
  static const max = 1.0;
  static const min = 0.0;
  static const minNegative = -1.0;
  static double value = 0;

  static void increment(double incrementValue) {
    if (value + incrementValue > max) {
      value = max;
    } else {
      value = (value + incrementValue).toPrecision(2);
    }
  }

  static void decrement(double decrementValue) {
    if (value - decrementValue < min) {
      value = (value - decrementValue).toPrecision(2);
    } else {
      value = min;
    }
  }

  static void decrementNegative(double decrementValue) {
    if (value + decrementValue > minNegative) {
      value = (value + decrementValue).toPrecision(2);
    } else {
      value = minNegative;
    }
  }

  static void reset() {
    value = 0;
  }

  static void setTo(double minSpeed) {
    if (minSpeed < min && minSpeed > max) {
      return;
    }
    value = minSpeed;
  }

  static bool isMin() {
    return value <= min;
  }
}

extension Precision on double {
  double toPrecision(int fractionDigits) {
    num mod = pow(10, fractionDigits.toDouble());
    return ((this * mod).round().toDouble() / mod);
  }
}
