
# Driving Policy (Advanced)

<p align="center">
  <span>English</span> |
  <a href="README_CN.md">简体中文</a>
</p>

In order to train an autonomous driving policy, you will first need to collect a dataset. The more data you collect, the better the resulting driving policy. For the experiments in our paper, we collected about 30 minutes worth of data. Note that the network will imitate your driving behaviour. The better and more consistent you drive, the better the network will learn to drive.

## DISCLAIMERS

1. **Safety:** Driving policies are not perfect and may crash the robot. Always make sure you operate in a safe environment! Keep in mind, that your phone could be damaged in a collision! Make sure you always have a game controller connected and are familiar with the key mapping so you can stop the vehicle at any time. Use at your own risk!
2. **Compute hardware:** Training a driving policy requires a lot of resources and may slow down or even freeze your machine. It is recommended to use a high-end laptop or workstation with large amount of RAM and dedicated GPU, especially when training with larger batch sizes. The documentation is currently also not very detailed. Use at your own risk!
3. **Patience required:** To get a good driving policy for your custom dataset will require some patience. It is not straight-forward, involves data collection, hyperparameter tuning, etc. If you have never trained machine learning models before, it will be challenging and may even get frustrating.

## Data Collection

1. Connect a bluetooth game controller to the phone (e.g. PS4 controller).
2. Select the AUTOPILOT_F network in the app.
3. Now drive drive the car via a game controller and record a dataset. On the PS4 controller logging can be toggled with the **X** button.

You will now find a folder called *Openbot* on the internal storage of your smartphone. For each recording, there will be zip file. The name of the zip file will be in the format *yyyymmdd_hhmmss.zip* corresponding to the timestamp of when the recording was started.

Your dataset should be stored in the following structure.

```markdown
dataset
└── train_data
 └── my_openbot_1
  └── recording_1
      recording_2
      ...
     my_openbot_2
     ...
 test_data
 └── my_openbot_3
  └── recording_1
      recording_2
      ...
```

Each recording corresponds to an extracted zip file that you have transferred from the *Openbot* folder on your phone.

## Policy Training

You first need to setup your training environment.

### Dependencies

We recommend to create a conda environment for OpenBot. Instructions on installing conda can be found [here](https://docs.conda.io/projects/conda/en/latest/user-guide/install/).
If you do not have a dedicated GPU (e.g. using your laptop) you can create a new environment with the following command:

```bash
conda create -n openbot python=3.7 tensorflow=2.0.0 notebook=6.1.1 matplotlib=3.3.1 pillow=7.2.0
```

Note that training will be very slow. So if you have access to a computer with dedicated GPU, we highly recommend to use it. In this case, you will need Tensorflow with GPU support. Run the following command to setup the conda environment:

```bash
conda create -n openbot python=3.7 tensorflow-gpu=2.0.0 notebook=6.1.1 matplotlib=3.3.1 pillow=7.2.0
```

If you prefer to setup the environment manually, here is a list of the dependencies:

- Tensorflow
- Jupyter Notebook
- Matplotlib
- Numpy
- PIL

If you want to use tensorflow=2.2.0 you may need to pass the custom metrics as custom objects dictionary. (See this [issue](https://github.com/intel-isl/OpenBot/issues/39).)

### Jupyter Notebook

We provide a [Jupyter Notebook](policy_learning.ipynb) that guides you through the steps for training an autonomous driving policy. The notebook will produce two tflite files corresponding to the best checkpoint according to the validation metrics and the last checkpoint. Pick one of them and rename them to autopilot_float.tflite. Replace the existing model at

```markdown
app
└── assets
 └── networks
  └── autopilot_float.tflite
```

and recompile the app.
