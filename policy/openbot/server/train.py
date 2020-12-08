import numpy as np
import matplotlib.pyplot as plt
import os

os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
# 0 = all messages are logged (default behavior)
# 1 = INFO messages are not printed
# 2 = INFO and WARNING messages are not printed
# 3 = INFO, WARNING, and ERROR messages are not printed

# On Mac you may encounter an error related to OMP, this is a workaround, but slows down the code
# https://github.com/dmlc/xgboost/issues/1715
os.environ['KMP_DUPLICATE_LIB_OK'] = 'True'

import tensorflow as tf
from openbot import (
    associate_frames,
    dataloader,
    utils,
    models,
    losses,
    metrics,
    callbacks
)

AUTOTUNE = tf.data.experimental.AUTOTUNE

base_dir = "../dataset"
models_dir = '../models'
dataset_name = "my_openbot"

train_data_dir = os.path.join(base_dir, "train_data")
test_data_dir = os.path.join(base_dir, "test_data")

TRAIN_BATCH_SIZE = 16  # 128
TEST_BATCH_SIZE = 16  # 128
LR = 0.0001  # 0.0003
NUM_EPOCHS = 10  # 100

# Input dimensions
IMG_HEIGHT = 720
IMG_WIDTH = 1280

# Offset dimensions (crop)
OFFSET_IMG_HEIGHT = 240
OFFSET_IMG_WIDTH = 0

# Target dimensions
TARGET_IMG_HEIGHT = IMG_HEIGHT - OFFSET_IMG_HEIGHT
TARGET_IMG_WIDTH = IMG_WIDTH - OFFSET_IMG_WIDTH

# Network dimensions
NETWORK_IMG_HEIGHT = TARGET_IMG_HEIGHT // 5
NETWORK_IMG_WIDTH = TARGET_IMG_WIDTH // 5

BN = True
FLIP_AUG = False
CMD_AUG = False


class Training:

    def __init__(self):
        self.train_datasets = []
        self.test_datasets = []
        self.image_count_train = 0
        self.image_count_test = 0
        self.train_ds = None
        self.test_ds = None
        self.history = None
        self.checkpoint_path = ""
        self.log_path = ""
        self.loss_fn = None
        self.metric_list = None


def process_data():
    tr = Training()
    tr.train_datasets = [d for d in os.listdir(train_data_dir) if os.path.isdir(os.path.join(train_data_dir, d))]
    tr.test_datasets = [d for d in os.listdir(test_data_dir) if os.path.isdir(os.path.join(test_data_dir, d))]

    print('Train Datasets: ', len(tr.train_datasets))
    print('Test Datasets: ', len(tr.test_datasets))

    print(tr.__dict__)
    # 1ms
    max_offset = 1e3
    train_frames = associate_frames.match_frame_ctrl_cmd(train_data_dir,
                                                         tr.train_datasets,
                                                         max_offset,
                                                         redo_matching=False,
                                                         remove_zeros=True)
    test_frames = associate_frames.match_frame_ctrl_cmd(test_data_dir,
                                                        tr.test_datasets,
                                                        max_offset,
                                                        redo_matching=False,
                                                        remove_zeros=True)

    tr.image_count_train = len(train_frames)
    tr.image_count_test = len(test_frames)
    print("There are %d train images and %d test images" % (tr.image_count_train, tr.image_count_test))

    return tr


