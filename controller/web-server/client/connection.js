/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Mon Nov 29 2021
 */

import { ErrorDisplay } from './error-display.js'
import {Commands} from "./commands";
import {RemoteKeyboard} from "./remote_keyboard";

export function Connection() {
  const connectToServer = async () => {
    // const ws = new WebSocket(`ws://${window.location.hostname}:8080/ws`);
    // const ws = new WebSocket(`ws://verdant-imported-peanut.glitch.me`);
    const ws = new WebSocket(`ws://verdant-imported-peanut.glitch.me`);
    return new Promise((resolve, reject) => {
      const timer = setInterval(() => {
        if (ws.readyState === 1) {
          clearInterval(timer);
          resolve(ws);
        }
      }, 10);
    });
  };

  const sendToBot = (message) => {
    this.send(message);
  };

  const sendId = () => {
    const response = {
      roomId: '123456789'
    };
    this.send(JSON.stringify(response));
  };

  this.start = async (onData) => {
    let ws = await connectToServer();
    this.send = (data) => {
      if (ws) {
        ws.send(data);
      }
    };
    sendId();
    const errDisplay = new ErrorDisplay();
    let idSent = false;

    ws.onmessage = (webSocketMessage) => {
      let msg = JSON.parse(webSocketMessage.data);
      if (Object.keys(msg)[0] === 'roomId' && !idSent) {
        console.log("sending id");
        sendId();
        idSent = true;
      } else {
        console.log(webSocketMessage.data);
        onData(webSocketMessage.data);
      }
    };

    ws.onclose = () => {
      console.log('Disconnected from the server. To reconnect, reload this page.');
      errDisplay.set('Disconnected from the server. To reconnect, reload this page.')
      idSent = false;
    };

    ws.onopen = () => {
      console.log('Connection established.');
      errDisplay.reset();
      idSent = false;
    };



    this.stop = () => {
      ws.close();
      ws = null;
    };
  };
}
