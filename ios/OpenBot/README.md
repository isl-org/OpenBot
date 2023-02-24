# Robot iOS App - Beta Release

## DISCLAIMERS

1. **Safety:** Always make sure you operate in a safe environment. Keep in mind, that your phone could be damaged in a collision! Special
   care is necessary when using automated control (e.g. person following or driving policy). Make sure you always have a game controller connected and are familiar with the key mapping so you can stop the vehicle at any time. Use at your own risk!
2. **App under development:** The application is under development and may crash or exhibit unexpected behaviour depending on your phone model and version of the operating system. Make sure to test all functionalities with no wheels connected. Use at your own risk!

## App Screens

### Main Menu

The app starts with a menu screen that shows all available screens. The Bluetooth connection screen can be opened by clicking on the Bluetooth icon on top right hand side. The settings screen can be opened with a click on the settings icon right next to it. By clicking on the other icons, the user can access various screens whose functionalities are explained in subsequent sections.

<p align="left">
<img style="padding-right: 2%;" src="../../docs/images/ios_main_screen.jpeg" alt="Main Menu" width="25%"/>
<img style="padding-right: 2%;" src="../../docs/images/ios_bluetooth_screen.jpeg" alt="Bluetooth" width="25%"/>
<img style="padding-right: 2%;" src="../../docs/images/ios_settings_screen.jpeg" alt="Settings" width="25%"/>
</p>

#### Bluetooth Connection

