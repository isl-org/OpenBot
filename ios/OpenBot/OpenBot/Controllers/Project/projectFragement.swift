//
// Created by Nitish Yadav on 06/04/23.
//

import Foundation
import UIKit
class projectFragment: UIViewController {
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
        let firstLabel = CustomLabel(text: "Please sign in first to access your", fontSize: 16, fontColor: Colors.textColor ?? .black, frame: CGRect(x: width/2 - 115, y: height/2 - 20, width: 250, height: 40));
        let secondLabel = CustomLabel(text: "Projects.", fontSize: 16, fontColor:  Colors.textColor ?? .black, frame: CGRect(x: width/2 - 32.5, y: height/2 + 10, width: 65, height: 40));
        view.addSubview(firstLabel);
        view.addSubview(secondLabel)
    }

    func createSignInBtn(){
       let signInBtn =  GoogleSignInBtn(frame: CGRect(x: adapted(dimensionSize: 17, to: .width), y: height/2 + 60, width: width - adapted(dimensionSize: 34, to: .width), height: 52))
        view.addSubview(signInBtn);
    }

    @IBAction func scanner(_ sender: Any) {
        let storyboard = UIStoryboard(name: "openCode", bundle: nil)
        let viewController = (storyboard.instantiateViewController(withIdentifier: "qrScanner"))
        navigationController?.pushViewController(viewController, animated: true);
    }
}

