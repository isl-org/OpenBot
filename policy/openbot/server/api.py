import asyncio
import json
import os
import shutil
import threading
from typing import List

import aiohttp
from aiohttp import web

from .dataset import get_dataset_list, get_dir_info, get_info
from .models import get_models
from .. import dataset_dir
from ..train import CancelledException, Hyperparameters, MyCallback, start_train

active_ws: List[web.WebSocketResponse] = []
event_cancelled = threading.Event()


async def handle_test(_: web.Request):
    return web.json_response({"openbot": 1})


async def async_broadcast(event, payload=None):
    data_str = json.dumps({
        "event": event,
        "payload": payload,
    })
    print('websocket send ' + data_str)
    for ws in active_ws:
        await ws.send_str(data_str)


async def websocket_handler(request: web.Request):
    print('websocket connection started')
    ws = web.WebSocketResponse()
    active_ws.append(ws)
    await ws.prepare(request)

    print('websocket async for')
    msg: aiohttp.WSMessage
    async for msg in ws:
        if msg.type == aiohttp.WSMsgType.TEXT:
            print('websocket msg ' + msg.data)
            data = json.loads(msg.data)
            method = data.get('method')
            params = data.get('params')
            req_id = data.get('id')

            async def reply(result, error=None):
                data_str = json.dumps({
                    "jsonrpc": "2.0",
                    "result": result,
                    "id": req_id,
                    "error": error,
                })
                print('websocket reply ' + data_str)
                await ws.send_str(data_str)

            if method == 'listDir':
                path = params['path']
                basename = os.path.basename(path.rstrip("/"))
                dir_path = os.path.dirname(path.rstrip("/"))
                await reply({
                    "basename": basename,
                    "path": path,
                    "session": get_info(dir_path + "/", basename),
                    "file_list": get_dir_info(path),
                })
            elif method == 'moveSession':
                basename = os.path.basename(params['path'])
                src = os.path.join(dataset_dir + params["path"])
                dst = os.path.join(dataset_dir + params["new_path"], basename)
                os.rename(src, dst)
                await reply(True)
                await async_broadcast('moveSessionSuccess', {
                    "old_path": params["path"],
                    "new_path": os.path.join(params["new_path"], basename),
                })
            elif method == 'deleteSession':
                real_dir = dataset_dir + params["path"]
                shutil.rmtree(real_dir)
                await reply(True)
                await async_broadcast('deleteSessionSuccess')
            elif method == 'start':
                event_cancelled.clear()
                await async_train(params)
                await reply(True)
            elif method == 'stop':
                event_cancelled.set()
                await reply(True)

            elif method == 'getDatasets':
                await reply({
                    "train": get_dataset_list("train_data"),
                    "test": get_dataset_list("test_data"),
                })

            elif method == 'getModels':
                await reply(get_models())

            elif method == 'getHyperparameters':
                await reply(Hyperparameters().__dict__)
            else:
                await reply(False, "Unknown method")

        elif msg.type == aiohttp.WSMsgType.ERROR:
            print('ws connection closed with exception %s' % ws.exception())

    print('websocket connection closed')
    active_ws.remove(ws)

    return ws


async def async_train(params):
    loop = asyncio.get_event_loop()

    def broadcast(event, payload=None):
        asyncio.run_coroutine_threadsafe(async_broadcast(event, payload), loop).result()

    hyper_params = Hyperparameters()
    for p in params:
        setattr(hyper_params, p, params[p])
    print(hyper_params.__dict__)
    loop.run_in_executor(None, train, hyper_params, broadcast, event_cancelled)


def train(params, broadcast, cancelled):
    try:
        broadcast("started")
        my_callback = MyCallback(broadcast, cancelled)
        tr = start_train(params, my_callback)
        broadcast("done", {"model": tr.model_name})
    except CancelledException:
        broadcast("cancelled")
