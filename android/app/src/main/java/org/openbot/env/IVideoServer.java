package org.openbot.env;

import android.content.Context;
import android.view.SurfaceView;

public interface IVideoServer {
  void stopServer();

  void init(Context context);

  boolean isRunning();

  void startClient();

  void sendServerUrl();

  void sendVideoStoppedStatus();

  void setView(SurfaceView view);
}
