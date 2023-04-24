

## Firebase Google Sign-In Authentication
- #### Usage
    In web application, we use Firebase for Google sign-in authentication to upload OpenBot Playground projects on Google Drive. If you clone this project and run on your device, you must set up your own Firebase project because the firebase config is required for sign-in authentication.
- #### About  Google Sign-In
    Firebase Google Sign-In Authentication is a feature of the Firebase platform that allows users to sign in to mobile or web apps using their Google credentials. This service provides a secure and convenient way for users to access apps without having to remember and manage separate login credentials. Firebase manages the entire authentication process, from verifying the user's identity with Google to providing a unique user ID that can be used to personalize the user's experience within the app. This feature also includes additional security measures, such as two-factor authentication, to help protect user accounts from unauthorized access.
****

### Setting up Firebase Project

- Go to the Firebase Console (https://console.firebase.google.com/) and sign in with your Google account.

- Click on the `Add Project` button to create a new Firebase project.

- Enter a name for your project, select your country/region, and then click on the `Create Project` button.
    <p align="left">
    <img style="padding-right: 2%; padding-top: 2%; padding-bottom: 2%" src="../../../docs/images/firebase_create_project.png" alt="Create New Project" width="30%"/>
    <img style="padding-right: 2%;padding-top: 2%; padding-bottom: 2% " src="../../../docs/images/firebase_success_creation.png" alt="Create New Project" width="30%"/>
    </p>

- Once your project is created, click on the `Web` icon to add Firebase to your web app and then enter an App nickname and click on the `Register App` button.
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
        
       - Using Environment Variables When using Firebase Authentication, you may need to store sensitive information such as API keys, database credentials, and other secrets. To do this securely, you can use environment variables to store this information outside of your code. by doing following steps.

          1. Create a new file in OpenBot Playground called .env.
               <p align="left">
              <img style="padding-right: 2%;padding-top: 2%; padding-bottom: 2%;" src="../../../docs/images/firebase_env.png" alt="Create New Project" width="30%"/>
               </p> 

          3. Add following environment variables to the .env file that will be used in firebase.js file.

           ```bash
             REACT_APP_FIREBASE_API_KEY=YOUR_REACT_APP_FIREBASE_API_KEY
             REACT_APP_AUTH_DOMAIN=YOUR_REACT_APP_AUTH_DOMAIN
             REACT_APP_PROJECT_ID=YOUR_REACT_APP_PROJECT_ID
             REACT_APP_STORAGE_BUCKET=YOUR_REACT_APP_STORAGE_BUCKET
             REACT_APP_MESSAGING_SENDER_ID=Y0UR_REACT_APP_MESSAGING_SENDER_ID
             REACT_APP_APP_ID=YOUR_REACT_APP_APP_ID
             REACT_APP_MEASUREMENT_ID=YOUR_REACT_APP_MEASUREMENT_ID
             GENERATE_SOURCEMAP=false
           ```

- Enable Firebase Authentication SignIn method using Google.

  <p align="left">

  <img style="padding-right: 2%; padding-top: 2%; padding-bottom: 2%;" src="../../../docs/images/firebase_authantication.png" alt="Create New Project" width="30%"/>

  <img style="padding-right: 2%; padding-top: 2%; padding-bottom: 2%;" src="../../../docs/images/firebase_google_option.png" alt="Create New Project" width="30%"/>

  <img style="padding-right: 2%;padding-top: 2%; padding-bottom: 2%;" src="../../../docs/images/firebase_google_signin.png" alt="Create New Project" width="30%"/>

  </p>
