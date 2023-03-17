package org.openbot.projects;

import android.webkit.JavascriptInterface;

import org.openbot.vehicle.Control;
import org.openbot.vehicle.Vehicle;

import timber.log.Timber;

public class BotFunctions {
    private Vehicle v;

    public BotFunctions(Vehicle vehicle) {
        v = vehicle;
    }

    @JavascriptInterface
    public void moveCircular(int a) {
        Control control = new Control(0.1F, 0.5F);
        v.setControl(control);
    }

    @JavascriptInterface
    public void moveForward(int a) {
        Timber.tag("Qr sanjeev").d("moveForward = %s", a);
    }
}
