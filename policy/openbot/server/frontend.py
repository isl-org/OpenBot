import os
from subprocess import Popen

from aiohttp import web

from openbot import base_dir


async def init_frontend(app: web.Application):
    if os.getenv('FE_DEV'):
        await run_frontend_dev_server(app)
        return

    # todo:
    # get version from file
    # download from github if needed
    # unzip
    pass


async def run_frontend_dev_server(app: web.Application):
    if is_port_in_use(3000):
        return
    print("Start Frontend Development Server...")
    Popen(
        ["yarn", "run", "start"],
        cwd=os.path.join(base_dir, "frontend"),
    )


def is_port_in_use(port):
    import socket
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        return s.connect_ex(('localhost', port)) == 0