Unlike the Android app, which allows connecting the smartphone to the low-level control board of an OpenBot via a USB cable, the iOS app relies solely on a Bluetooth Low-Energy (BLE) wireless connection. When opening the Bluetooth connection screen in the iOS application (by clicking on the bluetooth logo from the main screen or from any fragment), a list of all compatible devices is displayed. Compatibility is here enforced by using a range of specific UUIDs assigned to an OpenBot vehicle at both the [app](https://github.com/3dwesupport/OpenBot/blob/090dcb28206195a7ee45a13b8ded968a8d365abe/ios/OpenBot/OpenBot/Utils/Constants.swift#L57) and [firmware](https://github.com/3dwesupport/OpenBot/blob/090dcb28206195a7ee45a13b8ded968a8d365abe/firmware/openbot_nano/openbot_nano.ino#L115) levels. You must ensure that these UUIDs match. Pairing an iOS device to an OpenBot vehicle then simply requires to select that vehicle from the list and press the "Connect" button. The default baud rate for the connection is set to 115200 and can be changed at the app and firmware level.

<p align="left">
<img src="../../docs/images/ios_ble.gif" alt="BLE connection" width="25%" />
</p>

### Free Roam

Free Roam offers simple robot control with real time updates and information about battery, speed and distance from surfaces. It also offers controls related to controller, drive mode and speed.

<p align="left">
<img src="../../docs/images/ios_free_roam_screen.jpeg" alt="Alt text" width="50%" />
</p>

- **Battery**: The battery icon shows realtime battery levels of the connected robot.

- **Drive Mode**: There are 3 drive modes displayed on the view:

    - D -> Drive, when the robot is driving forward

    - N -> Neutral, when the robot is stationary

    - R -> Reverse, when the robot is moving backwards

- **Speed**: The speedometer shows the realtime speed of the robot.

- **Sonar**: The sonar view distance of robot from an oncoming object in cm.

- **Bluetooth**: Shows the status of bluetooth connection with the microcontroller. on tapping the icon, the user can also be redirected to the Bluetooth screen to view/modify the connection.

#### Control

The first button is for selecting the **control mode**. There are two different control modes:

- **Gamepad**: The app receives controls from a connected BT controller.
- **Phone (Coming Soon)**:  The robot can be controlled via another smartphone with the controller app installed or though a Python script running on a computer connected to the same network.

The second button is for selecting the **drive mode**. There are three different drive modes when using a game controller (e.g. PS4):

- **Game**: Use the right and left shoulder triggers (R2, L2) for forward and reverse throttle and either joystick for steering. This mode imitates the control mode of car racing video games.
- **Joystick**: Use either one of the joysticks to control the robot.
- **Dual**: Use the left and right joystick to control the left and right side of the car. This is raw differential steering.

The third button is for selecting the **speed mode**. There are three different speed modes:

- **Slow**: The voltage applied to the motors is limited to 50% of the input voltage (~6V).
- **Normal**: The voltage applied to the motors is limited to 75% of the input voltage (~9V).
- **Fast**: There is no limit. The full input voltage will be applied to the motors at full throttle (~12V). *This is the default setting for running the neural networks.*

Running at higher speeds will reduce the lifetime of the motors but is more fun. The controls that are sent to the robot are displayed on the right side. When using the game controller, the speed mode can be increased by pressing down the right joystick (R3) and decrased by pressing down the left joystick (L3).

[//]: # (#### Data Log)

[//]: # ()
[//]: # (There are four different logging modes:)

[//]: # ()
[//]: # (- **only_sensors**: All sensor data but no images are saved.)

[//]: # (- **crop_img**: All sensor data and a cropped images that have the input size of the network are saved. This is the default setting and is what should be used for data collection.)

[//]: # (- **preview_img**: All sensor data and a full-size images are saved. This will require a lot of memory and can be slow. However, it is nice for compiling FPV videos.)

[//]: # (- **all_imgs**: All sensor data and both cropped and full-size images are saved. This will require a lot of memory and can be slow.)

[//]: # ()
[//]: # (The switch on the right is used to toggle logging on and off. On the game controller this switch can be toggled with the X button.)

[//]: # ()
[//]: # (#### Camera)

[//]: # ()
[//]: # (The first item shows the preview resolution. The second item shows the crop resolution. This is the image that is used as input to the neural networks. You will notice that this resolution changes depending on which model you select below. If you train your own autopilot, make sure to select the `AUTOPILOT_F` model. The crop resolution should show `256x96`. The switch on the right is used to toggle between the rear and the front camera.)

[//]: # ()
[//]: # (#### Model)

[//]: # ()
[//]: # (There are two models that come with the app:)

[//]: # ()
[//]: # (- **MobileNetV1-300**: This model is used for person following. It uses a SSD object detector with MobileNet V1 backbone. The model is quantized for better performance on embedded devices. It comes with the app.)

[//]: # (- **CIL-Mobile**: This model is used for autonomous navigation. It will predict controls directly from the camera input. Chances are that it will not work in your environment. You should follow our instructions to train your own [Driving Policy]&#40;../../policy&#41; and replace it.)

[//]: # ()
[//]: # (Additonal models can be downloaded from the Model Management screen.)

[//]: # ()
[//]: # (The switch on the right is used to turn the network on and off. When the network is running, it produces the controls for the robot and the game controller is disabled. However, you may still use the buttons on the game controller, for example to toggle this switch with the R1 trigger button to regain control of the robot.)

[//]: # ()
[//]: # (#### Device)

[//]: # ()
[//]: # (Use the drop-down menu to select the device on which the neural network should be executed. You have the following choices:)

[//]: # ()
[//]: # (- **CPU**: Using the CPU works on most phones and is the default choice. You can adjust the number of threads to optimize performance.)

[//]: # (- **GPU**: Most smartphones have a GPU. Networks with large inputs such as images often run faster on a GPU.)

[//]: # (- **NNAPI**: This will use the [TensorFlow Lite NNAPI delegate]&#40;https://www.tensorflow.org/lite/performance/nnapi&#41;. Modern smartphones often come with dedicated AI accelerators. The [Neural Network API]&#40;https://developer.android.com/ndk/guides/neuralnetworks&#41; &#40;NNAPI&#41; provides acceleration for TensorFlow Lite models on Android devices with Graphics Processing Unit &#40;GPU&#41;, Digital Signal Processor &#40;DSP&#41; and Neural Processing Unit &#40;NPU&#41;. Note that on some older phones this can be very slow!)

[//]: # ()
[//]: # (If a model is active, the inference speed in [ms] will be displayed next to the device which is running the model.)

### Data Collection


Simple UI for collection of data sets.

<p align="left">

<img src="../../docs/images/ios_data_collection_screen.jpg" alt="Data Collection" width="50%" />

</p>


- **Preview Resolution**: Used to switch between resolutions of camera preview. There are 3 settings:

    - ***HIGH*** (1920x1080p)

    - ***MEDIUM*** (1280x720p)

    - ***LOW*** (640x360)


- **Model Resolution**: Used to switch between resolutions of images saved for training different models.

- **Log Collected Data**: the data collection process can be controlled from the screen or remotely, for instance from a bluetooth controller. When using a bluetooth controller, you may:

    - press the **A button** to **start** the data collection process

    - press the **A button again** to **stop** data collection and save the collected data in a .zip file

    - alternatively press the **R1 button** to **stop** data collection **without saving** the collected data (for instance because of an unexpected collision with the environment)

    - remember to use the controller mapping fragment to ensure you are using the correct buttons.

- **Vehicle Status**: The field **Battery** displays the battery voltage as measured by the microcontroller via the voltage divider. The field **Speed (l,r)** reports the left and right speed of the (front) wheels in rpm. It is measured by the microcontroller via the optical wheel speed sensors. The field **Sonar** shows the free space in front of the car in centimeters. It is measured by the microcontroller via the ultrasonic sensor. Note, you will only receive values a few seconds after the USB connections has been established.

- **Sensors**: Reports measurements from vehicle sensors. Currently, we record readings from following sensors: camera, gyroscope, accelerometer, magnetometer, ambient light sensor, and barometer. Using the iOS API, we are able to obtain the following sensor readings: RGB images, angular speed, linear acceleration, gravity, magnetic field strength, light intensity, atmospheric pressure, latitude, longitude, altitude, bearing, and speed. In addition to the phone sensors, we record body sensor readings (wheel odometry, obstacle distance and battery voltage), which are transmitted via the serial link. We also record and timestamp control signals received from a connected controller, if present. Lastly, we integrate several neural networks for person following and autonomous navigation.

### Controller Mapping

Simple UI to check the button and joystick mapping of a connected BT controller.

<p align="left">
<img src="../../docs/images/ios_controller_mapping.jpeg" alt="Controller mapping" width="30%" />
</p>

### Robot Info

Simple UI to get robot info and test basic functionality. The **Robot Type** as configured in the firmware is displayed as text and animation. The checkmarks in the sections **Sensors**, **Wheel Odometry** and **LEDs** show which features are supported by the connected robot. The section **Readings** provides the most important sensor measurements. In the section **Send Commands**, users can send basic motor commands by pressing the corresponding buttons and control the front and rear LEDs with a slider. 

<p align="left">
<img src="../../docs/images/ios_screen_robot_info.gif" alt="Robot Info" width="50%" />
</p>

### Autopilot


Simple UI for running autopilot models.


<p align="left">

<img src="../../docs/images/ios_autopilot_screen.jpeg" alt="Autopilot" width="50%" />

</p>


### Object Tracking


Simple UI for tracking objects of 80 different classes. A short description of the different AI models for object tracking and performance benchmarks can be found in [Model Management](#model-management).


<p align="left">
<img src="../../docs/images/ios_object_tracking_screen.jpeg" alt="Object Tracking" width="50%" />
</p>


### Model Management

All models are quantized for better performance on embedded devices. Note that models with larger input resolution might be better for smaller objects despite lower mAP.

<p align="left">
<img src="../../docs/images/ios_screen_model_management.gif" alt="Model Management" width="25%" />
</p>



[//]: # ()
[//]: # (### Point Goal Navigation)

[//]: # ()
[//]: # (Note that this fragment requires ARCore and camera permission. If your device does not support ARCore and you continue anyways, the app will crash. In this screen you can specify a goal via a 2D vector with respect to the current position and orientation of the robot. The 2D vector contains the distance to the front and left of the robot in meters. Both values can also be negative and correspond to back and right of the robot in that case. After specifying the goal and pressing `Start` the robot will exectue an AI policy that attempts to reach the goal while avoiding obstacles.)

[//]: # ()
[//]: # (<p align="left">)

[//]: # (<img src="../../../docs/images/screen_point_goal_nav.gif" alt="Alt text" width="50%" />)

[//]: # (</p>)

[//]: # ()
[//]: # (### Model Management)

[//]: # ()
[//]: # (All models are quantized for better performance on embedded devices. Please refer to the tables below for a short description of the available models and benchmarking results. The [mean Average Precision &#40;mAP&#41;]&#40;https://kharshit.github.io/blog/2019/09/20/evaluation-metrics-for-object-detection-and-segmentation&#41; is computed on the validation set of the [COCO Detection 2017]&#40;https://cocodataset.org/#detection-2017&#41; dataset. The runtime is averaged across 100 frames and reported in frames per second &#40;fps&#41;.)

[//]: # ()
[//]: # (<p align="left">)

[//]: # (<img src="../../../docs/images/screen_model_management.jpg" alt="Alt text" width="25%" />)

[//]: # (</p>)

[//]: # ()
[//]: # (#### MobileNetV1-300 &#40;pre-installed&#41; - mAP: 18%)

[//]: # ()
[//]: # (SSD object detector with MobileNet V3 backbone and input resolution of 300x300.)

[//]: # ()
[//]: # (|phone/device &#40;fps&#41;| CPU | GPU | NNAPI |)

[//]: # (|------------------|-----|-----|-------|)

[//]: # (| Samsung S20FE    |  34 |  57 |   87  |)

[//]: # (| Huawei P30 Pro   |  36 |  25 |   10  |)

[//]: # (| Google Pixel 6XL |  35 |  42 |   53  |)

[//]: # (| Xiaomi Mi9       |  22 |  41 |   33  |)

[//]: # ()
[//]: # (#### MobileNetV3-320 - mAP: 16%)

[//]: # ()
[//]: # (SSD object detector with MobileNet V3 backbone and input resolution of 320x320.)

[//]: # ()
[//]: # (|phone/device &#40;fps&#41;| CPU | GPU | NNAPI |)

[//]: # (|------------------|-----|-----|-------|)

[//]: # (| Samsung S20FE    |  34 |  42 |   28  |)

[//]: # (| Huawei P30 Pro   |  32 |  27 |   23  |)

[//]: # (| Google Pixel 6XL |  33 |  43 |   27  |)

[//]: # (| Xiaomi Mi9       |  20 |  45 |   10  |)

[//]: # ()
[//]: # (#### YoloV4-224 - mAP: mAP: 40.40%)

[//]: # ()
[//]: # (State-of-the-art object detector [YoloV4]&#40;https://arxiv.org/abs/2004.10934&#41; with input resolution of 224x224.)

[//]: # ()
[//]: # (|phone/device &#40;fps&#41;| CPU | GPU | NNAPI |)

[//]: # (|------------------|-----|-----|-------|)

[//]: # (| Samsung S20FE    | 3.1 | 7.1 |  4.2  |)

[//]: # (| Huawei P30 Pro   | 2.4 | 6.2 |  0.7  |)

[//]: # (| Google Pixel 6XL | 2.7 |  11 |  0.9  |)

[//]: # (| Xiaomi Mi9       | 2.1 | 6.4 |  1.7  |)

[//]: # ()
[//]: # (#### YoloV4-tiny-224 - mAP: 22.05%)

[//]: # ()
[//]: # (Tiny version of YoloV4 with input resolution of 224x224.)

[//]: # ()
[//]: # (|phone/device &#40;fps&#41;| CPU | GPU | NNAPI |)

[//]: # (|------------------|-----|-----|-------|)

[//]: # (| Samsung S20FE    |  30 |  21 |   14  |)

[//]: # (| Huawei P30 Pro   |  27 |  17 |   22  |)

[//]: # (| Google Pixel 6XL |  29 |  24 |   19  |)

[//]: # (| Xiaomi Mi9       |  16 |  14 |  9.3  |)

[//]: # ()
[//]: # (#### YoloV4-tiny-416 - mAP: 29.42%)

[//]: # ()
[//]: # (Tiny version of YoloV4 with input resolution of 416x416.)

[//]: # ()
[//]: # (|phone/device &#40;fps&#41;| CPU | GPU | NNAPI |)

[//]: # (|------------------|-----|-----|-------|)

[//]: # (| Samsung S20FE    |  12 | 9.4 |  7.7  |)

[//]: # (| Huawei P30 Pro   | 8.4 | 7.6 |  6.9  |)

[//]: # (| Google Pixel 6XL |  10 | 9.6 |  7.2  |)

[//]: # (| Xiaomi Mi9       | 9.0 | 7.3 |  5.0  |)

[//]: # ()
[//]: # (## Add your own fragment)

[//]: # ()
[//]: # (Please refer to the [ContributionGuide]&#40;ContributionGuide.md&#41; to learn how to add your own fragments to the OpenBot app.)

[//]: # ()
[//]: # (## Code Structure)

[//]: # ()
[//]: # (The [TensorFlow Lite Object Detection Android Demo]&#40;https://github.com/tensorflow/examples/tree/master/lite/examples/object_detection/android&#41; was used as starting point to integrate TFLite models and obtain the camera feed. The [DefaultActivity]&#40;src/main/java/org/openbot/robot/DefaultActivity.java&#41; runs the main thread and inherits from the [CameraActivity]&#40;src/main/java/org/openbot/robot/CameraActivity.java&#41; to manage the camera and UI. The [SensorService]&#40;src/main/java/org/openbot/robot/SensorService.java&#41; reads all other phone sensors and logs them. The [ServerService]&#40;src/main/java/org/openbot/robot/ServerService.java&#41; and [NsdService]&#40;src/main/java/org/openbot/robot/NsdService.java&#41; establish a connection to a local [Python server]&#40;../../policy/README.md#web-app&#41; with a React frontend. If you collect data it can be uploaded automatically for visualization, training ML models and downloading trained models to the robot. The [env]&#40;src/main/java/org/openbot/env&#41; folder contains utility classes such as the [Vehicle]&#40;src/main/java/org/openbot/env/Vehicle.java&#41; interface, [GameController]&#40;src/main/java/org/openbot/env/GameController.java&#41; interface, [PhoneController]&#40;src/main/java/org/openbot/env/PhoneController.java&#41; interface and an [AudioPlayer]&#40;src/main/java/org/openbot/env/AudioPlayer.java&#41; for the audible feedback. The [tflite]&#40;src/main/java/org/openbot/tflite&#41; folder contains the model definitions for the [Autopilot]&#40;src/main/java/org/openbot/tflite/Autopilot.java&#41; and [Detector]&#40;src/main/java/org/openbot/tflite/Detector.java&#41; networks.)

[//]: # ()
[//]: # (## Next &#40;optional&#41;)

[//]: # ()
[//]: # (Train your own [Driving Policy]&#40;../../policy/README.md&#41;)
