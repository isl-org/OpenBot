
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
You will need the following components.
- 4x TT motors with tires ([EU](https://www.conrad.de/de/p/joy-it-com-motor01-getriebemotor-gelb-schwarz-passend-fuer-einplatinen-computer-arduino-banana-pi-cubieboard-raspbe-1573543.html), [US](https://www.amazon.com/Driver-Module-Controller-H-Bridge-Stepper/dp/B0818Y9G3N), [AE](https://www.aliexpress.com/item/4000126948489.html?spm=a2g0o.productlist.0.0.15293359bK54gA&algo_pvid=75eb3a47-44d5-4f6c-9ea0-e758f0d08837&algo_expid=75eb3a47-44d5-4f6c-9ea0-e758f0d08837-3&btsid=0bb47a2215986857911752326e0253&ws_ab_test=searchweb0_0,searchweb201602_,searchweb201603_) )
- 1x L298N Motor Driver ([EU](https://www.conrad.de/de/p/joy-it-motormodul-2-u-4-phasen-6-bis-12v-1573541.html), [US](https://www.amazon.com/Driver-Module-Controller-H-Bridge-Stepper/dp/B0818Y9G3N), [AE](https://www.aliexpress.com/item/32994608743.html?spm=a2g0o.productlist.0.0.b8be157eJVP5lY&algo_pvid=088ff2ac-a4ec-4b17-be43-92254d84f9fa&algo_expid=088ff2ac-a4ec-4b17-be43-92254d84f9fa-1&btsid=0bb47a2215986861645618336e0253&ws_ab_test=searchweb0_0,searchweb201602_,searchweb201603_))
- 1x Arduino Nano ([EU](https://www.amazon.de/dp/B01MS7DUEM), [US](https://www.amazon.com/dp/B00NLAMS9C), [AE](https://www.aliexpress.com/item/32866959979.html?spm=a2g0o.productlist.0.0.d78d3391GJVFfZ&algo_pvid=da4460e1-407b-4132-a0db-947a431ab822&algo_expid=da4460e1-407b-4132-a0db-947a431ab822-0&btsid=0bb47a2215986862077318889e0253&ws_ab_test=searchweb0_0,searchweb201602_,searchweb201603_ ))
- 2 x Speed Sensor ([EU](https://www.conrad.de/de/p/joy-it-sen-speed-erweiterungsmodul-passend-fuer-einplatinen-computer-arduino-banana-pi-cubieboard-raspberry-pi-pc-1646891.html), [US](https://www.amazon.com/dp/B081W2TY6Q), [AE](https://www.aliexpress.com/i/32990256417.html))
- 3x 18650 battery ([EU](https://www.conrad.de/de/p/conrad-energy-18650-usb-spezial-akku-18650-li-ion-3-7-v-1400-mah-1525536.html), [US](https://www.amazon.com/dp/B083K4XSKG), [AE](https://www.aliexpress.com/item/32324914059.html?spm=a2g0o.productlist.0.0.2e8f4b074mHrYv&algo_pvid=fc2f97ca-c64c-43c1-a4b8-e9e7fe81cb09&algo_expid=fc2f97ca-c64c-43c1-a4b8-e9e7fe81cb09-0&btsid=0bb47a2215986864108333405e0253&ws_ab_test=searchweb0_0,searchweb201602_,searchweb201603_))
- 1x 18650 battery holder([EU](https://www.amazon.de/dp/B075V25QJ9), [US](https://www.amazon.com/dp/B07DWQYD7H), [AE](https://www.aliexpress.com/item/33037738446.html?spm=a2g0o.productlist.0.0.5c602568XlNOGM&algo_pvid=b667d7dc-7798-4fbc-8fbc-760fd4ebc4ef&algo_expid=b667d7dc-7798-4fbc-8fbc-760fd4ebc4ef-0&btsid=0bb47a2215986864956914727e0253&ws_ab_test=searchweb0_0,searchweb201602_,searchweb201603_))
- 1x USB OTG cable ([EU](https://www.amazon.de/gp/product/B075M4CQHZ) ,[US](https://www.amazon.com/dp/B07LBHKTMM), [AE](https://www.aliexpress.com/item/10000339378239.html?spm=a2g0o.productlist.0.0.31ae865fVUECCV&algo_pvid=c08f70c5-2e72-4324-b4c5-7734842d5b24&algo_expid=c08f70c5-2e72-4324-b4c5-7734842d5b24-1&btsid=0bb47a2215986866303645934e0253&ws_ab_test=searchweb0_0,searchweb201602_,searchweb201603_))
- 1x spring or rubber band ([EU](https://www.amazon.de/gp/product/B01N30EAZO/), [US](https://www.amazon.com/dp/B008RFVWU2), [AE](https://www.aliexpress.com/item/33046502168.html?spm=a2g0o.productlist.0.0.45964bccXYBjIU&algo_pvid=1ee2f573-9d37-4855-ac91-9aec2c00151c&algo_expid=1ee2f573-9d37-4855-ac91-9aec2c00151c-1&btsid=0bb47a2215986886760396354e0297&ws_ab_test=searchweb0_0,searchweb201602_,searchweb201603_))
- 16x M3x25 screw ([EU](https://www.amazon.de/dp/B07KFL3SSV), [US](https://www.amazon.com/dp/B07WJL3P3X), [AE](https://www.aliexpress.com/item/32850849521.html?spm=a2g0o.productlist.0.0.605d7959tDbcCu&algo_pvid=e2d60094-7a8a-46b2-b6df-ff08abecadfa&algo_expid=e2d60094-7a8a-46b2-b6df-ff08abecadfa-0&btsid=0bb47a2215986878981485610e029a&ws_ab_test=searchweb0_0,searchweb201602_,searchweb201603_))
- 16x M3 nut ([EU](https://www.amazon.de/dp/B07JMF3KMD), [US](https://www.amazon.com/dp/B071NLDW56), [AE](https://www.aliexpress.com/item/32977174437.html?spm=a2g0o.productlist.0.0.4ecb4db1RlFaMC&algo_pvid=5227fb58-7578-4afd-af6b-bd2583ae0bcd&algo_expid=5227fb58-7578-4afd-af6b-bd2583ae0bcd-0&btsid=0b01114515986880925225032e85af&ws_ab_test=searchweb0_0,searchweb201602_,searchweb201603_))
- 6x M3x5 screw ([EU](https://www.amazon.de/dp/B01HBRG3W8), [US](https://www.amazon.com/dp/B07MBHMLL2), [AE](https://www.aliexpress.com/item/32892594230.html?spm=a2g0o.productlist.0.0.21d63d1bmXjnRZ&algo_pvid=dc03fc60-3866-4e8c-a866-38a228c28eb5&algo_expid=dc03fc60-3866-4e8c-a866-38a228c28eb5-0&btsid=0bb47a1a15986884625777576e47f9&ws_ab_test=searchweb0_0,searchweb201602_,searchweb201603_))
- Wires ([EU](https://www.amazon.de/dp/B07KYHBVR7), [US](https://www.amazon.com/dp/B07GD2BWPY), [AE](https://www.aliexpress.com/item/4000766001685.html?spm=a2g0o.productlist.0.0.2a3b4533ROQOjj&algo_pvid=22f23780-d35e-4362-a277-1dab548bb846&algo_expid=22f23780-d35e-4362-a277-1dab548bb846-5&btsid=0b01114515986882153336964e85af&ws_ab_test=searchweb0_0,searchweb201602_,searchweb201603_))

(Optional)
- 1x Ultrasonic Sensor ([EU](https://www.amazon.de/dp/B00LSJWRXU), [US](https://www.amazon.com/dp/B0852V181G/), [AE](https://www.aliexpress.com/item/32713522570.html?spm=a2g0o.productlist.0.0.798110f86VwFId&algo_pvid=aa665343-c450-4ded-8d6a-6cef7e7f0af2&algo_expid=aa665343-c450-4ded-8d6a-6cef7e7f0af2-0&btsid=0bb47a1a15986882673204579e47f9&ws_ab_test=searchweb0_0,searchweb201602_,searchweb201603_))
- 1x On/Off Switch ([EU](https://www.amazon.de/dp/B07QB22J62), [US](https://www.amazon.com/dp/B01N2U8PK0), [AE](https://www.aliexpress.com/item/1000005699023.html?spm=a2g0o.productlist.0.0.1a6c7fb71FhAHs&algo_pvid=aecb292e-471e-4598-99f5-4f74161ddd75&algo_expid=aecb292e-471e-4598-99f5-4f74161ddd75-1&btsid=0bb47a1a15986883373795667e47f9&ws_ab_test=searchweb0_0,searchweb201602_,searchweb201603_))
- 2x Orange LED 5mm ([EU](https://www.amazon.de/gp/product/B01NCL0UTQ), [US](https://www.amazon.com/dp/B077XD7MVB), [AE](https://www.aliexpress.com/item/4000329069943.html?spm=a2g0o.productlist.0.0.61c2172d8P9Xa3&algo_pvid=b2723730-97d9-413c-96b9-ed257265ad2d&algo_expid=b2723730-97d9-413c-96b9-ed257265ad2d-5&btsid=0bb47a1a15986884170516963e47f9&ws_ab_test=searchweb0_0,searchweb201602_,searchweb201603_))

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
