/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Mon Nov 29 2021
 */

class RemoteKeyboard {
  constructor (commandHandler) {
    this.commandHandler = commandHandler

    this.pressedKeys = new Set()
  }

  start (onQuit) {
    this.onQuit = onQuit
  }

  getKeyboardCommands () {
    return [
      { key: 'W', description: 'Go forward' },
      { key: 'S', description: 'Go backward' },
      { key: 'A', description: 'Turn slightly left (while driving)' },
      { key: 'D', description: 'Turn slightly right (while driving)' },
      { key: 'Q', description: 'Rotate left' },
      { key: 'E', description: 'Rotate right' },
      { key: 'M', description: 'Drive mode' },
      { key: 'N', description: 'Toggle noise' },
      { key: 'Left', description: 'Left indicator' },
      { key: 'Right', description: 'Right indicator' },
      { key: 'Up', description: 'Cancel indicators' },
      { key: 'Down', description: 'Network mode' },
      { key: 'SPACE', description: 'Toggle logging' },
      { key: 'ESC', description: 'Quit' }
    ]
  }

  processKey (keyPress) {
    switch (keyPress.type) {
      case 'keyup':
        // keep track of what keys are currently pressed
        this.pressedKeys.delete(keyPress.key)

        if (['w', 's'].includes(keyPress.key)) {
          this.commandHandler.reset()
        }

        if (['a', 'd', 'q', 'e'].includes(keyPress.key)) {
          if (this.pressedKeys.has('w')) {
            this.commandHandler.goForward()
          }
          if (this.pressedKeys.has('s')) {
            this.commandHandler.goBackward()
          }
        }
        break

      case 'keydown':
        this.pressedKeys.add(keyPress.key)

        switch (keyPress.key) {
          case 'w':
            this.commandHandler.goForward()
            break
          case 's':
            this.commandHandler.goBackward()
            break
          case 'a':
            if (this.pressedKeys.has('w')) {
              this.commandHandler.forwardLeft()
            }
            if (this.pressedKeys.has('s')) {
              this.commandHandler.backwardLeft()
            }
            break
          case 'd':
            if (this.pressedKeys.has('w')) {
              this.commandHandler.forwardRight()
            }
            if (this.pressedKeys.has('s')) {
              this.commandHandler.backwardRight()
            }
            break
          case 'q':
            this.commandHandler.rotateLeft()
            break
          case 'e':
            this.commandHandler.rotateRight()
            break
          case 'n':
            this.commandHandler.sendCommand('NOISE')
            break
          case ' ':
            this.commandHandler.sendCommand('LOGS')
            break
          case 'ArrowRight':
            this.commandHandler.sendCommand('INDICATOR_RIGHT')
            break
          case 'ArrowLeft':
            this.commandHandler.sendCommand('INDICATOR_LEFT')
            break
          case 'ArrowUp':
            this.commandHandler.sendCommand('INDICATOR_STOP')
            break
          case 'ArrowDown':
            this.commandHandler.sendCommand('NETWORK')
            break
          case 'm':
            this.commandHandler.sendCommand('DRIVE_MODE')
            break
          case 'Escape':
            this.commandHandler.reset()
            this.onQuit()
            break
        }
    }
  }
}

module.exports = RemoteKeyboard
