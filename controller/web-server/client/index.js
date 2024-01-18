/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Mon Nov 29 2021
 */

import {Connection} from './connection.js'
import {Keyboard} from './keyboard.js'
import {BotMessageHandler} from './bot-message-handler'
import {Commands} from './commands'
import {RemoteKeyboard} from './remote_keyboard'
import {googleSigIn, googleSignOut, auth, uploadUserData, getUserPlan} from './authentication/authentication'
import {WebRTC} from './webrtc.js'
import {signInWithCustomToken} from 'firebase/auth'
import Cookies from 'js-cookie'

const connection = new Connection();
(async () => {
    const keyboard = new Keyboard()
    const botMessageHandler = new BotMessageHandler(connection)

    const onData = data => {
        const msg = JSON.parse(data)
        botMessageHandler.handle(JSON.parse(data).status, connection)
    }

    const onQuit = () => {
        connection.stop()
    }

    await connection.start(onData)
    const webRtc = new WebRTC(connection)
    const sendToBot = (key) => {
        const msg = JSON.parse(key)
        let commands = {}
        if (msg.driveCmd !== undefined) {
            commands = {
                driveCmd: msg.driveCmd,
                roomId: signedInUser.email
            }
        } else {
            commands = {
                command: msg.command,
                roomId: signedInUser.email
            }
        }
        // connection.send(JSON.stringify(commands)) //This is for sending via socket)
        botMessageHandler.handle(commands, connection) // This is for sending via webRtc
    }
    const onKeyPress = (key) => {
        const command = new Commands(sendToBot)
        const remoteKeyboard = new RemoteKeyboard(command.getCommandHandler())
        // Send keypress to server
        const keyPressObj = {KEYPRESS: key}
        console.log(keyPressObj)
        remoteKeyboard.processKey(keyPressObj.KEYPRESS)
    }

    keyboard.start(onKeyPress, onQuit)
})()

export let signedInUser = JSON.parse(localStorage.getItem('user'))

const signInButton = document.getElementsByClassName('google-sign-in-button')[0]
signInButton.addEventListener('click', handleSignInButtonClick)
const cancelButton = document.getElementById('logout-cancel-button')
const okButton = document.getElementById('logout-ok-button')
cancelButton.addEventListener('click', handleCancelButtonClick)
okButton.addEventListener('click', handleOkButtonClick)
const subscribeButton = document.getElementById('subscribe-button')
subscribeButton.addEventListener('click', handleSubscription)


/**
 * function to handle signIn on home page
 */
function handleSignInButtonClick() {
    if (localStorage.getItem('isSignIn') === 'false') {
        googleSigIn()
            .then((user) => {
                // Use the user data or store it in a variable for later use
                signedInUser = user
                const signInBtn = document.getElementsByClassName('google-sign-in-button')[0]
                signInBtn.innerText = user.displayName
                localStorage.setItem('user', JSON.stringify(user))
                localStorage.setItem('isSignIn', true.toString())
                sendId()
            })
            .catch((error) => {
                // Handle any errors that might occur during sign-in
                console.error('Error signing in:', error)
            })
    } else {
        showLogoutWrapper()
        hideExpirationWrapper()
    }
}

/**
 * function to sendId to remote server
 */
function sendId() {
    const response = {
        roomId: signedInUser.email
    }
    connection.send(JSON.stringify(response))
}

/**
 * function to handle signOut from google account
 */
function signOut() {
    signedInUser = null
    localStorage.setItem('user', null)
    localStorage.setItem('isSignIn', false.toString())
    const signInBtn = document.getElementsByClassName('google-sign-in-button')[0]
    signInBtn.innerText = 'Sign in with Google'
    googleSignOut()
    deleteCookie('serverDuration')
    deleteCookie('serverStartTime')
}

/**
 * function to handle cancel button on logout popup
 */
function handleCancelButtonClick() {
    hideLogoutWrapper()
}

/**
 * function to hide logout popup
 */
function hideLogoutWrapper() {
    const logout = document.getElementsByClassName('logout-wrapper')[0]
    logout.style.display = 'none'
}

/**
 * function to display logout popup
 */
function showLogoutWrapper() {
    const logout = document.getElementsByClassName('logout-wrapper')[0]
    logout.style.display = 'block'
}

/**
 * function to display expiration popup
 */
function showExpirationWrapper() {
    const expire = document.getElementsByClassName('plan-expiration-model')[0]
    expire.style.display = 'block'
}

/**
 * function to hide logout popup
 */
function hideExpirationWrapper() {
    const expire = document.getElementsByClassName('plan-expiration-model')[0]
    expire.style.display = 'none'
}

