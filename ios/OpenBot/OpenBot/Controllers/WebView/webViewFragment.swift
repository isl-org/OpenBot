//
// Created by Nitish Yadav on 15/05/23.
//

import Foundation
import UIKit
import WebKit
class openCodeWebView : UIViewController,WKUIDelegate{
    var webView: WKWebView!

    override func loadView() {
        let webConfiguration = WKWebViewConfiguration()
        webView = WKWebView(frame: .zero, configuration: webConfiguration)
        webView.configuration.defaultWebpagePreferences.allowsContentJavaScript = true
        webView.uiDelegate = self
        view = webView
    }

    override func viewDidLoad() {
        super.viewDidLoad()
//        let myURL = URL(string:"https://www.openbot.itinker.io/")
//        let myRequest = URLRequest(url: myURL!)
//        webView.load(myRequest)

        if let url = URL(string: "https://www.openbot.itinker.io/") {
            UIApplication.shared.open(url, options: [:], completionHandler: nil)
        }
    }

    func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
        // Check if the navigation action is for a sign-in process

        if navigationAction.navigationType == .formSubmitted {
            // Allow the navigation
            decisionHandler(.allow)
        } else {
            // Cancel the navigation
            decisionHandler(.cancel)
        }
    }

}
