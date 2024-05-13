from dataclasses import dataclass
import os
import re
import threading
import argparse
import matplotlib.pyplot as plt
import numpy as np
import tensorflow as tf
import absl.logging
from graphviz import Digraph

from . import (
    associate_frames,
    callbacks,
    dataloader,
    dataset_dir,
    data_augmentation,
    losses,
    metrics,
    models,
    models_dir,
    tfrecordRL,
    tfrecord_utils,
    utils,
)

absl.logging.set_verbosity(absl.logging.ERROR)

os.environ["TF_CPP_MIN_LOG_LEVEL"] = "2"
# 0 = all messages are logged (default behavior)
# 1 = INFO messages are not printed
# 2 = INFO and WARNING messages are not printed
# 3 = INFO, WARNING, and ERROR messages are not printed

# On Mac you may encounter an error related to OMP, this is a workaround, but slows down the code
# https://github.com/dmlc/xgboost/issues/1715
os.environ["KMP_DUPLICATE_LIB_OK"] = "True"

AUTOTUNE = tf.data.experimental.AUTOTUNE

if tf.test.gpu_device_name():
    print("Default GPU Device:{}".format(tf.test.gpu_device_name()))
else:
    print("Please install GPU version of TF if you have one.")

@dataclass
class Hyperparameters:
    MODEL: str = "pilot_net"
    POLICY: str = "Reinforcement"

    TRAIN_BATCH_SIZE: int = 128
    TEST_BATCH_SIZE: int = 16
    LEARNING_RATE: float = 0.0003
    NUM_EPOCHS: int = 100

    BATCH_NORM: bool = True
    IS_CROP: bool = False
    FLIP_AUG: bool = False
    CMD_AUG: bool = False

    USE_LAST: bool = False

    WANDB: bool = False

    @classmethod
    def parse(cls, name):
        m = re.match(
            r".*_((cil|pilot)_.+)_lr(\d+.\d+)_bz(\d+)(_bn)?(_flip)?(_cmd)?", name
        )
        params = Hyperparameters(m[1], int(m[4]), int(m[4]), float(m[3]))
        params.BATCH_NORM = m[5] is not None
        params.FLIP_AUG = m[6] is not None
        params.CMD_AUG = m[7] is not None
        return params

    def __str__(self):
        model_name = "{0}_lr{1}_bz{2}".format(
            self.MODEL, self.LEARNING_RATE, self.TRAIN_BATCH_SIZE
        )

        if self.BATCH_NORM:
            model_name += "_bn"
        if self.FLIP_AUG:
            model_name += "_flip"
        if self.CMD_AUG:
            model_name += "_cmd"

        return model_name
    
@dataclass
class Training:
    def __init__(self, params: Hyperparameters):
        self.hyperparameters = params
        self.NETWORK_IMG_WIDTH = 0
        self.NETWORK_IMG_HEIGHT = 0
        self.INITIAL_EPOCH = 0
        self.train_data_dir = ""
        self.test_data_dir = ""
        self.train_datasets = []
        self.test_datasets = []
        self.redo_matching = True
        self.remove_zeros = True
        self.image_count_train = 0
        self.image_count_test = 0
        self.train_ds = None
        self.test_ds = None
        self.history = None
        self.model_name = ""
        self.dataset_name = "openbot"
        self.checkpoint_path = ""
        self.log_path = ""
        self.loss_fn = None
        self.metric_list = None
        self.custom_objects = None

class CancelledException(BaseException):
    pass


class MyCallback(tf.keras.callbacks.Callback):
    cancelled: threading.Event
    model: tf.keras.Model

    def __init__(self, broadcast, cancelled, show_progress=False):
        super().__init__()
        self.broadcast = broadcast
        self.cancelled = cancelled
        self.show_progress = show_progress

        self.epoch = 0
        self.step = 0

    def on_epoch_begin(self, epoch, logs=None):
        self.epoch = epoch

    def on_epoch_end(self, epoch, logs=None):
        msg = logs.copy()
        msg["epoch"] = epoch
        print("on_epoch_end", msg)
        self.broadcast("logs", msg)

    def on_batch_end(self, batch, logs=None):
        if self.cancelled.is_set():
            raise CancelledException
        self.step = batch + 1
        epochs = self.params["epochs"]
        steps = self.params["steps"]
        if self.show_progress:
            self.broadcast(
                "progress",
                dict(
                    epoch=round(100 * self.step / steps, 1),
                    train=round(
                        100 * (self.epoch * steps + self.step) / (epochs * steps), 1
                    ),
                ),
            )

