import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
import {  signInWithPopup, GoogleAuthProvider } from "firebase/auth";


// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
    apiKey: "AIzaSyADqhKqGoMq80SsrxjY1hViq6ZoeAvJRJ8",
    authDomain: "open-code-openbot.firebaseapp.com",
    projectId: "open-code-openbot",
    storageBucket: "open-code-openbot.appspot.com",
    messagingSenderId: "265415454186",
    appId: "1:265415454186:web:6c00d55245bfc3d7c49693",
    measurementId: "G-3CSPVB42QQ"
};


const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const provider = new GoogleAuthProvider();

export function googleSigIn() {
    return new Promise((resolve, reject) => {
        signInWithPopup(auth, provider)
            .then((result) => {
                // This gives you a Google Access Token. You can use it to access the Google API.
                const credential = GoogleAuthProvider.credentialFromResult(result);
                const token = credential.accessToken;
                // The signed-in user info.
                const user = result.user;
                resolve(user);
            })
            .catch((error) => {
                // Handle Errors here.
                const errorCode = error.code;
                const errorMessage = error.message;
                // The email of the user's account used.
                const email = error.customData.email;
                // The AuthCredential type that was used.
                const credential = GoogleAuthProvider.credentialFromError(error);
                reject(error);
            });
    });
}
