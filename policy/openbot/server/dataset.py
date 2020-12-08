import glob
import os

from openbot import dataset_dir


def get_dataset_list(dir_path):
    datasets = []
    for d in listdir(dataset_dir, dir_path):
        file_list = get_dir_info(os.path.join(dir_path, d))
        datasets.append({
            "path": dir_path + "/" + d,
            "name": d,
            "sessions": list(filter(lambda f: f["is_session"], file_list)),
        })

    return datasets


def get_dir_info(dir_path):
    files = []
    list1 = listdir(dataset_dir, dir_path)
    for basename in list1:
        info = get_info(dir_path, basename)
        print("path", dir_path, basename)
        print(info)
        if info:
            files.append(info)

    return files


def listdir(*parts):
    list1 = os.listdir(os.path.join(*parts))
    list1.sort()
    return list1


def get_info(dir_path, basename):
    path = os.path.join(dir_path, basename)
    real_path = dataset_dir + "/" + path
    if not os.path.isdir(real_path):
        return None

    isSession = is_session(real_path)
    if isSession:
        return {
            "path": "/" + path,
            "name": basename,
            "is_session": isSession,
            "images": len(os.listdir(real_path + "/images")),
            "ctrl": count_lines(real_path + "/sensor_data/ctrlLog.txt") - 1,
            "indicator": count_lines(real_path + "/sensor_data/indicatorLog.txt") - 1,
        }

    files = os.listdir(real_path)
    file_count = len(files)
    dirs = glob.glob(real_path + "/*/")
    dir_count = len(dirs)

    return {
        "path": "/" + path,
        "name": basename,
        "is_session": isSession,
        "files": file_count - dir_count,
        "dirs": dir_count,
    }


def is_session(path):
    return os.path.isdir(path + "/images")


def count_lines(path):
    try:
        i = 0
        with open(path) as f:
            for i, l in enumerate(f):
                pass
        return i + 1
    except FileNotFoundError:
        return 0
