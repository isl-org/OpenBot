/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Mon Nov 29 2021
 */

const readlineModule = require('readline')

class LocalKeyboard {
  constructor (commandHandler) {
    this.commandHandler = commandHandler

    this.start = () => {
      this.showCommands()
      this.processKeys()
    }

    const getKeyboardCommands = () => {
      return [
        { key: 'W', description: 'Go forward' },
        { key: 'S', description: 'Go backward' },
        { key: 'A', description: 'Turn slightly left (while driving)' },
        { key: 'D', description: 'Turn slightly right (while driving)' },
        { key: 'Ctrl-A', description: 'Turn slightly left (while driving backward)' },
        { key: 'Ctrl-D', description: 'Turn slightly right (while driving backward)' },
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

    this.showCommands = () => {
      console.log('\nKeybord commnads (^C to exit):\n')
      getKeyboardCommands().forEach((command) => {
        console.log(`\t${command.key}\t\t${command.description}`)
      })
    }

    this.processKeys = () => {
      readlineModule.emitKeypressEvents(process.stdin)

      if (process.stdin.setRawMode !== undefined) {
        console.log('setting raw mode...')
        process.stdin.setRawMode(true)
      }

      process.stdin.on('keypress', (charater, key) => {
        if ((key && (key.name === 'c') && key.ctrl) ||
                    (key && key.name === 'escape')) {
          console.log('bye bye')
          process.exit()
        }

        console.log(`key ${JSON.stringify(key)}`)

        switch (key.name) {
          case 'n':
            this.commandHandler.sendCommand('NOISE')
            break

          case 'space':
            this.commandHandler.sendCommand('LOGS')
            break

          case 'right':
            this.commandHandler.sendCommand('INDICATOR_RIGHT')
            break

          case 'left':
            this.commandHandler.sendCommand('INDICATOR_LEFT')
            break

          case 'up':
            this.commandHandler.sendCommand('INDICATOR_STOP')
            break

          case 'down':
            this.commandHandler.sendCommand('NETWORK')
            break

          case 'm':
            this.commandHandler.sendCommand('DRIVE_MODE')
            break

          case 'w':
            this.commandHandler.goForward()
            break

          case 's':
            this.commandHandler.goBackward()
            break

          case 'a':
            (key.ctrl) ? this.commandHandler.backwardLeft() : this.commandHandler.forwardLeft()
            break

          case 'd':
            (key.ctrl) ? this.commandHandler.backwardRight() : this.commandHandler.forwardRight()
            break

          case 'q':
            this.commandHandler.rotateLeft()
            break

          case 'e':
            this.commandHandler.rotateRight()
            break
          default:
        }
      })
    }
  }
}

module.exports = LocalKeyboard
