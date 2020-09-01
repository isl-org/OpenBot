# 安卓 App

我们的应用程序派生自[TensorFlow Lite object Detection Android Demo](https://github.com/tensorflow/examples/tree/master/lite/examples/object_detection/android)。我们增加了一个数据记录器，并支持游戏控制器与机器人一起收集数据集。目前，我们记录以下传感器的读数：相机、陀螺仪、加速计、磁力计、环境光传感器和气压计。使用Android API，我们可以获得以下传感器读数：RGB图像、角速度、线加速度、重力、磁场强度、光强、大气压力、纬度、经度、海拔、方位和速度。除了手机传感器，我们还记录车身传感器读数(车轮里程计、障碍物距离和电池电压)，这些读数通过串口传输。最后，我们记录从连接的游戏控制器接收道德命令(如果存在)。我们还集成了多个神经网络用于人员跟踪和自主导航。

## 开始

### 先决条件

-设置[Android Studio](https://developer.android.com/studio/index.html)。
-Android设备和Android开发环境，最低API版本21。
-Android Studio 3.2或更高版本

### 构建
-打开Android Studio，选择[打开已有的Android Studio项目]。
-选择OpenBot/Android目录，点击OK。
-如有必要，确认Gradle Sync。
-连接您的安卓设备，确保在[开发者选项](https://developer.android.com/studio/debug/dev-options)]中开启usb调试。根据您的开发环境[可能需要更多步骤](https://developer.android.com/studio/run/device)。
-单击Run按钮(绿色箭头)或从顶部菜单中选择Run>Run‘Android’。您可能需要使用“Build”>“Rebuild Project”重新构建项目。
-如果系统要求您使用即时运行，请单击 *Proceed Without Instant Run*.。

### 代码结构
Tensorlfow Lite目标检测示例程序 [TensorFlow Lite Object Detection Android Demo](https://github.com/tensorflow/examples/tree/master/lite/examples/object_detection/android) 作为集成TFLite模型并获取相机源的起点。 Main activity 是运行主线程的[NetworkActivity](app/src/main/java/org/openbot/NetworkActivity.java)。它继承自管理摄像机和UI的[CameraActivity](app/src/main/java/org/openbot/CameraActivity.java)。[SensorService](app/src/main/java/org/openbot/SensorService.java)读取所有其他电话传感器并记录它们。[env](app/src/main/java/org/openbot/env)文件夹包含实用程序类，如[GameController](app/src/main/java/org/openbot/env/GameController.java)接口和用于声音反馈的[AudioPlayer](app/src/main/java/org/openbot/env/AudioPlayer.java)。[tflite](app/src/main/java/org/openbot/tflite)文件夹包含[Autopilot](app/src/main/java/org/openbot/tflite/Autopilot.java)和[Detector](app/src/main/java/org/openbot/tflite/Detector.java)网络的模型定义。

## 如何使用应用程序
即将到来...
<p align="center">
  <img src="../docs/images/app_teaser.jpg" alt="App Teaser" width="100%"/>
</p>