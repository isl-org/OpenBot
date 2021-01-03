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
}
