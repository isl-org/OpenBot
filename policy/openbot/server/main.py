from aiohttp import web

from .api import init_api
from .frontend import init_frontend
from .zeroconf import register

app = web.Application()

app.on_startup.append(register)
app.on_startup.append(init_api)
app.on_startup.append(init_frontend)
