package org.openbot.convertJStoJAVA;

import android.webkit.JavascriptInterface;

import timber.log.Timber;

public class BotFunction {

    @JavascriptInterface
    public static void moveCircular(int a) {
        Timber.tag("Qr sanjeev").d("i am moveCircular = " + a);
    }

    @JavascriptInterface
    public static void moveForward(int a) {
        Timber.tag("Qr sanjeev").d("i am moveForward = " + a);
    }
}
