package org.openbot.modelManagement;

import android.os.Environment;
import com.nononsenseapps.filepicker.AbstractFilePickerFragment;
import com.nononsenseapps.filepicker.FilePickerActivity;
import java.io.File;

public class BackHandlingFilePickerActivity extends FilePickerActivity {

  /** Need access to the fragment */
  BackHandlingFilePickerFragment currentFragment;

  /** Return a copy of the new fragment and set the variable above. */
  @Override
  protected AbstractFilePickerFragment<File> getFragment(
      final String startPath,
      final int mode,
      final boolean allowMultiple,
      final boolean allowDirCreate,
      final boolean allowExistingFile,
      final boolean singleClick) {

    // startPath is allowed to be null.
    // In that case, default folder should be SD-card and not "/"
    String path =
        (startPath != null ? startPath : Environment.getExternalStorageDirectory().getPath());

    currentFragment = new BackHandlingFilePickerFragment();
    currentFragment.setArgs(
        path, mode, allowMultiple, allowDirCreate, allowExistingFile, singleClick);
    return currentFragment;
  }

  /** Override the back-button. */
  @Override
  public void onBackPressed() {
    // If at top most level, normal behaviour
    if (currentFragment.isBackTop()) {
      super.onBackPressed();
    } else {
      // Else go up
      currentFragment.goUp();
    }
  }
}
