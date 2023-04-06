//
// Created by Nitish Yadav on 06/04/23.
//

import Foundation
import UIKit
class profileFragment : UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()
        print("inside profile")
        createMyProjectLabel();
        createPleaseSignInLabel();
        createSignInBtn();
    }

    func createMyProjectLabel(){
        let label = CustomLabel(text: "My Projects", fontSize: 15, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 20, y: 90, width: 100, height: 40));
        view.addSubview(label)

    }

    func createPleaseSignInLabel(){
        let firstLabel = CustomLabel(text: "Set up your profile by signing in with", fontSize: 16, fontColor: Colors.textColor ?? .black, frame: CGRect(x: width/2 - 135, y: height/2 - 20, width: 270, height: 40));
        let secondLabel = CustomLabel(text: "your Google account.", fontSize: 16, fontColor:  Colors.textColor ?? .black, frame: CGRect(x: width/2 - 80, y: height/2 + 10, width: 160, height: 40));
        view.addSubview(firstLabel);
        view.addSubview(secondLabel)
    }

    func createSignInBtn(){
        let signInBtn =  GoogleSignInBtn(frame: CGRect(x: adapted(dimensionSize: 17, to: .width), y: height/2 + 60, width: width - adapted(dimensionSize: 34, to: .width), height: 52))
        view.addSubview(signInBtn);
    }
}