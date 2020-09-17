# Created by Matthias Mueller - Intel Intelligent Systems Lab - 2020

import tensorflow as tf

"""
Constructors for standard MLPs and CNNs
"""
def create_cnn(width, height, depth, 
               cnn_filters=(8, 12, 16, 20, 24), 
               kernel_sz=(5, 5, 5, 3, 3), 
               stride=(2,2,2,2,2), 
               padding='same', 
               activation='relu',
               conv_dropout=0,
               mlp_filters=(64,16), 
               mlp_dropout=0.2, 
               bn=False):

    # define input shape, channel dimension (tf convention: channels last) and img input
    inputShape = (height, width, depth)
    channelDim = -1
    inputs = tf.keras.Input(shape=inputShape, name='img_input')
 
    # build the cnn layer by layer
    for (i, f) in enumerate(cnn_filters):
        # set the input if it is the first layer
        if i == 0:
            x = inputs
 
        # build one block with conv, activation and optional bn and dropout
        x = tf.keras.layers.Conv2D(f, (kernel_sz[i], kernel_sz[i]), strides=(stride[i], stride[i]), padding=padding, activation=activation)(x)
        if bn:
            x = tf.keras.layers.BatchNormalization(axis=channelDim)(x)
        if conv_dropout > 0:
            x = tf.keras.layers.Dropout(conv_dropout)(x)
        
    # flatten output of the cnn and build the mlp
    x = tf.keras.layers.Flatten()(x)
    # build the mlp layer by layer
    for (i, f) in enumerate(mlp_filters):
        x = tf.keras.layers.Dense(f, activation=activation)(x)
        if bn:
            x = tf.keras.layers.BatchNormalization(axis=channelDim)(x)
        if mlp_dropout > 0:
            x = tf.keras.layers.Dropout(mlp_dropout)(x)
 
    # assemble the model
    model = tf.keras.Model(inputs, x)
 
    # return the model
    return model


def create_mlp(in_dim, hidden_dim, out_dim, activation='relu', dropout=0.2):
    model = tf.keras.Sequential(name='MLP')
    model.add(tf.keras.layers.Dense(hidden_dim, input_dim = in_dim, activation=activation, name='cmd'))
    if dropout > 0:
        model.add(tf.keras.layers.Dropout(dropout)) 
    model.add(tf.keras.layers.Dense(out_dim, activation=activation))  
    return model

def pilot_net(img_width, img_height, bn=False):
    mlp = create_mlp(1,1,1,dropout=0)
    cnn = create_cnn(img_width, img_height, 3, 
                     cnn_filters=(24, 36, 48, 64, 64), 
                     kernel_sz=(5, 5, 5, 3, 3), 
                     stride=(2,2,2,1,1), 
                     padding='valid', 
                     activation='relu', 
                     mlp_filters=(1164,100), 
                     mlp_dropout=0,
                     bn=bn)
    
    # fuse input MLP and CNN
    combinedInput = tf.keras.layers.concatenate([mlp.input, cnn.output])

    # output MLP
    x = tf.keras.layers.Dense(50, activation="relu")(combinedInput)
    x = tf.keras.layers.concatenate([mlp.input, x])
    x = tf.keras.layers.Dense(10, activation="relu")(x)
    x = tf.keras.layers.concatenate([mlp.input, x])
    x = tf.keras.layers.Dense(2, activation="linear")(x)

    # our final model will accept commands on the MLP input
    # and images on the CNN input, outputting two values (left/right ctrl)
    model = tf.keras.Model(name='pilot_net',inputs=(cnn.input, mlp.input), outputs=x)
    return model

def cil_mobile(img_width, img_height, bn=True):
    mlp = create_mlp(1, 16, 16, dropout=0.5)
    cnn = create_cnn(img_width, img_height, 3, 
                     cnn_filters=(32, 64, 96, 128, 256), 
                     kernel_sz=(5, 3, 3, 3, 3), 
                     stride=(2,2,2,2,2), 
                     padding='same', 
                     activation='relu', 
                     conv_dropout=0.2,
                     mlp_filters=(128,64), 
                     mlp_dropout=0.5, 
                     bn=bn)
    
    # fuse input MLP and CNN
    combinedInput = tf.keras.layers.concatenate([mlp.output, cnn.output])

    # output MLP
    x = tf.keras.layers.Dense(64, activation="relu")(combinedInput)
    x = tf.keras.layers.Dropout(0.5)(x)
    x = tf.keras.layers.concatenate([mlp.input, x])
    x = tf.keras.layers.Dense(16, activation="relu")(x)
    x = tf.keras.layers.Dropout(0.5)(x)
    x = tf.keras.layers.concatenate([mlp.input, x])
    x = tf.keras.layers.Dense(2, activation="linear")(x)

    # our final model will accept commands on the MLP input
    # and images on the CNN input, outputting two values (left/right ctrl)
    model = tf.keras.Model(name='cil_mobile',inputs=(cnn.input, mlp.input), outputs=x)

    return model


def cil_mobile_fast(img_width, img_height, bn=True):
    mlp = create_mlp(1, 16, 16)
    cnn = create_cnn(img_width, img_height, 3, 
                     cnn_filters=(32, 32, 64, 64, 128), 
                     kernel_sz=(5, 3, 3, 3, 2), 
                     stride=(2,2,2,2,2), 
                     padding='valid', 
                     activation='relu', 
                     conv_dropout=0.2,
                     mlp_filters=(512,512), 
                     bn=bn)
    
    # fuse input MLP and CNN
    combinedInput = tf.keras.layers.concatenate([mlp.output, cnn.output])

    # output MLP
    x = tf.keras.layers.Dense(64, activation="relu")(combinedInput)
    x = tf.keras.layers.concatenate([mlp.input, x])
    x = tf.keras.layers.Dense(64, activation="relu")(x)
    x = tf.keras.layers.concatenate([mlp.input, x])
    x = tf.keras.layers.Dense(2, activation="linear")(x)

    # our final model will accept commands on the MLP input
    # and images on the CNN input, outputting two values (left/right ctrl)
    model = tf.keras.Model(name='cil_mobile_fast',inputs=(cnn.input, mlp.input), outputs=x)

    return model


def cil(img_width, img_height, bn=True):
    mlp = create_mlp(1, 64, 64, dropout=0.5)
    cnn = create_cnn(img_width, img_height, 3, 
                     cnn_filters=(32, 32, 64, 64, 128, 128, 256, 256), 
                     kernel_sz=(5, 3, 3, 3, 3, 3, 3, 3), 
                     stride=(2,1,2,1,2,1,1,1), 
                     padding='valid', 
                     activation='relu', 
                     conv_dropout=0.2,
                     mlp_filters=(512,512), 
                     mlp_dropout=0.5, 
                     bn=bn)
    
    # fuse input MLP and CNN
    combinedInput = tf.keras.layers.concatenate([mlp.output, cnn.output])

    # output MLP
    x = tf.keras.layers.Dense(256, activation="relu")(combinedInput)
    x = tf.keras.layers.Dropout(0.5)(x)
    x = tf.keras.layers.Dense(256, activation="relu")(x)
    x = tf.keras.layers.Dropout(0.5)(x)
    x = tf.keras.layers.Dense(2, activation="linear")(x)

    # our final model will accept commands on the MLP input
    # and images on the CNN input, outputting two values (left/right ctrl)
    model = tf.keras.Model(name='cil',inputs=(cnn.input, mlp.input), outputs=x)

    return model