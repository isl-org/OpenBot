//
// Created by Nitish Yadav on 05/04/23.
//

import Foundation
import UIKit
import GoogleSignIn
extension HomePageViewController {
    /**
     this function creates bottom sheet for signIn at homepage
     */
    func createSignInBottomSheet() {
        bottomSheet.frame = CGRect(x: 0, y: height - 200, width: width, height: 200);
        whiteSheet.addSubview(bottomSheet)
        bottomSheet.layer.cornerRadius = 30;
        bottomSheet.backgroundColor = Colors.bdColor
        bottomSheet.layer.maskedCorners = [.layerMaxXMinYCorner, .layerMinXMinYCorner]
        createSignInButton()
        createGuestSignInText(frame: CGRect(x: width/2 - 70, y: 113, width: 150, height: 40));
    }

    func createShadowSheet() {
        whiteSheet.backgroundColor = UIColor.black.withAlphaComponent(0.6)
        tabBarController?.view.addSubview(whiteSheet)
        createSignInBottomSheet()
    }

    func createSignInButton() {
        let signInBtn = bottomSheet.createSignInButton(frame: CGRect(x: adapted(dimensionSize: 17, to: .width), y: 42, width: width - adapted(dimensionSize: 34, to: .width), height: 52))
        bottomSheet.addSubview(signInBtn)
        signInBtn.isUserInteractionEnabled = true
        let tap = UITapGestureRecognizer(target: self, action: #selector(signIn))
        signInBtn.addGestureRecognizer(tap);
    }

    func createGuestSignInText(frame: CGRect) {
        let guestLabel = UILabel();
        guestLabel.frame = frame;
        guestLabel.text = "Continue as Guest";
        guestLabel.isUserInteractionEnabled = true;
        bottomSheet.addSubview(guestLabel)
        let tap = UITapGestureRecognizer(target: self, action: #selector(guestLogin))
        guestLabel.addGestureRecognizer(tap);
    }


    @objc func signIn(_ sender: UIView) {
       Authentication.init()
        print(GIDSignIn.sharedInstance.currentUser?.fetcherAuthorizer.userEmail)
    }

    @objc func guestLogin(_ sender: UILabel) {
        whiteSheet.removeFromSuperview();

    }
}

