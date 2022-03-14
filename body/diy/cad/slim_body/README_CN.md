# 超薄版车身

<p align="center">
  <a href="README.md">English</a> |
  <span>简体中文</span>
</p>

有些 3D 打印机的构建量太小，无法打印完整尺寸的 OpenBot 机身。
这个文件夹包含超薄版的OpenBot车身。
当零件旋转45度时，它可以用220mmx220mm的构建板打印。

![超薄机身](../../../docs/images/slim_body.jpg)

## 部件

1) `slim_body_bottom`([STL](slim_body_bottom.stl), [STEP](slim_body_bottom.step))
2) `slim_body_top`([STL](slim_body_top.stl), [STEP](slim_body_top.step))

为了使其合适，您可能需要调整以下设置以获得最大的印刷面积。

- 将 `Build Plate Adhesion Type`设置为 `None`（檐部、裙部和筏部会增加印刷品的整体尺寸）。
- 禁用主图块（在*Build Plate*部分）。
- 禁用第二台挤出机（如果您的打印机有）。

如果你有额外的空间(223mmx223mm)，你也可以打印`slim_body_top_rim`([STL](slim_body_top_rim.stl), [STEP](slim_body_top_rim.step))。它的边缘略大，使其更容易取下顶部。