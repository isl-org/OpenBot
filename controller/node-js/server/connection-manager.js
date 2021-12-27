/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Sun Nov 28 2021
 */

const BotConnection = require('./bot-connection.js')
const BrowserConnection = require('./browser-connection')

function ConnectionManager () {
  let onBotData = null

  const onWsClose = () => {
    botConnection.stop()
    botConnection.start(onBotData)
  }

  const botConnection = new BotConnection()
  const browserConnection = new BrowserConnection(onWsClose)

  this.start = (_onBotData, onBrowserData) => {
    onBotData = _onBotData
    botConnection.start(onBotData)
    browserConnection.start(onBrowserData)

    console.log('ConnectionManager: start() ...')
    this.toBot(JSON.stringify({ status: { VIDEO_COMMAND: 'START' } }))
  }

  this.stop = () => {
    if (botConnection && botConnection.stop) {
      botConnection.stop()
    }

    if (browserConnection && browserConnection.stop) {
      browserConnection.stop()
    }
  }

  this.toBot = msg => {
    botConnection.send(msg)
  }

  this.toBrowser = msg => {
    browserConnection.send(msg)
  }
}

module.exports = ConnectionManager
