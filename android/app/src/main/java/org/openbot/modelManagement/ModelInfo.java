package org.openbot.modelManagement;

public class ModelInfo {

  public enum MODEL_TYPE {
    AUTOPILOT,
    DETECTOR
  }

  private final String name;
  private final MODEL_TYPE modelType;

  public ModelInfo(String name, MODEL_TYPE modelType) {
    this.name = name;
    this.modelType = modelType;
  }

  public String getName() {
    return name;
  }

  public MODEL_TYPE getModelType() {
    return modelType;
  }
}
