/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Mon Nov 29 2021
 */

import { Connection } from './connection.js'
import { Keyboard } from './keyboard.js'
import { BotMessageHandler } from './bot-message-handler'
import { Buttons } from './buttons'

(async () => {
  const connection = new Connection()
  const keyboard = new Keyboard()
  const botMessageHandler = new BotMessageHandler(connection)

  const onData = data => {
    // console.log(`Got data from server [${data}]`)
    botMessageHandler.handle(JSON.parse(data).status, connection)
  }

  await connection.start(onData)

  const onKeyPress = (key) => {
    // Send keypress to server
    connection.send(JSON.stringify({ KEYPRESS: key }))
  }

  // eslint-disable-next-line no-new
  new Buttons(connection)

  keyboard.start(onKeyPress)
})()
