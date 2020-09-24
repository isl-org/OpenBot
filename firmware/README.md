# Firmware
[简体中文](README_CN.md)

We use a microcontroller unit (MCU) to act as a bridge between the robot body and the smartphone.  We provide our [firmware](openbot_v1_nano/openbot_v1_nano.ino) for the Arduino Nano with an ATmega328P microcontroller. 

## MCU Requirements
You can use any other MCU with the following features: 
- 1x USB-to-TTL Serial (communication with the smartphone)
- 4x PWM output (control the motors)
- 1x analog pin for battery monitoring
- 2x digital pin for the speed sensors
- 1x digital pin for the ultrasonic sensor (optional)
- 2x digital pin for the indicator LEDs (optional)

## Features
The main task of the MCU is to handle the low-level control of the vehicle and provide readings from low-level vehicle-mounted sensors. The MCU receives the vehicle controls and indicator signals via the serial connection. It converts the controls to PWM signals for the motor controller and toggles the LEDs according to the indicator signal. The Arduino program also keeps track of the wheel rotations by counting the interrupts of optical sensors on the left and right front wheels. It calculates the battery voltage by a scaled moving average of measurements at the voltage divider circuit. It can also measure the distance to obstacles in front of the car with an optional ultrasonic sensor. These measurements are sent back to the Android application through the serial link. 

## Dependencies
If you want to use the ultrasonic sensor, you need to install the [NewPing](https://playground.arduino.cc/Code/NewPing) library.
```
Tools
└─── Manage Libraries
```

If you want to disable the ultrasonic sensor, you need to set `HAS_SONAR = 0`

## Chinese clone Nano (e.g. US link)

You may need to download the [WCH340](http://www.wch.cn/product/CH340.html) drivers from the chip manufacturer (Chinese):
- [Windows](http://www.wch.cn/downloads/CH341SER_EXE.html)
- [Linux](http://www.wch.cn/download/CH341SER_LINUX_ZIP.html)
- [Mac](http://www.wch.cn/download/CH341SER_MAC_ZIP.html)
