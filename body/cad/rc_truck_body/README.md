# OpenBot: RC-Truck Body

<!---
<p align="center">
  <span>English</span> |
  <a href="README_CN.md">简体中文</a>
</p>
--->

We have designed a robot body for widely available 1:16 scale RC-toy trucks/buggies (such as [this](https://www.amazon.de/dp/B00M3J7DJW) on Amazon).

![RC-Truck-Banner](/docs/images/rc-truck-banner.jpg)

We also have a generic [body](/body/) designed for a simple wheeled robot which relies on low-cost, readily available hobby hardware. Build instructions for the regular OpenBot can be found [here](/body/README.md). 

## Chassis

The chassis of OpenBot RC-Truck consists of two main components: (a) A 1:16 scale RC-toy truck of your choice and (b) a number of custom-designed parts which we provide and can be 3D-printed.

### 1:16 RC-toy truck

To build your own OpenBot RC-Truck, you will need a 1:16 RC-toy truck/buggy. We provide Amazon links to compatible RC-toy trucks for Germany ([EU](https://www.amazon.de/dp/B00M3J7DJW)), ([EU](https://www.amazon.de/dp/B088FGVYNW)), and the United States ([US](https://www.amazon.com/gp/product/B09C8XMPQ9)) with fast shipping. A number of similar 1:16 scale toy trucks can also be found on other online retailers such as ebay, Alibaba, or AliExpress, often for discounted price but with slow shipping speed. 

Irrespective of the retailer and the version of RC-toy truck you choose for your build, please ensure that it is indeed a 1:16 scale RC-truck. This is important since our 3D printed parts are currently designed to fit only 1:16 scale RC-toy trucks with some minor adjustments (more on this topic later on). Some examples of compatible 1:16 scale RC-toy trucks/buggies are shown below.

<p float="left">
  <a href="https://www.amazon.de/dp/B00M3J7DJW" target="_blank"> <img src="/docs/images/rc_toy_1.jpg" width="34%" /> &nbsp
  </a>
  <a href="https://www.amazon.com/gp/product/B09C8XMPQ9" target="_blank"> <img src="/docs/images/rc_toy_2.jpg" width="27%" /> &nbsp &nbsp &nbsp &nbsp
  </a>
  <a href="https://www.amazon.de/dp/B088FGVYNW" target="_blank"> <img src="/docs/images/rc_toy_3.jpg" width="27%" />
  </a>
</p>


### 3D printing

You will need to print the following parts in order to build your OpenBot RC-Truck.

1) main_frame ([STL](/body/cad/rc_truck_body/main_frame.stl), [STEP](/body/cad/rc_truck_body/main_frame.stp))
2) side_cover \[x2\] ([STL](/body/cad/rc_truck_body/side_cover.stl), [STEP](/body/cad/rc_truck_body/side_cover.stp))
3) phone_mount_bottom ([STL](/body/cad/phone_mount/phone_mount_bottom.stl), [STEP](/body/cad/phone_mount/phone_mount_bottom.stp))
4) phone_mount_top ([STL](/body/cad/phone_mount/phone_mount_top.stl), [STEP](/body/cad/phone_mount/phone_mount_top.stp))

Notice that \[xN\] indicates the number of copies (i.e., N) you need to print of a particular part (wherever applicable).

Following parts are optional (but recommended) to make your OpenBot RC-Truck more compact and aesthetically pleasing.

5) camera_elevator ([STL](/body/cad/rc_truck_body/camera_elevator.stl), [STEP](/body/cad/rc_truck_body/camera_elevator.stp))
6) electronics_cover \[x2\] ([STL](/body/cad/rc_truck_body/electronics_cover.stl), [STEP](/body/cad/rc_truck_body/electronics_cover.stp))
7) spacer \[x4\] ([STL](/body/cad/rc_truck_body/spacer.stl), [STEP](/body/cad/rc_truck_body/spacer.stp))
8) front_light_spacer \[x2\] ([STL](/body/cad/rc_truck_body/front_light_spacer.stl), [STEP](/body/cad/rc_truck_body/front_light_spacer.stp))

For all the above parts, your build plate needs to be at least 260mmx220mm, which is the print size of the ```main_frame``` part.

