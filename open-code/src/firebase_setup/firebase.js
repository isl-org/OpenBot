
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import { getFirestore } from "@firebase/firestore"
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
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);
export const firestore = getFirestore(app)
