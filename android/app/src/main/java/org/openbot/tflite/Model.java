package org.openbot.tflite;

import android.util.Size;
import androidx.annotation.NonNull;

/** The model. */
public class Model {

  public Model(ID id, TYPE type, String name, String assetPath, String filePath, Size inputSize) {
    this.id = id;
    this.type = type;
    this.name = name;
    this.assetPath = assetPath;
    this.filePath = filePath;
    this.inputSize = inputSize;
  }

  public enum ID {
    AUTOPILOT_F,
    MOBILENETV1_1_0_Q,
    MOBILENETV3_S_Q,
    YOLOV4
  }

  public enum TYPE {
    AUTOPILOT,
    DETECTOR
  }

  public final ID id;
  public final TYPE type;
  public final String name;
  public final String assetPath;
  public final String filePath;
  public final Size inputSize;

  // TODO: Change this hacky code
  public static final Model Autopilot_F =
      new Model(
          ID.AUTOPILOT_F,
          TYPE.AUTOPILOT,
          "Autopilot_F",
          "networks/autopilot_float.tflite",
          null,
          new Size(256, 96));
  public static final Model MobileNetV1_1_0_Q =
      new Model(
          ID.MOBILENETV1_1_0_Q,
          TYPE.DETECTOR,
          "MobileNetV1_1.0_Q",
          "networks/mobile_ssd_v1_1.0_quant_coco.tflite",
          null,
          new Size(300, 300));
  public static final Model MobileNetV3_S_Q =
      new Model(
          ID.MOBILENETV3_S_Q,
          TYPE.DETECTOR,
          "MobileNetV3_S_Q",
          "networks/mobile_ssd_v3_small_quant_coco.tflite",
          null,
          new Size(320, 320));
  public static final Model YoloV4 =
      new Model(
          ID.YOLOV4,
          TYPE.DETECTOR,
          "YoloV4",
          "networks/yolo_v4_tiny_float_coco.tflite",
          null,
          new Size(416, 416));

  // TODO: Change this hacky code
  public static Model fromId(String id) {
    switch (ID.valueOf(id.toUpperCase())) {
      case AUTOPILOT_F:
        return Autopilot_F;
      case MOBILENETV1_1_0_Q:
        return MobileNetV1_1_0_Q;
      case MOBILENETV3_S_Q:
        return MobileNetV3_S_Q;
      case YOLOV4:
        return YoloV4;
    }
    throw new IllegalArgumentException("No model with id " + id);
  }

  // TODO: Change this hacky code
  @NonNull
  @Override
  public String toString() {
    if (filePath != null) {
      return filePath;
    }
    return id.name();
  }
}
