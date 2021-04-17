package org.openbot.autopilot;

import android.util.Size;
import androidx.annotation.NonNull;
import org.openbot.tflite.Model;

/** The model. */
public class AutopilotModel extends Model {

  public static final AutopilotModel AUTOPILOT_F =
      new AutopilotModel(null, ID.AUTOPILOT_F, TYPE.AUTOPILOT);

  public final ID id;
  public final TYPE type;
  public final String filename;

  public static AutopilotModel fromId(String id) {
    if (ID.valueOf(id) == ID.AUTOPILOT_F) {
      return AUTOPILOT_F;
    }
    throw new IllegalArgumentException("No model with id " + id);
  }

  public AutopilotModel(String filename, ID id, TYPE type) {
    super(filename, id, type);
    this.id = id;
    this.type = type;
    this.filename = filename;
  }

  public AutopilotModel(String filename) {
    super(filename, ID.AUTOPILOT_F, TYPE.AUTOPILOT);
    this.id = ID.AUTOPILOT_F;
    this.type = TYPE.AUTOPILOT;
    this.filename = filename;
  }

  public static Size getCroppedImageSize(String id) {
    if (ID.valueOf(id) == ID.AUTOPILOT_F) {
      return new Size(256, 96);
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
