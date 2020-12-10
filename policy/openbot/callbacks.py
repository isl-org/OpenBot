# Created by Matthias Mueller - Intel Intelligent Systems Lab - 2020

import os
import tensorflow as tf

def checkpoint_cb (checkpoint_path, steps_per_epoch=-1, num_epochs=10):
    # Create a callback that saves the model's weights every epochs
    checkpoint_callback = tf.keras.callbacks.ModelCheckpoint(
        filepath=os.path.join(checkpoint_path, 'cp-{epoch:04d}.ckpt'), 
        monitor='val_loss',
        verbose=0,
        save_best_only=False,
        save_weights_only=False,
        mode='auto',
        save_freq='epoch' if steps_per_epoch<0 else int(num_epochs*steps_per_epoch)
    )
    return checkpoint_callback

def tensorboard_cb (log_path):
    tensorboard_callback = tf.keras.callbacks.TensorBoard(
        log_dir=log_path,
        histogram_freq=0,
        write_graph=True,
        write_images=True,
        update_freq='epoch',
        profile_batch=2,
        embeddings_freq=0,
        embeddings_metadata=None,
    )
    return tensorboard_callback

def logger_cb (log_path):
    logger_callback = tf.keras.callbacks.CSVLogger(os.path.join(log_path,'log.csv'))
    return logger_callback


def early_stopping_cb ():
    early_stopping_callback = tf.keras.callbacks.EarlyStopping(
        monitor='val_loss',
        min_delta=0,
        patience=20,
        verbose=0,
        mode='auto',
        baseline=None,
        restore_best_weights=False
    )
    return early_stopping_callback

def reduce_lr_cb ():
    reduce_lr_callback = tf.keras.callbacks.ReduceLROnPlateau(
        monitor='val_loss', 
        factor=0.3,
        patience=2, 
        min_lr=0.0001
    )
    return reduce_lr_callback

def lr_schedule_cb ():
    return tf.keras.callbacks.LearningRateScheduler(scheduler)

# This function defines a custom learning schedule.
def scheduler(epoch):
  if epoch < 10:
    return 0.0002
  elif epoch < 20:
    return 0.0001
  else:
    return 0.00005