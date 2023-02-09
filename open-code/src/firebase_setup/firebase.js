import firebase from "firebase/compat/app";
import 'firebase/compat/auth';
import 'firebase/storage';
const firebaseConfig = {
    apiKey: "AIzaSyADqhKqGoMq80SsrxjY1hViq6ZoeAvJRJ8",
    authDomain: "open-code-openbot.firebaseapp.com",
    projectId: "open-code-openbot",
    storageBucket: "open-code-openbot.appspot.com",
    messagingSenderId: "265415454186",
    appId: "1:265415454186:web:6c00d55245bfc3d7c49693",
    measurementId: "G-3CSPVB42QQ"
};
export let userInformation;

// Initialize Firebase
firebase.initializeApp(firebaseConfig);
export const auth = firebase.auth();
export const provider = new firebase.auth.GoogleAuthProvider();
provider.setCustomParameters({prompt: 'select_account'});
provider.addScope('https://www.googleapis.com/auth/user.birthday.read')
export const signInWithGoogle = () => auth.signInWithPopup(provider);
export const storageRef = firebase.storage
export default firebase;
