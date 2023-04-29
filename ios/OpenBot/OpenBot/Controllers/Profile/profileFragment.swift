//
// Created by Nitish Yadav on 06/04/23.
//

import Foundation
import UIKit
import GoogleSignIn

/**
 Class for profile fragment extending ViewController.
 */
class profileFragment: UIViewController {
    let shadowSheet = UIView(frame: UIScreen.main.bounds);
    var logoutView = UIView()
    let alert = UIAlertController(title: "Loading", message: "Please wait while we load the profile...", preferredStyle: .alert)
    var firstLabel = UILabel()
    var secondLabel = UILabel()
    var signInBtn = GoogleSignInBtn()
    var editProfileLabel = UILabel()
    var logoutLabel = UILabel()
    var userImgView = UIImageView()
    var logoutImgView = UIImageView()
    var signInView = UIView()
    var guestView = UIView()

    /**
     Calls after view of profile will load. This will create UI of the View
     */
    override func viewDidLoad() {
        super.viewDidLoad()
        createMyProfileLabel();
        navigationController?.navigationBar.setBackgroundImage(UIImage(), for: .default)
        navigationController?.navigationBar.shadowImage = UIImage()
        signInView = CustomView(frame: view.bounds, backgroundColor: traitCollection.userInterfaceStyle == .dark ? UIColor.black : UIColor.white);
        guestView = CustomView(frame: view.bounds, backgroundColor: traitCollection.userInterfaceStyle == .dark ? UIColor.black : UIColor.white);
        view.addSubview(signInView);
        view.addSubview(guestView);
        createEditProfileAndLogoutLabel();
        createEditProfileAndLogoutIcons();
        createPleaseSignInLabel();
        createSignInBtn();
        updateViewsVisibility()
        NotificationCenter.default.addObserver(self, selector: #selector(googleSignIn), name: .googleSignIn, object: nil)

    }

    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        if currentOrientation == .portrait {
            userImgView.frame.origin.x = safeAreaLayoutValue.left + 38;
            logoutImgView.frame.origin.x = safeAreaLayoutValue.left + 38;
            editProfileLabel.frame.origin.x = userImgView.right + 20;
            logoutLabel.frame.origin.x = editProfileLabel.left;
        } else {
            userImgView.frame.origin.x = safeAreaLayoutValue.top + 38;
            logoutImgView.frame.origin.x = safeAreaLayoutValue.top + 38;
            editProfileLabel.frame.origin.x = userImgView.right + 20;
            logoutLabel.frame.origin.x = editProfileLabel.left;
        }
    }

