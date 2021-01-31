import os
import glob
import shutil

from openbot import models_dir
from openbot.server import api
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


def getModelFiles():
    return [
        dict(name=os.path.basename(p), mtime=int(os.path.getmtime(p)))
        for p in glob.glob(os.path.join(models_dir, "*.tflite"))
    ]


async def publishModel(params):
    src = os.path.join(models_dir, params["model"], "checkpoints", params["checkpoint"]) + ".tflite"
    dst = os.path.join(models_dir, params["name"]) + ".tflite"
    print(src, dst)
    shutil.copyfile(src, dst)
    await api.rpc.notify("modelFile")
    return True


async def deleteModelFile(params):
    real_dir = os.path.join(models_dir, params["path"])
    os.remove(real_dir)
    await api.rpc.notify("modelFile")
    return True
