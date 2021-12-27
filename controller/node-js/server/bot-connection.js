/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Sun Nov 28 2021
 */

const net = require('net')

function BotConnection () {
  const port = 19400
  let socket = null
  let server = null

  this.send = message => {
    if (socket) {
      socket.write(message + '\n')
    } else {
      console.log('Not connected, please connect to phone first...')
    }
  }

  this.stop = () => {
    if (server) {
      server.close()
    }
    if (socket && socket.end) {
      socket.end()
    }
  }

  this.start = onMessageReceived => {
    server = net.createServer(_socket => {
      console.log('createServer called...')
      socket = _socket
    })

    console.log('botConnection: start...')
    const handleConnection = conn => {
      const received = new MessageBuffer('\n')
      const onConnData = data => {
        received.push(data)
        while (!received.isFinished()) {
          const message = received.handleData()
          onMessageReceived(JSON.parse(message))
        }
      }

      const onConnClose = () => {
        console.log('connection from %s closed', remoteAddress)
      }

      const onConnError = err => console.log('Connection %s error: %s', remoteAddress, err.message)

      const remoteAddress = conn.remoteAddress + ':' + conn.remotePort
      console.log('Connected to Bot! 😃')
      conn.on('data', onConnData)
      conn.once('close', onConnClose)
      conn.on('error', onConnError)
    }

    server.on('connection', handleConnection)

    server.listen(port, () => {
      console.log(`listen on port ${port}`)
    })
  }
}

/*
    Utility function to accumulate data until a full JSON message has been received.
*/
function MessageBuffer (delimiter) {
  let buffer = ''

  this.isFinished = () => {
    if (
      buffer.length === 0 ||
      buffer.indexOf(delimiter) === -1
    ) {
      return true
    }
    return false
  }

  this.push = data => { buffer += data }

  const getMessage = () => {
    const delimiterIndex = buffer.indexOf(delimiter)
    if (delimiterIndex !== -1) {
      const message = buffer.slice(0, delimiterIndex)
      buffer = buffer.replace(message + delimiter, '')
      return message
    }
    return null
  }

  this.handleData = () => {
    /**
         * Try to accumulate the buffer with messages
         *
         * If the server isnt sending delimiters for some reason
         * then nothing will ever come back for these requests
         */
    const message = getMessage()
    return message
  }
}

module.exports = BotConnection
