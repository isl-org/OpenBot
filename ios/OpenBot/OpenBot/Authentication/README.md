
# Firebase

OpenBot utilizes Firebase, a mobile and web application development platform owned by Google, for Google sign-in. With Firebase, we can easily sign in to OpenBot using their Google account, which allows them to load existing projects from Google Drive and upload new ones.

In addition to these features, OpenBot also allows users to perform updates to their profile. Firebase provides a comprehensive set of tools for authentication, making it easy for users to securely manage their personal information and account settings.

### Prerequisites

To integrate Firebase into an iOS Swift project for Google sign-in, we will need a few prerequisites.

- A Google Account To use Google Sign-in with Firebase, you must have a Google account. If you don't have one, you can create one for free at https://accounts.google.com/signup.
- A Firebase Project To use Firebase, you need to create a Firebase project. You can create a project by visiting the Firebase Console at https://console.firebase.google.com.
- Cocoapods cocoapods is a dependency manager for Swift and Objective-C projects. To install the Firebase SDK, you need to have Cocoapods installed on your system.

### Pods

Make sure to Add following command to pod file.
```
  pod 'Firebase/Core'
  pod 'Firebase/Storage'
  pod 'Firebase/Auth'
  pod 'GoogleSignIn'
  pod 'GoogleSignInSwiftSupport'
  pod 'GoogleAPIClientForREST/Drive' 
  ```

### Create Firebase Project

- Go to the Firebase Console (https://console.firebase.google.com/) and sign in with your Google account.

- Click on the "Add Project" button to create a new Firebase project.

- Enter a name for your project, select your country/region, and then click on the "Create Project" button.

- Once your project is created, click on the "iOS" icon to add Firebase to your iOS app.

- Enter the bundle ID of your iOS app in the "iOS bundle ID" field.

- Enter an App nickname and click on the "Register App" button.

- Download the "GoogleService-Info.plist" configuration file and add it to your    Xcode project.

- Add the Firebase SDK to your Xcode project by adding all of above [Pods](#Pods).

### Set up the iOS Project

Install the Firebase SDK using CocoaPods. Open the Terminal app on your Mac and navigate to your Xcode project's root directory. Run the following command to create a Podfile in your Xcode project's root directory:
```
pod init
```
Open the Podfile using your preferred text editor and add the following lines at the end of the file:
```
  pod 'Firebase/Core'
  pod 'Firebase/Storage'
  pod 'Firebase/Auth'
  pod 'GoogleSignIn'
  pod 'GoogleSignInSwiftSupport'
  pod 'GoogleAPIClientForREST/Drive' 
  ```
Save and close the Podfile, then Run the following command to install the Firebase SDKs:
  ```
  pod install
```
You can now import the Firebase SDKs in your Swift code by adding the following lines at the top of your Swift file:
```
import Firebase
import FirebaseAuth
```

### Verify the Firebase Integration
To verify that the Firebase SDK has been integrated correctly, add the following code to your app's `AppDelegate.swift` file:
```
FirebaseApp.configure()
```
This code initializes Firebase and sets up the default Firebase app. You can place this code in the application(_:didFinishLaunchingWithOptions:) method.

Next, add the following code to a view controller:
```
if let currentUser = Auth.auth().currentUser {
    print("User is signed in with uid: \(currentUser.uid)")
} else {
    print("No user is signed in.")
}
```
This code checks if there is a signed-in user and prints the user's unique ID to the console. You can place this code in the viewDidLoad() method.

Run your app and check the console output. If you see "User is signed in with uid: " followed by a user ID, then the Firebase integration was successful!

### Troubleshooting

Some common issues that may occur during the Firebase configuration process and their solutions:

    1. CocoaPods installation errors: If you encounter issues during the CocoaPods installation, such as pod install failing to run or not installing the correct Firebase SDK version, try the following solutions:
- Update your CocoaPods version by running sudo gem install cocoapods.
- Delete the Podfile.lock file and run pod install again.
- Make sure you have added the Firebase SDK pod to your Podfile correctly, using the exact version specified in the Firebase Console.

2. **Firebase configuration errors: If you are unable to configure Firebase correctly, such as not being able to initialize Firebase in your app, try the following solutions:**

- Double-check that you have followed all the steps in the "Set up the iOS Project" section of this document correctly.
- Make sure you have added the Firebase configuration file (GoogleService-Info.plist) to your Xcode project correctly.
- Ensure that the Firebase SDK has been added to your Xcode project correctly and is being imported in your code.
- Verify that your Firebase project is set up correctly in the Firebase Console, including the correct bundle ID and other project settings.
- Conflicts with other libraries: If you experience conflicts with other libraries in your project, such as having conflicting dependencies or incompatible SDK versions, try the following solutions:
- Ensure that you have specified the correct Firebase SDK version in your Podfile.
- Try updating your other libraries to the latest version to see if the issue is   resolved.
- Check for any conflicting dependencies or library versions and try to resolve them by removing or updating the conflicting libraries.