/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Mon Nov 29 2021
 */

function RemoteKeyboard (commandHandler) {
  const pressedKeys = new Set()

  // This deconstruction does not work. The functions are not called. Why?
  // const {reset, goForward, goBackward, forwardLeft, forwardRight, backwardLeft, backwardRight} = commandHandler

  // Should this ne a state table?
  // const keyUpActions = [
  //   {whenPressedOneOf: ['w', 's'], action: commandHandler.reset() }
  //   ...
  // ]
  // const keyDownActions = [...]
  // ]

  this.processKey = keyPress => {
    switch (keyPress.type) {
      case 'keyup':
        // keep track of what keys are currently pressed
        pressedKeys.delete(keyPress.key)

        if (['w', 's'].includes(keyPress.key)) {
          commandHandler.reset()
          break
        }

        if (['a', 'd'].includes(keyPress.key)) {
          if (pressedKeys.has('w')) {
            commandHandler.goForward()
            break
          }
          if (pressedKeys.has('s')) {
            commandHandler.goBackward()
            break
          }
          commandHandler.reset()
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
            else if (pressedKeys.has('s')) {
              commandHandler.backwardLeft()
            }
            else {
              commandHandler.rotateLeft()
            }
            break
          case 'd':
            if (pressedKeys.has('w')) {
              commandHandler.forwardRight()
            }
            else if (pressedKeys.has('s')) {
              commandHandler.backwardRight()
            }
            else {
              commandHandler.rotateRight()
            }
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
          case 'q':
            commandHandler.sendCommand('SPEED_DOWN')
            break
          case 'e':
            commandHandler.sendCommand('SPEED_UP')
            break
          case 'Escape':
            commandHandler.reset()
            break
        }
    }
  }
}

module.exports = RemoteKeyboard
