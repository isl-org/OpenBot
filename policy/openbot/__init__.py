import os

def mkdir (path):
    if not (os.path.exists(path)):
        os.makedirs(path)

base_dir = os.path.dirname(os.path.dirname(__file__))
dataset_dir = os.path.join(base_dir, "dataset")
upload_dir = os.path.join(dataset_dir, "uploaded")
train_data_dir = os.path.join(dataset_dir, "train_data")
test_data_dir = os.path.join(dataset_dir, "test_data")
models_dir = os.path.join(base_dir, "models")

mkdir(upload_dir)
mkdir(train_data_dir)
mkdir(test_data_dir)
mkdir(models_dir)

if len([d for d in os.walk(train_data_dir)]) < 2:
    makedir(os.path.join(train_data_dir, "my_dataset_1"))

if len([d for d in os.walk(test_data_dir)]) < 2:
    makedir(os.path.join(test_data_dir, "my_dataset_2"))