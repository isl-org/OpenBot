import 'package:flutter/material.dart';

import 'component/onScreenIcon.dart';

class OnScreenMode extends StatefulWidget {
  const OnScreenMode({super.key});

  @override
  State<StatefulWidget> createState() {
    return OnScreenModeState();
  }
}

class OnScreenModeState extends State<OnScreenMode> {
  double sliderValueLeft = 50;
  double sliderValueRight = 50;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: const Color(0xFF202020),
        body: Column(
          children: [
            GestureDetector(
                onTap: () {
                  Navigator.pop(context);
                },
                child: Container(
                  alignment: Alignment.topRight,
                  padding: const EdgeInsets.fromLTRB(0, 20, 20, 0),
                  child: Image.asset(
                    "images/cross_icon.png",
                    height: 55,
                    width: 55,
                  ),
                )),
            Expanded(
              flex: 1,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Container(
                    margin: const EdgeInsets.fromLTRB(30, 0, 0, 0),
                    child: Theme(
                        data: Theme.of(context).copyWith(
                          sliderTheme: SliderThemeData(
                            thumbShape: SquareSliderComponentShape(),
                            trackShape: const MyRoundedRectSliderTrackShape(),
                          ),
                        ),
                        child: RotatedBox(
                          quarterTurns: -1,
                          child: Slider(
                            value: sliderValueLeft,
                            onChanged: (value) =>
                                {setState(() => sliderValueLeft = value),
                                  getLeftSliderValue()},
                            onChangeEnd: (value) =>
                                {setState(() => sliderValueLeft = 50),getLeftSliderValue()},
                            min: 0,
                            max: 100,
                            activeColor: Colors.white,
                            inactiveColor: const Color(0xFF292929),
                          ),
                        )),
                  ),
                  Container(
                    alignment: AlignmentDirectional.bottomEnd,
                    margin: const EdgeInsets.fromLTRB(0, 0, 0, 20),
                    child: const OnScreenIcon(),
                  ),
                  Container(
                      margin: const EdgeInsets.fromLTRB(0, 0, 30, 0),
                      child: Theme(
                          data: Theme.of(context).copyWith(
                            sliderTheme: SliderThemeData(
                              thumbShape: SquareSliderComponentShape(),
                              trackShape: const MyRoundedRectSliderTrackShape(),
                            ),
                          ),
                          child: RotatedBox(
                            quarterTurns: -1,
                            child: Slider(
                              value: sliderValueRight,
                              onChanged: (value) =>
                                  {setState(() => sliderValueRight = value),getRightSliderValue()},
                              onChangeEnd: (value) =>
                                  {setState(() => sliderValueRight = 50),getRightSliderValue()},
                              min: 0,
                              max: 100,
                              activeColor: Colors.white,
                              inactiveColor: const Color(0xFF292929),
                            ),
                          ))),
                ],
              ),
            ),
          ],
        ));
  }
  Future<void> getLeftSliderValue()async {
    print(sliderValueLeft);
  }
  Future<void> getRightSliderValue()async {
    print(sliderValueRight);
  }
}

class SquareSliderComponentShape extends SliderComponentShape {
  @override
  Size getPreferredSize(bool isEnabled, bool isDiscrete) {
    return const Size(20, 30);
  }

  @override
  void paint(PaintingContext context, Offset center,
      {required Animation<double> activationAnimation,
      required Animation<double> enableAnimation,
      required bool isDiscrete,
      required TextPainter labelPainter,
      required RenderBox parentBox,
      required SliderThemeData sliderTheme,
      required TextDirection textDirection,
      required double value,
      required double textScaleFactor,
      required Size sizeWithOverflow}) {
    final Canvas canvas = context.canvas;
    canvas.drawRRect(
      RRect.fromRectAndRadius(
        Rect.fromCenter(center: center, width: 10, height: 55),
        const Radius.circular(2),
      ),
      Paint()..color = const Color(0xFF0071c5),
    );
  }
}

class MyRoundedRectSliderTrackShape extends SliderTrackShape
    with BaseSliderTrackShape {
  const MyRoundedRectSliderTrackShape();

  @override
  void paint(
    PaintingContext context,
    Offset offset, {
    required RenderBox parentBox,
    required SliderThemeData sliderTheme,
    required Animation<double> enableAnimation,
    required TextDirection textDirection,
    required Offset thumbCenter,
    bool isDiscrete = false,
    bool isEnabled = false,
    double additionalTrackHeight = 30,
  }) {
    if (sliderTheme.trackHeight == null || sliderTheme.trackHeight! <= 0) {
      return;
    }

    final ColorTween activeTrackColorTween = ColorTween(
        begin: sliderTheme.disabledActiveTrackColor,
        end: sliderTheme.activeTrackColor);
    final ColorTween inactiveTrackColorTween = ColorTween(
        begin: sliderTheme.disabledInactiveTrackColor,
        end: sliderTheme.inactiveTrackColor);
    final Paint activePaint = Paint()
      ..color = activeTrackColorTween.evaluate(enableAnimation)!;
    final Paint inactivePaint = Paint()
      ..color = inactiveTrackColorTween.evaluate(enableAnimation)!;
    final Paint leftTrackPaint;
    final Paint rightTrackPaint;
    switch (textDirection) {
      case TextDirection.ltr:
        leftTrackPaint = activePaint;
        rightTrackPaint = inactivePaint;
        break;
      case TextDirection.rtl:
        leftTrackPaint = inactivePaint;
        rightTrackPaint = activePaint;
        break;
    }

    final Rect trackRect = getPreferredRect(
      parentBox: parentBox,
      offset: offset,
      sliderTheme: sliderTheme,
      isEnabled: isEnabled,
      isDiscrete: isDiscrete,
    );
    const Radius activeTrackRadius = Radius.circular(5);

    context.canvas.drawRRect(
      RRect.fromLTRBAndCorners(
        trackRect.left,
        trackRect.top - (additionalTrackHeight / 2),
        thumbCenter.dx,
        trackRect.bottom + (additionalTrackHeight / 2),
        topLeft: activeTrackRadius,
        bottomLeft: activeTrackRadius,
      ),
      leftTrackPaint,
    );
    context.canvas.drawRRect(
      RRect.fromLTRBAndCorners(
        thumbCenter.dx,
        trackRect.top - (additionalTrackHeight / 2),
        trackRect.right,
        trackRect.bottom + (additionalTrackHeight / 2),
        topRight: activeTrackRadius,
        bottomRight: activeTrackRadius,
      ),
      rightTrackPaint,
    );
  }
}
