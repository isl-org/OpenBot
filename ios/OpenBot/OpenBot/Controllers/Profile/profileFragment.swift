//
// Created by Nitish Yadav on 06/04/23.
//

import Foundation
import UIKit

class profileFragment: UIViewController {
    let shadowSheet = UIView(frame: UIScreen.main.bounds);
    var logoutView: UIView!;

    override func viewDidLoad() {
        super.viewDidLoad()
        createMyProjectLabel();
        createPleaseSignInLabel();
        createSignInBtn();
        createEditProfileAndLogoutLabel();
        createEditProfileAndLogoutIcons()
        createShadowSheet();
    }

    private func createMyProjectLabel() {
        let label = CustomLabel(text: "My Profile", fontSize: 15, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 20, y: 90, width: 100, height: 40));
        view.addSubview(label)
    }

    private func createPleaseSignInLabel() {
        let firstLabel = CustomLabel(text: "Set up your profile by signing in with", fontSize: 16, fontColor: Colors.textColor ?? .black, frame: CGRect(x: width / 2 - 135, y: height / 2 - 20, width: 270, height: 40));
        let secondLabel = CustomLabel(text: "your Google account.", fontSize: 16, fontColor: Colors.textColor ?? .black, frame: CGRect(x: width / 2 - 80, y: height / 2 + 10, width: 160, height: 40));
        view.addSubview(firstLabel);
        view.addSubview(secondLabel)
    }

    private func createSignInBtn() {
        let signInBtn = GoogleSignInBtn(frame: CGRect(x: adapted(dimensionSize: 17, to: .width), y: height / 2 + 60, width: width - adapted(dimensionSize: 34, to: .width), height: 52))
        view.addSubview(signInBtn);
    }

    private func createEditProfileAndLogoutLabel() {
        let editProfileLabel = CustomLabel(text: "Edit Profile", fontSize: 16, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 72, y: 154, width: 120, height: 40))
        view.addSubview(editProfileLabel);
        let logoutLabel = CustomLabel(text: "Logout", fontSize: 16, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 72, y: editProfileLabel.frame.origin.y + 52, width: 120, height: 40))
        view.addSubview(logoutLabel);
    }

    private func createEditProfileAndLogoutIcons() {
        let userIcon = UIImage(named: "user_icon");
        let logoutIcon = UIImage(named: "logout");
        let userImgView = UIImageView(frame: CGRect(x: 38, y: 162, width: userIcon?.size.width ?? 18, height: userIcon?.size.height ?? 18));
        let logoutImgView = UIImageView(frame: CGRect(x: 38, y: userImgView.frame.origin.y + 52, width: logoutIcon?.size.width ?? 24, height: logoutIcon?.size.height ?? 18));
        userImgView.image = userIcon;
        logoutImgView.image = logoutIcon;
        view.addSubview(userImgView);
        view.addSubview(logoutImgView);
    }

    private func createShadowSheet() {
        shadowSheet.backgroundColor = UIColor.black.withAlphaComponent(0.6)
        tabBarController?.view.addSubview(shadowSheet)
        createLogoutPopup()
    }

    private func createLogoutPopup() {
        logoutView = UIView(frame: CGRect(x: (width - width * 0.90) / 2, y: height / 2 - 20, width: width * 0.90, height: 168));
        shadowSheet.addSubview(logoutView);
        logoutView.backgroundColor = Colors.modelDetail;
        let confirmLogoutLabel = CustomLabel(text: "Confirm Logout", fontSize: 18, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 24, y: 22, width: 150, height: 40));
        let msg = CustomLabel(text: "Are you sure you want to logout?", fontSize: 16, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 24, y: confirmLogoutLabel.frame.origin.y + 35, width: width, height: 40));
        let cancelBtn = UIButton(frame: CGRect(x: 80, y: msg.frame.origin.y + 50, width: 100, height: 35));
        cancelBtn.setTitle("CANCEL", for: .normal);
        cancelBtn.addTarget(nil, action: #selector(cancel), for: .touchUpInside)
        cancelBtn.setTitleColor(Colors.title, for: .normal)
        logoutView.addSubview(confirmLogoutLabel);
        logoutView.addSubview(msg);
        logoutView.addSubview(cancelBtn);
        let logoutBtn = UIButton(frame: CGRect(x: cancelBtn.frame.origin.x + 130, y: cancelBtn.frame.origin.y, width: 100, height: 35))
        logoutBtn.setTitle("LOGOUT", for: .normal);
        logoutBtn.setTitleColor(Colors.title, for: .normal)
        logoutBtn.addTarget(nil, action: #selector(logout), for: .touchUpInside)
        logoutView.addSubview(logoutBtn);
    }

    @objc func cancel(_ sender: UIButton) {
        print("tapped cancel")
        logoutView.removeFromSuperview();
    }

    @objc func logout(_ sender : UIButton){
        print("tapped logout")
        logoutView.removeFromSuperview();

    }



}