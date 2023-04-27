## Firebase Google Sign-In Authentication

- #### Usage
  In web application, we use Firebase for Google sign-in authentication to upload OpenBot Playground projects on Google
  Drive. If you clone this project and run on your device, you must set up your own Firebase project because the
  firebase config is required for sign-in authentication.
- #### About  Google Sign-In
  Firebase Google Sign-In Authentication is a feature of the Firebase platform that allows users to sign in to mobile or
  web apps using their Google credentials. This service provides a secure and convenient way for users to access apps
  without having to remember and manage separate login credentials. Firebase manages the entire authentication process,
  from verifying the user's identity with Google to providing a unique user ID that can be used to personalize the user'
  s experience within the app. This feature also includes additional security measures, such as two-factor
  authentication, to help protect user accounts from unauthorized access.

****

### Setting up Firebase Project

- Go to the Firebase Console (https://console.firebase.google.com/) and sign in with your Google account.

- Click on the `Add Project` button to create a new Firebase project.

- Enter a name for your project, select your country/region, and then click on the `Create Project` button.
    <p align="left">
    <img style="padding-right: 2%; padding-top: 2%; padding-bottom: 2%" src="../../../docs/images/firebase_create_project.png" alt="Create New Project" width="30%"/>
    <img style="padding-right: 2%;padding-top: 2%; padding-bottom: 2% " src="../../../docs/images/firebase_success_creation.png" alt="Create New Project" width="30%"/>
    </p>

- Once your project is created, click on the `Web` icon to add Firebase to your web app and then enter an App nickname
  and click on the `Register App` button.
  <p align="left">
  <img style="padding-right: 2%; padding-top: 2%; padding-bottom: 2%;" src="../../../docs/images/firebase_web_icon.png" alt="Create New Project" width="30%"/>
  <img style="padding-right: 2%; padding-top: 2%; padding-bottom: 2%;" src="../../../docs/images/firebase_register_app.png" alt="Create New Project" width="30%"/>
  </p>

    - Add `firebase SDK` to your project's `env` file.
        - When creating project you will get firebase here, or you can get it from project setting.
          <p align="left">
          <img style="padding-right: 2%;padding-top: 2%; padding-bottom: 2%;" src="../../../docs/images/firebase_sdk.png" alt="Create New Project" width="30%"/>
          <img style="padding-right: 2%;padding-top: 2%; padding-bottom: 2%;" src="../../../docs/images/firebase_project_setting.png" alt="Create New Project" width="30%"/>
          <img style="padding-right: 2%;padding-top: 2%; padding-bottom: 2%;" src="../../../docs/images/firebase_project_setting_config.png" alt="Create New Project" width="30%"/>
          </p>

        - Using Environment Variables When using Firebase Authentication, you may need to store sensitive information
          such as API keys, database credentials, and other secrets. To do this securely, you can use environment
          variables to store this information outside of your code. by doing following steps.

            1. Create a new file in OpenBot Playground called .env.
                 <p align="left">
                <img style="padding-right: 2%;padding-top: 2%; padding-bottom: 2%;" src="../../../docs/images/firebase_env.png" alt="Create New Project" width="30%"/>
                 </p> 

            3. Add following environment variables to the .env file that will be used in firebase.js file.

            ```bash
              REACT_APP_FIREBASE_API_KEY=<REACT_APP_FIREBASE_API_KEY>
              REACT_APP_AUTH_DOMAIN=<REACT_APP_AUTH_DOMAIN>
              REACT_APP_PROJECT_ID=<REACT_APP_PROJECT_ID>
              REACT_APP_STORAGE_BUCKET=<REACT_APP_STORAGE_BUCKET>
              REACT_APP_MESSAGING_SENDER_ID=<REACT_APP_MESSAGING_SENDER_ID>
              REACT_APP_APP_ID=<REACT_APP_APP_ID>
              REACT_APP_MEASUREMENT_ID=<REACT_APP_MEASUREMENT_ID>
              GENERATE_SOURCEMAP=false
            ```

- Enable Firebase Authentication SignIn method using Google.

  <p align="left">

  <img style="padding-right: 2%; padding-top: 2%; padding-bottom: 2%;" src="../../../docs/images/firebase_authantication.png" alt="Create New Project" width="30%"/>

  <img style="padding-right: 2%; padding-top: 2%; padding-bottom: 2%;" src="../../../docs/images/firebase_google_option.png" alt="Create New Project" width="30%"/>

  <img style="padding-right: 2%;padding-top: 2%; padding-bottom: 2%;" src="../../../docs/images/firebase_google_signin.png" alt="Create New Project" width="30%"/>

  </p>

### Troubleshooting

Here are some common issues that may occur during the Firebase configuration process and their corresponding solutions.
```bash
  1. Invalid Credentials error: Inspect the browser console for any error messages or warnings related to Invalid Credentials.
```
- Verify that you have entered the correct client ID and API key in the Firebase Console.
- Double-check that there are no typos or errors in the values entered in environment variables.
- Make sure you have enabled the configuration settings properly when calling the firebase.auth().signInWithPopup() function.
- Ensure that you have specified correct firebase SDK version, and you are signing in with the valid Google account.

```bash
  2.  User account disabled error.
```
- The only way to fix this issue is to reactivate the existing account or create a new one.
- Additionally, you can check if account has been disabled or deleted before attempting to authenticate them with Firebase Google Sign-in, and display an error message if account is not active.


```bash
  3.  Cross-Origin Resource Sharing (CORS) error: If you notice that expected behavior of the web application is not occurring, such as data not being loaded or displayed correctly.
```
- Go to the Firebase Console, in the Authentication section select the "Sign-in method" tab. Under the "Authorized domains" section, make sure that your web application domain is added and that CORS is enabled for it.
- If you are using a server-side authentication flow, make sure that you have added the necessary CORS headers to your server response to allow requests from your web application domain.
- If you are hosting your web application on Firebase Hosting, it automatically enables CORS for your domain. You can also use Firebase Cloud Run to serve API requests with CORS headers included. 







