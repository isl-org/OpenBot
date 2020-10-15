# 定制PCB

<p align="center">
  <a href="README.md">English</a> |
  <span>简体中文</span>
</p>


定制PCB充当Arduino Nano的载板，并集成了现代电机驱动器，分压器电路和LED电阻器。只需将Arduino插入引脚，所有传感器和LED都通过杜邦线连接到适当的连接器。

![PCB_2D](../../docs/images/pcb_2d_v2.png)
![PCB_3D](../../docs/images/pcb_3d_v2.png)

最新的PCB是[版本2](v2)。以下是与[版本1](v1)相比的更改：

- 将右边的速度传感器移到D3引脚，以实现停止功能
- 为主电池添加电源LED
- 更新一些更常用的组件
- 将分压器更新为20k/10k，以实现更高的精度
- 将电机连接器更改为直立版本，以便于操作

如果您已经订购了PCB的[版本1](v1)([2D视图](../docs/images/pcb_2d_v1.png)，[3D视图](../docs/images/pcb_3d_v1.png) ，请放心，它会正常工作。只要确保在固件中设置正确的标志即可。

定制PCB涉及以下步骤：

1)**订购PCB：** 下载[Gerber](v2/gerber_v2.zip)文件，然后在您选择的供应商处订购PCB。您也可以直接在我们共享了OpenBot项目的[PCBWay](https://www.pcbway.com/project/shareproject/OpenBot__Turning_Smartphones_into_Robots.html)上订购PCB。
2)**订购组件：** 下载[BOM](v2/BOM_v2.csv)并在您选择的供应商处订购组件，例如[LCSC](https://lcsc.com)。
3)**PCB的组装：** 您既可以自己组装PCB，也可以由供应商组装。对于自动组装，您需要[Centroid File](v2/centroid_file_v2.csv)。如果您通过[JLCPCB](https://jlcpcb.com/)订购PCB，则可以使用其SMT组装服务。然后，您只需要自己订购和焊接通孔组件。我们发现这是最方便，最便宜和最快的选择。在PCB的[版本2](v2)中，我们更新了组件，以确保可以从[JLCPCB](https://jlcpcb.com/)直接获得所有组件。

您还可以找到可以为您提供涵盖所有3个步骤的TurnKey解决方案的供应商。他们将制造PCB，采购组件并组装PCB。这非常方便并且也不太昂贵。但是，交货时间通常很长(1-3个月)。

在[PCBWay](https://www.pcbway.com/orderonline.aspx)上请求报价时，可以在上载Gerber文件后选择组装服务。
![组装服务](../../docs/images/assembly_service.jpg)
在下一步中，您将需要上传[BOM](v2/BOM_v2.csv)和[Centroid File](v2/centroid_file_v2.csv)。您的报价将在几天之内进行审核和更新。然后，您可以在查看费用和交货时间后选择继续付款。