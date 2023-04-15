//
// Created by Nitish Yadav on 11/04/23.
//

import Foundation
import UIKit
import GoogleSignIn
import FirebaseAuth
import FirebaseStorage
class editProfileFragment: UIViewController,UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    private var profileIcon: UIImageView!
    private let authentication = Authentication()
    private var firstName: UILabel!
    private var lastName: UILabel!
    private var email: UILabel!
    private var dob: UILabel!
    private var firstNameField: UITextField!
    private var lastNameField: UITextField!
    let imagePickerVC = UIImagePickerController()
    let alert = UIAlertController(title: nil, message: "Please wait...", preferredStyle: .alert)

    override func viewDidLoad() {
        super.viewDidLoad()
        imagePickerVC.delegate = self;
        createUserProfileImageView()
        createLabels();
        createTextFields();
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard))
        view.addGestureRecognizer(tapGesture)
        createButtons();
    }

    private func createUserProfileImageView() {
        profileIcon = UIImageView(frame: CGRect(x: (width - 121) / 2, y: safeAreaLayoutValue.top + adapted(dimensionSize: 20, to: .height), width: adapted(dimensionSize: 100, to: .height), height: adapted(dimensionSize: 100, to: .height)));
        profileIcon.layer.cornerRadius = profileIcon.frame.size.width / 2
        profileIcon.clipsToBounds = true
        let imgUrl = (Auth.auth().currentUser?.photoURL ?? authentication.googleSignIn.currentUser?.profile?.imageURL(withDimension: 100))!;
        profileIcon.load(url: imgUrl);
        view.addSubview(profileIcon)
        let uploadImgIcon = UIImage(named: "uploadImage");
        let uploadImage = UIImageView(frame: CGRect(x: profileIcon.frame.origin.x + profileIcon.frame.width / 2 + adapted(dimensionSize: 10, to: .height), y: profileIcon.frame.origin.y + profileIcon.frame.height - 40.0, width: 40.5, height: 40.5));
        uploadImage.image = uploadImgIcon;
        uploadImage.isUserInteractionEnabled = true;
        let tap = UITapGestureRecognizer(target: self, action: #selector(openImagePicker))
        uploadImage.addGestureRecognizer(tap)
        view.addSubview(uploadImage);
    }

    private func createLabels() {
        firstName = CustomLabel(text: "First Name", fontSize: 16, fontColor: traitCollection.userInterfaceStyle == .dark ? UIColor.white : UIColor.black, frame: CGRect(x: 26, y: (profileIcon.frame.origin.y + profileIcon.frame.width + adapted(dimensionSize: 40, to: .height)), width: 150, height: 40))
        lastName = CustomLabel(text: "Last Name", fontSize: 16, fontColor: traitCollection.userInterfaceStyle == .dark ? UIColor.white : UIColor.black, frame: CGRect(x: 26, y: (firstName.frame.origin.y + 100.0), width: 150, height: 40))
        dob = CustomLabel(text: "Date Of Birth", fontSize: 16, fontColor: traitCollection.userInterfaceStyle == .dark ? UIColor.white : UIColor.black, frame: CGRect(x: 26, y: (lastName.frame.origin.y + 100.0), width: 150, height: 40))
        email = CustomLabel(text: "Email", fontSize: 16, fontColor:traitCollection.userInterfaceStyle == .dark ? UIColor.white : UIColor.black, frame: CGRect(x: 26, y: (dob.frame.origin.y + 100.0), width: 150, height: 40))
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
        textField.textColor = traitCollection.userInterfaceStyle == .dark ? UIColor.white : UIColor.black;
    }

    private func getFirstName(name: String) -> String {
        let indexOfSpace = name.firstIndex(of: " ");
        if indexOfSpace == nil {
            return name;
        }
        return String(name[..<indexOfSpace!])
    }

    private func getLastName(name: String) -> String {
        let indexOfSpace = name.firstIndex(of: " ");
        if indexOfSpace == nil {
            return name;
        }
        return String(name[indexOfSpace!...])
    }

    @objc func dismissKeyboard() {
        view.endEditing(true)
    }

    @objc func openImagePicker() {

        imagePickerVC.sourceType = .photoLibrary
        present(imagePickerVC, animated: true)
    }

    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        picker.dismiss(animated: true, completion: nil)
        if let image = info[.originalImage] as? UIImage {
            profileIcon.image = image
        }
    }

    @objc func doneButtonTapped(_ sender: UITextField) {
        // Dismiss the date picker
        sender.resignFirstResponder()
    }

    private func createButtons() {
        let cancelBtn = CustomButton(text: "Cancel", frame: CGRect(x: 17, y: height - safeAreaLayoutValue.bottom - adapted(dimensionSize: 47, to: .height), width: 147, height: 47), selector: #selector(cancel))
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
//                changeRequest.photoURL = URL(string: "https://www.google.com/imgres?imgurl=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fe%2Fe7%2FEverest_North_Face_toward_Base_Camp_Tibet_Luca_Galuzzi_2006.jpg&tbnid=ZH4n6DnAkszpmM&vet=12ahUKEwiZx6jc6aP-AhWdzaACHb-VD9MQMygAegUIARDdAQ..i&imgrefurl=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FMountain&docid=oiQv_zLtLLGShM&w=2000&h=1333&q=mountain&ved=2ahUKEwiZx6jc6aP-AhWdzaACHb-VD9MQMygAegUIARDdAQ")
                self.uploadImage();
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

    private func uploadImage(){
        // Get a reference to the storage service
        let storage = Storage.storage()

// Create a reference to the image file in Firebase storage using the user's UID as the filename
        let uid = Auth.auth().currentUser?.uid ?? ""
        let imageRef = storage.reference().child("users/\(uid)/profile.jpg")

// Convert the image to data
        guard let imageData = profileIcon.image?.jpegData(compressionQuality: 0.5) else {
            print("Error converting image to data")
            return
        }

// Upload the image data to Firebase storage
        let uploadTask = imageRef.putData(imageData, metadata: nil) { metadata, error in
            if let error = error {
                print("Error uploading image: \(error.localizedDescription)")
            } else {
                // Get the download URL of the uploaded image
                imageRef.downloadURL { url, error in
                    if let error = error {
                        print("Error getting download URL: \(error.localizedDescription)")
                    } else {
                        // Update the user's profile with the download URL of the uploaded image
                        let changeRequest = Auth.auth().currentUser?.createProfileChangeRequest()
                        changeRequest?.photoURL = url
                        changeRequest?.commitChanges { error in
                            if let error = error {
                                print("Error updating profile: \(error.localizedDescription)")
                            } else {
                                print("Profile updated successfully")
                            }
                        }
                    }
                }
            }
        }

// Observe the upload progress
        uploadTask.observe(.progress) { snapshot in
            let percentComplete = Double(snapshot.progress!.completedUnitCount) / Double(snapshot.progress!.totalUnitCount) * 100.0
            print("Upload progress: \(percentComplete)%")
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