def load_data(tr: Training):
    # list_train_ds = tf.data.Dataset.list_files(train_frames)
    # list_test_ds = tf.data.Dataset.list_files(test_frames)
    list_train_ds = tf.data.Dataset.list_files([str(train_data_dir + '/' + dataset + '/*/images/*')
                                                for dataset in tr.train_datasets])
    list_test_ds = tf.data.Dataset.list_files([str(test_data_dir + '/' + dataset + '/*/images/*')
                                               for dataset in tr.test_datasets])
    for f in list_train_ds.take(5):
        print(f.numpy())
    print()
    for f in list_test_ds.take(5):
        print(f.numpy())
    train_data = dataloader.dataloader(train_data_dir, tr.train_datasets)
    print("Number of train samples: %d" % len(train_data.labels))
    test_data = dataloader.dataloader(test_data_dir, tr.test_datasets)
    print("Number of test samples: %d" % len(test_data.labels))

    def augment_img(img):
        """Color augmentation

        Args:
          img: input image

        Returns:
          img: augmented image
        """
        img = tf.image.random_hue(img, 0.08)
        img = tf.image.random_saturation(img, 0.6, 1.6)
        img = tf.image.random_brightness(img, 0.05)
        img = tf.image.random_contrast(img, 0.7, 1.3)
        return img

    def augment_cmd(cmd):
        """
        Command augmentation

        Args:
          cmd: input command

        Returns:
          cmd: augmented command
        """
        if not (cmd > 0 or cmd < 0):
            coin = tf.random.uniform(shape=[1], minval=0, maxval=1, dtype=tf.dtypes.float32)
            if (coin < 0.25):
                cmd = -1.0
            elif coin < 0.5:
                cmd = 1.0
        return cmd

    def flip_sample(img, cmd, label):
        coin = tf.random.uniform(shape=[1], minval=0, maxval=1, dtype=tf.dtypes.float32)
        if coin < 0.5:
            img = tf.image.flip_left_right(img)
            cmd = -cmd
            label = tf.reverse(label, axis=[0])
        return img, cmd, label

    def decode_resize_img(img):
        img = decode_img(img)
        img = tf.image.crop_to_bounding_box(img, OFFSET_IMG_HEIGHT, OFFSET_IMG_WIDTH,
                                            TARGET_IMG_HEIGHT,
                                            TARGET_IMG_WIDTH)
        # resize the image to the desired size.
        img = tf.image.resize(img, [NETWORK_IMG_HEIGHT, NETWORK_IMG_WIDTH])
        return img

    def decode_img(img):
        # convert the compressed string to a 3D uint8 tensor
        img = tf.image.decode_jpeg(img, channels=3)
        # Use `convert_image_dtype` to convert to floats in the [0,1] range.
        img = tf.image.convert_image_dtype(img, tf.float32)
        return img

    def process_train_path(file_path):
        cmd, label = train_data.get_label(tf.strings.regex_replace(file_path, "[/\\\\]", "/"))
        # load the raw data from the file as a string
        img = tf.io.read_file(file_path)
        img = decode_img(img)
        img = augment_img(img)
        if FLIP_AUG:
            img, cmd, label = flip_sample(img, cmd, label)
        if CMD_AUG:
            cmd = augment_cmd(cmd)
        return (img, cmd), label

    def process_test_path(file_path):
        cmd, label = test_data.get_label(tf.strings.regex_replace(file_path, "[/\\\\]", "/"))
        # load the raw data from the file as a string
        img = tf.io.read_file(file_path)
        img = decode_img(img)
        return (img, cmd), label

    # Set `num_parallel_calls` so multiple images are loaded/processed in parallel.
    labeled_ds = list_train_ds.map(process_train_path, num_parallel_calls=4)
    for (image, cmd), label in labeled_ds.take(1):
        print("Image shape: ", image.numpy().shape)
        print("Command: ", cmd.numpy())
        print("Label: ", label.numpy())
    tr.train_ds = utils.prepare_for_training(
        ds=labeled_ds,
        batch_sz=TRAIN_BATCH_SIZE,
        shuffle_buffer_sz=100 * TRAIN_BATCH_SIZE,
        prefetch_buffer_sz=10 * TRAIN_BATCH_SIZE,
    )
    (image_batch, cmd_batch), label_batch = next(iter(tr.train_ds))
    utils.show_train_batch(image_batch.numpy(), cmd_batch.numpy(), label_batch.numpy())
    test_ds = list_test_ds.map(process_test_path, num_parallel_calls=4)
    test_ds = test_ds.batch(TEST_BATCH_SIZE)
    tr.test_ds = test_ds.prefetch(buffer_size=10 * TRAIN_BATCH_SIZE)


def do_training(tr: Training):
    model = models.cil_mobile(NETWORK_IMG_WIDTH, NETWORK_IMG_HEIGHT, BN)
    tr.loss_fn = losses.sq_weighted_mse_angle
    metric_list = ['MeanAbsoluteError', metrics.direction_metric, metrics.angle_metric]
    optimizer = tf.keras.optimizers.Adam(lr=LR)

    model.compile(optimizer=optimizer,
                  loss=tr.loss_fn,
                  metrics=metric_list)
    print(model.summary())

    MODEL_NAME = dataset_name + "_" + model.name + "_lr" + str(LR) + "_bz" + str(TRAIN_BATCH_SIZE)
    if BN:
        MODEL_NAME += "_bn"
    if FLIP_AUG:
        MODEL_NAME += "_flip"
    if CMD_AUG:
        MODEL_NAME += "_cmd"

    tr.checkpoint_path = os.path.join(models_dir, MODEL_NAME, 'checkpoints')
    tr.log_path = os.path.join(models_dir, MODEL_NAME, 'logs')
    print(MODEL_NAME)

    STEPS_PER_EPOCH = np.ceil(tr.image_count_train / TRAIN_BATCH_SIZE)
    tr.history = model.fit(tr.train_ds,
                           epochs=NUM_EPOCHS,
                           steps_per_epoch=STEPS_PER_EPOCH,
                           validation_data=tr.test_ds,
                           callbacks=[callbacks.checkpoint_cb(tr.checkpoint_path),
                                      callbacks.tensorboard_cb(tr.log_path),
                                      callbacks.logger_cb(tr.log_path)])


