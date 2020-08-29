
# OpenBot: Robot Body
We have designed a body for a wheeled robot which relies on low-cost, readily available hobby hardware. 
![Assembly](../docs/images/assembly.gif)

## 3D printed chassis
You will need to print the following parts in order to build your OpenBot. 
1) body_bottom ([STL](body_bottom.stl), [STEP](body_bottom.step))
2) body_top ([STL](body_top.stl), [STEP](body_top.step))
3) phone_mount_bottom ([STL](phone_mount_bottom.stl))
4) phone_mount_top ([STL](phone_mount_top.stl))

On an Ultimaker S5, we achieved good results with the following settings:
- layer height: 0.2mm
- wall thickness: 1.5mm
- infill density: 20%
- infill pattern: grid
- print speed 80 mm/s
- no support

We were able to print the chassis with PLA, ABS and CPE. In our experience the print was not affected very much by the print settings. However, if you have the patience, printing slower and with smaller layer height will improve the print. Also adding a support structure can improve the print, but it will require additional work to remove afterwards.

Since a lot of common 3D printers have a smaller build volume, we have also made a [body_bottom_slim.stl](body_bottom_slim.stl) and [body_top_slim.stl](body_bottom_slim.stl) which fit on a 223x223 build plate at 45 degrees. These have not been tested, but should work fine.

## Bill of materials
You will need the following components.
- 4x TT motors with tires ([EU](https://www.conrad.de/de/p/joy-it-com-motor01-getriebemotor-gelb-schwarz-passend-fuer-einplatinen-computer-arduino-banana-pi-cubieboard-raspbe-1573543.html), [US](https://www.amazon.com/dp/B076GZT5MB))
- 1x L298N Motor Driver ([EU](https://www.conrad.de/de/p/joy-it-motormodul-2-u-4-phasen-6-bis-12v-1573541.html), [US](https://www.amazon.com/dp/B076GZT5MB))
- 1x Arduino Nano ([EU](https://www.amazon.de/dp/B01MS7DUEM), [US](https://www.amazon.com/dp/B00NLAMS9C))
- 2 x Speed Sensor ([EU](https://www.conrad.de/de/p/joy-it-sen-speed-erweiterungsmodul-passend-fuer-einplatinen-computer-arduino-banana-pi-cubieboard-raspberry-pi-pc-1646891.html), [US](https://www.amazon.com/dp/B081W2TY6Q))
- 3x 18650 battery ([EU](https://www.conrad.de/de/p/conrad-energy-18650-usb-spezial-akku-18650-li-ion-3-7-v-1400-mah-1525536.html), [US](https://www.amazon.com/dp/B083K4XSKG))
- 1x 18650 battery holder ([EU](https://www.amazon.de/dp/B075V25QJ9), [US](https://www.amazon.com/dp/B07DWQYD7H))
- 1x USB OTG cable ([EU](https://www.amazon.de/gp/product/B075M4CQHZ) ,[US](https://www.amazon.com/dp/B07LBHKTMM))
- 1x spring or rubber band ([EU](https://www.amazon.de/gp/product/B01N30EAZO/), [US](https://www.amazon.com/dp/B008RFVWU2))
- 16x M3x25 screw ([EU](https://www.amazon.de/dp/B07KFL3SSV), [US](https://www.amazon.com/dp/B07WJL3P3X))
- 16x M3 nut ([EU](https://www.amazon.de/dp/B07JMF3KMD), [US](https://www.amazon.com/dp/B071NLDW56))
- 6x M3x5 screw ([EU](https://www.amazon.de/dp/B01HBRG3W8), [US](https://www.amazon.com/dp/B07MBHMLL2))
- Wires ([EU](https://www.amazon.de/dp/B07KYHBVR7), [US](https://www.amazon.com/dp/B07GD2BWPY))

(Optional)
- 1x Ultrasonic Sensor ([EU](https://www.amazon.de/dp/B00LSJWRXU), [US](https://www.amazon.com/dp/B0852V181G/))
- 1x On/Off Switch ([EU](https://www.amazon.de/dp/B07QB22J62), [US](https://www.amazon.com/dp/B01N2U8PK0))
- 2x Orange LED 5mm ([EU](https://www.amazon.de/gp/product/B01NCL0UTQ), [US](https://www.amazon.com/dp/B077XD7MVB))

## Build instructions
1) Connect wires to the motors if neccessary
2) Insert the positive and negative leads of two motors into OUT1 (+) and OUT2 (-) of the L298N board
3) Insert the positive and negative leads of the other two motors into OUT4 (+) and OUT3 (-) of the L298N board
4) Mount the L298N with four M3x5 screws and the motors with eight M3x25 screws and nuts
5) (Optional) Install the ultrasonic sensor and orange LEDs
6) Mount the bottom of the phone mount to the top plate using two M3x25 screws and nuts
7) Insert the top of the phone mount and install the spring or rubber band
8) Mount the speed sensors with one M3x5 screw each
9) Install the battery case (e.g. velcro)
10) (Optional) Insert the on/off switch
11) Connect the PWM inputs of the L298N to pins D5, D6, D9 and D10 of the Arduino
12) Connect the speed sensors and ultrasonic sensor to 5V and GND
13) Connect D0 of the speed sensors to pins D2 (left) and D3 (right) of the Arduino
14) Connect Echo and Trigger of the ultrasonic sensor to pin D4 of the Arduino
15) Connect the USB cable to the Arduino and route it through the top cover
16) (Optional) Connect the LEDs to pins D7 (left) and D8 (right) of the Arduino and GND
17) (Optional) Connect the voltage divider to pin A7 of the Arduino
18) Connect the battery cables to Vin of the L298N. If you installed the switch, put it in the current path.
19) Insert six M3 nuts into the bottom plate and mount the top cover with six M3x25 screws
20) Install the wheels
