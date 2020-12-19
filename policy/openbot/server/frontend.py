import os
from subprocess import Popen
import urllib.error
import urllib.request
import zipfile

from aiohttp import web

from openbot import base_dir


async def init_frontend(app: web.Application):
    if os.getenv('FE_DEV'):
        await run_frontend_dev_server(app)
        return

    frontend_dir = os.path.join(base_dir, "frontend")
    zip_path = os.path.join(base_dir, "frontend.zip")
    version_target = read_version(frontend_dir, ".version")
    version_current = read_version(frontend_dir, "build", ".version")
    if version_current == version_target:
        print("Frontend is up to date!")
        return

    # todo fix URL
    url = f"https://github.com/sanyatuning/OpenBot/releases/download/{version_target}/frontend.zip"
    print("Downloading frontend...")
    try:
        urllib.request.urlretrieve(url, zip_path)
        with zipfile.ZipFile(zip_path, 'r') as zip_ref:
            zip_ref.extractall(frontend_dir)
        with open(os.path.join(frontend_dir, "build", ".version"), 'w') as f:
            f.write(version_target)
        os.unlink(zip_path)
        print("Frontend is ready!")
    except urllib.error.HTTPError as e:
        print("Download error:", e)
        print("URL:", url)


async def run_frontend_dev_server(app: web.Application):
    if is_port_in_use(3000):
        return
    print("Start Frontend Development Server...")
    Popen(
        ["yarn", "run", "start"],
        cwd=os.path.join(base_dir, "frontend"),
    )


def read_version(*args):
    try:
        with open(os.path.join(*args)) as f:
            return f.read().strip()
    except FileNotFoundError:
        return None


def is_port_in_use(port):
    import socket
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        return s.connect_ex(('localhost', port)) == 0
