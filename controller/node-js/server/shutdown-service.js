/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Sun Nov 28 2021
 */

class ShutdownService {
  constructor (botConnection, browserConnection, commandHandler) {
    this.start = () => {
      console.log('ShutdownService started...')
      process.stdin.resume() // so the program will not close instantly

      const exitHandler = (options, exitCode) => {
        console.log('\nResetting Bot...')
        commandHandler.reset()

        if (botConnection && botConnection.stop) {
          botConnection.stop()
        }

        if (browserConnection && browserConnection.stop) {
          browserConnection.stop()
        }

        if (options.exit) process.exit()
      }

      // do something when app is closing
      process.on('exit', exitHandler.bind(null, { cleanup: true }))

      // catches ctrl+c event
      process.on('SIGINT', exitHandler.bind(null, { exit: true }))
      process.on('SIGTERM', exitHandler.bind(null, { exit: true }))
      process.on('SIGHUP', exitHandler.bind(null, { exit: true }))

      // catches "kill pid" (for example: nodemon restart) for dev
      // process.on('SIGUSR1', exitHandler.bind(null, { exit: true }));
      // process.on('SIGUSR2', exitHandler.bind(null, { exit: true }));

      // catches uncaught exceptions
      process.on('uncaughtException', exitHandler.bind(null, { exit: true }))
    }
  }
}

module.exports = ShutdownService
