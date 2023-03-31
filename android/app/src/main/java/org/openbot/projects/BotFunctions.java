package org.openbot.projects;

import android.webkit.JavascriptInterface;

import org.openbot.vehicle.Control;
import org.openbot.vehicle.Vehicle;

public class BotFunctions {
    private Vehicle v;

    public BotFunctions(Vehicle vehicle) {
        v = vehicle;
    }

    @JavascriptInterface
    public void openBotmoveCircular(int a) {
        Control control = new Control(1F, 1F);
        v.setControl(control);
    }

    @JavascriptInterface
    public void openBotmoveForward(int a) {
        Control control = new Control(1F, 1F);
        v.setControl(control);
    }

    @JavascriptInterface
    public void openBotmoveOpenBot(int left, int right) {
        Control control = new Control(left, right);
        v.setControl(control);
    }

    @JavascriptInterface
    public void openBotstop() {
        Control control = new Control(0, 0);
        v.setControl(control);
    }

    @JavascriptInterface
    public void openBotmoveBackward(int speed) {
        Control control = new Control(-1, -1);
        v.setControl(control);
    }

    @JavascriptInterface
    public void openBotmoveLeft(int speed) {
        Control control = new Control(0, 1);
        v.setControl(control);
    }

    @JavascriptInterface
    public void openBotmoveRight(int speed) {
        Control control = new Control(1, 0);
        v.setControl(control);
    }

    @JavascriptInterface
    public void openBotmove(int left, int right) {
        Control control = new Control(1, 1);
        v.setControl(control);
    }

    @JavascriptInterface
    public void openBotwait(int ms) {
        try {
            synchronized (this) {
                wait(ms); // Pauses the thread for 5 seconds
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Control control = new Control(0, 0);
        v.setControl(control);
    }
}
