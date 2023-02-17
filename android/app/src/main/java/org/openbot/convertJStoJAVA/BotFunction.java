package org.openbot.convertJStoJAVA;

import android.webkit.JavascriptInterface;

public class BotFunction {
    @JavascriptInterface
    public void blinkLeft() {
        System.out.println("i was called");
    }
}
