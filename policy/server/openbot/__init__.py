import os

dataset_dir = os.path.realpath("../dataset")

if not (os.path.exists(dataset_dir)):
    os.makedirs(dataset_dir)