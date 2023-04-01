# Large Body with Lego Support

This robot chassis provides some extra height to make it easier to fit in all the electronics and a lego-compatible top. This version offers the same structural integrity capabilities as the [regular body](../regular_body/), with additional features such as extra space inside the OpenBot shell, a lego-compatible top for play and learning and a footprint suitable for smaller print bed sizes while maintaining bumpers. 

![Large Body](../../../../docs/images/large_body.jpg)

## Parts

You will need to print the following STL files:

- `large_body_bottom`([STL](large_body_bottom.stl), [STEP](large_body_bottom.step)): bottom part of the body
- `large_body_top_lego`([STL](large_body_top_lego.stl), [STEP](large_body_top_lego.step)) or `large_body_top`([STL](large_body_top.stl), [STEP](large_body_top.step)): top part of the body, with or without lego-compatible surface

For the above parts, your build plate needs to be at least 222x150mm.

## Print Settings

For best results, we recommend using the following print settings:

- Layer height: 0.2mm
- Wall line count: 3 (more walls for better structural integrity of larger surfaces)
- Top Layers: 5
- Bottom Layers: 4
- Infill: 25%
- Infill Pattern: Concentric (this setting seems to save time and plastic)
- Print Speed: 50mm/sec
- Generate Support: Yes
- Support Pattern: Concentric
- Support density: 15%
- Enable Support Brim: Yes
- Build Plate Adhesion Type: None
