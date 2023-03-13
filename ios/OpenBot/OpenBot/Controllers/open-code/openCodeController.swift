//
// Created by Nitish Yadav on 04/02/23.
//

import Foundation
import UIKit
import JavaScriptCore

class openCodeController: UIViewController {
    var jsContext: JSContext!

    override func viewDidLoad() {
        super.viewDidLoad();
        initializeJS();
        let script = "function secondFunction() { return 'i am Second Function'; } function getString(arg) { return 'Hello ' + arg + '! ' + secondFunction() ;}"
        jsContext?.evaluateScript(script)
        jsContext?.evaluateScript(script)
        evaluateJavaScript()

    }

    func initializeJS() {
        jsContext = JSContext()
    }

    func swiftFunction() -> Void {
        print("calling swift from js")
    }


    func evaluateJavaScript() {
        if let context = JSContext() {
            let callSwiftFunction: @convention(block) () -> Void = { () in
                return self.swiftFunction()
            }
            context.setObject(callSwiftFunction,
                    forKeyedSubscript: "swiftFunction" as NSString)
            context.evaluateScript("""
                                   for(let i = 0;i<10;i++)
                                   {swiftFunction()
                                   }
                                   """)
        }

    }
}
