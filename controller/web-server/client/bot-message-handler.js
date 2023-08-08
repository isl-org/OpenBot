/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Mon Nov 29 2021
 */

import {WebRTC} from './webrtc.js'
import {ErrorDisplay} from './error-display.js'
import {Buttons} from './buttons.js'

export function BotMessageHandler(connection) {
    const webRtc = new WebRTC(connection)
    const buttons = new Buttons(connection)
    const errDisplay = new ErrorDisplay()

    webRtc.onDataMessageReceived((message) => {
        console.log('Received message: ', message + "\t" , Date.now(),webRtc.dataChannel);
        webRtc.dataChannel.send("gfwdcvwqhdbvwhgdcwgh");
        webRtc.send("jai shree ram");

    });

    this.handle = (msg, connection) => {
        console.log("msgType =======", msg,)
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

            default:
                // Process other status information here
                // This can be used to enhance the UI, for example
                // to display a blinking signal indicator, etc.
                break
        }
    }
}
