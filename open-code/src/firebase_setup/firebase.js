import firebase from "firebase/compat/app";
import 'firebase/compat/auth';
import {useEffect, useState} from "react";

const firebaseConfig = {
    apiKey: "AIzaSyBPZm6JnvHwLinjR6Dm45X6wX5BXuUzYqM",
    authDomain: "openbot-opencode.firebaseapp.com",
    projectId: "openbot-opencode",
    storageBucket: "openbot-opencode.appspot.com",
    messagingSenderId: "938751030511",
    appId: "1:938751030511:web:1a9b7d8f0c87b167b2171a",
    measurementId: "G-Q677HCC9QX"
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);
export const auth = firebase.auth();
export const provider = new firebase.auth.GoogleAuthProvider();
provider.setCustomParameters({prompt: 'select_account'});
export const signInWithGoogle = () => auth.signInWithPopup(provider);
export default firebase;
