// Created by Matthias Mueller - Intel Intelligent Systems Lab - 2020

package org.openbot.env;

import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;
import org.openbot.CameraActivity;
import org.openbot.CameraActivity.ControlSignal;
import org.openbot.CameraActivity.DriveMode;

public class GameController {
  private float left = 0;
  private float right = 0;
  private DriveMode driveMode;

  public GameController(DriveMode driveMode) {
    this.driveMode = driveMode;
  }

  public void setDriveMode(DriveMode mode) {
    driveMode = mode;
  }

  public DriveMode getDriveMode() {
    return driveMode;
  }

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

  public void processButtonInput(KeyEvent event) {
    switch (event.getKeyCode()) {
      case KeyEvent.KEYCODE_BUTTON_A:
        Toast.makeText(CameraActivity.getContext(), "A recognized", Toast.LENGTH_SHORT).show();
        return;
      case KeyEvent.KEYCODE_BUTTON_B:
        Toast.makeText(CameraActivity.getContext(), "B recognized", Toast.LENGTH_SHORT).show();
        return;
      case KeyEvent.KEYCODE_BUTTON_Y:
        Toast.makeText(CameraActivity.getContext(), "Y recognized", Toast.LENGTH_SHORT).show();
        return;
      case KeyEvent.KEYCODE_BUTTON_X:
        Toast.makeText(CameraActivity.getContext(), "X recognized", Toast.LENGTH_SHORT).show();
        return;
      case KeyEvent.KEYCODE_BUTTON_L1:
        Toast.makeText(CameraActivity.getContext(), "L1 recognized", Toast.LENGTH_SHORT).show();
        return;
      case KeyEvent.KEYCODE_BUTTON_R1:
        Toast.makeText(CameraActivity.getContext(), "R1 recognized", Toast.LENGTH_SHORT).show();
        return;
      default:
        Toast.makeText(
                CameraActivity.getContext(),
                "Key " + event.getKeyCode() + " not recognized",
                Toast.LENGTH_SHORT)
            .show();
        return;
    }
  }

  public ControlSignal processJoystickInput(MotionEvent event, int historyPos) {

    InputDevice inputDevice = event.getDevice();
    switch (driveMode) {
      case DUAL:
        float y = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_Y, historyPos);
        float rz = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_RZ, historyPos);
        left = -y;
        right = -rz;
        break;

      case GAME:
        float r_trigger = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_GAS, historyPos);
        if (r_trigger == 0) {
          r_trigger = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_RTRIGGER, historyPos);
        }

        float l_trigger = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_BRAKE, historyPos);
        if (l_trigger == 0) {
          l_trigger = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_LTRIGGER, historyPos);
        }

        // Calculate the steering magnitude by
        // using the input value from one of these physical controls:
        // the left control stick, hat axis, or the right control stick.
        float steering_offset = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_X, historyPos);
        if (steering_offset == 0) {
          steering_offset = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_HAT_X, historyPos);
        }
        if (steering_offset == 0) {
          steering_offset = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_Z, historyPos);
        }

        left = r_trigger - l_trigger;
        right = r_trigger - l_trigger;

        if (steering_offset > 0) {
          if (right >= 0) right -= steering_offset;
          else right += steering_offset;
        }
        if (steering_offset < 0) {
          if (left >= 0) left += steering_offset;
          else left -= steering_offset;
        }
        break;

      case JOYSTICK:
        // Calculate the vertical distance to move by
        // using the input value from one of these physical controls:
        // the left control stick, hat switch, or the right control stick.
        float y_axis = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_Y, historyPos);
        if (y_axis == 0) {
          y_axis = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_HAT_Y, historyPos);
        }
        if (y_axis == 0) {
          y_axis = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_RZ, historyPos);
        }

        // Calculate the horizontal distance to move by
        // using the input value from one of these physical controls:
        // the left control stick, hat axis, or the right control stick.
        float x_axis = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_X, historyPos);
        if (x_axis == 0) {
          x_axis = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_HAT_X, historyPos);
        }
        if (x_axis == 0) {
          x_axis = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_Z, historyPos);
        }

        left = -y_axis;
        right = -y_axis;

        if (x_axis > 0) {
          if (right >= 0) right -= x_axis;
          else right += x_axis;
        }
        if (x_axis < 0) {
          if (left >= 0) left += x_axis;
          else left -= x_axis;
        }

        break;

      default:
        left = 0;
        right = 0;
        break;
    }

    return new ControlSignal(left, right);
  }
}
