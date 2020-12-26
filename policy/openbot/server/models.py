import os

from openbot import base_dir
from openbot.utils import list_dirs


def get_models():
    return [get_model_info(name) for name in list_dirs(os.path.join(base_dir, "models"))]


def get_model_info(name):
    return dict(
        name=name,
    )

