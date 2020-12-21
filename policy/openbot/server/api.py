import asyncio
import os
import shutil
import threading

from aiohttp import web
from aiohttp_json_rpc import JsonRpc

from .dataset import get_dataset_list, get_dir_info, get_info
from .models import get_models
from .preview import handle_preview
from .upload import handle_file_upload
from .. import dataset_dir
from ..train import CancelledException, Hyperparameters, MyCallback, start_train

event_cancelled = threading.Event()
rpc = JsonRpc()


async def handle_test(_: web.Request):
    return web.json_response({"openbot": 1})


async def handle_upload(request: web.Request) -> web.Response:
    reader = await request.multipart()
    while not reader.at_eof():
        field = await reader.next()
        if field.name == 'file':
            return await handle_file_upload(field)

    return web.Response(text="file not found")


async def init_api(app: web.Application):
    app.add_routes([
        web.get('/test', handle_test),
        web.post('/upload', handle_upload),
        web.get('/{path:.*}/preview.gif', handle_preview),
    ])

    rpc.add_methods(
        ('', listDir),
        ('', getDatasets),
        ('', getModels),
        ('', getHyperparameters),
        ('', moveSession),
        ('', deleteSession),
        ('', start),
        ('', stop),
    )
    rpc.add_topics(
        'session',
        'training',
    )
    app.router.add_route('*', '/ws', rpc.handle_request)


def listDir(params):
    path = params['path']
    basename = os.path.basename(path.rstrip("/"))
    dir_path = os.path.dirname(path.rstrip("/"))
    return {
        "basename": basename,
        "path": path,
        "session": get_info(dir_path + "/", basename),
        "file_list": get_dir_info(path),
    }


async def moveSession(params):
    basename = os.path.basename(params['path'])
    src = os.path.join(dataset_dir + params["path"])
    dst = os.path.join(dataset_dir + params["new_path"], basename)
    os.rename(src, dst)
    await rpc.notify('session')
    return True


async def deleteSession(params):
    real_dir = dataset_dir + params["path"]
    shutil.rmtree(real_dir)
    await rpc.notify('session')
    return True


def stop():
    event_cancelled.set()
    return True


def getDatasets():
    return {
        "train": get_dataset_list("train_data"),
        "test": get_dataset_list("test_data"),
    }


def getModels():
    return get_models()


def getHyperparameters():
    return Hyperparameters().__dict__


async def start(params):
    event_cancelled.clear()
    loop = asyncio.get_event_loop()

    def broadcast(event, payload=None):
        print("broadcast", event, payload)
        data = {"event": event, "payload": payload}
        asyncio.run_coroutine_threadsafe(rpc.notify("training", data), loop).result()

    hyper_params = Hyperparameters()
    for p in params:
        setattr(hyper_params, p, params[p])
    print(hyper_params.__dict__)
    loop.run_in_executor(None, train, hyper_params, broadcast, event_cancelled)
    return True


def train(params, broadcast, cancelled):
    try:
        broadcast("started")
        my_callback = MyCallback(broadcast, cancelled)
        tr = start_train(params, my_callback)
        broadcast("done", {"model": tr.model_name})
    except CancelledException:
        broadcast("cancelled")
