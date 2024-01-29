import {initializeApp} from 'firebase/app'
import {getAuth, signOut, signInWithPopup, GoogleAuthProvider} from 'firebase/auth'
import {getFirestore} from '@firebase/firestore'
import {getStorage} from 'firebase/storage'
import Cookies from 'js-cookie'
import {checkPlanExpiration, getCookie} from '../index'
import {getUserPlan} from './APIs'
import {localStorageKeys} from '../utils/constants'

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
    const serverValidity = 999999 // 60 minutes free connection time
    return new Promise((resolve, reject) => {
        signInWithPopup(auth, provider)
            .then((result) => {
                // The signed-in user info.
                const user = result.user
                getUserPlan().then((res) => {
                    const subscriptionEndTime = res ?? getCookie(localStorageKeys.subscriptionEndTime) !== '' ? getCookie(localStorageKeys.subscriptionEndTime) : new Date(new Date().getTime() + serverValidity * 60 * 1000)
                    Cookies.set(localStorageKeys.subscriptionEndTime, subscriptionEndTime)
                    checkPlanExpiration()
                })
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
        localStorage.setItem(localStorageKeys.isSignIn, 'false')
    }).catch((error) => {
        console.log('Sign-out error ', error)
    })
}
