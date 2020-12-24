import hashlib
import os
import zipfile

from aiohttp import multipart, web

from .. import dataset_dir


async def handle_file_upload(field: multipart.BodyPartReader) -> web.Response:
    size = 0
    hash = hashlib.sha1()
    path = os.path.join(dataset_dir, field.filename)
    with open(path, "wb") as f:
        while True:
            chunk = await field.read_chunk()  # 8192 bytes by default.
            if not chunk:
                break
            size += f.write(chunk)
            hash.update(chunk)
    with zipfile.ZipFile(path, "r") as zip_ref:
        zip_ref.extractall(dataset_dir + "/uploaded/" + field.filename[:-4])

    os.unlink(path)

    return web.json_response(
        {
            "filename": field.filename,
            "size": size,
            "hash": hash.hexdigest(),
        }
    )
