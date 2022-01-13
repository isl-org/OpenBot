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

  // Create connections to Bot and Browser
  const botConnection = new BotConnection()
  const browserConnection = new BrowserConnection(onWsClose)

  this.start = (_onBotData, onBrowserData) => {
    onBotData = _onBotData

    botConnection.start(onBotData)
    browserConnection.start(onBrowserData)
  }

  this.stop = () => {
    console.log('\nClosing ConnectionManager')

    if (botConnection.stop) {
      botConnection.stop()
    }

    if (browserConnection.stop) {
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
