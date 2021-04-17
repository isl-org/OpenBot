package org.openbot.tflite;

import android.app.Activity;
import android.graphics.RectF;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetectorFloatYoloV4 extends Detector {

  /** Additional normalization of the used input. */
  private static final float IMAGE_MEAN = 0.0f;

  private static final float IMAGE_STD = 255.0f;

  // Only return this many results.
  private static final int NUM_DETECTIONS = 2535;

  // outputLocations: array of shape [Batchsize, NUM_DETECTIONS,4]
  // contains the location of detected boxes
  private float[][][] outputLocations;
  // outputScores: array of shape [Batchsize, NUM_DETECTIONS,labels.size()]
  // contains the scores of detected boxes
  private float[][][] outputScores;

  /**
   * Initializes a {@code ClassifierQuantizedMobileNet}.
   *
   * @param activity
   */
  public DetectorFloatYoloV4(Activity activity, Model model, Device device, int numThreads)
      throws IOException {
    super(activity, model, device, numThreads);
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
    return 416;
  }

  @Override
  public int getImageSizeY() {
    return 416;
  }

  @Override
  protected String getModelPath() {
    // you can download this file from
    // see build.gradle for where to obtain this file. It should be auto
    // downloaded into assets.
    return "networks/yolo_v4_tiny_float_coco.tflite";
  }

  @Override
  protected String getLabelPath() {
    return "networks/coco.txt";
  }

  @Override
  protected int getNumBytesPerChannel() {
    // the float model uses a four bytes
    return 4;
  }

  @Override
  protected final int getNumDetections() {
    return NUM_DETECTIONS;
  }

  @Override
  protected void addPixelValue(int pixelValue) {
    imgData.putFloat((((pixelValue >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
    imgData.putFloat((((pixelValue >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
    imgData.putFloat(((pixelValue & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
  }

  @Override
  protected void runInference() {
    Object[] inputArray = {imgData};
    tflite.runForMultipleInputsOutputs(inputArray, outputMap);
  }

  @Override
  protected void feedData() {
    outputLocations = new float[1][getNumDetections()][4];
    outputScores = new float[1][getNumDetections()][labels.size()];

    outputMap.put(0, outputLocations);
    outputMap.put(1, outputScores);
  }

  @Override
  protected List<Recognition> getRecognitions(String className) {
    // Show the best detections.
    // after scaling them back to the input size.
    final ArrayList<Recognition> recognitions = new ArrayList<>(getNumDetections());
    for (int i = 0; i < getNumDetections(); ++i) {
      float maxClass = 0;
      int classId = -1;
      final float[] classes = new float[labels.size()];
      System.arraycopy(outputScores[0][i], 0, classes, 0, labels.size());
      for (int c = 0; c < labels.size(); ++c) {
        if (classes[c] > maxClass) {
          classId = c;
          maxClass = classes[c];
        }
      }
      final float score = maxClass;
      if (classId == 0) { // only consider persons
        final float xPos = outputLocations[0][i][0];
        final float yPos = outputLocations[0][i][1];
        final float w = outputLocations[0][i][2];
        final float h = outputLocations[0][i][3];
        final RectF detection =
            new RectF(
                Math.max(0, xPos - w / 2),
                Math.max(0, yPos - h / 2),
                Math.min(getImageSizeX() - 1, xPos + w / 2),
                Math.min(getImageSizeY() - 1, yPos + h / 2));
        if (labels.get(classId).contentEquals(className)) {
          recognitions.add(new Recognition("" + i, labels.get(classId), score, detection, classId));
        }
      }
    }
    return nms(recognitions);
  }
}
