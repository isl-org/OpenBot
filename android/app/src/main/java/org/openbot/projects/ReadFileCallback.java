package org.openbot.projects;

import java.io.IOException;

public interface ReadFileCallback {
  void onFileReadSuccess(String fileContents);

  void onFileReadFailed(IOException e);
}
