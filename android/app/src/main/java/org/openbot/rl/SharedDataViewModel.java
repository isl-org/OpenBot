package org.openbot.rl;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.opencv.core.Point;

public class SharedDataViewModel extends ViewModel {
    private MutableLiveData<Point> centroidValue = new MutableLiveData<>();

    // Getter and Setter methods for centroidValue
    public LiveData<Point> getCentroidValue() {
        return centroidValue;
    }

    public void setCentroidValue(Point point) {
        centroidValue.setValue(point);
    }


}

