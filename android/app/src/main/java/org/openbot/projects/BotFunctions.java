package org.openbot.projects;

import android.webkit.JavascriptInterface;

import org.openbot.env.AudioPlayer;
import org.openbot.utils.Enums;
import org.openbot.vehicle.Control;
import org.openbot.vehicle.Vehicle;

import timber.log.Timber;

public class BotFunctions {
    private Vehicle v;
    private AudioPlayer ap;
    private String TAG = "sensor reading";

    public BotFunctions(Vehicle vehicle, AudioPlayer audioPlayer) {
        v = vehicle;
        ap = audioPlayer;
    }

    /**
     * openBot Movement functions
     *
     * @param speed
     */
    public void moveForward(int speed) {
        double speedResult = (double) speed / (double) v.getSpeedMultiplier();
        v.setControl((float) speedResult, (float) speedResult);
    }

    @JavascriptInterface
    public void moveBackward(int speed) {
        double speedResult = (double) speed / (double) v.getSpeedMultiplier();
        v.setControl((float) -speedResult, (float) -speedResult);
    }

    @JavascriptInterface
    public void moveLeft(int speed) {
        double speedResult = (double) speed / (double) v.getSpeedMultiplier();
        v.setControl(0, (float) speedResult);
    }

    @JavascriptInterface
    public void moveRight(int speed) {
        double speedResult = (double) speed / (double) v.getSpeedMultiplier();
        v.setControl((float) speedResult, 0);
    }

    @JavascriptInterface
    public void moveOpenBot(int leftSpeed, int rightSpeed) {
        double leftSpeedResult = (double) leftSpeed / (double) v.getSpeedMultiplier();
        double rightSpeedResult = (double) rightSpeed / (double) v.getSpeedMultiplier();
        v.setControl((float) leftSpeedResult, (float) rightSpeedResult);
    }

//    @JavascriptInterface
//    public void openBotmoveCircular(int a) {
//        Control control = new Control(1F, 1F);
//        v.setControl(control);
//    }

    @JavascriptInterface
    public void pause(int ms) {
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

    @JavascriptInterface
    public void stopRobot() {
        v.setControl(0, 0);
    }

    /**
     * openBot Sensor functions
     */
    @JavascriptInterface
    public void sonarReading() {
        Timber.tag(TAG).d("sonarReading - %s", v.getSonarReading());
    }

    @JavascriptInterface
    public void speedReading() {
        Timber.tag(TAG).d("Left speed - %s", v.getLeftWheelRpm());
        Timber.tag(TAG).d("Right speed - %s", v.getRightWheelRpm());
    }

    @JavascriptInterface
    public void voltageDividerReading() {
        Timber.tag(TAG).d("Battery Voltage - %s", v.getBatteryVoltage());
        Timber.tag(TAG).d("Battery Percentage - %s", v.getBatteryPercentage());
    }

    @JavascriptInterface
    public void frontWheelReading() {
        Timber.tag(TAG).d("Odometer Front - %s", v.isHasWheelOdometryFront());
    }

    @JavascriptInterface
    public void backWheelReading() {
        Timber.tag(TAG).d("Odometer Back - %s", v.isHasWheelOdometryBack());
    }

    @JavascriptInterface
    public void gyroscopeReading() {

    }

    @JavascriptInterface
    public void accelerationReading() {

    }

    @JavascriptInterface
    public void magneticReading() {

    }

    @JavascriptInterface
    public void indicatorReading() {
        Timber.tag(TAG).d("Indicator - %s", v.isHasIndicators());
    }

    /**
     * service command to robot
     */
    @JavascriptInterface
    public void noiseEnable(boolean playSound) {
        ap.playNoise("matthew", playSound);
    }

    @JavascriptInterface
    public void playSoundSpeed(String speedMode) {
        switch (speedMode) {
            case "slow":
                ap.playSpeedMode("matthew", Enums.SpeedMode.SLOW);
            case "medium":
                ap.playSpeedMode("matthew", Enums.SpeedMode.NORMAL);
            case "fast":
                ap.playSpeedMode("matthew", Enums.SpeedMode.FAST);
        }
    }

    @JavascriptInterface
    public void rightIndicatorOn() {
        v.setIndicator(1);
    }

    @JavascriptInterface
    public void leftIndicatorOn() {
        v.setIndicator(-1);
    }

    @JavascriptInterface
    public void IndicatorOff() {
        v.setIndicator(0);
    }
}
