package org.openbot.robot;

import android.view.MotionEvent;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RobotCommunicationViewModel extends ViewModel {

  private final MutableLiveData<Integer> keyEvent = new MutableLiveData<>();

  public void setKeyEvent(Integer data) {
    keyEvent.setValue(data);
  }

  public LiveData<Integer> getKeyEvent() {
    return keyEvent;
  }

  private final MutableLiveData<MotionEvent> genericMotionEvent = new MutableLiveData<>();

  public void setGenericMotionEvent(MotionEvent motionEvent) {
    genericMotionEvent.setValue(motionEvent);
  }

  public MutableLiveData<MotionEvent> getGenericMotionEvent() {
    return genericMotionEvent;
  }
}
