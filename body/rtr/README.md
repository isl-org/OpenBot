# OpenBot: Ready-To-Run (RTR) vehicles

The ready-to-run (RTR) versions of the OpenBot vehicle are targetting an audience which does not have the will or time to build its own robot. The RTR vehicles come with fully integrated electronics, are already supported at the firmware level and have been thoroughly tested both from a software and hardware perspective. The RTR vehicles are available in two different flavours, referred to as "RTR_TT" and "RTR_520". Both vehicles are built around the same splash-proof ABS shell, but are intended for different purposes. While the RTR_TT is primarily intended for indoor use, the RTR_520 comes with a more powerful processor, better motors, stronger metal gearboxes and also has a set of all-terrain wheels supporting both indoor and outdoor use. 
<p align="center">
  <a> <img src="/docs/images/RTR_TT.jpg" width="35.8%" /> &nbsp
  </a>
  <a> <img src="/docs/images/RTR_520.jpg" width="33%" />
  </a>
</p>

### Ordering

The RTR OpenBot vehicles can be ordered [here](http://www.openbot.info/).

## Building the RTR yourself

In case you want to build your own OpenBot RTR, you will need to print the chassis, manufacture the PCBs and buy the motors and a phone mount.

### 3D printing

In case you still want to print your own OpenBot RTR, you will need to print the following parts.

1) ```shell_bottom``` ([STL](cad/rtr_bottom.stl), [STEP](cad/rtr_bottom.step))
2) ```shell_top``` ([STL](cad/rtr_top.stl), [STEP](cad/rtr_top.step)) 
3) ```phone_mount``` ([STL](cad/rtr_mount.stl), [STEP](cad/rtr_mount.step))

<p align="center">
  <img src="../../docs/images/rtr_tt_assembly.gif" width="600" alt="App GUI"/>
</p>

### PCBs

For each of the PCBs, there are three files. The gerber file contains the actual PCB, the BOM (bill of materials) file contains all components to be soldered onto the PCB and the centroid file contains the coordinates of each componnent for automatic PCB assembly. The base board contains the majority of the components. There are three variants of the base board. Variant A is a bare board with connectors for external motor driver boards and an external microcontroller board. Variant B is a modular board with a pin header for an external microcontroller. Variant C is the fully integrated base board which we would recommend for most users. The front bump sensor board contains two bump sensors, a sonar sensor and the USB driver. There are two variants of the front bump sensor board, one with the cheaper CH340G USB driver and one with the more reliable CP2102N USB driver. Depending on which version (TT-motor or 520-motor) you want to build, you will need the following PCBs.

#### TT-motor

1x Base board (Arduino)
1x Status LED board
1x Front/Top/Back bump sensor board
4x Speed sensor board (Arduino)

#### 520-motor

1x Base board (ESP32)
1x Status LED board
1x Front/Top/Back bump sensor board

#### Board reference

- Status LED Board ([gerber](),[bom](),[centroid]())
- Top Bump Sensor Board ([gerber](),[bom](),[centroid]())
- Back Bump Sensor Board ([gerber](),[bom](),[centroid]())
- Front Bump Sensor Board (CH340G) ([gerber](),[bom](),[centroid]())
- Front Bump Sensor Board (CP2102N) ([gerber](),[bom](),[centroid]())
- Speed Sensor Board (Arduino) ([gerber](),[bom](),[centroid]())
- Integrated Base Board C (Arduino) ([gerber](),[bom](),[centroid]())
- Integrated Base Board C (ESP32) ([gerber](),[bom](),[centroid]())
- Modular Base Board B (Arduino) ([gerber](),[bom](),[centroid]())
- Modular Base Board B (ESP32) ([gerber](),[bom](),[centroid]())
- Bare Base Board A (Arduino) ([gerber](),[bom](),[centroid]())
- Motor Driver DRV8870 Board (Arduino) ([gerber](),[bom](),[centroid]())
- Bare Base Board A (ESP32) ([gerber](),[bom](),[centroid]())
- Motor Driver DRV8870 Board (ESP32) ([gerber](),[bom](),[centroid]())

## Next

Flash the [Arduino Firmware](../../firmware/README.md)
