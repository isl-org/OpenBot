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
import android.graphics.RectF;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** This TensorFlow Lite classifier works with the quantized MobileNet model. */
public class DetectorQuantizedMobileNetV3 extends Detector {

  // Only return this many results.
  private static final int NUM_DETECTIONS = 10;

  // outputLocations: array of shape [Batchsize, NUM_DETECTIONS,4]
  // contains the location of detected boxes
  private float[][][] outputLocations;
  // outputClasses: array of shape [Batchsize, NUM_DETECTIONS]
  // contains the classes of detected boxes
  private float[][] outputClasses;
  // outputScores: array of shape [Batchsize, NUM_DETECTIONS]
  // contains the scores of detected boxes
  private float[][] outputScores;
  // numDetections: array of shape [Batchsize]
  // contains the number of detected boxes
  private float[] numDetections;

  /**
   * An array to hold inference results, to be feed into Tensorflow Lite as outputs. This isn't part
   * of the super class, because we need a primitive array here.
   */
  private byte[][] labelProbArray = null;

  /**
   * Initializes a {@code ClassifierQuantizedMobileNet}.
   *
   * @param activity
   */
  public DetectorQuantizedMobileNetV3(Activity activity, Device device, int numThreads)
      throws IOException {
    super(activity, device, numThreads);
    labelProbArray = new byte[1][getNumLabels()];
  }

  @Override
  public boolean getMaintainAspect() {
    return false;
  }

  @Override
  public RectF getCropRect() {
    return new RectF(0.0f, 0.0f, 0.0f, 0.0f);
  }

  @Override
  public int getImageSizeX() {
    return 320;
  }

  @Override
  public int getImageSizeY() {
    return 320;
  }

  @Override
  protected String getModelPath() {
    // you can download this file from
    // see build.gradle for where to obtain this file. It should be auto
    // downloaded into assets.
    return "networks/mobile_ssd_v3_small_quant_coco.tflite";
  }

  @Override
  protected String getLabelPath() {
    return "labelmap.txt";
  }

  @Override
  protected int getNumBytesPerChannel() {
    // the quantized model uses a single byte only
    return 1;
  }

  @Override
  protected final int getNumDetections() {
    return NUM_DETECTIONS;
  }

  @Override
  protected void addPixelValue(int pixelValue) {
    imgData.put((byte) ((pixelValue >> 16) & 0xFF));
    imgData.put((byte) ((pixelValue >> 8) & 0xFF));
    imgData.put((byte) (pixelValue & 0xFF));
  }

  @Override
  protected float getProbability(int labelIndex) {
    return labelProbArray[0][labelIndex];
  }

  @Override
  protected void setProbability(int labelIndex, Number value) {
    labelProbArray[0][labelIndex] = value.byteValue();
  }

  @Override
  protected float getNormalizedProbability(int labelIndex) {
    return (labelProbArray[0][labelIndex] & 0xff) / 255.0f;
  }

  @Override
  protected void runInference() {
    // tflite.run(imgData, labelProbArray);
    Object[] inputArray = {imgData};
    tflite.runForMultipleInputsOutputs(inputArray, outputMap);
  }

  @Override
  protected void feedData() {
    outputLocations = new float[1][getNumDetections()][4];
    outputClasses = new float[1][getNumDetections()];
    outputScores = new float[1][getNumDetections()];
    numDetections = new float[1];

    // Object[] inputArray = {imgData};
    //    Map<Integer, Object> outputMap = new HashMap<>();
    outputMap.put(0, outputLocations);
    outputMap.put(1, outputClasses);
    outputMap.put(2, outputScores);
    outputMap.put(3, numDetections);
  }

  @Override
  protected List<Recognition> getRecognitions() {
    // Show the best detections.
    // after scaling them back to the input size.
    final ArrayList<Recognition> recognitions = new ArrayList<>(getNumDetections());
    for (int i = 0; i < getNumDetections(); ++i) {
      final RectF detection =
          new RectF(
              outputLocations[0][i][1] * getImageSizeY(),
              outputLocations[0][i][0] * getImageSizeX(),
              outputLocations[0][i][3] * getImageSizeY(),
              outputLocations[0][i][2] * getImageSizeX());
      // SSD Mobilenet V1 Model assumes class 0 is background class
      // in label file and class labels start from 1 to number_of_classes+1,
      // while outputClasses correspond to class index from 0 to number_of_classes
      int labelOffset = 1;
      if (labels.get((int) outputClasses[0][i] + labelOffset).contentEquals("person")) {
        recognitions.add(
            new Recognition(
                "" + i,
                labels.get((int) outputClasses[0][i] + labelOffset),
                outputScores[0][i],
                detection));
      }
    }
    return recognitions;
  }
}