    /**
     Function to create My profile Label on top
     */
    private func createMyProfileLabel() {
        let label = CustomLabel(text: "My Profile", fontSize: 20, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 20, y: 90, width: 100, height: 40));
        view.addSubview(label)
    }

    /**
     Function to create the message for sign-in
     */
    private func createPleaseSignInLabel() {
        firstLabel = CustomLabel(text: "Set up your profile by signing in with", fontSize: 16, fontColor: Colors.textColor ?? .black, frame: CGRect(x: width / 2 - 135, y: height / 2 - 20, width: 270, height: 40));
        secondLabel = CustomLabel(text: "your Google account.", fontSize: 16, fontColor: Colors.textColor ?? .black, frame: CGRect(x: width / 2 - 80, y: height / 2 + 10, width: 160, height: 40));
//        view.addSubview(firstLabel);
//        view.addSubview(secondLabel);
        guestView.addSubview(firstLabel);
        guestView.addSubview(secondLabel);
    }

    /**
     Function to create google sign-in button.
     */
    private func createSignInBtn() {
        signInBtn = GoogleSignInBtn(frame: CGRect(x: adapted(dimensionSize: 17, to: .width), y: height / 2 + 60, width: width - adapted(dimensionSize: 34, to: .width), height: 52))
        let tap = UITapGestureRecognizer(target: self, action: #selector(signIn))
        signInBtn.addGestureRecognizer(tap);
//        view.addSubview(signInBtn);
        guestView.addSubview(signInBtn);
    }

    /**
     Function to create labels for edit profile and logout
     */
    private func createEditProfileAndLogoutLabel() {
        editProfileLabel = CustomLabel(text: "Edit Profile", fontSize: 16, fontColor: Colors.textColor ?? .black, frame: CGRect(x: userImgView.right + 72, y: 145, width: 120, height: 50))
        signInView.addSubview(editProfileLabel);
        editProfileLabel.isUserInteractionEnabled = true;
        let tapOnEditProfile = UITapGestureRecognizer(target: self, action: #selector(editProfileHandler))
        editProfileLabel.addGestureRecognizer(tapOnEditProfile);
        logoutLabel = CustomLabel(text: "Logout", fontSize: 16, fontColor: Colors.textColor ?? .black, frame: CGRect(x: userImgView.right + 72, y: editProfileLabel.bottom + 25, width: 120, height: 50))
        signInView.addSubview(logoutLabel);
        logoutLabel.isUserInteractionEnabled = true;
        let tapOnLogout = UITapGestureRecognizer(target: self, action: #selector(logoutHandler))
        logoutLabel.addGestureRecognizer(tapOnLogout);
    }

    /**
     Function to create edit profile and logout icons
     */
    private func createEditProfileAndLogoutIcons() {
        let userIcon = UIImage(named: "user_icon");
        let logoutIcon = UIImage(named: "logout");
        userImgView = UIImageView(frame: CGRect(x: safeAreaLayoutValue.left + 38, y: 162, width: userIcon?.size.width ?? 18, height: userIcon?.size.height ?? 18));
        logoutImgView = UIImageView(frame: CGRect(x: safeAreaLayoutValue.left + 38, y: userImgView.bottom + 52, width: logoutIcon?.size.width ?? 24, height: logoutIcon?.size.height ?? 18));
        userImgView.image = userIcon;
        logoutImgView.image = logoutIcon;
        signInView.addSubview(userImgView);
        signInView.addSubview(logoutImgView);
    }

    /**
     Function to create shadow sheet which will load on logout popup.
     */
    private func createShadowSheet() {
        shadowSheet.backgroundColor = UIColor.black.withAlphaComponent(0.6)
        tabBarController?.view.addSubview(shadowSheet)
        createLogoutPopup()
    }

    /**
     Function to create logout popup
     */
    private func createLogoutPopup() {
        logoutView = UIView(frame: CGRect(x: (width - width * 0.90) / 2, y: height / 2 - 20, width: width * 0.90, height: 168));
        shadowSheet.addSubview(logoutView);
        logoutView.backgroundColor = Colors.lightBlack;
        let confirmLogoutLabel = CustomLabel(text: "Confirm Logout", fontSize: 18, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 24, y: 22, width: 150, height: 40));
        let msg = CustomLabel(text: "Are you sure you want to logout?", fontSize: 16, fontColor: Colors.textColor ?? .black, frame: CGRect(x: 24, y: confirmLogoutLabel.frame.origin.y + 35, width: width, height: 40));
        let cancelBtn = UIButton(frame: CGRect(x: 80, y: msg.frame.origin.y + 50, width: 100, height: 35));
        cancelBtn.setTitle("CANCEL", for: .normal);
        cancelBtn.addTarget(self, action: #selector(cancel), for: .touchUpInside)
        cancelBtn.setTitleColor(Colors.title, for: .normal)
        logoutView.addSubview(confirmLogoutLabel);
        logoutView.addSubview(msg);
        logoutView.addSubview(cancelBtn);
        let logoutBtn = UIButton(frame: CGRect(x: cancelBtn.frame.origin.x + 130, y: cancelBtn.frame.origin.y, width: 100, height: 35))
        logoutBtn.setTitle("LOGOUT", for: .normal);
        logoutBtn.setTitleColor(Colors.title, for: .normal)
        logoutBtn.addTarget(self, action: #selector(logout), for: .touchUpInside)
        logoutView.addSubview(logoutBtn);
    }

    /**
     Function that will add edit profile view controller to navigation controller
     */
    @objc func editProfileHandler() {
        createOverlayAlert()
        let storyboard = UIStoryboard(name: "openCode", bundle: nil)
        let viewController = storyboard.instantiateViewController(withIdentifier: "editProfile")
        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
            self.navigationController?.pushViewController(viewController, animated: true)
            self.alert.dismiss(animated: true, completion: nil)

        }
    }

    /**
     Function for logout from google
     */
    @objc func logoutHandler() {
        print("inside logoutHandler")
        createShadowSheet();
    }

    @objc func cancel(_ sender: UIButton) {
        shadowSheet.removeFromSuperview();
    }

    /**
     Function for logout from google
     */
    @objc func logout(_ sender: UIButton) {
        shadowSheet.removeFromSuperview();
        GIDSignIn.sharedInstance.signOut()
        updateViewsVisibility()
    }

    /**
     Refresh the UI based on google sign-in and guest sign-in
     */
    private func updateViewsVisibility() {
        if GIDSignIn.sharedInstance.currentUser != nil {
            // Show firstLabel, secondLabel, and SignInBtn, and hide other views
            signInView.isHidden = false;
            guestView.isHidden = true;
        } else {
            // Show editProfileLabel, logoutLabel, userImgView, and logoutImgView, and hide other views
            signInView.isHidden = true;
            guestView.isHidden = false;
        }
    }

    /**
     Function to call Authentication class constructor.
     */
    @objc func signIn() {
        Authentication();
    }

    @objc func googleSignIn(_ notification: Notification) {
        updateViewsVisibility()
    }

    /**
     Method to create animation
     */
    func createOverlayAlert() {
        let loadingIndicator: UIActivityIndicatorView = UIActivityIndicatorView(frame: CGRectMake(10, 5, 50, 50)) as UIActivityIndicatorView
        loadingIndicator.startAnimating();
        loadingIndicator.hidesWhenStopped = true
        loadingIndicator.style = UIActivityIndicatorView.Style.medium
        alert.view.addSubview(loadingIndicator)
        present(alert, animated: true, completion: nil)
    }


}