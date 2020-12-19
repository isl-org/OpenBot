import os

from openbot import base_dir
from openbot.utils import list_dirs


def get_models():
    return [{"name": name} for name in list_dirs(os.path.join(base_dir, "models"))]
