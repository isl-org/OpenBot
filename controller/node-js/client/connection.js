/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Mon Nov 29 2021
 */

import { ErrorDisplay } from './error-display.js'

export function Connection () {
  const connectToServer = async () => {
    const ws = new WebSocket(`ws://${window.location.hostname}:7071/ws`)
    return new Promise((resolve, reject) => {
      const timer = setInterval(() => {
        if (ws.readyState === 1) {
          clearInterval(timer)
          resolve(ws)
        }
      }, 10)
    })
  }

  this.start = async (onData) => {
    let ws = await connectToServer()
    const errDisplay = new ErrorDisplay()

    ws.onmessage = webSocketMessage => onData(webSocketMessage.data)
    ws.onclose = () => errDisplay.set('Disconnected from the server. To reconnect, reload this page.')
    ws.onopen = () => errDisplay.reset()

    this.send = data => {
      if (ws) {
        ws.send(data)
      }
    }

    this.stop = () => {
      ws.close()
      ws = null
    }
  }
}