/**
 * function to handle "ok" button for logout popup
 */
function handleOkButtonClick() {
    hideLogoutWrapper()
    signOut()
}

/**
 * function to handle subscribe now button
 */
function handleSubscription() {
    console.log('Navigate to subscription page')
}

/**
 * function to get cookie from browser storage
 * @param cname
 * @returns {string}
 */
export function getCookie(cname) {
    const name = cname + '='
    const decodedCookie = decodeURIComponent(document.cookie)
    const ca = decodedCookie.split(';')
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i]
        while (c.charAt(0) === ' ') {
            c = c.substring(1)
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length)
        }
    }
    return ''
}

/**
 * function to delete cookie from browser storage
 * @param name
 */
export const deleteCookie = (name) => {
    document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;'
}

/**
 * function to handle single sign on from openbot dashboard
 */
function handleSingleSignOn() {
    const cookie = getCookie('user')
    if (cookie) {
        const result = cookie
        localStorage.setItem('isSignIn', 'true')
        if (getCookie('serverDuration')) {
            uploadUserData(JSON.parse(getCookie('serverDuration'))).then(() => {
                deleteCookie('serverDuration')
                deleteCookie('serverStartTime')
                deleteCookie('endTime')
            })
        }
        signInWithCustomToken(auth, result).then((res) => {
            // Use the user data or store it in a variable for later use
            signedInUser = res.user
            const signInBtn = document.getElementsByClassName('google-sign-in-button')[0]
            signInBtn.innerText = res.user.displayName
            localStorage.setItem('user', JSON.stringify(res.user))
            localStorage.setItem('isSignIn', true.toString())
            restrictUserOnExpiration()
            getUserPlan().then((res) => {
                if (res !== undefined) {
                    Cookies.set('endTime', res)
                }
                checkPlanExpiration()
            })
            deleteCookie('user')
        })
            .catch((error) => {
                console.log('error::', error)
            })
    }
}

/**
 * function to handle access token
 */
function handleAccessToken() {
    const tokenCookie = getCookie('accessToken')
    if (tokenCookie) {
        deleteCookie('accessToken')
    }
}

/**
 * function to handle auth status on refreshing page
 */
function handleAuthChangedOnRefresh() {
    if (localStorage.getItem('isSignIn') === 'true') {
        setTimeout(() => {
            auth.onAuthStateChanged((res) => {
                if (res != null) {
                    signedInUser = res
                    const signInBtn = document.getElementsByClassName('google-sign-in-button')[0]
                    signInBtn.innerText = res.displayName
                    localStorage.setItem('user', JSON.stringify(res))
                    localStorage.setItem('isSignIn', 'true')
                    restrictUserOnExpiration()
                    getUserPlan().then((res) => {
                        if (res !== undefined) {
                            Cookies.set('endTime', res)
                        }
                        checkPlanExpiration()
                    })
                    if (getCookie('serverDuration')) {
                        uploadUserData(JSON.parse(getCookie('serverDuration'))).then(() => {
                            deleteCookie('serverDuration')
                            deleteCookie('serverStartTime')
                        })
                    }
                }
            })
        }, 1000)
    }
}

handleAccessToken()
handleSingleSignOn()
handleAuthChangedOnRefresh()

// handling user usage for server duration
window.onbeforeunload = function () {
    if (getCookie('serverStartTime')) {
        const serverStartTime = getCookie('serverStartTime')
        const endTIme = new Date()
        const previousStartTime = new Date(decodeURIComponent(serverStartTime))
        const serverDuration = Math.floor((endTIme - previousStartTime) / 1000) // in seconds
        Cookies.set('serverDuration', serverDuration)
    }
}

/**
 * function to check whether user subscription expires or not
 */
export function checkPlanExpiration() {
    if (localStorage.getItem('isSignIn') === 'true') {
        if (getCookie('endTime')) {
            const endTimeCheckInterval = setInterval(() => {
                const currentTime = new Date()
                // Check if the end time has been reached
                if (currentTime >= new Date(decodeURIComponent(getCookie('endTime')))) {
                    clearInterval(endTimeCheckInterval)
                    showExpirationWrapper()
                }
            }, 100) // 1 minute in milliseconds
        }
    }
}

/**
 * function to restrict user for sending room key to remote server
 */
export function restrictUserOnExpiration () {
    if (getCookie('endTime')) {
        const currentTime = new Date()
        if (currentTime < new Date(decodeURIComponent(getCookie('endTime')))) {
            sendId()
        }
    }
}
