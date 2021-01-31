package org.openbot.tflite;

import androidx.annotation.NonNull;

/** The model. */
public class Model {
  public enum ID {
    DETECTOR_V1_1_0_Q,
    DETECTOR_V3_S_Q,
    AUTOPILOT_F,
  }

  public static final Model DETECTOR_V1_1_0_Q = new Model(ID.DETECTOR_V1_1_0_Q);
  public static final Model DETECTOR_V3_S_Q = new Model(ID.DETECTOR_V3_S_Q);
  public static final Model AUTOPILOT_F = new Model(ID.AUTOPILOT_F);

  public final ID id;
  public final String filename;

  public static Model fromId(String id) {
    switch (ID.valueOf(id)) {
      case DETECTOR_V1_1_0_Q:
        return DETECTOR_V1_1_0_Q;
      case DETECTOR_V3_S_Q:
        return DETECTOR_V3_S_Q;
      case AUTOPILOT_F:
        return AUTOPILOT_F;
    }
    throw new IllegalArgumentException("No model with id " + id);
  }

  private Model(ID id) {
    this.id = id;
    this.filename = null;
  }

  public Model(String filename) {
    this.id = ID.AUTOPILOT_F;
    this.filename = filename;
  }

  @NonNull
  @Override
  public String toString() {
    if (filename != null) {
      return filename;
    }
    return id.name();
  }
}
