//
// Created by Nitish Yadav on 15/05/23.
//

import Foundation
import UIKit
import WebKit
import SafariServices
/**
 class for webview
 */
class openCodeWebView: UIViewController, WKUIDelegate, WKNavigationDelegate,SFSafariViewControllerDelegate {
    override func viewDidLoad() {
        super.viewDidLoad()
        openSafariViewController();
    }

    func openSafariViewController() {
        if let url = URL(string: "https://www.openbot.itinker.io/") {
            let safariViewController = SFSafariViewController(url: url)
            safariViewController.delegate = self
            present(safariViewController, animated: true, completion: nil)
        }
    }

    func safariViewControllerDidFinish(_ controller: SFSafariViewController) {
        // Handle the Safari view controller being dismissed
        print("finished");
    }
}


