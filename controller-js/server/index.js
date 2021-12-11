const Dnssd = require('./dns.js')

const BotConnection = require('./bot-connection.js')
const BrowserConnection = require('./browser-connection')
const { Commands } = require('./commands.js')
// const LocalKeyboard = require('./local-keyboard.js')
const RemoteKeyboard = require('./remote-keyboard.js')
const ShutdownService = require('./shutdown-service')

const botConnection = new BotConnection()
const browserConnection = new BrowserConnection()

const commands = new Commands(botConnection, browserConnection)
const remoteKeyboard = new RemoteKeyboard(commands.getCommandHandler())

const onQuit = () => {
  browserConnection.stop()
}

remoteKeyboard.start(onQuit)

browserConnection.start(data => {
  // incoming data/commands from browser
  const dataJson = JSON.parse(data)

  switch (Object.keys(dataJson)[0]) {
    case 'KEYPRESS':
      remoteKeyboard.processKey(dataJson.KEYPRESS)
      break

    // redirect all other data to robot
    default:
      botConnection.send(data)
  }
})

new Dnssd().start(
  () => botConnection.start(commands.handleStatus), // onServiceUp
  botConnection.stop) // onServiceDown

// handle exit gracefully
new ShutdownService(botConnection, browserConnection).start()

/* Uncoment this to run server in headless mode:
    cd server
    npm start */
// new LocalKeyboard(commands.getCommandHandler()).start()
