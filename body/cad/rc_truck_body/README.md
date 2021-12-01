# OpenBot: RC-Truck Body

<!---
<p align="center">
  <span>English</span> |
  <a href="README_CN.md">简体中文</a>
</p>
--->

We have designed a body for widely available 1:16 scale RC-toy trucks (such as [this](https://www.amazon.de/dp/B00M3J7DJW) on Amazon (EU)).

![RC-Truck-Banner](/docs/images/rc-truck-banner.jpg)

We also have a generic [body](/body/) designed for a simple wheeled robot which relies on low-cost, readily available hobby hardware. Instructions [here](/body/README.md). 

## Chassis

The chassis of OpenBot RC-Truck consists of two main components: (a) A 1:16 scale RC-toy truck and (b) some custom-designed parts which we provide and can be 3D-printed.

### RC-toy truck

To build your own OpenBot RC-Truck, you will need a 1:16 RC-toy truck/buggy. We provide Amazon links to compatible RC-toy trucks for Germany ([EU](https://www.amazon.de/dp/B00M3J7DJW)) and the United States ([US](https://www.amazon.com/gp/product/B09C8XMPQ9/)) with fast shipping. A wide variety of similar trucks can also be found on other online retailers such as ebay, Alibaba, AliExpress etc. often for a discounted price but with slow shipping speed. However, while purchasing one for your build (using our 3D-printed parts), please ensure that it is indeed a 1:16 scale RC-truck.

<p float="left">
  <img src="/docs/images/rc_toy_1.jpg" width="32%" />
  <img src="/docs/images/rc_toy_2.jpg" width="32%" /> 
  <img src="/docs/images/rc_toy_3.jpg" width="32%" />
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
Second option requires modifying the original ```main_frame``` part file. We recommend using Autodesk Fusion 360 for such CAD modifications. 
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

Before you proceed with the build, you may need to clean the 3D print. However, using the above settings, we did not need any filing or cleaning during our build process. If possible, we recommend using a combination of two different colors (for example green/black or red/black) for printing different parts of the same OpenBot RC-Truck build (as shown below for green/black combo).

<p float="left">
  <img src="/docs/images/3d_print_rc_1.jpg" width="32%" />
  <img src="/docs/images/3d_print_rc_2.jpg" width="32%" /> 
  <img src="/docs/images/3d_print_rc_3.jpg" width="32%" />
</p>


## Assembly

