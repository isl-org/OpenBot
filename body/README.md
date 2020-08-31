
# OpenBot: Robot Body

[简体中文](./README_CN.md)

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

Since a lot of common 3D printers have a smaller build volume, we have also made a [body_bottom_slim.stl](body_bottom_slim.stl) and [body_top_slim.stl](body_top_slim.stl) which fit on a 223x223 build plate at 45 degrees. These have not been tested, but should work fine.

## Bill of materials
Our robot body relies on readily available hobby electronics. We provide links for Germany (EU) and the United States (US) with fast shipping. If you have the patience to wait a bit longer, you can also get the compoenents a lot cheaper from AliExpress (AE). You will need the following components.
- 4x TT motors with tires ([EU](https://www.conrad.de/de/p/joy-it-com-motor01-getriebemotor-gelb-schwarz-passend-fuer-einplatinen-computer-arduino-banana-pi-cubieboard-raspbe-1573543.html), [US](https://www.amazon.com/dp/B0818Y9G3N), [AE](https://www.aliexpress.com/item/4000126948489.html))
- 1x L298N Motor Driver ([EU](https://www.conrad.de/de/p/joy-it-motormodul-2-u-4-phasen-6-bis-12v-1573541.html), [US](https://www.amazon.com/dp/B0818Y9G3N), [AE](https://www.aliexpress.com/item/32994608743.html))
- 1x Arduino Nano ([EU](https://www.amazon.de/dp/B01MS7DUEM), [US](https://www.amazon.com/dp/B00NLAMS9C), [AE](https://www.aliexpress.com/item/32866959979.html))
- 2 x Speed Sensor ([EU](https://www.conrad.de/de/p/joy-it-sen-speed-erweiterungsmodul-passend-fuer-einplatinen-computer-arduino-banana-pi-cubieboard-raspberry-pi-pc-1646891.html), [US](https://www.amazon.com/dp/B081W2TY6Q), [AE](https://www.aliexpress.com/i/32850602744.html))
- 3x 18650 battery ([EU](https://www.conrad.de/de/p/conrad-energy-18650-usb-spezial-akku-18650-li-ion-3-7-v-1400-mah-1525536.html), [US](https://www.amazon.com/dp/B083K4XSKG), [AE](https://www.aliexpress.com/item/32352434845.htm))
- 1x 18650 battery holder([EU](https://www.amazon.de/dp/B075V25QJ9), [US](https://www.amazon.com/dp/B07DWQYD7H), [AE](https://www.aliexpress.com/item/33037738446.html))
- 1x USB OTG cable ([EU](https://www.amazon.de/gp/product/B075M4CQHZ) ,[US](https://www.amazon.com/dp/B07LBHKTMM), [AE](https://www.aliexpress.com/item/10000330515850.html))
- 1x spring or rubber band ([EU](https://www.amazon.de/gp/product/B01N30EAZO/), [US](https://www.amazon.com/dp/B008RFVWU2), [AE](https://www.aliexpress.com/item/33043769059.html))
- 16x M3x25 screw ([EU](https://www.amazon.de/dp/B07KFL3SSV), [US](https://www.amazon.com/dp/B07WJL3P3X), [AE](https://www.aliexpress.com/item/4000173341865.html))
- 16x M3 nut ([EU](https://www.amazon.de/dp/B07JMF3KMD), [US](https://www.amazon.com/dp/B071NLDW56), [AE](https://www.aliexpress.com/item/32977174437.html))
- 6x M3x5 screw ([EU](https://www.amazon.de/dp/B01HBRG3W8), [US](https://www.amazon.com/dp/B07MBHMLL2), [AE](https://www.aliexpress.com/item/32892594230.html))
- Wires ([EU](https://www.amazon.de/dp/B07KYHBVR7), [US](https://www.amazon.com/dp/B07GD2BWPY), [AE](https://www.aliexpress.com/item/4000766001685.html))

(Optional)
- 1x Ultrasonic Sensor ([EU](https://www.amazon.de/dp/B00LSJWRXU), [US](https://www.amazon.com/dp/B0852V181G/), [AE](https://www.aliexpress.com/item/32713522570.html))
- 1x On/Off Switch ([EU](https://www.amazon.de/dp/B07QB22J62), [US](https://www.amazon.com/dp/B01N2U8PK0), [AE](https://www.aliexpress.com/item/1000005699023.html))
- 2x Orange LED 5mm ([EU](https://www.amazon.de/gp/product/B01NCL0UTQ), [US](https://www.amazon.com/dp/B077XD7MVB), [AE](https://www.aliexpress.com/item/4000329069943.html))

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

## Custom PCB
![PCB](../docs/images/pcb_3d.png)
The custom PCB involves the following steps:
1) **Order the PCB**: Download the [Gerber](gerber.zip) files and order the PCB at the vendor of your choice. You can also order the PCB directly on [PCBWay](https://www.pcbway.com/project/shareproject/OpenBot__Turning_Smartphones_into_Robots.html) where we have shared a project for OpenBot.
2) **Order the components:** Download the [BOM](BOM.csv) and order the compenents at the vendor of your choice, for example [LCSC](https://lcsc.com).
3) **Assembly of the PCB:** You can either assemble the PCB yourself or have them assembled by a vendor. For automated assembly you will need the [Centroid File](centroid_file.csv)

You can also find vendors that will provide you a TurnKey solution covering all 3 steps. They will manufacture the PCB, source the components and assemble the PCB. This is very convenient and also not too expensive. However, delivery times are often very long (1-3 months). 

When requesting a quote at [PCBWay](https://www.pcbway.com/orderonline.aspx), you can select the assembly service after uploading the Gerber file. 
![Assembly Service](../docs/images/assembly_service.jpg)
In the next step, you will need to upload the [BOM](BOM.csv) and the [Centroid File](centroid_file.csv). Your quote will then be reviewed and updated within a few days. You can then choose to proceed with payment after reviewing cost and delivery time. 
