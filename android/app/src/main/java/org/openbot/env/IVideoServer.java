package org.openbot.env;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import net.majorkernelpanic.streaming.Session;

public interface IVideoServer extends Session.Callback, SurfaceHolder.Callback {
  void stopServer();

  void init(Context context);

  boolean isRunning();

  void startClient();

  void sendServerUrl();

  void sendVideoStoppedStatus();

  void setView(SurfaceView view);
}
