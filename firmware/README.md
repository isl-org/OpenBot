# Firmware

<p align="center">
  <span>English</span> |
  <a href="README_CN.md">简体中文</a>
</p>

We use a microcontroller unit (MCU) to act as a bridge between the robot body and the smartphone.  We provide our [firmware](openbot_v1_nano/openbot_v1_nano.ino) for the Arduino Nano with an ATmega328P microcontroller.

## Features

The main task of the MCU is to handle the low-level control of the vehicle and provide readings from low-level vehicle-mounted sensors. The MCU receives the vehicle controls and indicator signals via the serial connection. It converts the controls to PWM signals for the motor controller and toggles the LEDs according to the indicator signal. The Arduino program also keeps track of the wheel rotations by counting the interrupts of optical sensors on the left and right front wheels. It calculates the battery voltage by a scaled moving average of measurements at the voltage divider circuit. It can also measure the distance to obstacles in front of the car with an optional ultrasonic sensor. These measurements are sent back to the Android application through the serial link.

## Setup

First you have to set up your hardware configuration at the beginning of the code. If you did the DIY build (using the L298N motor driver), set `OPENBOT DIY`.
If you used the custom PCB, check the version and set either `OPENBOT PCB_V1` or `OPENBOT PCB_V2`.

Next, you need to configure which features you want to enable. Disabled features are not compiled to save memory and make the code faster.

- Enable the voltage divider by setting `HAS_VOLTAGE_DIVIDER 1` (default: 0)
- Enable the indicator LEDs by setting `HAS_INDICATORS 1` (default: 0)
- Enable the speed sensors by setting `HAS_SPEED_SENSORS 1` (default: 0)
- Enable the ultrasonic sensor by setting `HAS_SONAR 1` (default: 0)
- Enable the median filter for sonar measurements by setting `USE_MEDIAN 1` (default: 0)
- Enable the OLED display by setting `HAS_OLED 1` (default: 0)

### Dependencies

