package org.openbot.env;

import org.json.JSONObject;

import io.reactivex.rxjava3.subjects.PublishSubject;

public final class ControllerToBotEventBus {

  private ControllerToBotEventBus() {}
  private static PublishSubject<JSONObject> subject = PublishSubject.create();
  public static PublishSubject<JSONObject> getProcessor() {
    return subject;
  }
  public static void emitEvent(JSONObject event) {

    subject.onNext(event);
  }
}
