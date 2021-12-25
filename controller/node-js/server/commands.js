/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Mon Nov 29 2021
 */

class Commands {
  constructor (botConnection, browserConnection) {
    // this.botConnection = botConnection
    // this.browserConnection = browserConnection

    const commandHandler = new CommandHandler(botConnection)

    this.getCommandHandler = () => {
      return commandHandler
    }

    this.handleStatus = (_status /* Json */) => {
      // forward status and WebRTC negotition to browser
      browserConnection.send(JSON.stringify(_status))
    }
  }
}

class DriveValue {
  constructor () {
    const MAX = 1.0
    const MIN = -1.0

    let value = 0.0

    this.reset = () => {
      value = 0
      return value
    }

    this.max = () => {
      value = MAX
      return value
    }

    this.min = () => {
      value = MIN
      return value
    }

    this.write = _value => {
      value = _value
      return value
    }

    this.read = () => {
      return Math.round(value, 3)
    }
  }
}

class CommandHandler {
  constructor (botConnection) {
    const left = new DriveValue()
    const right = new DriveValue()
    const commandReducer = new DriveCommandReducer()

    const sendDriveCommand = (left, right) => {
      commandReducer.send({ driveCmd: { l: left, r: right } }, botConnection)
    }

    this.reset = () => {
      left.reset()
      right.reset()
      commandReducer.send({ driveCmd: { l: left.read(), r: right.read() } }, botConnection)
    }

    this.forwardLeft = () => {
      sendDriveCommand(left.write(0.5), right.max())
    }

    this.forwardRight = () => {
      sendDriveCommand(left.max(), right.write(0.5))
    }

    this.backwardLeft = () => {
      sendDriveCommand(right.write(-0.5), left.min())
    }

    this.backwardRight = () => {
      sendDriveCommand(right.min(), left.write(-0.55))
    }

    this.rotateLeft = () => {
      sendDriveCommand(left.min(), right.max())
    }

    this.rotateRight = () => {
      sendDriveCommand(left.max(), right.min())
    }

    this.goForward = () => {
      sendDriveCommand(left.max(), right.max())
    }

    this.goBackward = () => {
      sendDriveCommand(left.min(), right.min())
    }
  }
}

// Utility class to reduce number of commands being sent to the robot
// by not sending duplicate consecutive commands.
class DriveCommandReducer {
  constructor () {
    let lastCommand = null

    this.send = (commandAsJson, connection) => {
      if (isEqual(commandAsJson, lastCommand)) {
        return
      }
      lastCommand = commandAsJson

      const strCommand = JSON.stringify(commandAsJson) + '\n'
      connection.send(strCommand)
    }

    const isEqual = (current, last) => {
      if (!last || !current) {
        return false
      }
      return last.driveCmd.l === current.driveCmd.l && last.driveCmd.r === current.driveCmd.r
    }
  }
}

module.exports = { Commands, CommandHandler }
