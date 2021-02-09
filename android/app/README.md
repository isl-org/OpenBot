# OpenBot App

Our application is derived from the [TensorFlow Lite Object Detection Android Demo](https://github.com/tensorflow/examples/tree/master/lite/examples/object_detection/android). We add a data logger and support for game controllers to collect datasets with the robot. Currently, we record readings from following sensors: camera, gyroscope, accelerometer, magnetometer, ambient light sensor, and barometer. Using the Android API, we are able to obtain the following sensor readings: RGB images, angular speed, linear acceleration, gravity, magnetic field strength, light intensity, atmospheric pressure, latitude, longitude, altitude, bearing, and speed. In addition to the phone sensors, we also record body sensor readings (wheel odometry, obstacle distance and battery voltage), which are transmitted via the serial link. Lastly, we record control commands received from a connected game controller, if present. We also integrate several neural networks for person following and autonomous navigation.

## DISCLAIMERS

1. **Safety:** Always make sure you operate in a safe environment. Keep in mind, that your phone could be damaged in a collision! Special 
care is neccessary when using automated control (e.g. person following or driving policy). Make sure you always have a game controller connected and are familiar with the key mapping so you can stop the vehicle at any time. Use at your own risk!
2. **App under development:** The application is under development and may crash or exhibit unexpected behaviour depending on your phone model and version of the operating system. Make sure to test all functionalities with no wheels connected. Use at your own risk!

## How to Use the App

The app starts with the screen comprising of all the robot's functionality.

<p align="center">
  <img src="../../docs/images/app_gui_1.jpg" alt="App GUI" width="49%"/>
  <img src="../../docs/images/app_gui_2.jpg" alt="App GUI" width="50%"/>
</p>

### USB Connection

The drop-down menu is used to set the baud rate. The default is 115200 and you should not need to change this unless you mess with the Arduino firmware. The app will attempt to connect automatically, but in case you encounter issues you can use this switch to disconnect/connect.

### Vehicle Status

The field **Battery** displays the battery voltage as measured by the Arduino via the voltage divider. The field **Speed (l,r)** reports the left and right speed of the (front) wheels in rpm. It is measured by the Arduino via the optical wheel speed sensors. The field **Sonar** shows the free space in front of the car in centimeters. It is measured by the Arduino via the ultrasonic sensor. Note, you will only receive values a few seconds after the USB connections has been established.

### Control

The first button is for selecting the **control mode**. Currenlty, the only control mode is **Gamepad**. In the future, it will be possible to control the robot with another smartphone, i.e. **Phone** or via **WebRTC**. 

The second button is for selecting the **drive mode**. There are three different drive modes when using a game controller (e.g. PS4):

- **Game**: Use the right and left shoulder triggers (R2, L2) for forward and reverse throttle and either joystick for steering. This mode imitates the control mode of car racing video games.
- **Joystick**: Use either one of the joysticks to control the robot.
- **Dual**: Use the left and right joystick to control the left and right side of the car. This is raw differential steering.

The third button is for selecting the **speed mode**. There are three different speed modes:

- **Slow**: The voltage applied to the motors is limited to 50% of the input voltage (~6V).
- **Normal**: The voltage applied to the motors is limited to 75% of the input voltage (~9V).
- **Fast**: There is no limit. The full input voltage will be applied to the motors at full throttle (~12V). *This is the default setting for running the neural networks.*

Running at higher speeds will reduce the lifetime of the motors but is more fun. The controls that are sent to the robot are displayed on the right side. When using the game controller, the speed mode can be increased by pressing down the right joystick (R3) and decrased by pressing down the left joystick (L3).

### Data Log

There are four different logging modes:

- **only_sensors**: All sensor data but no images are saved.
- **crop_img**: All sensor data and a cropped images that have the input size of the network are saved. This is the default setting and is what should be used for data collection.
- **preview_img**: All sensor data and a full-size images are saved. This will require a lot of memory and can be slow. However, it is nice for compiling FPV videos.
- **all_imgs**: All sensor data and both cropped and full-size images are saved. This will require a lot of memory and can be slow.

The switch on the right is used to toggle logging on and off. On the game controller this switch can be toggled with the X button. 

### Camera

The first item shows the preview resolution. The second item shows the crop resolution. This is the image that is used as input to the neural networks. You will notice that this resolution changes depending on which model you select below. If you train your own autopilot, make sure to select the `AUTOPILOT_F` model. The crop resolution should show `256x96`. The switch on the right is used to toggle between the rear and the front camera. 

### Model

There are three models that come with the app:

- **DETECTOR_V1_1_0_Q**: This model is used for person following. It uses a SSD object detector with MobileNet V1 backbone. The model is quantized for better performance on embedded devices.
- **DETECTOR_V3_S_Q**: This model is used for person following. It uses a SSD object detector with MobileNet V3 backbone. The model is quantized for better performance on embedded devices.
- **AUTOPILOT_F**: This model is used for autonomous navigation. It will predict controls directly from the camera input. Chances are that it will not work in your environment. You should follow our instructions to train your own [Driving Policy](../../policy) and replace it.

The switch on the right is used to turn the network on and off. When the network is running, it produces the controls for the robot and the game controller is disabled. However, you may still use the buttons on the game controller, for example to toggle this switch with the R1 trigger button to regain control of the robot. 

### Device

Use the drop-down menu to select the device on which the neural network should be executed. You have the following choices:

- **CPU**: Using the CPU works on most phones and is the default choice. You can adjust the number of threads to optimize performance. 
- **GPU**: Most smartphones have a GPU. Networks with large inputs such as images often run faster on a GPU.
- **NNAPI**: This will use the [TensorFlow Lite NNAPI delegate](https://www.tensorflow.org/lite/performance/nnapi). Modern smartphones often come with dedicated AI accelerators. The [Neural Network API](https://developer.android.com/ndk/guides/neuralnetworks) (NNAPI) provides acceleration for TensorFlow Lite models on Android devices with Graphics Processing Unit (GPU), Digital Signal Processor (DSP) and Neural Processing Unit (NPU). Note that on some older phones this can be very slow!

If a model is active, the inference speed in [ms] will be displayed next to the device which is running the model.

### Features

Pressing the back button opens up the menu screen to choose specific features to use.

<p align="center">
<img src="../../docs/images/menu_selector.png" alt="App GUI" width="25%"/>
</p>

- **Robot**: Robot opens the screen containing all the functionality described above.


- **Free Roam**: Free Roam offers simple robot control with real time updates and information about battery, speed and distance from surfaces.

<p align="center">
<img src="../../docs/images/game_screen.png" alt="Alt text" width="50%" />
</p>

## Code Structure

The [TensorFlow Lite Object Detection Android Demo](https://github.com/tensorflow/examples/tree/master/lite/examples/object_detection/android) was used as starting point to integrate TFLite models and obtain the camera feed. The main activity is the [NetworkActivity](src/main/java/org/openbot/robot/NetworkActivity.java) which runs the main thread. It inherits from the [CameraActivity](src/main/java/org/openbot/robot/CameraActivity.java) which manages the camera and UI. The [SensorService](src/main/java/org/openbot/robot/SensorService.java) reads all other phone sensors and logs them. The [ServerService](src/main/java/org/openbot/robot/ServerService.java) and [NsdService](src/main/java/org/openbot/robot/NsdService.java) establish a connection to a local [Python server](../../policy/README.md#web-app) with a React frontend. If you collect data it can be uploaded automatically for visualization, training ML models and downloading trained models to the robot. The [env](src/main/java/org/openbot/env) folder contains utility classes such as the [Vehicle](src/main/java/org/openbot/env/Vehicle.java) interface, [GameController](src/main/java/org/openbot/env/GameController.java) interface, [PhoneController](src/main/java/org/openbot/env/PhoneController.java) interface and an [AudioPlayer](src/main/java/org/openbot/env/AudioPlayer.java) for the audible feedback. The [tflite](src/main/java/org/openbot/tflite) folder contains the model definitions for the [Autopilot](src/main/java/org/openbot/tflite/Autopilot.java) and [Detector](src/main/java/org/openbot/tflite/Detector.java) networks.

## Next (optional)

Train your own [Driving Policy](../../policy/README.md)
