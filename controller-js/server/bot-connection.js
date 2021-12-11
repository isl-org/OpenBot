/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Sun Nov 28 2021
 */

const net = require('net')

class BotConnection {
  constructor () {
    this.port = 19400
    this.server = net.createServer((socket) => {
      this.socket = socket
    })

    this.send = (message) => {
      if (this.socket !== undefined && this.socket !== null) {
        this.socket.write(message + '\n')
      } else {
        console.log('Not connected, please connect to phone first...')
      }
    }

    this.stop = () => {
      console.log('Closing BotConnection ...')
      this.server.close()
    }

    this.start = (onMessageReceived) => {
      const handleConnection = (conn) => {
        const received = new MessageBuffer('\n')
        const onConnData = (data) => {
          received.push(data)
          while (!received.isFinished()) {
            const message = received.handleData()
            onMessageReceived(JSON.parse(message))
          }
        }

        const onConnClose = () => {
          console.log('connection from %s closed', remoteAddress)
        }

        const onConnError = (err) => {
          console.log('Connection %s error: %s', remoteAddress, err.message)
        }

        const remoteAddress = conn.remoteAddress + ':' + conn.remotePort
        console.log('Connected to Bot! 😃')
        conn.on('data', onConnData)
        conn.once('close', onConnClose)
        conn.on('error', onConnError)
      }

      this.server.on('connection', handleConnection)

      this.server.listen(this.port, () => {
        console.log(`listen on port ${this.port}`)
      })
    }
  }
}

/*
    Utility calss to accumulate data until a full JSON message has been received.
*/
class MessageBuffer {
  constructor (delimiter) {
    this.delimiter = delimiter
    this.buffer = ''
  }

  isFinished () {
    if (
      this.buffer.length === 0 ||
      this.buffer.indexOf(this.delimiter) === -1
    ) {
      return true
    }
    return false
  }

  push (data) {
    this.buffer += data
  }

  getMessage () {
    const delimiterIndex = this.buffer.indexOf(this.delimiter)
    if (delimiterIndex !== -1) {
      const message = this.buffer.slice(0, delimiterIndex)
      this.buffer = this.buffer.replace(message + this.delimiter, '')
      return message
    }
    return null
  }

  handleData () {
    /**
         * Try to accumulate the buffer with messages
         *
         * If the server isnt sending delimiters for some reason
         * then nothing will ever come back for these requests
         */
    const message = this.getMessage()
    return message
  }
}

module.exports = BotConnection
