import os

base_dir = os.path.dirname(os.path.dirname(__file__))
dataset_dir = os.path.join(base_dir, "dataset")

if not (os.path.exists(dataset_dir)):
    os.makedirs(dataset_dir)
