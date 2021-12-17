/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Mon Nov 29 2021
 */

import { ErrorDisplay } from './error-display.js'

export class Connection {
  async connectToServer () {
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

  async start (onData) {
    const ws = await this.connectToServer()

    ws.onmessage = webSocketMessage => onData(webSocketMessage.data)
    ws.onclose = () => ErrorDisplay.set('Disconnected from the server. Please restart your nodejs')
    ws.onopen = () => ErrorDisplay.reset()

    this.send = data => ws.send(data)
  }
}
