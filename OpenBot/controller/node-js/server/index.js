const Dnssd = require('./dns.js')

const { Commands } = require('./commands.js')
const RemoteKeyboard = require('./remote-keyboard.js')
const ShutdownService = require('./shutdown-service')
const ConnectionManager = require('./connection-manager')

const connectionManager = new ConnectionManager()
const commands = new Commands(connectionManager.toBot)
const remoteKeyboard = new RemoteKeyboard(commands.getCommandHandler())

const onBrowserData = data => {
  const dataJson = JSON.parse(data)

  switch (Object.keys(dataJson)[0]) {
    case 'KEYPRESS':
      remoteKeyboard.processKey(dataJson.KEYPRESS)
      break

    // redirect all other data to robot
    default:
      connectionManager.toBot(data)
  }
}

// redirect status info from Bot to Browser
const onBotData = status => connectionManager.toBrowser(JSON.stringify(status))

new Dnssd().start(
  () => connectionManager.start(onBotData, onBrowserData), // onServiceUp
  connectionManager.stop) // onServiceDown

// handle exit gracefully
new ShutdownService(connectionManager, commands.getCommandHandler()).start()