def load_tfrecord(tr: Training, verbose=0):
    def process_train_sample(features):
        # image = tf.image.resize(features["image"], size=(224, 224))
        image = features["image"]

        actions = [features["forward"], features["left"], features["right"]]
        rewards = features["rewards"]
        done = features["done"]
        image = data_augmentation.augment_img(image)
        if tr.hyperparameters.FLIP_AUG:
                print(
                    "Image flip augmentation is not implemented for Reinforcement."
                )
        if tr.hyperparameters.CMD_AUG:
            print(
                "Command augmentation is not implemented for Reinforcement."
            )

        return (image, actions), rewards, done

    def process_test_sample(features):
        image = features["image"]

        actions = [features["forward"], features["left"], features["right"]]
        rewards = features["rewards"]
        done = features["done"]

        return (image, actions), rewards, done


    train_dataset = (
        tf.data.TFRecordDataset(tr.train_data_dir, num_parallel_reads=AUTOTUNE)
        .map(
            tfrecord_utils.parse_tfrecord_fn_reinforcement, num_parallel_calls=AUTOTUNE
        )
        .map(process_train_sample, num_parallel_calls=AUTOTUNE)
    )
    

    # Obtains the images shapes of records from .tfrecords.
    for (image, actions), rewards, done in train_dataset.take(1):
        shape = image.numpy().shape
        tr.NETWORK_IMG_HEIGHT = shape[0]
        tr.NETWORK_IMG_WIDTH = shape[1]
        print("Image shape: ", shape)
        print("Actions: ", actions.numpy())
        print("Rewards: ", rewards.numpy())
        print("Done: ", done.numpy())

    
    test_dataset = (
        tf.data.TFRecordDataset(tr.test_data_dir, num_parallel_reads=AUTOTUNE)
        .map(
            tfrecord_utils.parse_tfrecord_fn_reinforcement, num_parallel_calls=AUTOTUNE
        )
        .map(process_test_sample, num_parallel_calls=AUTOTUNE)
    )
    

    # Obtains the total number of records from .tfrecords file
    # https://stackoverflow.com/questions/40472139/obtaining-total-number-of-records-from-tfrecords-file-in-tensorflow
    tr.image_count_train = sum(1 for _ in train_dataset)
    print("Number of training instances: ", tr.image_count_train)

    tr.image_count_test = sum(1 for _ in test_dataset)
    print("Number of test instances: ", tr.image_count_test)

    # Prepare train and test datasets for training
    tr.train_ds = (
        train_dataset.shuffle(tr.hyperparameters.TRAIN_BATCH_SIZE * 10)
        .repeat()
        .batch(tr.hyperparameters.TRAIN_BATCH_SIZE)
        .prefetch(AUTOTUNE)
    )

    tr.test_ds = test_dataset.batch(tr.hyperparameters.TEST_BATCH_SIZE).prefetch(
        AUTOTUNE
    )

def process_data(tr: Training):
    tr.train_datasets = utils.list_dirs(tr.train_data_dir)
    tr.test_datasets = utils.list_dirs(tr.test_data_dir)

    print("Train Datasets: ", len(tr.train_datasets))
    print("Test Datasets: ", len(tr.test_datasets))

    # 1ms
    max_offset = 1e3
    train_frames = associate_frames.match_frame_info_input(
        tr.train_data_dir,
        tr.train_datasets,
        max_offset,
        redo_matching=tr.redo_matching,
        remove_zeros=tr.remove_zeros,
        policy=tr.hyperparameters.POLICY,
    )

    test_frames = associate_frames.match_frame_info_input(
        tr.test_data_dir,
        tr.test_datasets,
        max_offset,
        redo_matching=tr.redo_matching,
        remove_zeros=tr.remove_zeros,
        policy=tr.hyperparameters.POLICY,
    )

    tr.image_count_train = len(train_frames)
    tr.image_count_test = len(test_frames)
    print(
        "There are %d train images and %d test images"
        % (tr.image_count_train, tr.image_count_test)
    )





def create_tfrecord(callback: MyCallback, policy="autopilot"):
    callback.broadcast(
        "message", "Converting data to tfrecord (this may take some time)..."
    )
    tfrecordRL.convert_dataset_info(
        os.path.join(dataset_dir, "train_data"),
        os.path.join(dataset_dir, "tfrecords"),
        "train.tfrec",
        policy=policy,
    )
    tfrecordRL.convert_dataset_info(
        os.path.join(dataset_dir, "test_data"),
        os.path.join(dataset_dir, "tfrecords"),
        "test.tfrec",
        policy=policy,
    )


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Input Arguments")
    parser.add_argument(
        "--no_tf_record",
        action="store_true",
        help="do not load a tfrecord but a directory of files",
    )
    parser.add_argument(
        "--create_tf_record", action="store_true", help="create a new tfrecord"
    )
    parser.add_argument(
        "--model",
        type=str,
        default="pilot_net",
        choices=["cil_mobile", "cil_mobile_fast", "cil", "pilot_net"],
        help="network architecture (default: pilot_net)",
    )
    parser.add_argument(
        "--batch_size",
        type=int,
        default=128,
        help="number of training epochs (default: 128)",
    )
    parser.add_argument(
        "--learning_rate",
        type=float,
        default=0.0003,
        help="learning rate (default: 0.0003)",
    )
    parser.add_argument(
        "--num_epochs", type=int, default=100, help="number of epochs (default: 100)"
    )
    parser.add_argument("--batch_norm", action="store_true", help="use batch norm")
    parser.add_argument(
        "--flip_aug",
        action="store_true",
        help="randomly flip images and controls for augmentation",
    )
    parser.add_argument(
        "--cmd_aug",
        action="store_true",
        help="add noise to command input for augmentation",
    )
    parser.add_argument(
        "--resume", action="store_true", help="resume previous training"
    )
    parser.add_argument(
        "--wandb", action="store_true", help="training logs with weights & biases"
    )
    parser.add_argument(
        "--policy",
        type=str,
        default="autopilot",
        choices=["autopilot", "point_goal_nav"],
        help="the type of policy to be trained (default: autopilot)",
    )

    args = parser.parse_args()

    params = Hyperparameters()
    params.MODEL = args.model
    params.POLICY = args.policy
    params.TRAIN_BATCH_SIZE = args.batch_size
    params.TEST_BATCH_SIZE = args.batch_size
    params.LEARNING_RATE = args.learning_rate
    params.NUM_EPOCHS = args.num_epochs
    params.BATCH_NORM = args.batch_norm
    params.FLIP_AUG = args.flip_aug
    params.CMD_AUG = args.cmd_aug
    params.USE_LAST = args.resume
    params.WANDB = args.wandb
    params.IS_CROP = args.policy == "point_goal_nav"

    def broadcast(event, payload=None):
        print()
        print(event, payload)

    event = threading.Event()
    my_callback = MyCallback(broadcast, event)

    if args.create_tf_record:
        create_tfrecord(my_callback, args.policy)



    

