/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Mon Nov 29 2021
 */

import { Connection } from './connection.js'
import { Keyboard } from './keyboard.js'
import { BotMessageHandler } from './bot-message-handler'
import {Commands} from './commands'
import {RemoteKeyboard} from './remote_keyboard';
import { googleSigIn } from "./authentication/authentication";
import {WebRTC} from './webrtc.js'
const connection = new Connection();
(async () => {

  const keyboard = new Keyboard()
  const botMessageHandler = new BotMessageHandler(connection)


  const onData = data => {
    console.log("data is --->",data)
    let msg = JSON.parse(data)
    console.log("status is ===>",msg.status)
    botMessageHandler.handle(JSON.parse(data).status, connection)
  }

  const onQuit = () => {
    connection.stop()
  }

  await connection.start(onData)
  const webRtc = new WebRTC(connection)
const sendToBot = (key)=>{
  console.log(key)
  let msg = JSON.parse(key);
let commands = {}
  if(msg.driveCmd !== undefined){
    commands = {
      driveCmd : msg.driveCmd,
      roomId : signedInUser.email
    }
  }
  else{
    commands = {
      command : msg.command,
      roomId : signedInUser.email
    }
  }
  // connection.send(JSON.stringify(commands)) //This is for sending via socket)
      botMessageHandler.handle(commands,connection); // This is for sending via webRtc

}
  const onKeyPress = (key) => {
    const command = new Commands(sendToBot);
    const remoteKeyboard = new RemoteKeyboard(command.getCommandHandler());
    // Send keypress to server
    let keyPressObj = { KEYPRESS: key }
    console.log(keyPressObj);
    remoteKeyboard.processKey(keyPressObj.KEYPRESS)
  }



  keyboard.start(onKeyPress, onQuit)
})()

export let  signedInUser = null;
function handleSignInButtonClick() {

  googleSigIn()
      .then((user) => {
        // Use the user data or store it in a variable for later use
        signedInUser = user;
        console.log("Signed-in user:", user);
        console.log(signedInUser.email)
       sendId();

      })
      .catch((error) => {
        // Handle any errors that might occur during sign-in
        console.error("Error signing in:", error);
      });
}
function sendId() {
  const response = {
    roomId: signedInUser.email
  };
  console.log(connection);
  connection.send(JSON.stringify(response));
  console.log("id has been sent");
}

// Add an event listener to the button
const signInButton = document.getElementById("google-signin-button");
signInButton.addEventListener("click", handleSignInButtonClick);
