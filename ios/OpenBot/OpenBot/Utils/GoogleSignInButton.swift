//
// Created by Nitish Yadav on 06/04/23.
//

import Foundation
import UIKit
class GoogleSignInBtn : UIView {

    override init(frame: CGRect) {
        super.init(frame: frame)
        createSignInBtn(frame: frame)
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }

    func createSignInBtn(frame: CGRect) {
        self.backgroundColor = UIColor(named: "signInButtonColor");
        layer.cornerRadius = 10;
        addSubview(createGoogleIcon(frame: CGRect(x: self.frame.width / 2 - 100, y: 16, width: 20, height: 20)));
        addSubview(createSingInText(frame: CGRect(x: self.frame.width / 2 - 69, y: 5, width: 160, height: 40)))
    }

    private func createGoogleIcon(frame: CGRect) -> UIImageView {
        let googleIcon = UIImageView(frame: frame);
        googleIcon.image = UIImage(named: "googleIcon");
        return googleIcon;
    }

    private func createSingInText(frame: CGRect) -> UILabel {
        let signInText = UILabel(frame: frame);
        signInText.text = "Sign-in with Google";
        signInText.textColor = Colors.bdColor;
        signInText.font = signInText.font?.withSize(18);
        return signInText;
    }

}