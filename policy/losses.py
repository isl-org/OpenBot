# Created by Matthias Mueller - Intel Intelligent Systems Lab - 2020

import tensorflow as tf

def weighted_mse_raw(y_true, y_pred):
    weight = tf.math.abs(y_true[:,0] - y_true[:,1] + 0.05)
    return weight*tf.keras.losses.mean_squared_error(y_true, y_pred)

def weighted_mse_angle(y_true, y_pred):
    angle_true = y_true[:,1] - y_true[:,0]
    angle_pred = y_pred[:,1] - y_pred[:,0]
    weight = tf.math.abs(angle_true + 0.05)
    return tf.math.square(weight)*(tf.keras.losses.mean_squared_error(angle_true, angle_pred) + tf.keras.losses.mean_squared_error(y_true, y_pred))

def sq_weighted_mse_angle(y_true, y_pred):
    angle_true = y_true[:,1] - y_true[:,0]
    angle_pred = y_pred[:,1] - y_pred[:,0]
    weight = tf.math.abs(angle_true + 0.05)
    return tf.math.square(weight)*(tf.keras.losses.mean_squared_error(angle_true, angle_pred) + tf.keras.losses.mean_squared_error(y_true, y_pred))