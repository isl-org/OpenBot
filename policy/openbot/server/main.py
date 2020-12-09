import os

from aiohttp import web
import aiohttp_jinja2
import jinja2
from .api import dataset_dir, handle_api, get_dir_info
from .preview import handle_preview
from .upload import handle_file_upload
from .zeroconf import register


@aiohttp_jinja2.template("index.html")
async def handle_get(request: web.Request):
    path = request.match_info.get("path")
    return {
        "path": path,
        "file_list": get_dir_info(dataset_dir + "/" + path),
    }


async def handle_post(request: web.Request) -> web.Response:
    reader = await request.multipart()
    while not reader.at_eof():
        field = await reader.next()
        if field.name == 'file':
            return await handle_file_upload(field)

    return web.Response(text="file not found")


app = web.Application()
app.add_routes([
    web.post('/upload', handle_post),
    web.get('/api/dataset', handle_api),
    web.get('/{path:.*}/preview.gif', handle_preview),
    web.get('/{path:.*}', handle_get),
])
app.on_startup.append(register)
aiohttp_jinja2.setup(
    app, loader=jinja2.FileSystemLoader(os.path.join(os.path.dirname(__file__), "templates"))
)
