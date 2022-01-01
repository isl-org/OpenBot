/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Sun Nov 28 2021
 */

const dnssd = require('dnssd2')

/*
This class is used to create a network service with a name "OPEN_BOT_CONTROLLER" and type "_openbot._tcp.",
and advertise this service on the network. The Android bot app can look up this service and get the
IP address of this application, and connects automatically. The corresponding class in the Android app
is "NetworkServiceConnection".

Note: we are using the "dnssd2" package instead of the more common "bounjour" or "zerocofig", becase the latter
is not compatible with the Robot's Android app.
*/

function Dnssd () {
  const type = 'openbot'
  const name = 'OPEN_BOT_CONTROLLER'
  const port = 19400

  this.start = (onServiceUp, onSevideDown) => {
    const ad = new dnssd.Advertisement(dnssd.tcp(type), port, { name: name, resolve: true })
    ad.start()

    dnssd.Browser(dnssd.tcp('openbot'))
      .on('serviceUp', onServiceUp)
      .on('serviceDown', onSevideDown)
      .start()
  }
}

module.exports = Dnssd
