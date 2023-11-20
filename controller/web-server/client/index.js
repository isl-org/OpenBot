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
import {googleSigIn} from "./authentication/authentication";
import {WebRTC} from './webrtc.js'

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

function sendId() {
    const response = {
        roomId: signedInUser.email
    };
    connection.send(JSON.stringify(response));
}

function signOut() {
    console.log("sign out");
    signedInUser.signOut;
    signedInUser = null;
    localStorage.setItem("user", null);
    localStorage.setItem("isSignIn", false.toString());
    let signInBtn = document.getElementsByClassName("google-sign-in-button")[0]
    signInBtn.innerText = "Sign in with Google"
}

function handleCancelButtonClick() {
hideLogoutWrapper()
}

function hideLogoutWrapper(){
    const logout = document.getElementsByClassName("logout-wrapper")[0];
    logout.style.display = 'none';
}

function showLogoutWrapper(){
    const logout = document.getElementsByClassName("logout-wrapper")[0];
    logout.style.display = 'block';
}

function handleOkButtonClick() {
    hideLogoutWrapper();
    signOut();
}

window.onbeforeunload = function (event) {
    localStorage.setItem("user", null);
    localStorage.setItem("isSignIn", false.toString());
};
