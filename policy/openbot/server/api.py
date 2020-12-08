import os

import aiohttp
from aiohttp import web

from .dataset import get_dir_info
from .train import start_train


async def handle_test(request: web.Request):
    return web.json_response({"openbot": 1})


async def handle_uploaded(request: web.Request):
    files = get_dir_info("uploaded")
    return web.json_response(files)


async def websocket_handler(request: web.Request):
    print('websocket connection started')
    ws = web.WebSocketResponse()
    await ws.prepare(request)

    print('websocket async for')
    msg: aiohttp.WSMessage
    async for msg in ws:
        if msg.type == aiohttp.WSMsgType.TEXT:
            print('websocket msg ' + msg.data)
            if msg.data == 'close':
                await ws.close()
            elif msg.data == 'start':
                print(os.getcwd())
                # await start_train(ws)
            else:
                await ws.send_str(msg.data + '/answer')

        elif msg.type == aiohttp.WSMsgType.ERROR:
            print('ws connection closed with exception %s' % ws.exception())

    print('websocket connection closed')
    return ws
