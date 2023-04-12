//
// Created by Nitish Yadav on 11/04/23.
//

import Foundation
import UIKit
import GoogleSignIn
import FirebaseAuth

class editProfileFragment: UIViewController {
    private var profileIcon: UIImageView!
    private let authentication = Authentication()
    private var firstName: UILabel!
    private var lastName: UILabel!
    private var email: UILabel!
    private var dob: UILabel!
    private var firstNameField: UITextField!
    private var lastNameField: UITextField!
    let alert = UIAlertController(title: nil, message: "Please wait...", preferredStyle: .alert)
    override func viewDidLoad() {
        super.viewDidLoad()
        print(authentication.googleSignIn.currentUser?.profile)
        createUserProfileImageView()
        getUserDetails();
        createLabels();
        createTextFields();
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard))
        view.addGestureRecognizer(tapGesture)
        createButtons();
    }

    private func createUserProfileImageView() {

        profileIcon = UIImageView(frame: CGRect(x: (width - 121) / 2, y: adapted(dimensionSize: 40, to: .height), width: adapted(dimensionSize: 100, to: .height), height:adapted(dimensionSize: 100, to: .height)));
        print(profileIcon.frame.width)
        profileIcon.layer.cornerRadius = profileIcon.frame.size.width / 2
        profileIcon.clipsToBounds = true
        profileIcon.layer.borderWidth = 2;
        let imgUrl = (Auth.auth().currentUser?.photoURL ?? authentication.googleSignIn.currentUser?.profile?.imageURL(withDimension: 100))! ;
        profileIcon.load(url: imgUrl );
        view.addSubview(profileIcon)
        let uploadImgIcon = UIImage(named: "uploadImage");
        let uploadImage = UIImageView(frame: CGRect(x:profileIcon.frame.origin.x + profileIcon.frame.width/2 + adapted(dimensionSize: 10, to: .height), y:profileIcon.frame.origin.y +  profileIcon.frame.height - 40.0, width: 40.5, height: 40.5));
        uploadImage.image = uploadImgIcon;
        view.addSubview(uploadImage);
    }

    func getUserDetails() {
        // Access the user details from the Authentication object
        let email = authentication.googleSignIn.currentUser?.profile?.email
        let name = authentication.googleSignIn.currentUser?.profile?.name
        print(email, name)

    }

    private func createLabels() {
        firstName = CustomLabel(text: "First Name", fontSize: 16, fontColor: UIColor(named: "gamepad") ?? .black, frame: CGRect(x: 26, y: (profileIcon.frame.origin.y + profileIcon.frame.width + adapted(dimensionSize: 40, to: .height)), width: 150, height: 40))
        lastName = CustomLabel(text: "Last Name", fontSize: 16, fontColor: UIColor(named: "gamepad") ?? .black, frame: CGRect(x: 26, y: (firstName.frame.origin.y + 100.0), width: 150, height: 40))
        dob = CustomLabel(text: "Date Of Birth", fontSize: 16, fontColor: UIColor(named: "gamepad") ?? .black, frame: CGRect(x: 26, y: (lastName.frame.origin.y + 100.0), width: 150, height: 40))
        email = CustomLabel(text: "Email", fontSize: 16, fontColor: UIColor(named: "gamepad") ?? .black, frame: CGRect(x: 26, y: (dob.frame.origin.y + 100.0), width: 150, height: 40))
        view.addSubview(firstName);
        view.addSubview(lastName);
        view.addSubview(dob);
        view.addSubview(email);

    }

    private func createTextFields() {
        firstNameField = CustomTextField(frame: CGRect(x: 17, y: firstName.frame.origin.y + adapted(dimensionSize: 30, to: .height), width: width - 34, height: 47));
        view.addSubview(firstNameField);
        setTextField(textField: firstNameField, value: getFirstName(name: Auth.auth().currentUser?.displayName ?? ""));
        lastNameField = CustomTextField(frame: CGRect(x: 17, y: lastName.frame.origin.y + adapted(dimensionSize: 30, to: .height), width: width - 34, height: 47));
        setTextField(textField: lastNameField, value: getLastName(name: Auth.auth().currentUser?.displayName ?? ""));
        let dobField = CustomTextField(frame: CGRect(x: 17, y: dob.frame.origin.y + adapted(dimensionSize: 30, to: .height), width: width - 34, height: 47));
        setTextField(textField: dobField, value: "19/09/2000");
        let datePicker = UIDatePicker()
        datePicker.datePickerMode = .date
        dobField.inputView = datePicker
        let toolbar = UIToolbar(frame: CGRect(x: 0, y: 0, width: view.frame.width, height: 44))
        let doneButton = UIBarButtonItem(barButtonSystemItem: .done, target: self, action: #selector(doneButtonTapped))
        toolbar.items = [UIBarButtonItem.flexibleSpace(), doneButton]
        dobField.inputAccessoryView = toolbar
        let img = UIImageView(frame: CGRect(x: dobField.frame.size.width - 40, y: 15, width: 20, height: 20));
        img.image = UIImage(named: "calendar");
        dobField.addSubview(img)
        let emailField = CustomTextField(frame: CGRect(x: 17, y: email.frame.origin.y + adapted(dimensionSize: 30, to: .height), width: width - 34, height: 47));
        setTextField(textField: emailField, value: authentication.googleSignIn.currentUser?.profile?.email ?? "");
        emailField.isEnabled = false
        view.addSubview(firstNameField);
        view.addSubview(lastNameField);
        view.addSubview(dobField);
        view.addSubview(emailField);
    }

    private func setTextField(textField: UITextField, value: String) {
        textField.layer.masksToBounds = true;
        textField.layer.borderColor = UIColor(red: 0 / 255, green: 113 / 255, blue: 197 / 255, alpha: 0.4).cgColor
        textField.layer.borderWidth = 1.5
        textField.layer.cornerRadius = 8;
        textField.text = value;
        textField.textColor = UIColor(named: "gamepad");
    }

   private  func getFirstName(name : String)->String{
        let indexOfSpace = name.firstIndex(of: " ");
       if indexOfSpace == nil {
           return name;
       }
       return String(name[..<indexOfSpace!])
    }

    private func getLastName(name : String)->String{
        let indexOfSpace = name.firstIndex(of: " ");
        if indexOfSpace == nil {
            return name;
        }
        return String(name[indexOfSpace!...])
    }

    @objc func dismissKeyboard() {
        view.endEditing(true)
    }

    @objc func doneButtonTapped(_ sender: UITextField) {
        // Dismiss the date picker
        sender.resignFirstResponder()
    }

    private func createButtons() {
        let cancelBtn = CustomButton(text: "Cancel", frame: CGRect(x: 17, y: height - 60, width: 147, height: 47), selector: #selector(cancel))
        view.addSubview(cancelBtn);
        let saveChangesBtn = CustomButton(text: "Save Changes", frame: CGRect(x: cancelBtn.frame.origin.x + 194.0, y: cancelBtn.frame.origin.y, width: 147, height: 47), selector: #selector(saveChanges))
        view.addSubview(saveChangesBtn)
        saveChangesBtn.backgroundColor = Colors.title
    }

    @objc func cancel(_ sender: UIButton) {
        navigationController?.popViewController(animated: true);
    }

    @objc func saveChanges(_ sender: UIButton) {
        createOverlayAlert()
        let credential = GoogleAuthProvider.credential(withIDToken: (authentication.googleSignIn.currentUser?.idToken!.tokenString)!, accessToken: (authentication.googleSignIn.currentUser?.accessToken.tokenString)!)
        Auth.auth().signIn(with: credential) { result, error in
            if let user = result?.user {
                let changeRequest = user.createProfileChangeRequest()
                changeRequest.displayName = (self.firstNameField.text ?? "") + " " + (self.lastNameField.text ?? "");
                changeRequest.photoURL = URL(string: "https://www.google.com/imgres?imgurl=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fe%2Fe7%2FEverest_North_Face_toward_Base_Camp_Tibet_Luca_Galuzzi_2006.jpg&tbnid=ZH4n6DnAkszpmM&vet=12ahUKEwiZx6jc6aP-AhWdzaACHb-VD9MQMygAegUIARDdAQ..i&imgrefurl=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FMountain&docid=oiQv_zLtLLGShM&w=2000&h=1333&q=mountain&ved=2ahUKEwiZx6jc6aP-AhWdzaACHb-VD9MQMygAegUIARDdAQ")
                print(user.displayName)
                changeRequest.commitChanges { error in
                    if let error = error {
                        print("Error updating profile: \(error.localizedDescription)")
                    } else {
                        print("Profile updated successfully")
                        self.alert.dismiss(animated: true);


                    }
                }
            }
        }
    }

    func createOverlayAlert() {
        let loadingIndicator: UIActivityIndicatorView = UIActivityIndicatorView(frame: CGRectMake(10, 5, 50, 50)) as UIActivityIndicatorView
        loadingIndicator.startAnimating();
        loadingIndicator.hidesWhenStopped = true
        loadingIndicator.style = UIActivityIndicatorView.Style.medium
        alert.view.addSubview(loadingIndicator)
        present(alert, animated: true, completion: nil)
    }

}
