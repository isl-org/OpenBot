package org.openbot.env;

import android.util.Log;

import io.reactivex.rxjava3.subjects.PublishSubject;
import org.json.JSONObject;

public final class ControllerToBotEventBus {

  private ControllerToBotEventBus() {}

  private static PublishSubject<JSONObject> subject = PublishSubject.create();

  public static PublishSubject<JSONObject> getProcessor() {
    return subject;
  }

  public static void emitEvent(JSONObject event) {
    Log.i(null, "ControllerToBotEventBus: Sending event: " + event);
    subject.onNext(event);
  }
}
