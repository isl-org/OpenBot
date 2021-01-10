import os
import glob
import shutil

from openbot import models_dir
from openbot.train import Hyperparameters
from openbot.utils import list_dirs, read_csv_dict


def get_models():
    return [name for name in list_dirs(models_dir)]


def get_model_info(name):
    params = Hyperparameters.parse(name)
    csv_path = os.path.join(models_dir, name, "logs", "log.csv")
    logs = read_csv_dict(csv_path)
    params.NUM_EPOCHS = len(logs)

    return dict(
        name=name,
        path="/models/" + name,
        params=params.__dict__,
        logs=logs,
    )


def getPublished():
    return [
        dict(name=os.path.basename(p), mtime=int(os.path.getmtime(p)))
        for p in glob.glob(os.path.join(models_dir, "*.tflite"))
    ]


def publishModel(params):
    src = os.path.join(models_dir, params["model"], "checkpoints", params["checkpoint"]) + ".tflite"
    dst = os.path.join(models_dir, params["name"]) + ".tflite"
    print(src, dst)
    shutil.copyfile(src, dst)
    return True
