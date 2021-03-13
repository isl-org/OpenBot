package org.openbot;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;
import timber.log.Timber;

public class OpenBotApplication extends Application {

  static Context context;

  public static Context getContext() {
    return context;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    context = getApplicationContext();
    if (BuildConfig.DEBUG) {
      Timber.plant(
          new Timber.DebugTree() {
            @NonNull
            @Override
            protected String createStackElementTag(@NotNull StackTraceElement element) {
              return super.createStackElementTag(element) + ":" + element.getLineNumber();
            }
          });
    }
  }
}
