# OpenBot 控制器

<p align="center">
  <a href="README.md">English</a> |
  <span>简体中文</span> |
  <a href="README.de-DE.md">Deutsch</a> |
  <a href="README.fr-FR.md">Français</a> |
  <a href="README.es-ES.md">Español</a>
</p>

有几种方法可以控制 OpenBot 机器人。

## 蓝牙控制器

控制机器人最简单的方法是通过蓝牙连接游戏控制器。大多数蓝牙游戏控制器都应该可以使用。我们已经测试了[PS4 控制器](https://www.amazon.de/-/en/Sony-Dualshock-Gamepad-Playstation-Black/dp/B01LYWPQUN)、[XBox 控制器](https://www.amazon.de/-/en/QAT-00002/dp/B07SDFLVKD)和一些第三方控制器，例如[X3](https://www.amazon.com/Controller-Wireless-Joystick-Bluetooth-Android/dp/B08H5MM64P)。

## [Node.js 控制器](node-js)

此控制器要求您的手机和计算机（例如笔记本电脑、树莓派）连接到同一个 WiFi 网络。成功连接后，您将获得实时低延迟视频流，并可以使用键盘从浏览器控制机器人。

特别感谢 Ivo Zivkov [izivkov@gmail.com](mailto:izivkov@gmail.com) 的开发。

## [Python 控制器](python)

此控制器要求您的手机和计算机（例如笔记本电脑、树莓派）连接到同一个 WiFi 网络。成功连接后，您将获得实时 RTSP 视频流，并可以使用键盘从终端控制机器人。您可以使用 Python 脚本作为模板来开发自己的控制器。

特别感谢 Ivo Zivkov [izivkov@gmail.com](mailto:izivkov@gmail.com) 的开发。