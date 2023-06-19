//
// Created by Nitish Yadav on 06/04/23.
//

import Foundation
import UIKit
/**
 a custom class to create google sign-in button
 */
class GoogleSignInBtn : UIView {

    /**
     initializer of class
     - Parameter frame:
     */
    override init(frame: CGRect) {
        super.init(frame: frame)
        createSignInBtn(frame: frame)
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }

    /**
     Function to create button
     - Parameter frame:
     */
    func createSignInBtn(frame: CGRect) {
        self.backgroundColor = UIColor(named: "signInButtonColor");
        layer.cornerRadius = 10;
        addSubview(createGoogleIcon(frame: CGRect(x: self.frame.width / 2 - 100, y: 16, width: 20, height: 20)));
        addSubview(createSingInText(frame: CGRect(x: self.frame.width / 2 - 69, y: 5, width: 160, height: 40)))
    }

    /**
     Function to add google icon inside the button
     - Parameter frame:
     - Returns:
     */
    private func createGoogleIcon(frame: CGRect) -> UIImageView {
        let googleIcon = UIImageView(frame: frame);
        googleIcon.image = UIImage(named: "googleIcon");
        return googleIcon;
    }

    /**
     Function to add text inside the function
     - Parameter frame:
     - Returns:
     */
    private func createSingInText(frame: CGRect) -> UILabel {
        let signInText = UILabel(frame: frame);
        signInText.text = "Sign-in with Google";
        signInText.textColor = traitCollection.userInterfaceStyle == .dark ? UIColor.white : UIColor.black
        signInText.font = signInText.font?.withSize(18);
        return signInText;
    }

}