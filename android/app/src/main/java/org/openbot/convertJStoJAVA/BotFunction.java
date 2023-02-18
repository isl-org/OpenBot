package org.openbot.convertJStoJAVA;

import android.webkit.JavascriptInterface;

public class BotFunction {

    @JavascriptInterface
    public static void blinkLeft(int a) {
        System.out.println("i was called = " + a);
    }
}
