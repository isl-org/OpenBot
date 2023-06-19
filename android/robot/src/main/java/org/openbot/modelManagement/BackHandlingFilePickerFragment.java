package org.openbot.modelManagement;

import androidx.annotation.NonNull;
import com.nononsenseapps.filepicker.FilePickerFragment;
import java.io.File;

public class BackHandlingFilePickerFragment extends FilePickerFragment {

  /**
   * For consistency, the top level the back button checks against should be the start path. But it
   * will fall back on /.
   */
  public File getBackTop() {
    return getPath(getArguments().getString(KEY_START_PATH, "/"));
  }

  /** @return true if the current path is the startpath or / */
  public boolean isBackTop() {
    return 0 == compareFiles(mCurrentPath, getBackTop())
        || 0 == compareFiles(mCurrentPath, new File("/"));
  }

  /** Go up on level, same as pressing on "..". */
  public void goUp() {
    mCurrentPath = getParent(mCurrentPath);
    mCheckedItems.clear();
    mCheckedVisibleViewHolders.clear();
    refresh(mCurrentPath);
  }

  // File extension to filter on
  private static final String EXTENSION = ".tflite";

  /**
   * @param file
   * @return The file extension. If file has no extension, it returns null.
   */
  private String getExtension(@NonNull File file) {
    String path = file.getPath();
    int i = path.lastIndexOf(".");
    if (i < 0) {
      return null;
    } else {
      return path.substring(i);
    }
  }

  @Override
  protected boolean isItemVisible(final File file) {
    boolean ret = super.isItemVisible(file);
    if (ret && !isDir(file) && (mode == MODE_FILE || mode == MODE_FILE_AND_DIR)) {
      String ext = getExtension(file);
      return EXTENSION.equalsIgnoreCase(ext);
    }
    return ret;
  }
}
