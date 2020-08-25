
# OpenBot: Robot Body
We have designed a body for a wheeled robot which relies on low-cost, readily available hobby hardware. The total cost of building such a robot is only $50.
![Assembly](../docs/images/assembly.gif)

## 3D printed chassis
You will need to print the following parts in order to build your OpenBot. 
1) body_bottom ([STL](body_bottom.stl))
2) body_top ([STL](body_bottom.stl))
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

## Components
You will need to buy the following components.
- 4x TT motors with tires ([EU](https://www.conrad.de/de/p/joy-it-com-motor01-getriebemotor-gelb-schwarz-passend-fuer-einplatinen-computer-arduino-banana-pi-cubieboard-raspbe-1573543.html), [US](https://www.amazon.com/dp/B076GZT5MB))
- 1x L298N Motor Driver ([EU](https://www.conrad.de/de/p/joy-it-motormodul-2-u-4-phasen-6-bis-12v-1573541.html), [US](https://www.amazon.com/dp/B076GZT5MB))
- 1x Arduino Nano ([EU](https://www.amazon.de/AZDelivery-Nano-verl%C3%B6tete-Version-Kompatibel/dp/B01MS7DUEM), [US](https://www.amazon.com/ATmega328P-Microcontroller-Board-Cable-Arduino/dp/B00NLAMS9C))
- 2 x Speed Sensor ([EU](https://www.conrad.de/de/p/joy-it-sen-speed-erweiterungsmodul-passend-fuer-einplatinen-computer-arduino-banana-pi-cubieboard-raspberry-pi-pc-1646891.html), [US](https://www.amazon.com/DAOKI-Measuring-Optocoupler-Arduino-Encoders/dp/B081W2TY6Q))
- 3x 18650 battery ([EU](https://www.conrad.de/de/p/conrad-energy-18650-usb-spezial-akku-18650-li-ion-3-7-v-1400-mah-1525536.html), [US](https://www.amazon.com/Flashlight-Tactical-Rechargeable-Zoomable-Emergency/dp/B083K4XSKG))
- 1x 18650 battery holder ([EU](https://www.amazon.de/KEESIN-Batteriehalter-Aufbewahrungsbox-Befestigung-Kabelbinder-3-Solts-%C3%97-5-St%C3%BCck/dp/B075V25QJ9), [US](https://www.amazon.com/18650-Battery-Holder-Storage-Spring/dp/B07DWQYD7H))
- 1x USB OTG cable ([EU](https://www.amazon.de/gp/product/B075M4CQHZ) ,[US](https://www.amazon.com/Micro-B-Adapter-Converter-MacBook-Charging/dp/B07LBHKTMM))
- 1x spring or rubber band ([EU](https://www.amazon.de/gp/product/B01N30EAZO/), [US](https://www.amazon.com/Prime-Line-Products-SP-9600-extensi%C3%B3n/dp/B008RFVWU2))
- 16x M3x25 screw ([EU](https://www.amazon.de/St%C3%BCck-Senkkopf-Schrauben-3x25-Senkkopfschrauben/dp/B07KFL3SSV), [US](https://www.amazon.com/uxcell-M3x25mm-Machine-Stainless-Fasteners/dp/B07WJL3P3X))
- 16x M3 nut ([EU](https://www.amazon.de/100-St%C3%BCck-Sechskantmuttern-DIN-934/dp/B07JMF3KMD), [US](https://www.amazon.com/Shapenty-100PCS-Stainless-Female-Fastener/dp/B071NLDW56))
- 6x M3x5 screw ([EU](https://www.amazon.de/Poppstar-Festplattenschrauben-Festplatten-M3x5mm-silber/dp/B01HBRG3W8), [US](https://www.amazon.com/uxcell-Machine-Phillips-Stainless-Fasteners/dp/B07MBHMLL2))
- 1x On/Off Switch ([EU](https://www.amazon.de/KeeYees-Kippschalter-2-Polig-Schalter-Ausschalter/dp/B07QB22J62), [US](https://www.amazon.com/ZUPAYIPA-Solder-Rocker-Switch-Toggle/dp/B01N2U8PK0))
- 2x Orange LED 5mm ([EU](https://www.amazon.de/gp/product/B01NCL0UTQ), [US](https://www.amazon.com/EDGELEC-Orange-Orange-Diffused-Resistors-Included/dp/B077XD7MVB))