Since a lot of common 3D printers have a smaller build volume (usually 220mmx220mm), there are two more options that can work. 
First option is to print the ```main_frame``` part at 45 degrees with additional support material. 
Second option requires modifying the original ```main_frame``` part file. We recommend using [Autodesk Fusion 360](https://www.autodesk.com/products/fusion-360/overview) for such CAD modifications (Fusion 360 has a free 1-year academic license available). 
For this option, we make its [STEP](/body/cad/rc_truck_body/main_frame.stp) file available, which you can cut into two/three smaller parts. 
The resulting sub-parts will then fit on a standard (i.e., 220mmx220mm) build plate and can be joined together after printing. 
In future, we may also release such a modular version of the ```main_frame``` part here. All other parts require a minimum build plate of 220mmx60mm.

On an Ultimaker S5, we achieved good results with the following settings:

- layer height: 0.2mm
- wall thickness: 1.5mm
- infill density: 20%
- infill pattern: grid
- print speed 80 mm/s
- no support

We were able to print the chassis with PLA, CPE and ABS. In our experience the print was not affected very much by the print settings. However, if you have the patience, printing slower and with smaller layer height will improve the print. Also adding a support structure can improve the print, but it will require additional work to remove afterwards.

Before you proceed with the build, you may need to clean the 3D print. However, using the above settings, we did not need any filing or cleaning during our build process. If possible, we recommend using a combination of two different colors (for example green/black or red/black) for printing different parts of the same OpenBot RC-Truck build as shown below.

<p float="left">
  <img src="/docs/images/3d_print_rc_1.png" width="32%" />
  <img src="/docs/images/3d_print_rc_2.png" width="32%" /> 
  <img src="/docs/images/3d_print_rc_3.png" width="32%" />
</p>


## Assembly

While it is possible to build your OpenBot RC-Truck with a DIY approach similar to the regular OpenBot (please see DIY build components and instructions for OpenBot [here](/body/README.md)), we recommend using the OpenBot [custom PCB](pcb) for building and assembling the OpenBot RC-Truck. This option is recommended if you desire a cleaner build or want to build multiple OpenBot RC-Trucks. An additional advantage of using our [custom PCB](pcb) is that you can use the same components to build and switch between different OpenBot variations.


### Bill of materials

OpenBot RC-Truck body relies on readily available hobby electronics. We provide links for Germany (EU) and the United States (US) with fast shipping. If you have the patience to wait a bit longer, you can also get the components a lot cheaper from AliExpress (AE). You will need the following components.

#### Required components

- 1x RC-toy truck or buggy ([EU](https://www.amazon.de/dp/B00M3J7DJW), [US](https://www.amazon.com/gp/product/B09C8XMPQ9/))
- 1x Arduino Nano ([EU](https://www.amazon.de/dp/B01MS7DUEM), [US](https://www.amazon.com/dp/B00NLAMS9C), [AE](https://www.aliexpress.com/item/32866959979.html))
- 1x [Custom PCB](pcb)
- 1x USB OTG cable ([EU](https://www.amazon.de/gp/product/B075M4CQHZ) ,[US](https://www.amazon.com/dp/B07LBHKTMM), [AE](https://www.aliexpress.com/item/10000330515850.html))
- 1x spring or rubber band ([EU](https://www.amazon.de/gp/product/B01N30EAZO/), [US](https://www.amazon.com/dp/B008RFVWU2), [AE](https://www.aliexpress.com/item/33043769059.html))
- 16x M3x25 screw ([EU](https://www.amazon.de/dp/B07KFL3SSV), [US](https://www.amazon.com/dp/B07WJL3P3X), [AE](https://www.aliexpress.com/item/4000173341865.html))
- 16x M3 nut ([EU](https://www.amazon.de/dp/B07JMF3KMD), [US](https://www.amazon.com/dp/B071NLDW56), [AE](https://www.aliexpress.com/item/32977174437.html))
- 6x M3x5 screw ([EU](https://www.amazon.de/dp/B01HBRG3W8), [US](https://www.amazon.com/dp/B07MBHMLL2), [AE](https://www.aliexpress.com/item/32892594230.html))
- Dupont cables ([EU](https://www.amazon.de/dp/B07KYHBVR7), [US](https://www.amazon.com/dp/B07GD2BWPY), [AE](https://www.aliexpress.com/item/4000766001685.html))

#### Optional components

- 1x Ultrasonic Sensor ([EU](https://www.amazon.de/dp/B00LSJWRXU), [US](https://www.amazon.com/dp/B0852V181G/), [AE](https://www.aliexpress.com/item/32713522570.html))
- 2x On/Off Switch ([EU](https://www.amazon.de/dp/B07QB22J62), [US](https://www.amazon.com/dp/B01N2U8PK0), [AE](https://www.aliexpress.com/item/1000005699023.html))
- 4x Orange LED 5mm ([EU](https://www.amazon.de/gp/product/B01NCL0UTQ), [US](https://www.amazon.com/dp/B077XD7MVB), [AE](https://www.aliexpress.com/item/4000329069943.html))
- 4x Red LED 5mm ([EU](https://www.amazon.de/gp/product/B01NCL0UTQ), [US](https://www.amazon.com/dp/B077XD7MVB), [AE](https://www.aliexpress.com/item/4000329069943.html))
- 2x White LED lamps ([EU](https://www.amazon.de/gp/product/B01NCL0UTQ), [US](https://www.amazon.com/dp/B077XD7MVB), [AE](https://www.aliexpress.com/item/4000329069943.html))
- (Optional) Resistors (2x 150<span>&#8486;</span> for the LEDs and a 20 k<span>&#8486;</span> and 10k<span>&#8486;</span> for the voltage divider)
- (Optional) 5x Micro JST PH 2.0 cable ([EU](https://www.amazon.de/gp/product/B07449V33P), [US](https://www.amazon.com/dp/B07449V33P), [AE](https://www.aliexpress.com/item/32963304134.html))


### Build instructions

**Tip:** Click on the images to open them in full resolution in a new tab.


<p float="left">
  <img src="/docs/images/diy_parts.jpg" height="300" />
  <img src="/docs/images/wiring_diagram.png" height="300" /> 
</p>

**Tip:** To make all the wiring easier you can build a small power distributor for the 5V and GND connections by soldering a 6x2 male header to a perfboard. Then connect the power distributor with the 5V / GND of the motor driver.

1. Solder wires to the motors and add the encoder disks to the two front motors if you intend to use the speed sensors.
    <p float="left">
      <img src="/docs/images/add_wires_motor.jpg" width="32%" />
      <img src="/docs/images/add_disk_motor.jpg" width="32%" /> 
    </p>
2. Insert the positive and negative leads of the two left motors into OUT1 (+) and OUT2 (-) of the L298N board. Insert the positive and negative leads of the two right motors into OUT4 (+) and OUT3 (-) of the L298N board.
3. Mount the motors with eight M3x25 screws and nuts.
    <p float="left">
      <img src="/docs/images/attach_motors_1.jpg" width="32%" />
      <img src="/docs/images/attach_motors_2.jpg" width="32%" /> 
      <img src="/docs/images/attach_motors_3.jpg" width="32%" />
    </p>
4. Mount the L298N with four M3x5 screws
5. (Optional) Install the ultrasonic sensor and replace the angled connector with a straigt one (or carefully bend the pins).
    <p float="left">
      <img src="/docs/images/sonar_front.jpg" width="32%" />
      <img src="/docs/images/sonar_back.jpg" width="32%" /> 
      <img src="/docs/images/sonar_bend_pins.jpg" width="32%" />
    </p>
6. (Optional) Install the orange LEDs for the indicator signals.
    <p float="left">
      <img src="/docs/images/led_insert.jpg" width="32%" />
      <img src="/docs/images/led_left.jpg" width="32%" /> 
      <img src="/docs/images/led_right.jpg" width="32%" />
    </p>
7. Mount the bottom of the phone mount to the top plate using two M3x25 screws and nuts.
    <p float="left">
      <img src="/docs/images/install_camera_mount_1.jpg" width="32%" />
      <img src="/docs/images/install_camera_mount_2.jpg" width="32%" /> 
      <img src="/docs/images/install_camera_mount_3.jpg" width="32%" />
    </p>
8. Insert the top of the phone mount and install the spring or rubber band.
    <p float="left">
      <img src="/docs/images/install_spring_1.jpg" width="32%" />
      <img src="/docs/images/install_spring_2.jpg" width="32%" /> 
    </p>
9. Replace the angled connector with a straigt one (or carefully bend the pins) and then mount the speed sensors with one M3x5 screw each.
    <p float="left">
      <img src="/docs/images/install_speed_sensor_1.jpg" width="32%" />
      <img src="/docs/images/install_speed_sensor_2.jpg" width="32%" /> 
      <img src="/docs/images/install_speed_sensor_3.jpg" width="32%" />
    </p>
10. Install the battery case (e.g. velcro).
    <p float="left">
      <img src="/docs/images/install_battery_1.jpg" width="32%" />
      <img src="/docs/images/install_battery_2.jpg" width="32%" /> 
      <img src="/docs/images/install_battery_3.jpg" width="32%" />
    </p>
11. (Optional) Insert the on/off switch put it in the current path.
    1. Push the switch into the appropriate opening until you hear a click.
    2. Solder the red wires (12V) of the battery case and the power cable each to one of the pins of the switch. Connect the black wires (GND), and cover the connection with some heatshrink.
    3. Fix the cables with some tape.
    <p float="left">
      <img src="/docs/images/install_switch_1.jpg" width="32%" />
      <img src="/docs/images/install_switch_2.jpg" width="32%" /> 
      <img src="/docs/images/install_switch_3.jpg" width="32%" />
    </p>
12. (Optional) Attach the OLED display.
13. Connect the PWM inputs of the L298N to pins D5, D6, D9 and D10 of the Arduino.
14. Connect the speed sensors and ultrasonic sensor to 5V and GND.
15. Connect pin D0 of the speed sensors to pins D2 (left) and D3 (right) of the Arduino.
16. Connect pins Echo and Trigger of the ultrasonic sensor to pins D11 and D12 of the Arduino respectively.
17. (Optional) Connect the LEDs to pins D4 (left) and D7 (right) of the Arduino and GND. We recommend to add a 150 Ohm resistor in series to limit the current draw.
18. (Optional) Connect the voltage divider to pin A7 of the Arduino. It is used to measure the battery voltage.
19. (Optional) Connect the OLED display (SSD1306 chip) via the I2C bus to the Arduino Nano
    1. Connect the VIN and GND pins of the display to 5V and GND.
    2. Connect the SCL pin of the display to the A5 pin.
    3. Connect the SDA pin of the display to the A4 pin.
20. Connect the power cables to +12V and GND of the L298N.
21. Connect the USB cable to the Arduino and route it through the top cover.
22. Insert six M3 nuts into the bottom plate and mount the top cover with six M3x25 screws.
23. Install the wheels.



1. Solder wires with Micro JST PH 2.0 connectors to the motors and add the encoder disks to the two front motors if you intend to use the speed sensors.
    <p float="left">
      <img src="/docs/images/add_wires_motor.jpg" width="32%" />
      <img src="/docs/images/add_disk_motor.jpg" width="32%" /> 
    </p>
2. Mount the motors with eight M3x25 screws and nuts.
    <p float="left">
      <img src="/docs/images/attach_motors_1.jpg" width="32%" />
      <img src="/docs/images/attach_motors_2.jpg" width="32%" /> 
      <img src="/docs/images/attach_motors_3.jpg" width="32%" />
    </p>
3. Connect the left two motors to M3 and M4 and the right two motors to M1 and M2.
    <p float="left">
      <img src="/docs/images/connect_motors_pcb.jpg" width="32%" />
    </p>
4. Mount the PCB with four M3x5 screws and the motors with eight M3x25 screws and nuts.
    <p float="left">
      <img src="/docs/images/attach_pcb.jpg" width="32%" />
      <img src="/docs/images/chassis_motors_pcb.jpg" width="32%" />
    </p>
5. Follow steps 5-12 from the DIY option.
6. Connect the ultrasonic sensor (VCC/+, Trig, Echo, GND/-) to the 4-pin header labeled *SONAR* on the PCB.
    <p float="left">
      <img src="/docs/images/connect_sonar_sensor.jpg" width="32%" />
    </p>
7. Connect the left and right indicator signals (orange LEDs) to the 2-pin headers labeled *SIGNAL_L* and *SIGNAL_R* on the PCB. The longer leg is + and the shorter one -.
8. Connect the left and right speed sensors (VCC/+, GND/-, D0) to the 3-pin headers labeled *SPEED_L* and *SPEED_R*.
9. (Optional) Connect the OLED display (SSD1306 chip) to the IO2 header on the PCB.
    1. Connect the VIN and GND pins of the display to 5V and GND.
    2. Connect the SCL pin of the display to the A5 pin.
    3. Connect the SDA pin of the display to the A4 pin.
10. Connect the power cables to Vin (Micro JST PH 2.0 connector) of the PCB.
11. Follow steps 21-23 from the DIY option.

## Next

Flash the [Arduino Firmware](../firmware/README.md)


