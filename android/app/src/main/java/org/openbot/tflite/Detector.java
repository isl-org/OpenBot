/* Copyright 2019 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

// Modified by Matthias Mueller - Intel Intelligent Systems Lab - 2020

package org.openbot.tflite;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.SystemClock;
import android.os.Trace;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for frozen detection models trained using the Tensorflow Object Detection API:
 * github.com/tensorflow/models/tree/master/research/object_detection
 */
public abstract class Detector extends Network {

  /** Labels corresponding to the output of the vision model. */
  protected List<String> labels;

  /**
   * Creates a detector with the provided configuration.
   *
   * @param activity The current Activity.
   * @param model The model to use for classification.
   * @param device The device to use for classification.
   * @param numThreads The number of threads to use for classification.
   * @return A detector with the desired configuration.
   */
  public static Detector create(Activity activity, Model model, Device device, int numThreads)
      throws IOException {
    switch (model) {
      case DETECTOR_V1_1_0_Q:
        return new DetectorQuantizedMobileNetV1(activity, device, numThreads);
      case DETECTOR_V3_S_Q:
        return new DetectorQuantizedMobileNetV3(activity, device, numThreads);
      default:
        return new DetectorQuantizedMobileNetV1(activity, device, numThreads);
    }
  }

  /** An immutable result returned by a Classifier/Detector describing what was recognized. */
  public static class Recognition {
    /**
     * A unique identifier for what has been recognized. Specific to the class, not the instance of
     * the object.
     */
    private final String id;

    /** Display name for the recognition. */
    private final String title;

    /**
     * A sortable score for how good the recognition is relative to others. Higher should be better.
     */
    private final Float confidence;

    /** Optional location within the source image for the location of the recognized object. */
    private RectF location;

    public Recognition(
        final String id, final String title, final Float confidence, final RectF location) {
      this.id = id;
      this.title = title;
      this.confidence = confidence;
      this.location = location;
    }

    public String getId() {
      return id;
    }

    public String getTitle() {
      return title;
    }

    public Float getConfidence() {
      return confidence;
    }

    public RectF getLocation() {
      return new RectF(location);
    }

    public void setLocation(RectF location) {
      this.location = location;
    }

    @Override
    public String toString() {
      String resultString = "";
      if (id != null) {
        resultString += "[" + id + "] ";
      }

      if (title != null) {
        resultString += title + " ";
      }

      if (confidence != null) {
        resultString += String.format("(%.1f%%) ", confidence * 100.0f);
      }

      if (location != null) {
        resultString += location + " ";
      }

      return resultString.trim();
    }
  }

  /** Initializes a {@code Detector}. */
  protected Detector(Activity activity, Device device, int numThreads) throws IOException {
    super(activity, device, numThreads);
    labels = loadLabelList(activity);
    LOGGER.d("Created a Tensorflow Lite Detector.");
  }

  /** Reads label list from Assets. */
  private List<String> loadLabelList(Activity activity) throws IOException {
    List<String> labels = new ArrayList<String>();
    BufferedReader reader =
        new BufferedReader(new InputStreamReader(activity.getAssets().open(getLabelPath())));
    String line;
    while ((line = reader.readLine()) != null) {
      labels.add(line);
    }
    reader.close();
    return labels;
  }

  public List<Recognition> recognizeImage(final Bitmap bitmap) {
    // Log this method so that it can be analyzed with systrace.
    Trace.beginSection("recognizeImage");

    Trace.beginSection("preprocessBitmap");
    convertBitmapToByteBuffer(bitmap);
    Trace.endSection(); // preprocessBitmap

    // Copy the input data into TensorFlow.
    Trace.beginSection("feed");
    feedData();
    Trace.endSection();

    // Run the inference call.
    Trace.beginSection("runInference");
    long startTime = SystemClock.uptimeMillis();
    runInference();
    // tflite.runForMultipleInputsOutputs(inputArray, outputMap);
    long endTime = SystemClock.uptimeMillis();
    Trace.endSection();
    LOGGER.v("Timecost to run model inference: " + (endTime - startTime));

    Trace.endSection(); // "recognizeImage"
    return getRecognitions();
  }

  /**
   * Get the name of the label file stored in Assets.
   *
   * @return
   */
  protected abstract String getLabelPath();

  /**
   * Read the probability value for the specified label This is either the original value as it was
   * read from the net's output or the updated value after the filter was applied.
   *
   * @param labelIndex
   * @return
   */
  protected abstract float getProbability(int labelIndex);

  /**
   * Set the probability value for the specified label.
   *
   * @param labelIndex
   * @param value
   */
  protected abstract void setProbability(int labelIndex, Number value);

  /**
   * Get the normalized probability value for the specified label. This is the final value as it
   * will be shown to the user.
   *
   * @return
   */
  protected abstract float getNormalizedProbability(int labelIndex);

  /**
   * Run inference using the prepared input in {@link #imgData}. Afterwards, the result will be
   * provided by getProbability().
   *
   * <p>This additional method is necessary, because we don't have a common base for different
   * primitive data types.
   */
  protected abstract void runInference();

  /**
   * Feeds the data
   *
   * <p>This additional method is necessary, because we can have different number of detections
   */
  protected abstract void feedData();

  /**
   * Get the total number of labels.
   *
   * @return
   */
  protected int getNumLabels() {
    return labels.size();
  }

  /**
   * Get the number of detections.
   *
   * @return
   */
  protected abstract int getNumDetections();

  /**
   * Get the recognitions.
   *
   * @return
   */
  protected abstract List<Recognition> getRecognitions();
}
