"""
Created by Marcel Santos - Intel Intelligent Systems Lab - 2021
This script implements several routines for data augmentation.
"""
import tensorflow as tf


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
        if coin < 0.25:
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
