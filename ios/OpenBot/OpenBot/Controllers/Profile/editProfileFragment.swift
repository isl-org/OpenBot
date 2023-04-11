//
// Created by Nitish Yadav on 11/04/23.
//

import Foundation
import UIKit
import GoogleSignIn

class editProfileFragment: UIViewController {
    private var profileIcon: UIImageView!
    private let authentication = Authentication()
    private var firstName: UILabel!
    private var lastName: UILabel!
    private var email: UILabel!
    private var dob: UILabel!
    private var firstNameField : UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
        createUserProfileImageView()
        getUserDetails();
        createLabels();
        createTextFields();
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard))
        view.addGestureRecognizer(tapGesture)
    }

    private func createUserProfileImageView() {

        profileIcon = UIImageView(frame: CGRect(x: (width - 121) / 2, y: 138, width: 121, height: 121));
        profileIcon.layer.cornerRadius = profileIcon.frame.size.width / 2
        profileIcon.clipsToBounds = true
        let img = authentication.googleSignIn.currentUser?.profile?.imageURL(withDimension: 100);
        profileIcon.load(url: img!);
        self.view.addSubview(self.profileIcon)
    }

    func getUserDetails() {
        // Access the user details from the Authentication object
        let email = authentication.googleSignIn.currentUser?.profile?.email
        let name = authentication.googleSignIn.currentUser?.profile?.name
        print(email, name)

    }

    private func createLabels() {
        firstName = CustomLabel(text: "First Name", fontSize: 16, fontColor: UIColor(named: "gamepad") ?? .black ,frame: CGRect(x: 26, y:(profileIcon.frame.origin.y + 171.0) , width: 150, height: 40))
        lastName = CustomLabel(text: "Last Name", fontSize: 16, fontColor: UIColor(named: "gamepad") ?? .black ,frame: CGRect(x: 26, y:(firstName.frame.origin.y + 100.0) , width: 150, height: 40))
        dob = CustomLabel(text: "Date Of Birth", fontSize: 16, fontColor: UIColor(named: "gamepad") ?? .black ,frame: CGRect(x: 26, y:(lastName.frame.origin.y + 100.0) , width: 150, height: 40))
        email = CustomLabel(text: "Email", fontSize: 16, fontColor: UIColor(named: "gamepad") ?? .black ,frame: CGRect(x: 26, y:(dob.frame.origin.y + 100.0) , width: 150, height: 40))
        view.addSubview(firstName);
        view.addSubview(lastName);
        view.addSubview(dob);
        view.addSubview(email);
    }

    private func createTextFields(){
        firstNameField = CustomTextField(frame: CGRect(x: 17, y: firstName.frame.origin.y + 36.0 , width: width - 34, height: 47));
        view.addSubview(firstNameField);
        setTextField(textField: firstNameField, value:authentication.googleSignIn.currentUser?.profile?.name ?? "" );
        let lastNameField = CustomTextField(frame: CGRect(x: 17, y: lastName.frame.origin.y + 36.0, width: width - 34, height: 47));
        setTextField(textField: lastNameField, value:authentication.googleSignIn.currentUser?.profile?.name ?? "" );
        let dobField = CustomTextField(frame: CGRect(x: 17, y: dob.frame.origin.y + 36.0, width:  width - 34, height: 47));
        setTextField(textField: dobField, value: "19/09/2000");
        let emailField = CustomTextField(frame: CGRect(x: 17, y: email.frame.origin.y + 36.0, width: width - 34, height: 47));
        setTextField(textField: emailField, value: authentication.googleSignIn.currentUser?.profile?.email ?? "");
        emailField.isEnabled = false
        view.addSubview(firstNameField);
        view.addSubview(lastNameField);
        view.addSubview(dobField);
        view.addSubview(emailField);
    }

    private func setTextField(textField : UITextField,value : String){
        textField.layer.masksToBounds = true;
        textField.layer.borderColor = UIColor(red: 0/255, green: 113/255, blue: 197/255, alpha: 0.4).cgColor
        textField.layer.borderWidth = 2.0
        textField.layer.cornerRadius = 5;
        textField.text = value;
        textField.textColor = UIColor(named: "gamepad");
    }

    @objc func dismissKeyboard() {
        view.endEditing(true)
    }


}
