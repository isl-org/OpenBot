// Created by Matthias Mueller - Intel Intelligent Systems Lab - 2020

package org.openbot.tflite;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.os.Trace;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import org.openbot.vehicle.Control;
import timber.log.Timber;

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

  /** A ByteBuffer to hold data, to be feed into Tensorflow Lite as inputs. */
  protected ByteBuffer cmdBuffer = null;

  private int cmdIndex;
  private int imgIndex;

  public static Autopilot create(Activity activity, Model model, Device device, int numThreads)
      throws IOException, IllegalArgumentException {
    return new AutopilotFloat(activity, model, device, numThreads);
  }

  /** Initializes a {@code Autopilot}. */
  protected Autopilot(Activity activity, Model model, Device device, int numThreads)
      throws IOException, IllegalArgumentException {
    super(activity, model, device, numThreads);
    try {
      cmdIndex = tflite.getInputIndex("serving_default_cmd_input:0");
      imgIndex = tflite.getInputIndex("serving_default_img_input:0");
    } catch (IllegalArgumentException e) {
      cmdIndex = tflite.getInputIndex("cmd_input");
      imgIndex = tflite.getInputIndex("img_input");
    }
    if (!Arrays.equals(
        tflite.getInputTensor(imgIndex).shape(),
        new int[] {1, getImageSizeY(), getImageSizeX(), 3}))
      throw new IllegalArgumentException("Invalid tensor dimensions");

    cmdBuffer = ByteBuffer.allocateDirect(4);
    cmdBuffer.order(ByteOrder.nativeOrder());

    Timber.d("Created a Tensorflow Lite Autopilot.");
  }

  private void convertIndicatorToByteBuffer(int indicator) {
    if (cmdBuffer == null) {
      return;
    }
    cmdBuffer.rewind();
    cmdBuffer.putFloat(indicator);
  }

  public Control recognizeImage(final Bitmap bitmap, final int indicator) {
    // Log this method so that it can be analyzed with systrace.
    Trace.beginSection("recognizeImage");
    Trace.beginSection("preprocessBitmap");
    convertBitmapToByteBuffer(bitmap);
    convertIndicatorToByteBuffer(indicator);
    Trace.endSection(); // preprocessBitmap

    // Run the inference call.
    Trace.beginSection("runInference");
    long startTime = SystemClock.elapsedRealtime();
    Object[] inputArray;
    if (cmdIndex == 0) {
      inputArray = new Object[] {cmdBuffer, imgData};
    } else {
      inputArray = new Object[] {imgData, cmdBuffer};
    }

    float[][] predicted_ctrl = new float[1][2];
    outputMap.put(0, predicted_ctrl);
    tflite.runForMultipleInputsOutputs(inputArray, outputMap);
    long endTime = SystemClock.elapsedRealtime();
    Trace.endSection();
    Timber.v("Timecost to run model inference: %s", (endTime - startTime));

    Trace.endSection(); // "recognizeImage"
    return new Control(predicted_ctrl[0][0], predicted_ctrl[0][1]);
  }
}
