package org.openbot.controller;

import android.util.Pair;
import android.view.InputDevice;
import android.view.MotionEvent;

public class JoyStickProcessor {
  private static float getCenteredAxis(
      MotionEvent event, InputDevice device, int axis, int historyPos) {
    final InputDevice.MotionRange range = device.getMotionRange(axis, event.getSource());

    // A joystick at rest does not always report an absolute position of
    // (0,0). Use the getFlat() method to determine the range of values
    // bounding the joystick axis center.
    if (range != null) {
      final float flat = range.getFlat();
      final float value =
          historyPos < 0
              ? event.getAxisValue(axis)
              : event.getHistoricalAxisValue(axis, historyPos);

      // Ignore axis values that are within the 'flat' region of the
      // joystick axis center.
      if (Math.abs(value) > flat) {
        return value;
      }
    }
    return 0;
  }

  public static Pair<Float, Float> processJoystickInputLeft(MotionEvent event, int historyPos) {

    InputDevice inputDevice = event.getDevice();

    // Calculate the horizontal distance to move by
    // using the input value from one of these physical controls:
    // the left control stick, hat axis, or the right control stick.
    float x = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_X, historyPos);

    // Calculate the vertical distance to move by
    // using the input value from one of these physical controls:
    // the left control stick, hat switch, or the right control stick.
    float y = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_Y, historyPos);

    return new Pair<>(x, y);
    // Update the ship object based on the new x and y values
  }

  public static Pair<Float, Float> processJoystickInputRight(MotionEvent event, int historyPos) {

    InputDevice inputDevice = event.getDevice();

    // Calculate the horizontal distance to move by
    // using the input value from one of these physical controls:
    // the left control stick, hat axis, or the right control stick.
    float x = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_Z, historyPos);

    // Calculate the vertical distance to move by
    // using the input value from one of these physical controls:
    // the left control stick, hat switch, or the right control stick.
    float y = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_RZ, historyPos);

    return new Pair<>(x, y);
    // Update the ship object based on the new x and y values
  }
}
