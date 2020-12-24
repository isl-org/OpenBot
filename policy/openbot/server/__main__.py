from aiohttp import web
from openbot.server.main import app

if __name__ == "__main__":
    web.run_app(app, port=8000)
