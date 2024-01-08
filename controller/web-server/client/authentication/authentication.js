import {initializeApp} from "firebase/app";
import {getAuth, signOut} from "firebase/auth";
import {signInWithPopup, GoogleAuthProvider} from "firebase/auth";


// Your web app's Firebase configuration
const firebaseConfig = {
    apiKey: "AIzaSyCITlkh63TnSnJQBlzqbJwwtBDr_w3e1Pg",
    authDomain: "opencode-openbot.firebaseapp.com",
    databaseURL: "https://opencode-openbot-default-rtdb.asia-southeast1.firebasedatabase.app",
    projectId: "opencode-openbot",
    storageBucket: "opencode-openbot.appspot.com",
    messagingSenderId: "955078484078",
    appId: "1:955078484078:web:64774c120f9d3a0f65867f",
    measurementId: "G-SZJL3F5QXF"
};


const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);
const provider = new GoogleAuthProvider();

/**
 * Function to google Sign in
 * @returns {Promise<unknown>}
 */
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

/**
 * function to log out user from Google account
 * @returns {Promise<void>}
 */
export function googleSignOut() {
    signOut(auth).then(() => {
        localStorage.setItem("isSignIn", "false")
    }).catch((error) => {
        console.log("Sign-out error ", error)
    });
}
