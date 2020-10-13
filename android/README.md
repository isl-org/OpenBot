# Android App

<p align="center">
  <span>English</span> |
  <a href="README_CN.md">简体中文</a>
</p>

Our application is derived from the [TensorFlow Lite Object Detection Android Demo](https://github.com/tensorflow/examples/tree/master/lite/examples/object_detection/android). We add a data logger and support for game controllers to collect datasets with the robot. Currently, we record readings from following sensors: camera, gyroscope, accelerometer, magnetometer, ambient light sensor, and barometer. Using the Android API, we are able to obtain the following sensor readings: RGB images, angular speed, linear acceleration, gravity, magnetic field strength, light intensity, atmospheric pressure, latitude, longitude, altitude, bearing, and speed. In addition to the phone sensors, we also record body sensor readings (wheel odometry, obstacle distance and battery voltage), which are transmitted via the serial link. Lastly, we record control commands received from a connected game controller, if present. We also integrate several neural networks for person following and autonomous navigation.

## DISCLAIMERS

1. **Safety:** Always make sure you operate in a safe environment. Keep in mind, that your phone could be damaged in a collision! Special care is neccessary when using automated control (e.g. person following or driving policy). Make sure you always have a game controller connected and are familiar with the key mapping so you can stop the vehicle at any time. Use at your own risk!
2. **App under development:** The application is under development and may crash or exhibit unexpected behaviour depending on your phone model and version of the operating system. Make sure to test all functionalities with no wheels connected. Use at your own risk!

## Getting Started

### Prerequisites

- [Android Studio 3.2 or later](https://developer.android.com/studio/index.html) for building and installing the apk, or otherwise download the apk from the assets of the [latest release](https://github.com/intel-isl/OpenBot/releases/latest).
- Android device and Android development environment with minimum API 21

### Building

- In case you are using the apk from the assets of the [latest release](https://github.com/intel-isl/OpenBot/releases/latest), you can skip the below steps and instead just [install](https://www.lifewire.com/install-apk-on-android-4177185) it on your phone directly. Note that that apk is signed with a debug key.
- Open Android Studio and select *Open an existing Android Studio project*.
- Select the OpenBot/android directory and click OK.
- Confirm Gradle Sync if neccessary.
- Connect your Android device and make sure USB Debugging in the [developer options](https://developer.android.com/studio/debug/dev-options) is enabled. Depending on your development environment [further steps](https://developer.android.com/studio/run/device) might be necessary.
- Click the Run button (the green arrow) or select Run > Run 'android' from the top menu. You may need to rebuild the project using Build > Rebuild Project.
- If it asks you to use Instant Run, click *Proceed Without Instant Run*.

### Code Structure

The [TensorFlow Lite Object Detection Android Demo](https://github.com/tensorflow/examples/tree/master/lite/examples/object_detection/android) was used as starting point to integrate TFLite models and obtain the camera feed. The main activity is the [NetworkActivity](app/src/main/java/org/openbot/NetworkActivity.java) which runs the main thread. It inherits from the [CameraActivity](app/src/main/java/org/openbot/CameraActivity.java) which manages the camera and UI. The [SensorService](app/src/main/java/org/openbot/SensorService.java) reads all other phone sensors and logs them. The [env](app/src/main/java/org/openbot/env) folder contains utility classes such as the [GameController](app/src/main/java/org/openbot/env/GameController.java) interface and an [AudioPlayer](app/src/main/java/org/openbot/env/AudioPlayer.java) for the audible feedback. The [tflite](app/src/main/java/org/openbot/tflite) folder contains the model definitions for the [Autopilot](app/src/main/java/org/openbot/tflite/Autopilot.java) and [Detector](app/src/main/java/org/openbot/tflite/Detector.java) networks.

## How to Use the App

<p align="center">
  <img src="../docs/images/app_gui.jpg" alt="App GUI" width="100%"/>
</p>

### Data Logger

There are four different logging modes:

- **only_sensors**: All sensor data but no images are saved.
- **crop_img**: All sensor data and a cropped images that have the input size of the network are saved. This is the default setting and is what should be used for data collection.
- **preview_img**: All sensor data and a full-size images are saved. This will require a lot of memory and can be slow. However, it is nice for compiling FPV videos.
- **all_imgs**: All sensor data and both cropped and full-size images are saved. This will require a lot of memory and can be slow.

The switch on the right is used to toggle logging on and off. On the game controller this switch can be toggled with the X button. 

### USB Connection

The drop-down menu is used to set the baud rate. The default is 115200 and you should not need to change this unless you mess with the Arduino firmware. The app will attempt to connect automatically, but in case you encounter issues you can use this switch to disconnect/connect.

### Drive Mode

There are three different drive modes when using a game controller (e.g. PS4):

- **Game Mode**: Use the right and left shoulder triggers (R2, L2) for forward and reverse throttle and either joystick for steering. This mode imitates the control mode of car racing video games. 
- **Joystick**: Use either one of the joysticks to control the robot.
- **Dual**: Use the left and right joystick to control the left and right side of the car. This is raw differential steering. 

The switch on the right is used to toggle the control between a game controller and the network. On the game controller this switch can be toggled with the R1 trigger button. 

### Vehicle Control

There are three different speeds:

- **Slow**: The voltage applied to the motors is limited to 50% of the input voltage (~6V).
- **Normal**: The voltage applied to the motors is limited to 75% of the input voltage (~9V).
- **Fast**: There is no limit. The full input voltage will be applied to the motors at full throttle (~12V). *This is the default setting for running the neural networks.*

Running at higher speeds will reduce the lifetime of the motors but is more fun. The controls received from a connected game controller or predicted by the network are displayed on the right side.

### Model

There are three models that come with the app:

- **DETECTOR_V1_1_0_Q**: This model is used for person following. It uses a SSD object detector with MobileNet V1 backbone. The model is quantized for better performance on embedded devices.
- **DETECTOR_V3_S_Q**: This model is used for person following. It uses a SSD object detector with MobileNet V3 backbone. The model is quantized for better performance on embedded devices.
- **AUTOPILOT_F**: This model is used for autonomous navigation. It will predict controls directly from the camera input. Chances are that it will not work in your environment. You should follow our instructions to train your own [Driving Policy](../policy) and replace it.

If a model is active, the inference speed in [ms] will be displayed on the right side.

### Device

Use the drop-down menu to select the device on which the neural network should be executed. You have the following choices:

- **CPU**: Using the CPU works on most phones and is the default choice. You can adjust the number of threads to optimize performance. 
- **GPU**: Most smartphones have a GPU. Networks with large inputs such as images often run faster on a GPU.
- **NNAPI**: This will use the [TensorFlow Lite NNAPI delegate](https://www.tensorflow.org/lite/performance/nnapi). Modern smartphones often come with dedicated AI accelerators. The [Neural Network API](https://developer.android.com/ndk/guides/neuralnetworks) (NNAPI) provides acceleration for TensorFlow Lite models on Android devices with Graphics Processing Unit (GPU), Digital Signal Processor (DSP) and Neural Processing Unit (NPU). Note that on some older phones this can be very slow!

## Next (optional)

Train your own [Driving Policy](../policy/README.md)
