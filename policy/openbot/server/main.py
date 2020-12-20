import os

from aiohttp import web
import aiohttp_jinja2
import jinja2

from . import api
from .dataset import get_dir_info, get_info
from .frontend import init_frontend
from .preview import handle_preview
from .upload import handle_file_upload
from .zeroconf import register


async def handle_get(request: web.Request):
    path = request.match_info.get("path")
    basename = os.path.basename(path.rstrip("/"))
    dir_path = os.path.dirname(path.rstrip("/"))
    info = get_info(dir_path + "/", basename)

    return web.json_response({
        "basename": basename,
        "path": path,
        "session": info,
        "file_list": get_dir_info(path),
    })


async def handle_upload(request: web.Request) -> web.Response:
    reader = await request.multipart()
    while not reader.at_eof():
        field = await reader.next()
        if field.name == 'file':
            return await handle_file_upload(field)

    return web.Response(text="file not found")


def urljoin(*parts):
    return '/'.join(s.strip('/') for s in parts)


app = web.Application()
app.add_routes([
    web.get('/ws', api.websocket_handler),
    web.post('/upload', handle_upload),
    web.get('/test', api.handle_test),
    web.get('/{path:.*}/preview.gif', handle_preview),
])
app.on_startup.append(register)
app.on_startup.append(init_frontend)
aiohttp_jinja2.setup(
    app, loader=jinja2.FileSystemLoader(os.path.join(os.path.dirname(__file__), "templates"))
)
