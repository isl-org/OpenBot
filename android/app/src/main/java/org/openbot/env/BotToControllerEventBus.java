package org.openbot.env;

import io.reactivex.rxjava3.subjects.PublishSubject;
import org.json.JSONObject;

public final class BotToControllerEventBus {

  private BotToControllerEventBus() {}

  private static PublishSubject<JSONObject> subject = PublishSubject.create();

  public static PublishSubject<JSONObject> getProcessor() {
    return subject;
  }

  public static void emitEvent(JSONObject event) {
    subject.onNext(event);
  }
}
