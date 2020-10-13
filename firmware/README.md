# Firmware

<p align="center">
  <span>English</span> |
  <a href="README_CN.md">简体中文</a>
</p>

We use a microcontroller unit (MCU) to act as a bridge between the robot body and the smartphone.  We provide our [firmware](openbot_v1_nano/openbot_v1_nano.ino) for the Arduino Nano with an ATmega328P microcontroller.

## Features

The main task of the MCU is to handle the low-level control of the vehicle and provide readings from low-level vehicle-mounted sensors. The MCU receives the vehicle controls and indicator signals via the serial connection. It converts the controls to PWM signals for the motor controller and toggles the LEDs according to the indicator signal. The Arduino program also keeps track of the wheel rotations by counting the interrupts of optical sensors on the left and right front wheels. It calculates the battery voltage by a scaled moving average of measurements at the voltage divider circuit. It can also measure the distance to obstacles in front of the car with an optional ultrasonic sensor. These measurements are sent back to the Android application through the serial link.

## Setup

You just have to set up your hardware configuration at the beginning for the code. If you did the DIY build (using the L298N motor driver), set `OPENBOT DIY`.
If you used the custom PCB, check the version and set either `OPENBOT PCB_V1` or `OPENBOT PCB_V2`.

### Dependencies

If you want to use the ultrasonic sensor, you need to install the [NewPing](https://playground.arduino.cc/Code/NewPing) library.

```markdown
Tools
└─── Manage Libraries
```

If you want to enable the ultrasonic sensor, you need to set `HAS_SONAR 1`.

### Chinese clone Nano (e.g. US link)

You may need to download the [WCH340](http://www.wch.cn/product/CH340.html) drivers from the chip manufacturer (Chinese):

- [Windows](http://www.wch.cn/downloads/CH341SER_EXE.html)
- [Linux](http://www.wch.cn/download/CH341SER_LINUX_ZIP.html)
- [Mac](http://www.wch.cn/download/CH341SER_MAC_ZIP.html)

## Upload

### Settings

- `Tools -> Board -> Arduino AVR Boards -> Arduino Nano`
- `Tools -> Processor -> ATmega328P (Old Bootloader)`
- `Tools -> Port -> *Select the USB port*`

The firmware can now be uploaded through `Sketch -> Upload` or by pressing the upload button (right arrow).
![Firmware Upload](../docs/images/firmware_upload.png)

NOTE: Currently, most cheap Arduino Nano boards come with the *Old Bootloader*. However, depending on the seller you may also get one with the new bootloader. So if you are unable to upload the firmware, chances are that you need to change the processor to *ATmega328P*.

### Testing

This section explains how to test all functionalities of the car after the firmware was flashed successfully.

1. Confirm that:
    - the wheels are not connected to the car
    - the Arduino is connected the the computer
    - the correct USB port is selected
2. Open the Serial Monitor: `Tools -> Serial Monitor`

You should now see four comma-seperated values that update once per second:

![Serial Monitor](../docs/images/serial_monitor.png)

- The first value is the battery voltage. If you connect the battery to the car (i.e. turn on the switch), it should show the battery voltage. If you disconnect the battery (i.e. turn off the switch), it should show a small value.
- The second and third values are the raw readings of the speed sensors. Each hole in the encoder disk will produce two counts. You can set the number of holes with the parameter `DISK_HOLES`. If you are using the stardard disk with 20 holes, there will be 40 counts for each revolution of the wheel. Hence, if you divide the displayed number by 40, you will get the revolutions per second.
- The fourth value is the estimated free space in front of the ultrasonic sensor in cm up to the `MAX_DISTANCE` which is `300` by default. If the ultrasonic sensor is disabled, it will show `65535`.

You can also send messages to the Arudino by typing a command into the input field on the top and then pressing send. The following commands are available:

- `c<left>,<right>` where `<left>` and `<right>` are both in the range [-255,255]. A value of `0` will stop the motors. A value of `255` applies the maximum voltage driving the motors at the full speed forward. Lower values lead to proportionlly lower voltages and speeds. Negative values apply the corresponding voltages in reverse polarity driving the motors in reverse.
- `i<cmd>` where `<cmd>` is in the range [-1,1]. A value of `-1` turns on the left indicator LED and a value of `1` turns on the right indicator LED. A value of `0` turns the indicator LEDs off. Only one state at a time is possible.

The following test procedure can be used to test all functionalities of the car:

1. Turn on the car and observe the battery voltage. You can verify the reading with a multimeter and adjust the `VOLTAGE_DIVIDER_FACTOR` if neccessary.
2. If you have a ultrasonic sensor installed:
    a. Hold your hand in front of the sensor and move it back and forth. You should see the readings change correspondingly.
    b. Make sure, there is nothing in front of the ultrasonic sensor before moving to the next step. You should see a reading of `300`. If you do not have enough space, make sure that the reading is at least above the `STOP_THRESHOLD` which is `64` by default.
3. Send the command `c128,128`. The motors will start spinning at *slow speed* (50% PWM). The speed sensor readings should be similar to the values in the image above. If you are using the DIY version or a weaker battery, values may be lower. Check that all motors are spinning forward.
4. Try sending different controls and observe the speed sensor readings. For example, the command `c-128,-128` will spin all motors backward at *slow speed* (50% PWM). The command `c255,-255` will spin the left motors forward and the right motors backward at *fast speed* (100% PWM). The command `c-192,192` will spin the left motors backward and the right motors forward at *normal speed* (75% PWM).
5. Stop the motors by holding your hand in front of the ultrasonic sensor or by sending the command `c0,0`.
6. Send the command `i-1` and observe the left indicator light flashing. The send the command `i1` and observe the right indicator light flashing. Finally, turn the indicator off by sending the command `i0`.

### No Phone Mode

Before testing the car with a smartphone that has the OpenBot application installed, you can also test the car without a phone first. Simply set the option `NO_PHONE_MODE` to `1`. The car will now drive at *normal_speed* (75% PWM) and slow down as it detects obstacles with the ultrasonic sensor. If it gets within the `STOP_THRESHOLD` (default: 64cm), it will turn to the right. Note that both the car and the Arduino need to be powered. The Arduino can be powered by connecting the 5V pin to the 5V output of the L298N motor driver, or by connecting the USB cable to a power source.

Before running the car, we recommend to remove the tires, connect the Arduino to a computer and observe the serial monitor like in the section [Testing](#testing). The output is a bit easier to parse and shows the battery voltage, the rpm for the left and right motors and the estimated free space in front of the car. You can move a large object back and forth in front of ultrasonic sensor and observe the speed of the motors changing.

WARNING: If you do not have an ultrasonic sensor installed or if it is disabled, the car will just drive forward at *normal_speed* (75% PWM) and will collide. Even with the sensor installed, the car may collide occasionally due to noisy readings.

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
