import os

from aiohttp import web
import aiohttp_jinja2
import jinja2
from openbot.api import dataset_dir, handle_api
from openbot.upload import handle_file_upload
from openbot.zeroconf import register


print("Dataset dir: " + dataset_dir)


@aiohttp_jinja2.template("index.html")
async def handle_get(request: web.Request):
    return {
        "dir": request.match_info.get("dir")
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
    web.get('/{dir:.*}', handle_get),
    web.get('/api/dataset', handle_api),
    web.post('/upload', handle_post),
])
app.on_startup.append(register)
aiohttp_jinja2.setup(
    app, loader=jinja2.FileSystemLoader(os.path.join(os.getcwd(), "templates"))
)
if __name__ == '__main__':
    web.run_app(app, port=8000)
