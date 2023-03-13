package org.openbot.convertJStoJAVA;

import android.webkit.JavascriptInterface;

public class BotFunction {

    @JavascriptInterface
    public static void blinkLeft(int a) {
        System.out.println("i am blinkLeft = " + a);
    }
    @JavascriptInterface
    public static void moveForward(int a) {
        System.out.println("i am moveForward = " + a);
    }
    @JavascriptInterface
    public static void moveCircular(int a) {
        System.out.println("i am moveCircular = " + a);
    }
}
