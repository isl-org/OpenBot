import csv
import os

from openbot import base_dir
from openbot.train import Hyperparameters
from openbot.utils import list_dirs

models_dir = os.path.join(base_dir, "models")


def get_models():
    return [get_model_info(name) for name in list_dirs(models_dir)]


def get_model_info(name):
    params = Hyperparameters.parse(name)
    csv_path = os.path.join(models_dir, name, "logs", "log.csv")
    logs = []
    with open(csv_path, newline="") as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            logs.append(row)
    params.NUM_EPOCHS = len(logs)

    return dict(
        name=name,
        path="/models/" + name,
        params=params.__dict__,
        logs=logs,
    )
