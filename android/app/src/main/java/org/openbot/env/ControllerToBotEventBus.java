package org.openbot.env;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.subjects.PublishSubject;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public final class ControllerToBotEventBus {

  private static final Map<String, Disposable> subscribers = new HashMap<>();

  private ControllerToBotEventBus() {}

  private static final PublishSubject<JSONObject> subject = PublishSubject.create();

  public static void emitEvent(JSONObject event) {
    subject.onNext(event);
  }

  public static void subscribe(
      String subscriberName,
      @NonNull Consumer<? super JSONObject> onNext,
      @NonNull Consumer<? super Throwable> onError) {
    if (subscribers.containsKey(subscriberName)) {
      // This name already subscribed, cannot subscribe multiple times;
      return;
    }
    @NonNull Disposable subscriber = subject.subscribe(onNext, onError);
    subscribers.put(subscriberName, subscriber);
  }

  public static void unsubscribe(String name) {
    Disposable subscriber = subscribers.get(name);
    if (subscriber != null) {
      subscriber.dispose();
      subscribers.remove(name);
    }
  }
}