def do_evaluation(tr: Training):
    history = tr.history
    log_path = tr.log_path
    plt.plot(history.history['MeanAbsoluteError'], label='mean_absolute_error')
    plt.plot(history.history['val_MeanAbsoluteError'], label='val_mean_absolute_error')
    plt.xlabel('Epoch')
    plt.ylabel('Mean Absolute Error')
    plt.legend(loc='lower right')
    plt.savefig(os.path.join(log_path, 'error.png'))

    plt.plot(history.history['direction_metric'], label='direction_metric')
    plt.plot(history.history['val_direction_metric'], label='val_direction_metric')
    plt.xlabel('Epoch')
    plt.ylabel('Direction Metric')
    plt.legend(loc='lower right')
    plt.savefig(os.path.join(log_path, 'direction.png'))

    plt.plot(history.history['angle_metric'], label='angle_metric')
    plt.plot(history.history['val_angle_metric'], label='val_angle_metric')
    plt.xlabel('Epoch')
    plt.ylabel('Angle Metric')
    plt.legend(loc='lower right')
    plt.savefig(os.path.join(log_path, 'angle.png'))

    plt.plot(history.history['loss'], label='loss')
    plt.plot(history.history['val_loss'], label='val_loss')
    plt.xlabel('Epoch')
    plt.ylabel('Loss')
    plt.legend(loc='lower right')
    plt.savefig(os.path.join(log_path, 'loss.png'))

    checkpoint_path = tr.checkpoint_path
    print("checkpoint_path", checkpoint_path)
    best_index = np.argmax(
        np.array(history.history['val_angle_metric'])
        + np.array(history.history['val_direction_metric']))
    best_checkpoint = str("cp-%04d.ckpt" % (best_index + 1))
    best_tflite = utils.generate_tflite(checkpoint_path, best_checkpoint)
    utils.save_tflite(best_tflite, checkpoint_path, "best")
    print("Best Checkpoint (val_angle: %s, val_direction: %s): %s" % (
        history.history['val_angle_metric'][best_index],
        history.history['val_direction_metric'][best_index],
        best_checkpoint))

    last_checkpoint = \
        sorted([d for d in os.listdir(checkpoint_path) if os.path.isdir(os.path.join(checkpoint_path, d))])[-1]
    last_tflite = utils.generate_tflite(checkpoint_path, last_checkpoint)
    utils.save_tflite(last_tflite, checkpoint_path, "last")
    print("Last Checkpoint (val_angle: %s, val_direction: %s): %s" % (
        history.history['val_angle_metric'][-1],
        history.history['val_direction_metric'][-1],
        last_checkpoint))

    best_model = utils.load_model(os.path.join(checkpoint_path, best_checkpoint), tr.loss_fn, tr.metric_list)
    test_loss, test_acc, test_dir, test_ang = best_model.evaluate(tr.test_ds,
                                                                  steps=tr.image_count_test / TEST_BATCH_SIZE,
                                                                  verbose=2)
    NUM_SAMPLES = 15
    (image_batch, cmd_batch), label_batch = next(iter(tr.test_ds))
    pred_batch = best_model.predict(
        (tf.slice(image_batch, [0, 0, 0, 0], [NUM_SAMPLES, -1, -1, -1]), tf.slice(cmd_batch, [0], [NUM_SAMPLES])))
    utils.show_test_batch(image_batch.numpy(), cmd_batch.numpy(), label_batch.numpy(), pred_batch)

    utils.compare_tf_tflite(best_model, best_tflite)


def start_train():
    tr = process_data()
    load_data(tr)
    do_training(tr)
    do_evaluation(tr)


if __name__ == '__main__':
    start_train()
