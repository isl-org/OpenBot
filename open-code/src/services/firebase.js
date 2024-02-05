import firebase from "firebase/compat/app";
import 'firebase/compat/auth';
import {getDownloadURL, getStorage, ref, uploadBytes} from 'firebase/storage';
import 'firebase/compat/firestore';
import {
    getDoc,
    getFirestore,
    collection,
    doc,
    setDoc,
} from "firebase/firestore";
import {getAuth, signOut} from "firebase/auth";
import {localStorageKeys, tables} from "../utils/constants";
import {setConfigData} from "./workspace";
import configData from "../config.json";

const firebaseConfig = {
    apiKey: process.env.REACT_APP_FIREBASE_API_KEY,
    authDomain: process.env.REACT_APP_AUTH_DOMAIN,
    projectId: process.env.REACT_APP_PROJECT_ID,
    storageBucket: process.env.REACT_APP_STORAGE_BUCKET,
    messagingSenderId: process.env.REACT_APP_MESSAGING_SENDER_ID,
    appId: process.env.REACT_APP_APP_ID,
    measurementId: process.env.REACT_APP_MEASUREMENT_ID,
};

// Initialize Firebase
const app = firebase.initializeApp(firebaseConfig);


export const auth = firebase.auth();

export const provider = new firebase.auth.GoogleAuthProvider();
provider.setCustomParameters({prompt: 'select_account'});
provider.addScope("https://www.googleapis.com/auth/user.birthday.read")
provider.addScope("https://www.googleapis.com/auth/drive.file")
export const FirebaseStorage = getStorage()
export const db = getFirestore(app)
export default firebase;

/**
 * function to upload the profile picture of the user on firebase.
 * @param file file path of the profile picture
 * @param fileName name of the picture
 * @returns {Promise<string>} returns the downloadable url of the picture.
 */
export async function uploadProfilePic(file, fileName) {
    if (fileName === undefined) {
        return
    }
    const fileRef = ref(FirebaseStorage, "profile_pictures/" + auth.currentUser.uid + ".jpg")
    await uploadBytes(fileRef, file);
    return getDownloadURL(fileRef)
}

/**
 * function to log in user with Google credentials provided by the user.
 * @returns {Promise<firebase.auth.UserCredential>}
 */
export async function googleSigIn() {
    let isAndroid = /Android/i.test(navigator.userAgent);
    if (isAndroid) {
        await auth.signInWithRedirect(provider)
    } else {
        const signIn = await auth.signInWithPopup(provider);
        localStorage.setItem("isSigIn", "true");
        localStorage.setItem(localStorageKeys.accessToken, signIn.credential?.accessToken);
        await setConfigData();
        return signIn
    }
}

/**
 * function to log out user from Goole account
 * @returns {Promise<void>}
 */
export async function googleSignOut() {
    const auth = getAuth();
    signOut(auth).then(() => {
        window.location.reload()
        localStorage.setItem("isSigIn", "false")
        localStorage.setItem(localStorageKeys.accessToken, " ");
        localStorage.setItem(localStorageKeys.configData, JSON.stringify(configData));
        // delete_cookie("user");
    }).catch((error) => {
        console.log("Sign-out error ", error)
    });
}

/**
 * function to get saved date of birth from firebase
 * @returns {Promise<string>}
 */
export async function getDateOfBirth() {
    const docRef = doc(db, "users", auth.currentUser?.uid);
    const docSnap = await getDoc(docRef);
    if (docSnap.exists()) {
        if(docSnap.data()?.dob) {
            const date = new Date(docSnap.data().dob.toDate());
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-based, so we add 1
            const day = String(date.getDate()).padStart(2, '0');
            return `${year}-${month}-${day}`;
        }
    }
    return undefined
}

/**
 * function to store date of birth in firebase
 * @param DOB
 * @returns {Promise<void>}
 */
export async function setDateOfBirth(DOB) {
    try {
        const workspaceRef = doc(collection(db, tables.users), auth.currentUser?.uid);
        setDoc(workspaceRef, DOB, {merge: true}).catch((e) => console.log(e));
    } catch (e) {
        console.log("error in setting DOB:", e);
    }
}