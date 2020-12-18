import glob
import os

from aiohttp import web

from .. import dataset_dir


async def handle_api(request):
    files = get_dir_info(dataset_dir)
    return web.json_response(files)


def is_dataset(path):
    return os.path.isdir(path + "/images") and os.path.isdir(path + "/sensor_data")


def get_dir_info(dir_path):
    files = []
    list1 = glob.glob(dir_path + "/*")
    list1.sort()
    for path in list1:
        basename = os.path.basename(path)
        info = get_info(path, basename)
        if info:
            files.append(info)

    return files


def get_info(path, basename):
    if not os.path.isdir(path):
        return None

    files = os.listdir(path)
    file_count = len(files)
    if file_count == 1 and os.path.isdir(path + "/" + files[0]):
        path += "/" + files[0]
        basename += "/" + files[0]

    isDataset = is_dataset(path)
    if isDataset:
        return {
            "name": basename,
            "is_dataset": isDataset,
            "images": len(os.listdir(path + "/images")),
            "ctrl": count_lines(path + "/sensor_data/ctrlLog.txt") - 1,
            "indicator": count_lines(path + "/sensor_data/indicatorLog.txt") - 1,
        }

    files = os.listdir(path)
    file_count = len(files)
    dirs = glob.glob(path + "/*/")
    dir_count = len(dirs)

    return {
        "name": basename,
        "is_dataset": isDataset,
        "files": file_count - dir_count,
        "dirs": dir_count,
    }


def count_lines(path):
    i = 0
    with open(path) as f:
        for i, l in enumerate(f):
            pass
    return i + 1
