/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Mon Nov 29 2021
 */

import { ErrorDisplay } from './error-display.js'
import io from 'socket.io-client';
export function Connection() {
  const connectToServer = () => {
    const socket = io.connect('https://four-sleet-yarn.glitch.me/'); // Use the Socket.IO library to connect

    return new Promise((resolve, reject) => {
      socket.on('connect', () => {
        resolve(socket);
      });
    });
  };

  this.start = async (onData) => {
    let socket = await connectToServer();
    const errDisplay = new ErrorDisplay();

    socket.on('msg', (message) => onData(message));// No need to access .data as Socket.IO sends the message directly
    socket.on('signalingMessage', (message) => onData(message));// No need to access .data as Socket.IO sends the message directly

    socket.on('msg',(data)=>{
      onData(data);
      console.log("data received")
    })
    socket.on('signalingMessage',(signalingMessage)=>{
      onData(signalingMessage);
    })
    socket.on('disconnect', () => {
      errDisplay.set('Disconnected from the server. To reconnect, reload this page.');
    });

    socket.on('connect', () => {
      errDisplay.reset();
    });

    this.send = (data) => {
      if (socket) {
        socket.emit('chatMessage', data); // Emit 'chatMessage' event to the server
      }
    };

    this.stop = () => {
      socket.close();
      socket = null;
    };
  };
}
