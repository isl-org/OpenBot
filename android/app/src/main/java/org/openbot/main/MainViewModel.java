package org.openbot.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import org.openbot.model.SubCategory;

public class MainViewModel extends ViewModel {

  private final MutableLiveData<SubCategory> selectedMode = new MutableLiveData<>();

  public void selectMode(SubCategory subCategory) {
    selectedMode.setValue(subCategory);
  }

  public LiveData<SubCategory> getSelectedMode() {
    return selectedMode;
  }

  private final MutableLiveData<String> usbData = new MutableLiveData<>();

  public void setUsbData(String data) {
    usbData.setValue(data);
  }

  public LiveData<String> getUsbData() {
    return usbData;
  }
}
