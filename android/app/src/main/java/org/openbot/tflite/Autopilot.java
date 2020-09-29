// Created by Matthias Mueller - Intel Intelligent Systems Lab - 2020

package org.openbot.tflite;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.os.Trace;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.openbot.CameraActivity.ControlSignal;

public abstract class Autopilot extends Network {

  /**
   * Creates a autopilot with the provided configuration.
   *
   * @param activity The current Activity.
   * @param model The model to use for classification.
   * @param device The device to use for classification.
   * @param numThreads The number of threads to use for classification.
   * @return A detector with the desired configuration.
   */

  /** A ByteBuffer to hold image data, to be feed into Tensorflow Lite as inputs. */
  protected ByteBuffer indicatorBuffer = null;

  public static Autopilot create(Activity activity, Model model, Device device, int numThreads)
      throws IOException {
    switch (model) {
      case AUTOPILOT_F:
        return new AutopilotFloat(activity, device, numThreads);
      default:
        return new AutopilotFloat(activity, device, numThreads);
    }
  }

  /** Initializes a {@code Autopilot}. */
  protected Autopilot(Activity activity, Device device, int numThreads) throws IOException {
    super(activity, device, numThreads);
    indicatorBuffer = ByteBuffer.allocateDirect(4);
    indicatorBuffer.order(ByteOrder.nativeOrder());
    LOGGER.d("Created a Tensorflow Lite Autopilot.");
  }

  private void convertIndicatorToByteBuffer(int indicator) {
    if (indicatorBuffer == null) {
      return;
    }
    indicatorBuffer.rewind();
    indicatorBuffer.putFloat(indicator);
  }

  public ControlSignal recognizeImage(final Bitmap bitmap, final int indicator) {
    // Log this method so that it can be analyzed with systrace.
    Trace.beginSection("recognizeImage");
    Trace.beginSection("preprocessBitmap");
    convertBitmapToByteBuffer(bitmap);
    convertIndicatorToByteBuffer(indicator);
    Trace.endSection(); // preprocessBitmap

    // Run the inference call.
    Trace.beginSection("runInference");
    long startTime = SystemClock.uptimeMillis();
    Object[] inputArray;
    if (tflite.getInputIndex("cmd_input") == 0) {
      inputArray = new Object[] {indicatorBuffer, imgData};
    } else {
      inputArray = new Object[] {imgData, indicatorBuffer};
    }

    float[][] predicted_ctrl = new float[1][2];
    outputMap.put(0, predicted_ctrl);
    tflite.runForMultipleInputsOutputs(inputArray, outputMap);
    long endTime = SystemClock.uptimeMillis();
    Trace.endSection();
    LOGGER.v("Timecost to run model inference: " + (endTime - startTime));

    Trace.endSection(); // "recognizeImage"
    return new ControlSignal(predicted_ctrl[0][0], predicted_ctrl[0][1]);
  }
}
