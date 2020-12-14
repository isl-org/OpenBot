import asyncio
import json
from operator import itemgetter
import os
import shutil
import threading
import time

import aiohttp
from aiohttp import web

from .dataset import get_dir_info
from .. import dataset_dir

current_train_process = None


async def handle_test(request: web.Request):
    return web.json_response({"openbot": 1})


async def handle_uploaded(request: web.Request):
    files = get_dir_info("uploaded")
    return web.json_response(files)


async def websocket_handler(request: web.Request):
    global current_train_process
    print('websocket connection started')
    ws = web.WebSocketResponse()
    await ws.prepare(request)

    async def send(data):
        data_str = json.dumps(data)
        print('websocket send ' + data_str)
        await ws.send_str(data_str)

    event = threading.Event()
    print('websocket async for')
    msg: aiohttp.WSMessage
    async for msg in ws:
        if msg.type == aiohttp.WSMsgType.TEXT:
            print('websocket msg ' + msg.data)
            data = json.loads(msg.data)
            action, payload = itemgetter('action', 'payload')(data)
            if action == 'moveSession':
                name = os.path.basename(payload['path'])
                src = os.path.join(dataset_dir, payload["path"])
                dst = os.path.join(dataset_dir, payload["new_path"], name)
                # os.rename(src, dst)
                await send({
                    "event": "moveSessionSuccess",
                    "old_path": payload["path"],
                    "new_path": os.path.join(payload["new_path"], name),
                })
            elif action == 'deleteSession':
                real_dir = dataset_dir + payload["path"]
                shutil.rmtree(real_dir)
                await send({
                    "event": "deleteSessionSuccess",
                    "path": payload["path"],
                })
            elif action == 'start':
                # await start_train(ws)
                event.clear()
                await async_train(send, event)
            elif action == 'stop':
                event.set()

        elif msg.type == aiohttp.WSMsgType.ERROR:
            print('ws connection closed with exception %s' % ws.exception())

    print('websocket connection closed')
    return ws


async def async_train(async_send, cancelled):
    loop = asyncio.get_event_loop()

    def send(msg):
        asyncio.run_coroutine_threadsafe(async_send(msg), loop).result()

    loop.run_in_executor(None, train, send, cancelled)


def train(send, cancelled):
    send({"event": "started"})
    for i in range(10):
        if cancelled.is_set():
            send({"event": "cancelled"})
            return
        time.sleep(1)
        send({"event": "progress", "current": i + 1})
    send({"event": "done"})
