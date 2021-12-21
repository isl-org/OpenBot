/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Mon Nov 29 2021
 */

class RemoteKeyboard {
  constructor (commandHandler, onQuit) {
    const pressedKeys = new Set()

    this.processKey = keyPress => {
      switch (keyPress.type) {
        case 'keyup':
          // keep track of what keys are currently pressed
          pressedKeys.delete(keyPress.key)

          if (['w', 's'].includes(keyPress.key)) {
            commandHandler.reset()
          }

          if (['a', 'd', 'q', 'e'].includes(keyPress.key)) {
            if (pressedKeys.has('w')) {
              commandHandler.goForward()
            } else {
              commandHandler.reset()
            }
            if (pressedKeys.has('s')) {
              commandHandler.goBackward()
            } else {
              commandHandler.reset()
            }
          }
          break

        case 'keydown':
          pressedKeys.add(keyPress.key)

          switch (keyPress.key) {
            case 'w':
              if (pressedKeys.has('a')) {
                commandHandler.forwardLeft()
                break
              }
              if (pressedKeys.has('d')) {
                commandHandler.forwardRight()
                break
              }
              commandHandler.goForward()
              break
            case 's':
              if (pressedKeys.has('a')) {
                commandHandler.backwardLeft()
                break
              }
              if (pressedKeys.has('d')) {
                commandHandler.backwardRight()
                break
              }
              commandHandler.goBackward()
              break
            case 'a':
              if (pressedKeys.has('w')) {
                commandHandler.forwardLeft()
              }
              if (pressedKeys.has('s')) {
                commandHandler.backwardLeft()
              }
              break
            case 'd':
              if (pressedKeys.has('w')) {
                commandHandler.forwardRight()
              }
              if (pressedKeys.has('s')) {
                commandHandler.backwardRight()
              }
              break
            case 'q':
              commandHandler.rotateLeft()
              break
            case 'e':
              commandHandler.rotateRight()
              break
            case 'n':
              commandHandler.sendCommand('NOISE')
              break
            case ' ':
              commandHandler.sendCommand('LOGS')
              break
            case 'ArrowRight':
              commandHandler.sendCommand('INDICATOR_RIGHT')
              break
            case 'ArrowLeft':
              commandHandler.sendCommand('INDICATOR_LEFT')
              break
            case 'ArrowUp':
              commandHandler.sendCommand('INDICATOR_STOP')
              break
            case 'ArrowDown':
              commandHandler.sendCommand('NETWORK')
              break
            case 'm':
              commandHandler.sendCommand('DRIVE_MODE')
              break
            case 'Escape':
              commandHandler.reset()
              onQuit()
              break
          }
      }
    }
  }
}

module.exports = RemoteKeyboard
