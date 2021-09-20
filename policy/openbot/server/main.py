from aiohttp import web

from .api import init_api
from .frontend import init_frontend
from .zeroconf import register

app = web.Application()


async def up(app: web.Application):
    print("Server is running, press Ctrl-C to exit...")


app.on_startup.append(register)
app.on_startup.append(init_api)
app.on_startup.append(init_frontend)
app.on_startup.append(up)
