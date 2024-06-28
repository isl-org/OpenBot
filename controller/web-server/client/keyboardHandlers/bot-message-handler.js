/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Mon Nov 29 2021
 */

import {WebRTC} from '../webRTC/webrtc.js'
import {ErrorDisplay} from '../utils/error-display.js'
import {Buttons} from './buttons.js'

export function BotMessageHandler (connection) {
    const webRtc = new WebRTC(connection)
    const buttons = new Buttons(connection)
    const errDisplay = new ErrorDisplay()

    webRtc.onDataMessageReceived((message) => {
        // Do something on data received;
    })

    this.handle = (msg, connection) => {
        if (msg === undefined || msg === null) {
            return
        }

        const msgType = Object.keys(msg)[0]
        switch (msgType) {
            case 'VIDEO_PROTOCOL':
                if (msg.VIDEO_PROTOCOL !== 'WEBRTC') {
                    errDisplay.set('Only WebRTC video supported. Please set your andoid app for WebRTC')
                } else {
                    errDisplay.reset()
                }
                break

            case 'VIDEO_COMMAND':
                switch (msg.VIDEO_COMMAND) {
                    case 'START':
                        webRtc.start()
                        buttons.setMirrored(false)
                        break

                    case 'STOP':
                        webRtc.stop()
                        break
                }
                break

            case 'WEB_RTC_EVENT':
                webRtc.handle(msg.WEB_RTC_EVENT, connection)
                break
            case 'driveCmd' :
                connection.send(JSON.stringify(msg))
                // webRtc.send(JSON.stringify(msg))
                break

            case 'command' :
                connection.send(JSON.stringify(msg))
                // webRtc.send(JSON.stringify(msg))
                break

            default:
                // Process other status information here
                // This can be used to enhance the UI, for example
                // to display a blinking signal indicator, etc.
                break
        }
    }
}
