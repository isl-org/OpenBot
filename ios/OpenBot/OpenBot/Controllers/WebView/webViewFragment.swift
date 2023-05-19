//
// Created by Nitish Yadav on 15/05/23.
//

import Foundation
import UIKit
import WebKit

class openCodeWebView: UIViewController, WKUIDelegate, WKNavigationDelegate {
    var webView: WKWebView!
    var newWebviewPopupWindow: WKWebView?
    let authentication = Authentication.googleAuthentication
    override func loadView() {
        let webConfiguration = WKWebViewConfiguration()
        webConfiguration.applicationNameForUserAgent = "Mozilla/6.0 (iPhone; CPU iPhone OS 16_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.3 Mobile/15E148 Safari/604.1"
        webView = WKWebView(frame: .zero, configuration: webConfiguration)
        webView.configuration.defaultWebpagePreferences.allowsContentJavaScript = true
        webView.uiDelegate = self
        view = webView

        // Add userContentController with WKUserScript to log JavaScript errors
        let userContentController = WKUserContentController()
        let userScriptSource = "window.onerror = function(message, source, lineno, colno, error) { console.log('JavaScript Error: ' + message + ' at ' + source + ':' + lineno); };"
        let userScript = WKUserScript(source: userScriptSource, injectionTime: .atDocumentStart, forMainFrameOnly: true)
        userContentController.addUserScript(userScript)
        webConfiguration.userContentController = userContentController

    }

    override func viewDidLoad() {
        super.viewDidLoad()
        webView.navigationDelegate = self
        let myURL = URL(string: "https://www.openbot.itinker.io/")
        let myRequest = URLRequest(url: myURL!)
        webView.load(myRequest)
    }

    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        print("didFinish")

        // Access cookies
        webView.configuration.websiteDataStore.httpCookieStore.getAllCookies { cookies in
            for cookie in cookies {
                print("Cookie: \(cookie.name) = \(cookie.value)")
            }
        }

        if let accessToken = authentication.googleSignIn.currentUser?.accessToken.tokenString {
            let script = "window.localStorage.setItem('accessToken', '\(accessToken)')"
            webView.evaluateJavaScript(script) { result, error in
                if let error = error {
                    print("Error setting accessToken in local storage: \(error)")
                } else {
                    print("accessToken added to local storage")
                }
            }
        } else {
            print("accessToken is nil")
        }

        // Access local storage
        webView.evaluateJavaScript("Object.entries(window.localStorage)") { result, error in
            if let error = error {
                print("Error accessing local storage: \(error)")
            } else if let entries = result as? [[String]] {
                for entry in entries {
                    let key = entry[0]
                    let value = entry[1]
                    print("Key: \(key), Value: \(value)")
                }
            }
        }
    }

    func webView(_ webView: WKWebView, createWebViewWith configuration: WKWebViewConfiguration, for navigationAction: WKNavigationAction, windowFeatures: WKWindowFeatures) -> WKWebView? {
        newWebviewPopupWindow = WKWebView(frame: view.bounds, configuration: configuration)
        newWebviewPopupWindow!.backgroundColor = UIColor.clear // Set background color to clear
        newWebviewPopupWindow!.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        newWebviewPopupWindow!.navigationDelegate = self
        newWebviewPopupWindow!.uiDelegate = self
        view.addSubview(newWebviewPopupWindow!)
        print(newWebviewPopupWindow)
        return newWebviewPopupWindow!
    }


    func webViewDidClose(_ webView: WKWebView) {
        webView.removeFromSuperview()
        newWebviewPopupWindow = nil
    }


}
