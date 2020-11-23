import hashlib
import os

from aiohttp import multipart, web

from . import dataset_dir


async def handle_file_upload(field: multipart.BodyPartReader) -> web.Response:
    size = 0
    hash = hashlib.sha1()
    with open(os.path.join(dataset_dir, field.filename), 'wb') as f:
        while True:
            chunk = await field.read_chunk()  # 8192 bytes by default.
            if not chunk:
                break
            size += f.write(chunk)
            hash.update(chunk)

    return web.json_response({
        "filename": (field.filename),
        "size": size,
        "hash": hash.hexdigest(),
    })
