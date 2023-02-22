import firebase from "firebase/compat/app";
import 'firebase/compat/auth';
import {getDownloadURL, getStorage, ref, uploadBytes} from 'firebase/storage';
import 'firebase/compat/firestore';
import { getFirestore,  collection, addDoc, getDocs} from "firebase/firestore";
import { getAuth, signOut } from "firebase/auth";

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
    const fileRef = ref(FirebaseStorage, auth.currentUser.uid + fileName)
    const snapShot = await uploadBytes(fileRef, file)
    const photoURL = getDownloadURL(fileRef)
    return photoURL
}

export async function googleSigIn() {
    const siginIn = await auth.signInWithPopup(provider)
        localStorage.setItem("isSigIn", "true")
    return siginIn
}

export async function googleSignOut(){
    const auth = getAuth();
    signOut(auth).then(() => {
        localStorage.setItem("isSigIn", "false")
    }).catch((error) => {
        console.log("Sign-out error ", error)
    });
}

const myData  = {
    firstName: "sanjeev",
    lastName: "yadav",
    dob: 1998
}
// const dbRef = collection(db, "users");

// if(auth){
//     try {
//         const docRef = addDoc(collection(db, auth.currentUser.uid), myData).then((res) => {
//             console.log("Document written with ID: ", res);
//         });
//         const querySnapshot = getDocs(collection(db, "users")).then((responseDoc ) => {
//             console.log(responseDoc)
//             responseDoc.forEach((doc) => {
//                 console.log(`${doc.id} => ${doc.data()}`);
//                 console.log(doc.data())
//             });
//         });
//     } catch (e){
//         console.error("Error adding document: ", e);
//     }
// }
