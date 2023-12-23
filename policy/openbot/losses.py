# Created by Matthias Mueller - Intel Intelligent Systems Lab - 2020

import tensorflow as tf


def angle(y):
    return y[:, 0] - y[:, 1]


def angle_weight(y_gt, eps=0.05):
    return tf.math.square(angle(y_gt)) + eps


def mse_raw(y_gt, y_pred):
    return tf.keras.losses.mean_squared_error(y_gt, y_pred)


def mae_raw(y_gt, y_pred):
    return tf.keras.losses.mean_absolute_error(y_gt, y_pred)


def huber_raw(y_gt, y_pred):
    huber = tf.keras.losses.Huber()
    return tf.keras.losses.huber(y_gt, y_pred)


def mse_angle(y_gt, y_pred):
    return tf.keras.losses.mean_squared_error(angle(y_gt), angle(y_pred))


def weighted_mse_raw(y_gt, y_pred):
    return angle_weight(y_gt) * mse_raw(y_gt, y_pred)


def weighted_mse_angle(y_gt, y_pred):
    return angle_weight(y_gt) * mse_angle(y_gt, y_pred)


def sq_weighted_mse_angle(y_gt, y_pred):
    return angle_weight(y_gt) * mse_angle(y_gt, y_pred)


def weighted_mse_raw_angle(y_gt, y_pred):
    return angle_weight(y_gt) * (mse_raw(y_gt, y_pred) + mse_angle(y_gt, y_pred))


def mae_raw_weighted_mse_angle(y_gt, y_pred):
    return mae_raw(y_gt, y_pred) + weighted_mse_angle(y_gt, y_pred)


def weighted_mse_raw(y_true, y_pred):
    weight = tf.math.abs(y_true[:, 0] - y_true[:, 1] + 0.05)
    return weight * tf.keras.losses.mean_squared_error(y_true, y_pred)


def sq_weighted_mse_angle(y_true, y_pred):
    angle_true = y_true[:, 1] - y_true[:, 0]
    angle_pred = y_pred[:, 1] - y_pred[:, 0]
    weight = tf.math.abs(angle_true + 0.05)
    return tf.math.square(weight) * (
        tf.keras.losses.mean_squared_error(angle_true, angle_pred)
        + tf.keras.losses.mean_squared_error(y_true[:,:2], y_pred)
    )

def custom_loss(y_true, y_pred):
    # Extract components from y_true
    rewards = y_true[:, 2]
    label = y_true[:, :2]

    # Extract components from y_pred
    predicted_label = y_pred[:, :2]

    # Define weights for different components
    weight_label = 1.0
    weight_reward = 0.8 # Adjust the weight based on the importance of rewards
    weight_angle = 1.0
    
    # Compute the mean squared error for the angles
    
    mse = mse_angle(label, predicted_label)

    # Compute the mean of the rewards
    negative_reward_mean = tf.math.reduce_mean(rewards)

    # Combine the components with weights
    loss = weight_angle * mse + weight_label*tf.keras.losses.mean_squared_error(label, predicted_label) - weight_reward * negative_reward_mean

    return loss