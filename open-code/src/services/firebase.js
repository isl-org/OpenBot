import firebase from "firebase/compat/app";
import 'firebase/compat/auth';
import {getDownloadURL, getStorage, ref, uploadBytes} from 'firebase/storage';
import 'firebase/compat/firestore';
import {getFirestore} from "firebase/firestore";
import {getAuth, signOut} from "firebase/auth";

const firebaseConfig = {
    apiKey: "AIzaSyCITlkh63TnSnJQBlzqbJwwtBDr_w3e1Pg",
    authDomain: "opencode-openbot.firebaseapp.com",
    projectId: "opencode-openbot",
    storageBucket: "opencode-openbot.appspot.com",
    messagingSenderId: "955078484078",
    appId: "1:955078484078:web:64774c120f9d3a0f65867f",
    measurementId: "G-SZJL3F5QXF"
};

// Initialize Firebase
const app = firebase.initializeApp(firebaseConfig);


export const auth = firebase.auth();

export const provider = new firebase.auth.GoogleAuthProvider();
provider.setCustomParameters({prompt: 'select_account'});
provider.addScope('https://www.googleapis.com/auth/user.birthday.read')
export const FirebaseStorage = getStorage()
export const db = getFirestore(app)
export default firebase;

export async function uploadProfilePic(file, fileName) {
    if (fileName === undefined) {
        return
    }
    const fileRef = ref(FirebaseStorage, auth.currentUser.uid + fileName)
    await uploadBytes(fileRef, file);
    return getDownloadURL(fileRef)
}

export async function googleSigIn() {
    const siginIn = await auth.signInWithPopup(provider)
    localStorage.setItem("isSigIn", "true")
    return siginIn
}

export async function googleSignOut() {
    const auth = getAuth();
    signOut(auth).then(() => {
        localStorage.setItem("isSigIn", "false")
    }).catch((error) => {
        console.log("Sign-out error ", error)
    });
}