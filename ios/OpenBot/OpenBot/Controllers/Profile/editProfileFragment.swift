//
// Created by Nitish Yadav on 11/04/23.
//

import Foundation
import UIKit
import GoogleSignIn
import FirebaseAuth
import FirebaseStorage

/***
 class for fragment of edit profile section
 */
class editProfileFragment: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
/**
 Outlets for the user interface elements used in this view controller.
*/
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

/**
    Method calls after view loaded
 */
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

    /**
     function creates the user profile image view and related components.
     */
    private func createUserProfileImageView() {
        profileIcon = UIImageView(frame: CGRect(x: (width - 121) / 2, y: safeAreaLayoutValue.top + adapted(dimensionSize: 20, to: .height), width: adapted(dimensionSize: 100, to: .height), height: adapted(dimensionSize: 100, to: .height)));
        profileIcon.layer.cornerRadius = profileIcon.frame.size.width / 2
        profileIcon.clipsToBounds = true
        let imgUrl = (Auth.auth().currentUser?.photoURL ?? authentication.googleSignIn.currentUser?.profile?.imageURL(withDimension: 100))!;
        profileIcon.load(url: imgUrl);
        view.addSubview(profileIcon)
        profileIcon.contentMode = .scaleAspectFill
        let uploadImgIcon = UIImage(named: "uploadImage");
        let uploadImage = UIImageView(frame: CGRect(x: profileIcon.frame.origin.x + profileIcon.frame.width / 2 + adapted(dimensionSize: 10, to: .height), y: profileIcon.frame.origin.y + profileIcon.frame.height - 40.0, width: 40.5, height: 40.5));
        uploadImage.image = uploadImgIcon;
        uploadImage.isUserInteractionEnabled = true;
        let tap = UITapGestureRecognizer(target: self, action: #selector(openImagePicker))
        uploadImage.addGestureRecognizer(tap)
        view.addSubview(uploadImage);
    }

    /**
     This function creates the labels for the text fields.
     */
    private func createLabels() {
        firstName = CustomLabel(text: "First Name", fontSize: 16, fontColor: traitCollection.userInterfaceStyle == .dark ? UIColor.white : UIColor.black, frame: CGRect(x: 26, y: (profileIcon.frame.origin.y + profileIcon.frame.width + adapted(dimensionSize: 40, to: .height)), width: 150, height: 40))
        lastName = CustomLabel(text: "Last Name", fontSize: 16, fontColor: traitCollection.userInterfaceStyle == .dark ? UIColor.white : UIColor.black, frame: CGRect(x: 26, y: (firstName.frame.origin.y + 100.0), width: 150, height: 40))
        dob = CustomLabel(text: "Date Of Birth", fontSize: 16, fontColor: traitCollection.userInterfaceStyle == .dark ? UIColor.white : UIColor.black, frame: CGRect(x: 26, y: (lastName.frame.origin.y + 100.0), width: 150, height: 40))
        email = CustomLabel(text: "Email", fontSize: 16, fontColor: traitCollection.userInterfaceStyle == .dark ? UIColor.white : UIColor.black, frame: CGRect(x: 26, y: (dob.frame.origin.y + 100.0), width: 150, height: 40))
        view.addSubview(firstName);
        view.addSubview(lastName);
        view.addSubview(dob);
        view.addSubview(email);
    }

    /**
     This function creates input fields
     */
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
        dob.isEnabled = false;
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

    /**
     Function to setup the textFields their radius background Colors
     - Parameters:
       - textField: the field to be set
       - value: default value to textField
     */
    private func setTextField(textField: UITextField, value: String) {
        textField.layer.masksToBounds = true;
        textField.layer.borderColor = UIColor(red: 0 / 255, green: 113 / 255, blue: 197 / 255, alpha: 0.4).cgColor
        textField.layer.borderWidth = 1.5
        textField.layer.cornerRadius = 8;
        textField.text = value;
        textField.textColor = traitCollection.userInterfaceStyle == .dark ? UIColor.white : UIColor.black;
    }

    /**
     Function to get first name of user from Full name
     - Parameter name:
     - Returns: first name of user
     */
    private func getFirstName(name: String) -> String {
        let indexOfSpace = name.firstIndex(of: " ");
        if indexOfSpace == nil {
            return name;
        }
        return String(name[..<indexOfSpace!])
    }

    /**
     function to return the last name of user from google sign-in
     - Parameter name:
     - Returns:
     */
    private func getLastName(name: String) -> String {
        let indexOfSpace = name.firstIndex(of: " ");
        if indexOfSpace == nil {
            return name;
        }
        return String(name[indexOfSpace!...])
    }

    /**
     function to remove the keyboard from screen
     */
    @objc func dismissKeyboard() {
        view.endEditing(true)
    }

    /**
     Function to open the image picker on tap of image
     */
    @objc func openImagePicker() {
        imagePickerVC.sourceType = .photoLibrary
        present(imagePickerVC, animated: true)
    }

    /**
     delegate called after successfully selecting an image
     - Parameters:
       - picker:
       - info:
     */
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey: Any]) {
        picker.dismiss(animated: true, completion: nil)
        if let image = info[.originalImage] as? UIImage {
            profileIcon.image = image
        }
    }

    /**
     handler of done button of date picker
     - Parameter sender:
     */
    @objc func doneButtonTapped(_ sender: UITextField) {
        // Dismiss the date picker
        sender.resignFirstResponder()
    }

    /**
     Function to create button named cancle and done.
     */
    private func createButtons() {
        let cancelBtn = CustomButton(text: "Cancel", frame: CGRect(x: 17, y: height - safeAreaLayoutValue.bottom - adapted(dimensionSize: 47, to: .height), width: 147, height: 47), selector: #selector(cancel))
        view.addSubview(cancelBtn);
        let saveChangesBtn = CustomButton(text: "Save Changes", frame: CGRect(x: cancelBtn.frame.origin.x + 194.0, y: cancelBtn.frame.origin.y, width: 147, height: 47), selector: #selector(saveChanges))
        view.addSubview(saveChangesBtn)
        saveChangesBtn.backgroundColor = Colors.title
    }

    /**
     handle of cancel button tap. This will return to profile page
     - Parameter sender:
     */
    @objc func cancel(_ sender: UIButton) {
        navigationController?.popViewController(animated: true);
    }

    /**
     Handler of done button. Calls apis for update the profile
     - Parameter sender:
     */
    @objc func saveChanges(_ sender: UIButton) {
        createOverlayAlert()
        let credential = GoogleAuthProvider.credential(withIDToken: (authentication.googleSignIn.currentUser?.idToken!.tokenString)!, accessToken: (authentication.googleSignIn.currentUser?.accessToken.tokenString)!)
        Auth.auth().signIn(with: credential) { result, error in
            if let user = result?.user {
                let changeRequest = user.createProfileChangeRequest()
                changeRequest.displayName = (self.firstNameField.text ?? "") + " " + (self.lastNameField.text ?? "");
                self.uploadImage();
                changeRequest.commitChanges { error in
                    if let error = error {
                        print("Error updating profile: \(error.localizedDescription)")
                    } else {
                        print("Profile updated successfully")
                        self.alert.dismiss(animated: true);
                        self.showToast("Profile updated successfully")
                    }
                }
            }
        }
    }

    /**
     A function for update of user image. Calls after done button tap
     */
    private func uploadImage() {
        // Get a reference to the storage service
        let storage = Storage.storage()

// Create a reference to the image file in Firebase storage using the user's UID as the filename
        let uid = Auth.auth().currentUser?.uid ?? ""
        let imageRef = storage.reference().child("profile_pictures/\(uid).jpg")

// Convert the image to data
        guard let image = profileIcon.image, let imageData = image.jpegData(compressionQuality: 0.5) else {
            print("Error converting image to data")
            return
        }

        let maxSize: CGFloat = 1024 // maximum size of the image
        var actualHeight = image.size.height
        var actualWidth = image.size.width
        var maxHeight = maxSize
        var maxWidth = maxSize

// Check the aspect ratio of the image
        let aspectRatio = actualWidth/actualHeight

        if aspectRatio > 1 {
            // Landscape image
            maxWidth = maxSize
            maxHeight = maxSize/aspectRatio
        } else {
            // Portrait image
            maxHeight = maxSize
            maxWidth = maxSize*aspectRatio
        }

        let rect = CGRect(x: 0.0, y: 0.0, width: maxWidth, height: maxHeight)
        UIGraphicsBeginImageContext(rect.size)
        image.draw(in: rect)
        let compressedImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()

        guard let compressedImageData = compressedImage?.jpegData(compressionQuality: 0.5) else {
            print("Error compressing image")
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
                                self.showToast("Profile updated successfully")
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
