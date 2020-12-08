import os

from aiohttp import web
from aiohttp.web_exceptions import HTTPBadRequest, HTTPNotFound
import aiohttp_jinja2
import jinja2

from . import api
from .dataset import get_dataset_list, get_dir_info, get_info
from .preview import handle_preview
from .upload import handle_file_upload
from .zeroconf import register
from .. import dataset_dir


async def handle_get(request: web.Request):
    path = request.match_info.get("path")
    basename = os.path.basename(path.rstrip("/"))
    dir_path = os.path.dirname(path.rstrip("/"))
    info = get_info(dir_path + "/", basename)
    action = request.rel_url.query.get('action')
    if not action:
        action = 'session' if info['is_session'] else 'list'
    return web.json_response({
        "basename": basename,
        "path": path,
        "action": action,
        "train_datasets": get_dataset_list("train_data"),
        "test_datasets": get_dataset_list("test_data"),
        "session": info,
        "file_list": get_dir_info(path),
    })


@aiohttp_jinja2.template("index.html")
async def handle_train(request: web.Request):
    return {
        "action": "train",
    }


async def handle_post(request: web.Request):
    action = request.rel_url.query.get('action')
    form = await request.post()
    path = request.match_info.get("path").rstrip("/")

    if action == "rename":
        rename_dataset(path, form["new_name"])
    if action == "move":
        basename = os.path.basename(path)
        move_dir(path, form["new_path"] + "/" + basename)

    raise HTTPBadRequest


def rename_dataset(old_path, new_name):
    base_path = os.path.dirname(old_path)
    new_path = urljoin(base_path, new_name)
    move_dir(old_path, new_path)


def move_dir(old_path, new_path):
    os.rename(
        os.path.join(dataset_dir, old_path),
        os.path.join(dataset_dir, new_path),
    )
    raise web.HTTPFound(location=f'/{new_path}/')


async def handle_upload(request: web.Request) -> web.Response:
    reader = await request.multipart()
    while not reader.at_eof():
        field = await reader.next()
        if field.name == 'file':
            return await handle_file_upload(field)

    return web.Response(text="file not found")


async def handle_favicon(request: web.Request):
    raise HTTPNotFound


def urljoin(*parts):
    return '/'.join(s.strip('/') for s in parts)


app = web.Application()
app.add_routes([
    web.get('/ws', api.websocket_handler),
    web.get('/favicon.ico', handle_favicon),
    web.get('/train', handle_train),
    web.post('/upload', handle_upload),
    web.get('/test', api.handle_test),
    web.get('/api/uploaded', api.handle_uploaded),
    web.get('/{path:.*}/preview.gif', handle_preview),
    web.get('/{path:.*}', handle_get),
    web.post('/{path:.*}', handle_post),
])
app.on_startup.append(register)
aiohttp_jinja2.setup(
    app, loader=jinja2.FileSystemLoader(os.path.join(os.path.dirname(__file__), "templates"))
)