If you have enabled the speed sensors or the ultrasonic sensor, you need to install the [PinChangeInterrupt](https://github.com/NicoHood/PinChangeInterrupt) library. The Arduino Nano only has two external interrupt pins (D2 and D3) and D3 is also one of only six pins that support PWM. Fortunately, it also has three port interrupts that cover all pins on the Arduino. This library parses these port interrupts allowing all pins of the Arduino to be used as interrupts.

If you have enabled the OLED, you need to install the libraries [Adafruit_SSD1306](https://github.com/adafruit/Adafruit_SSD1306) and [Adafruit_GFX Library](https://github.com/adafruit/Adafruit-GFX-Library).

You can install libraries by following these steps:
1. Open the Library Manager: `Tools` :arrow_right: `Manage Libraries`
2. Enter the name of the library in the search bar.
3. Select the latest version and click install. If you have already installed the library it will show and you may be able to update it.

<p float="left">
  <img src="../docs/images/manage_libraries.jpg" height="300" />
  <img src="../docs/images/install_library.jpg" height="300" /> 
</p>

### Chinese clone Nano (e.g. US link)

You may need to download the [WCH340](http://www.wch.cn/product/CH340.html) drivers from the chip manufacturer (Chinese):

- [Windows](http://www.wch.cn/downloads/CH341SER_EXE.html)
- [Linux](http://www.wch.cn/download/CH341SER_LINUX_ZIP.html)
- [Mac](http://www.wch.cn/download/CH341SER_MAC_ZIP.html)

## Upload

### Settings

- `Tools` :arrow_right: `Board` :arrow_right: `Arduino AVR Boards` :arrow_right: `Arduino Nano`
- `Tools` :arrow_right: `Processor` :arrow_right: `ATmega328P (Old Bootloader)`
- `Tools` :arrow_right: `Port` :arrow_right: `*Select the USB port*`

The firmware can now be uploaded through `Sketch` :arrow_right: `Upload` or by pressing the upload button (right arrow).
![Firmware Upload](../docs/images/firmware_upload.png)

:memo: NOTE: Currently, most cheap Arduino Nano boards come with the *Old Bootloader*. However, depending on the seller you may also get one with the new bootloader. So if you are unable to upload the firmware, chances are that you need to change the processor to *ATmega328P*.

### Testing

This section explains how to test all functionalities of the car after the firmware was flashed successfully.

1. Confirm that:
    1. the wheels are not connected to the car
    2. the Arduino is connected to the computer
    3. the correct USB port is selected
2. Open the Serial Monitor: `Tools` :arrow_right: `Serial Monitor`

#### Vehicle Status

You should now see four comma-seperated values that update once per second:

![Serial Monitor](../docs/images/serial_monitor.png)

- The first value is the battery voltage. If you connect the battery to the car (i.e. turn on the switch), it should show the battery voltage. If you disconnect the battery (i.e. turn off the switch), it should show a small value.
- The second and third values are the raw readings of the speed sensors. Each hole in the encoder disk will increment the counter by plus/minus one depending on the direction. You can set the number of holes with the parameter `DISK_HOLES`. If you are using the stardard disk with 20 holes, there will be 20 counts for each revolution of the wheel. Hence, if you divide the displayed number by 20, you will get the revolutions per second.
- The fourth value is the estimated free space in front of the ultrasonic sensor in cm. If the ultrasonic sensor is disabled or unable to get a reading, it will show `65535`.

#### Vehicle Control

You can also send messages to the Arudino by typing a command into the input field on the top and then pressing send. The following commands are available:

- `c<left>,<right>` where `<left>` and `<right>` are both in the range [-255,255]. A value of `0` will stop the motors. A value of `255` applies the maximum voltage driving the motors at the full speed forward. Lower values lead to proportionlly lower voltages and speeds. Negative values apply the corresponding voltages in reverse polarity driving the motors in reverse.
- `i<cmd>` where `<cmd>` is in the range [-1,1]. A value of `-1` turns on the left indicator LED and a value of `1` turns on the right indicator LED. A value of `0` turns the indicator LEDs off. Only one state at a time is possible.

#### Test procedure

Before you proceed, make sure the tires are removed. You will need the Serial Monitor open to send commands and you will see the messages received from your OpenBot. If you have the OLED display installed, you will also see the vehicle status displayed there in a more human-readable format. The following test procedure can be used to test all functionalities of the car:

1. Turn on the car and observe the battery voltage (first value). You can verify the reading with a multimeter and adjust the `VOLTAGE_DIVIDER_FACTOR` if neccessary.
2. If you have a ultrasonic sensor installed:
    1. Hold your hand in front of the sensor and move it back and forth. You should see the readings (fourth value) change correspondingly.
    2. We have observed that the ultrasonic sensor is very sensitive to vibrations! So it is advisable to make sure you will get reliable readings during operation by performing the following test:
        1. Place the OpenBot with the ultrasonic sensor installed such that there is at least 200cm of free space in front of it. You should see a reading of `200` or more.
        2. Observe the readings on the serial monitor for some time and then enter the command `c128,128`.
        3. If the sensor readings change significantly, you will need to dampen the vibrations transmitted to the ultrasonic sensor from the chassis (e.g. add some silicon, adjust the mounting position).
3. If you have the speed sensors installed:
    1. Make sure, you have plenty of free space in front of the ultrasonic sensor. The reading (fourth value) needs to be at least above the `STOP_THRESHOLD` which is `32` by default. If the ultrasonic sensor is not installed, you should see a reading of `65535`.
    2. Send the command `c128,128`. The motors will start spinning at *slow speed* (50% PWM). The speed sensor readings (second and third value) should be similar to the values in the image above. If you are using the DIY version or a weaker battery, values may be lower. Check that all motors are spinning forward and that the speed sensor readings are positive. Note: If you multiply these values by 3, they correspond to the rpm for the standard disk with 20 holes.
    3. Try sending different controls and observe the speed sensor readings. For example, the command `c-128,-128` will spin all motors backward at *slow speed* (50% PWM). The command `c255,-255` will spin the left motors forward and the right motors backward at *fast speed* (100% PWM). The command `c-192,192` will spin the left motors backward and the right motors forward at *normal speed* (75% PWM).
4. Stop the motors by sending the command `c0,0` or by holding your hand in front of the ultrasonic sensor
5. If you have the indicator LEDs installed, send the command `i-1` and observe the left indicator light flashing. The send the command `i1` and observe the right indicator light flashing. Finally, turn the indicator off by sending the command `i0`.

### No Phone Mode

Before testing the car with a smartphone that has the OpenBot application installed, you can also test the car without a phone first. Simply set the option `NO_PHONE_MODE` to `1`. The car will now drive at *normal_speed* (75% PWM) and slow down as it detects obstacles with the ultrasonic sensor. Once it gets close to the `STOP_THRESHOLD` (default: 32cm), it will start turning in a random direction and turn on the LED on that side. If the estimated free space in front of the car falls below the `STOP_THRESHOLD`, it will slowly go backwards and both LEDs will turn on. Note that both the car and the Arduino need to be powered. The Arduino can be powered by connecting the 5V pin to the 5V output of the L298N motor driver, or by connecting the USB cable to a power source (e.g. phone).

Before running the car, we recommend to remove the tires, connect the Arduino to a computer and observe the serial monitor like in the section [Testing](#testing). The output on the serial monitor is a bit easier to parse (same as OLED) and shows the battery voltage, the rpm for the left and right motors and the estimated free space in front of the car. You can move a large object back and forth in front of ultrasonic sensor and observe the speed of the motors changing.

:warning: WARNING: If you do not have an ultrasonic sensor installed or if it is disabled, the car will just drive forward at *normal_speed* (75% PWM) and will eventually collide. Even with the sensor installed, the car may collide occasionally due to noisy readings.

## Using other MCUs (requirements)

You can use any other MCU with the following features:

- 1x USB-to-TTL Serial (communication with the smartphone)
- 4x PWM output (control the motors)
- 1x analog pin for battery monitoring
- 2x digital pin for the speed sensors
- 1x digital pin for the ultrasonic sensor (optional)
- 2x digital pin for the indicator LEDs (optional)

## Next

Compile and run the [Android App](../android/README.md)
