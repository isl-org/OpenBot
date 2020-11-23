""" Example of announcing a service (in this case, a fake HTTP server) """

import asyncio
import logging
import socket
import sys

from aiohttp import web
from aiozeroconf import ServiceInfo, Zeroconf
from netifaces import interfaces, ifaddresses, AF_INET


async def register(app: web.Application):
    await run_test(zc)
    app.on_shutdown.append(on_shutdown)


async def run_test(zc):
    global info, desc
    desc = {}
    local_ip = ip4_address()

    info = ServiceInfo("_http._tcp.local.",
                       "Openbot Web Site._http._tcp.local.",
                       address=socket.inet_aton(local_ip),
                       port=8000,
                       weight=0, priority=0, properties=desc, server="openbot.local.")
    print("Registration of a service, press Ctrl-C to exit...")
    await zc.register_service(info)


def ip4_address():
    for interface in interfaces():
        if interface == "lo" or interface.startswith("tun"):
            continue
        addresses = ifaddresses(interface)
        if AF_INET in addresses:
            for link in addresses[AF_INET]:
                return link['addr']


async def do_close(zc):
    global info
    await zc.unregister_service(info)
    await zc.close()


async def on_shutdown(app):
    print("Unregistering...")
    await do_close(zc)


loop = asyncio.get_event_loop()
zc = Zeroconf(loop)

if __name__ == '__main__':
    logging.basicConfig(level=logging.DEBUG)
    if len(sys.argv) > 1:
        assert sys.argv[1:] == ['--debug']
        logging.getLogger('aiozeroconf').setLevel(logging.DEBUG)

    try:
        xx = loop.create_task(run_test(zc))
        loop.run_forever()
    except KeyboardInterrupt:
        print("Unregistering...")
        loop.run_until_complete(do_close(zc))
    finally:
        loop.close()
