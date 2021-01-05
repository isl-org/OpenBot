import glob
import os
import traceback

from .. import associate_frames, dataset_dir


def get_dataset_list(dir_path):
    return [get_dataset_info(dir_path, name) for name in listdir(dataset_dir, dir_path)]


def get_dataset_info(dir_path, name):
    file_list = get_dir_info(os.path.join(dir_path, name))
    return dict(
        name=name,
        path="/" + dir_path + "/" + name,
        sessions=list(filter(lambda f: f["is_session"], file_list)),
    )


def get_dir_info(dir_path):
    files = []
    list1 = listdir(dataset_dir, dir_path)
    for basename in list1:
        info = get_info(dir_path, basename)
        if info:
            files.append(info)

    return files


def listdir(*parts):
    list1 = [d for d in os.listdir(os.path.join(*parts)) if ".DS_Store" not in d]
    list1.sort()
    return list1


def get_info(path, basename=None):
    path = path.lstrip("/")
    if basename:
        path = os.path.join(path, basename)
    else:
        basename = os.path.basename(path)
    real_path = dataset_dir + "/" + path
    if not os.path.isdir(real_path):
        return None

    isSession = is_session(real_path)
    if isSession:
        try:
            max_offset = 1e3
            frames = associate_frames.match_frame_session(
                real_path,
                max_offset,
                redo_matching=False,
                remove_zeros=True,
            )
            keys = list(frames.keys())
            seconds = int((keys[-1] - keys[0]) / 1000 / 1000 / 1000)
            ctrl = []
            for key in frames:
                frame = frames[key]
                frame[0] = os.path.basename(frame[0])
                ctrl.append(frame)
            error = None
        except Exception as e:
            traceback.print_exc()
            seconds = 0
            ctrl = []
            error = str(e)

        return {
            "path": "/" + path,
            "name": basename,
            "is_session": isSession,
            "ctrl": ctrl,
            "seconds": seconds,
            "error": error,
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
