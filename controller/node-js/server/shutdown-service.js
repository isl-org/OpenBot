/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Sun Nov 28 2021
 */

function ShutdownService (connectionManager, commandHandler) {
  this.start = () => {
    process.stdin.resume() // so the program will not close instantly

    const exitHandler = (options, exitCode) => {
      commandHandler.reset()
      connectionManager.stop()

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

module.exports = ShutdownService
