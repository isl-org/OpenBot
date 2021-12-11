/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Mon Nov 29 2021
 */

import { WebRTC } from './webrtc.js'
import { ErrorDisplay } from './error-display.js'
import { Buttons } from './buttons.js'

export class BotMessageHandler {
  constructor (connection) {
    const webRtc = new WebRTC(connection)

    this.handle = (msg) => {
      const msgType = Object.keys(msg)[0]
      switch (msgType) {
        case 'VIDEO_PROTOCOL':
          if (msg.VIDEO_PROTOCOL !== 'WEBRTC') {
            ErrorDisplay.set('Only WebRTC video supported. Please set your andoid app for WebRTC')
          } else {
            ErrorDisplay.reset()
          }
          break

        case 'VIDEO_COMMAND':
          switch (msg.VIDEO_COMMAND) {
            case 'START':
              webRtc.start()
              break

            case 'STOP':
              webRtc.stop()
              break
          }
          break

        case 'TOGGLE_MIRROR':
          Buttons.toggleMirror(msg.TOGGLE_MIRROR === 'true')
          break

        case 'WEB_RTC_EVENT':
          webRtc.handle(msg.WEB_RTC_EVENT, connection)
          break

        default:
          // process other status information here
          // This can be used ti enhance the UI, for example
          // do display a blinking sigan indicator, etc
          break
      }
    }
  }
}
