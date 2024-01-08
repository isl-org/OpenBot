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
import {RemoteKeyboard} from './remote_keyboard';
import {googleSigIn, googleSignOut} from "./authentication/authentication";
import {WebRTC} from './webrtc.js'
import {auth} from "./authentication/authentication";
import {signInWithCustomToken} from "firebase/auth";

const connection = new Connection();
(async () => {

    const keyboard = new Keyboard()
    const botMessageHandler = new BotMessageHandler(connection)


    const onData = data => {
        let msg = JSON.parse(data)
        botMessageHandler.handle(JSON.parse(data).status, connection)
    }

    const onQuit = () => {
        connection.stop()
    }

    await connection.start(onData)
    const webRtc = new WebRTC(connection)
    const sendToBot = (key) => {
        let msg = JSON.parse(key);
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
        botMessageHandler.handle(commands, connection); // This is for sending via webRtc

    }
    const onKeyPress = (key) => {
        const command = new Commands(sendToBot);
        const remoteKeyboard = new RemoteKeyboard(command.getCommandHandler());
        // Send keypress to server
        let keyPressObj = {KEYPRESS: key}
        console.log(keyPressObj);
        remoteKeyboard.processKey(keyPressObj.KEYPRESS)
    }


    keyboard.start(onKeyPress, onQuit)
})()

export let signedInUser = JSON.parse(localStorage.getItem("user"));

const signInButton = document.getElementsByClassName("google-sign-in-button")[0];
signInButton.addEventListener("click", handleSignInButtonClick);
const cancelButton = document.getElementById("logout-cancel-button");
const okButton = document.getElementById("logout-ok-button");
cancelButton.addEventListener("click", handleCancelButtonClick);
okButton.addEventListener("click", handleOkButtonClick);

/**
 * function to handle signIn on home page
 */
function handleSignInButtonClick() {
    if (localStorage.getItem("isSignIn") === "false") {
        googleSigIn()
            .then((user) => {
                // Use the user data or store it in a variable for later use
                signedInUser = user;
                console.log("Signed-in user:", user);
                let signInBtn = document.getElementsByClassName("google-sign-in-button")[0]
                signInBtn.innerText = user.displayName
                localStorage.setItem("user", JSON.stringify(user));
                localStorage.setItem("isSignIn", true.toString());
                sendId();
            })
            .catch((error) => {
                // Handle any errors that might occur during sign-in
                console.error("Error signing in:", error);
            });
    } else {
        showLogoutWrapper();
    }
}

/**
 * function to sendId to remote server
 */
function sendId() {
    const response = {
        roomId: signedInUser.email
    };
    connection.send(JSON.stringify(response));
}

/**
 * function to handle signOut from google account
 */
function signOut() {
    console.log("sign out");
    signedInUser.signOut;
    signedInUser = null;
    localStorage.setItem("user", null);
    localStorage.setItem("isSignIn", false.toString());
    let signInBtn = document.getElementsByClassName("google-sign-in-button")[0]
    signInBtn.innerText = "Sign in with Google"
    googleSignOut();
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
    const logout = document.getElementsByClassName("logout-wrapper")[0];
    logout.style.display = 'none';
}

/**
 * function to display logout popup
 */
function showLogoutWrapper() {
    const logout = document.getElementsByClassName("logout-wrapper")[0];
    logout.style.display = 'block';
}

/**
 * function to handle "ok" button for logout popup
 */
function handleOkButtonClick() {
    hideLogoutWrapper();
    signOut();
}

/**
 * function to get cookie from browser storage
 * @param cname
 * @returns {string}
 */
function getCookie(cname) {
    let name = cname + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

/**
 * function to delete cookie from browser storage
 * @param name
 */
const delete_cookie = function (name) {
    document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
};

/**
 * function to handle single sign on from openbot dashboard
 */
function handleSingleSignOn() {
    let cookie = getCookie("user");
    if (cookie) {
        let result = cookie
        console.log("result:::", result);
        localStorage.setItem("isSignIn", "true");
        signInWithCustomToken(auth, result).then((res) => {
            // Use the user data or store it in a variable for later use
            signedInUser = res.user;
            console.log("Signed-in user:", res.user);
            let signInBtn = document.getElementsByClassName("google-sign-in-button")[0]
            signInBtn.innerText = res.user.displayName
            localStorage.setItem("user", JSON.stringify(res.user));
            localStorage.setItem("isSignIn", true.toString());
            sendId();// delete_cookie("user");
            delete_cookie("user");
        })
            .catch((error) => {
                console.log("error::", error);
            })
    }
}

/**
 * function to handle access token
 */
function handleAccessToken() {
    let tokenCookie = getCookie("accessToken");
    if (tokenCookie) {
        delete_cookie("accessToken");
    }
}

/**
 * function to handle auth status on refreshing page
 */
function handleAuthChangedOnRefresh() {
    if (localStorage.getItem("isSignIn") === "true") {
        auth.onAuthStateChanged((res) => {
            if (res != null) {
                signedInUser = res;
                console.log("Signed-in user:", res);
                let signInBtn = document.getElementsByClassName("google-sign-in-button")[0]
                signInBtn.innerText = res.displayName
                localStorage.setItem("user", JSON.stringify(res));
                localStorage.setItem("isSignIn", "true");
                sendId();//
            }
        })
    }
}

handleAccessToken();
handleSingleSignOn()
handleAuthChangedOnRefresh()
