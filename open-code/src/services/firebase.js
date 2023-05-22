import firebase from "firebase/compat/app";
import 'firebase/compat/auth';
import {getDownloadURL, getStorage, ref, uploadBytes} from 'firebase/storage';
import 'firebase/compat/firestore';
import {getFirestore} from "firebase/firestore";
import {getAuth, signOut} from "firebase/auth";
import {localStorageKeys} from "../utils/constants";


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
    const fileRef = ref(FirebaseStorage, "profile_pictures/"+auth.currentUser.uid + ".jpg")
    await uploadBytes(fileRef, file);
    return getDownloadURL(fileRef)
}

/**
 * function to log in user with Google credentials provided by the user.
 * @returns {Promise<firebase.auth.UserCredential>}
 */
export async function googleSigIn() {
    const signIn = await auth.signInWithPopup(provider)
    localStorage.setItem("isSigIn", "true");
    localStorage.setItem(localStorageKeys.accessToken, signIn.credential?.accessToken);
    return signIn
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
    }).catch((error) => {
        console.log("Sign-out error ", error)
    });
}


//
// /**
//  * get project from firebase when user signedIn
//  * @param driveProjects
//  * @returns {Promise<void>}
//  */
// async function getFirBaseProject(driveProjects) {
//     if (auth.currentUser?.uid) {
//         try {
//             // getting all docs from firebase
//             const projects = await getDocs(collection(db, auth.currentUser?.uid));
//             projects.forEach((doc) => {
//                 driveProjects?.push({storage: "drive", id: doc.id, ...doc.data()});
//             })
//         } catch (error) {
//             console.error(error);
//         }
//     }
// }

/**
 * update project on drive when change on blockly workspace
 * @returns {Promise<void>}
 */
// export async function updateProjectOnDrive() {
//     const date = new Date();
//     const dateOptions = {day: 'numeric', month: 'long', year: 'numeric'};
//     const currentDate = date.toLocaleDateString('en-US', dateOptions);
//     const timeOptions = {hour: 'numeric', minute: 'numeric', hour12: false};
//     const currentTime = date.toLocaleTimeString('en-US', timeOptions);
//     const workspaceRef = doc(collection(db, auth.currentUser.uid), getCurrentProject().id);
//     try {
//         await updateDoc(workspaceRef, {
//             updatedDate: currentDate,
//             xmlValue: getCurrentProject().xmlValue,
//             time: currentTime,
//         })
//     } catch (err) {
//         console.log(err);
//     }
// }

//
// /**
//  * project upload on drive when user signedIn.
//  * @param data
//  * @param uniqueId
//  * @returns {Promise<void>}
//  */
// export async function uploadOnDrive(data, uniqueId) {
//     if (localStorage.getItem("isSigIn") === "true") {
//         try {
//             const workspaceRef = doc(collection(db, auth.currentUser.uid), uniqueId);
//             await setDoc(workspaceRef, data);
//         } catch (err) {
//             console.log(err);
//         }
//     } else {
//         //alert for login first
//     }
// }
//
// /**
//  * delete project from local and drive also if you are signedIn.
//  * @param currentProjectId
//  * @returns {Promise<void>}
//  */
// export async function deleteProject(currentProjectId) {
//     try {
//         if (localStorage.getItem("isSigIn") === "true") {
//             //deleting file from firebase
//             await deleteDoc(doc(db, auth.currentUser.uid, currentProjectId))
//
//         } else {
//             JSON.parse(localStorage?.getItem(localStorageKeys.allProjects))?.find((project) => {
//                 if (project.id === currentProjectId) {
//                     const restObject = getAllLocalProjects().filter((res) => (res.id !== project.id));
//                     localStorage.setItem(localStorageKeys.allProjects, JSON.stringify(restObject))
//                 }
//             })
//         }
//     } catch (err) {
//         console.log(err);
//     }
// }


// selected project from firebase
// const blockSnap = doc(db, auth.currentUser.uid, projectData.id);
// const workspaceRef = await getDoc(blockSnap);
// if (workspaceRef.exists()) {
//     const projectId = workspaceRef.id;
//     setCurrentProjectId(projectId);
//     const projectXmlData = workspaceRef.data().xmlValue;
//     const projectName = workspaceRef.data().projectName;
//     setCurrentProjectXml(projectXmlData);
//     setProjectName(projectName);
//     openExistingProject();
// }