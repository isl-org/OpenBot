import math
import os

from aiohttp import web
import imageio

from .. import dataset_dir


async def handle_preview(request):
    gif_path = dataset_dir + "/" + request.match_info.get("path")
    dir_path = os.path.dirname(gif_path)
    if not os.path.isfile(gif_path):
        with imageio.get_writer(gif_path, mode="I", fps=8) as writer:
            images = os.listdir(dir_path + "/images/")
            images.sort()
            len1 = len(images)
            start = math.ceil(len1 * 0.1)
            step = minmax(8, 16, len1 / 100)
            stop = minmax(len1, 500, len1)
            for i in range(start, stop, step):
                image = imageio.imread(dir_path + "/images/" + images[i])
                writer.append_data(image)

    return web.FileResponse(gif_path)


def minmax(min_value, max_value, value):
    return max(min_value, min(max_value, math.ceil(value)))
