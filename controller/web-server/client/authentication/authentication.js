import {initializeApp} from 'firebase/app'
import {getAuth, signOut, signInWithPopup, GoogleAuthProvider} from 'firebase/auth'
import {doc, getDoc, getFirestore, setDoc, updateDoc} from '@firebase/firestore'
import {getStorage} from 'firebase/storage'

// Your web app's Firebase configuration
const firebaseConfig = {
    apiKey: 'AIzaSyCITlkh63TnSnJQBlzqbJwwtBDr_w3e1Pg',
    authDomain: 'opencode-openbot.firebaseapp.com',
    databaseURL: 'https://opencode-openbot-default-rtdb.asia-southeast1.firebasedatabase.app',
    projectId: 'opencode-openbot',
    storageBucket: 'opencode-openbot.appspot.com',
    messagingSenderId: '955078484078',
    appId: '1:955078484078:web:64774c120f9d3a0f65867f',
    measurementId: 'G-SZJL3F5QXF'
}

const app = initializeApp(firebaseConfig)
export const auth = getAuth(app)
const provider = new GoogleAuthProvider()
export const FirebaseStorage = getStorage()
export const db = getFirestore(app)

/**
 * Function to google Sign in
 * @returns {Promise<unknown>}
 */
export function googleSigIn () {
    return new Promise((resolve, reject) => {
        signInWithPopup(auth, provider)
            .then((result) => {
                // The signed-in user info.
                const user = result.user
                resolve(user)
            })
            .catch((error) => {
                // Handle Errors here.
                reject(error)
            })
    })
}

/**
 * function to log out user from Google account
 * @returns {Promise<void>}
 */
export function googleSignOut () {
    signOut(auth).then(() => {
        localStorage.setItem('isSignIn', 'false')
    }).catch((error) => {
        console.log('Sign-out error ', error)
    })
}

export const uploadUserData = async (time) => {
    try {

        const Month = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']
        const docRef = doc(db, 'users', auth.currentUser?.uid)
        const docSnapshot = await getDoc(docRef)
        const dataArray = []
        const date = new Date()
        const monthName = Month[date.getMonth()] + ' ' + date.getFullYear()
        const userObject = {
            [monthName]: {
                projects: 0,
                models: 0,
                remoteServer: time
            }
        }
        dataArray.push(userObject)
        if (docSnapshot.exists()) {
            if (docSnapshot.data().userData !== undefined) {
                const docSnapData = docSnapshot.data().userData || [] // user data
                // eslint-disable-next-line no-prototype-builtins
                const index = docSnapData.findIndex(entry => entry.hasOwnProperty(monthName))
                if (index !== -1) {
                    docSnapData[index][monthName].remoteServer += time
                } else {
                    docSnapData.push(userObject)
                }
                await updateDoc(docRef, {
                    userData: docSnapData
                }).catch((e) => {
                    console.log('error in updating:', e)
                })
            } else {
                await setDoc(docRef, {
                    userData: dataArray
                }, {merge: true}).catch((e) => {
                    console.log('error in setting userData:', e)
                })
            }
        }
    } catch
        (e) {
        console.log('error in setting projects:', e)
    }
}
