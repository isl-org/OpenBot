//
// Created by Nitish Yadav on 04/02/23.
//

import Foundation
import UIKit
import JavaScriptCore
class openCodeController : UIViewController {
    var jsContext: JSContext!
    override func viewDidLoad() {
        super.viewDidLoad();
        initializeJS();
        let script = "function secondFunction() { return 'i am Second Function'; } function getString(arg) { return 'Hello ' + arg + '! ' + secondFunction(); }"
        jsContext?.evaluateScript(script)
        jsContext?.evaluateScript(script)
        evaluateJavaScript()

    }
    func initializeJS() {
        jsContext = JSContext()
    }

    func evaluateJavaScript(){

        if let function = jsContext?.objectForKeyedSubscript("getString") {
            let stringResult = function.call(withArguments: ["javscript inside swift"]).toString()
            print("Result: \(stringResult)")
        }
    }


}
