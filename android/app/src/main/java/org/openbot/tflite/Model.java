package org.openbot.tflite;

import android.util.Size;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

/** The model. */
public class Model {

  public Model(
      Integer id,
      CLASS classType,
      TYPE type,
      String name,
      PATH_TYPE pathType,
      String path,
      Size inputSize) {
    this.id = id;
    this.classType = classType;
    this.type = type;
    this.name = name;
    this.pathType = pathType;
    this.path = path;
    this.inputSizeObject = inputSize;
  }

  public enum CLASS {
    AUTOPILOT_F,
    MOBILENETV1_1_0_Q,
    MOBILENETV3_S_Q,
    YOLOV4
  }

  public enum TYPE {
    AUTOPILOT,
    DETECTOR
  }

  public enum PATH_TYPE {
    URL,
    ASSET,
    FILE
  }

  @SerializedName("class")
  public CLASS classType;

  public Integer id;

  public TYPE type;
  public String name;
  public PATH_TYPE pathType;
  public String path;
  private Size inputSizeObject;

  @SerializedName("inputSize")
  private String inputSizeString;

  // TODO: Change this hacky code
  public static final Model Autopilot_F =
      new Model(
          1,
          CLASS.AUTOPILOT_F,
          TYPE.AUTOPILOT,
          "Autopilot_F",
          PATH_TYPE.ASSET,
          "networks/autopilot_float.tflite",
          new Size(256, 96));
  public static final Model MobileNetV1_1_0_Q =
      new Model(
          2,
          CLASS.MOBILENETV1_1_0_Q,
          TYPE.DETECTOR,
          "MobileNetV1_1.0_Q",
          PATH_TYPE.ASSET,
          "networks/mobile_ssd_v1_1.0_quant_coco.tflite",
          new Size(300, 300));
  public static final Model MobileNetV3_S_Q =
      new Model(
          3,
          CLASS.MOBILENETV3_S_Q,
          TYPE.DETECTOR,
          "MobileNetV3_S_Q",
          PATH_TYPE.ASSET,
          "networks/mobile_ssd_v3_small_quant_coco.tflite",
          new Size(320, 320));
  public static final Model YoloV4 =
      new Model(
          4,
          CLASS.YOLOV4,
          TYPE.DETECTOR,
          "YoloV4",
          PATH_TYPE.ASSET,
          "networks/yolo_v4_tiny_float_coco.tflite",
          new Size(416, 416));

  // TODO: Change this hacky code
  public static Model fromId(String id) {
    switch (CLASS.valueOf(id.toUpperCase())) {
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
    if (path != null) {
      return path;
    }
    return classType.name();
  }

  public String getName() {
    return name;
  }

  public Size getInputSize() {
    if (inputSizeObject != null) return inputSizeObject;

    return Size.parseSize(inputSizeString);
  }

  public void setPath(String path) {
    this.path = path;
  }

  public void setPathType(PATH_TYPE pathType) {
    this.pathType = pathType;
  }

  public void setInputSize(Size inputSize) {
    this.inputSizeObject = inputSize;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setClassType(CLASS classType) {
    this.classType = classType;
  }

  public void setType(TYPE type) {
    this.type = type;
  }
}
