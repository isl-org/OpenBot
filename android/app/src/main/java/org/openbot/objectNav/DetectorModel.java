package org.openbot.objectNav;

import android.util.Size;
import androidx.annotation.NonNull;
import org.openbot.tflite.Model;

/** The model. */
public class DetectorModel extends Model {
  public enum ID {
    DETECTOR_V1_1_0_Q,
    DETECTOR_V3_S_Q,
    YOLO_V4_TINY_F
  }

  public enum TYPE {
    DETECTOR
  }

  public static final DetectorModel DETECTOR_V1_1_0_Q =
      new DetectorModel(null, ID.DETECTOR_V1_1_0_Q, TYPE.DETECTOR);
  public static final DetectorModel DETECTOR_V3_S_Q =
      new DetectorModel(null, ID.DETECTOR_V3_S_Q, TYPE.DETECTOR);
  public static final DetectorModel YOLO_V4_TINY_F =
      new DetectorModel(null, ID.YOLO_V4_TINY_F, TYPE.DETECTOR);

  public final ID id;
  public final TYPE type;
  public final String filename;

  public static DetectorModel fromId(String id) {
    switch (ID.valueOf(id)) {
      case DETECTOR_V1_1_0_Q:
        return DETECTOR_V1_1_0_Q;
      case DETECTOR_V3_S_Q:
        return DETECTOR_V3_S_Q;
      case YOLO_V4_TINY_F:
        return YOLO_V4_TINY_F;
    }
    throw new IllegalArgumentException("No model with id " + id);
  }

  public DetectorModel(String filename, ID id, TYPE type) {
    super(filename);
    this.id = id;
    switch (id) {
      case DETECTOR_V1_1_0_Q:
      case DETECTOR_V3_S_Q:
      case YOLO_V4_TINY_F:
        this.type = TYPE.DETECTOR;
        break;
      default:
        this.type = null;
        break;
    }
    this.filename = null;
  }

  public DetectorModel(String filename) {
    super(filename);
    this.id = ID.DETECTOR_V1_1_0_Q;
    this.type = TYPE.DETECTOR;
    this.filename = filename;
  }

  public static Size getCroppedImageSize(String id) {
    switch (ID.valueOf(id)) {
      case DETECTOR_V1_1_0_Q:
        return new Size(300, 300);
      case DETECTOR_V3_S_Q:
        return new Size(320, 320);
      case YOLO_V4_TINY_F:
        return new Size(416, 416);
    }
    throw new IllegalArgumentException("No size with id " + id);
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
