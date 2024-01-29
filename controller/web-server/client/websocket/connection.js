/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Mon Nov 29 2021
 */

import {ErrorDisplay} from '../utils/error-display.js'
import {deleteCookie, getCookie} from '../index'
import {localStorageKeys} from '../utils/constants'
import {uploadServerUsage} from '../firebase/APIs'

/**
 * function to connect websocket to remote server
 * @constructor
 */
export function Connection() {
    const connectToServer = async () => {
        const ws = new WebSocket(`ws://${window.location.hostname}:8080/ws`)
        // const ws = new WebSocket(`ws://verdant-imported-peanut.glitch.me`);
        // const ws = new WebSocket(`ws://cooked-onyx-tamarind.glitch.me`);
        return new Promise((resolve, reject) => {
            const timer = setInterval(() => {
                if (ws.readyState === 1) {
                    clearInterval(timer)
                    resolve(ws)
                }
            }, 10)
        })
    }

    const sendToBot = (message) => {
        this.send(message)
    }

    this.start = async (onData) => {
        let ws = await connectToServer()
        this.send = (data) => {
            if (ws) {
                console.log(('sending to server' + data))
                ws.send(data)
            }
        }
        const errDisplay = new ErrorDisplay()
        let idSent = false

        ws.onmessage = (webSocketMessage) => {
            const msg = JSON.parse(webSocketMessage.data)
            if (Object.keys(msg)[0] === 'roomId' && !idSent) {
                idSent = true
            } else {
                console.log(webSocketMessage.data)
                onData(webSocketMessage.data)
            }
        }

        ws.onclose = () => {
            errDisplay.set('Disconnected from the server. To reconnect, reload this page.')
            idSent = false
            if (localStorage.getItem(localStorageKeys.isSignIn) === 'true') {
                if (getCookie(localStorageKeys.serverStartTime)) {
                    const time = new Date()
                    uploadServerUsage(getCookie(localStorageKeys.serverStartTime), time).then(() => {
                        deleteCookie(localStorageKeys.serverStartTime)
                        deleteCookie(localStorageKeys.serverEndTime)
                    })
                }
            }
        }

        ws.onopen = () => {
            errDisplay.reset()
            idSent = false
        }

        this.stop = () => {
            if (ws != null) {
                ws.close()
                ws = null
            }
        }
    }
}
