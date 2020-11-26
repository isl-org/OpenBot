/* Copyright 2019 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

// Modified by Matthias Mueller - Intel Intelligent Systems Lab - 2020

package org.openbot.tracking;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import org.openbot.CameraActivity.ControlSignal;
import org.openbot.env.BorderedText;
import org.openbot.env.ImageUtils;
import org.openbot.env.Logger;
import org.openbot.tflite.Detector.Recognition;
import org.tensorflow.lite.examples.posenet.lib.BodyPart;
import org.tensorflow.lite.examples.posenet.lib.KeyPoint;
import org.tensorflow.lite.examples.posenet.lib.Person;
import org.tensorflow.lite.examples.posenet.lib.Position;

import static java.lang.StrictMath.abs;
import static org.openbot.env.PoseNetUtilsKt.MODEL_HEIGHT;
import static org.openbot.env.PoseNetUtilsKt.MODEL_WIDTH;

/** A tracker that handles non-max suppression and matches existing objects to new detections. */
public class MultiBoxTracker {
  private static final float TEXT_SIZE_DIP = 18;
  private static final float MIN_SIZE = 16.0f;
  private static final int[] COLORS = {
    Color.BLUE,
    Color.RED,
    Color.GREEN,
    Color.YELLOW,
    Color.CYAN,
    Color.MAGENTA,
    Color.WHITE,
    Color.parseColor("#55FF55"),
    Color.parseColor("#FFA500"),
    Color.parseColor("#FF8888"),
    Color.parseColor("#AAAAFF"),
    Color.parseColor("#FFFFAA"),
    Color.parseColor("#55AAAA"),
    Color.parseColor("#AA33AA"),
    Color.parseColor("#0D0068")
  };
  final List<Pair<Float, RectF>> screenRects = new LinkedList<Pair<Float, RectF>>();
  private final Logger logger = new Logger();
  private final Queue<Integer> availableColors = new LinkedList<Integer>();
  private final List<TrackedRecognition> trackedObjects = new LinkedList<TrackedRecognition>();
  private final Paint boxPaint = new Paint();
  private final float textSizePx;
  private final BorderedText borderedText;
  private Matrix frameToCanvasMatrix;
  private int frameWidth;
  private int frameHeight;
  private int sensorOrientation;
  private float leftControl;
  private float rightControl;
  private Person person;
  private float minConfidencePose = 0.50f;
  private float circleRadius = 8.0f;
  private Paint paint = new Paint();

  List<Pair<BodyPart, BodyPart>> bodyJoints = Arrays.asList(
          new Pair(BodyPart.LEFT_WRIST, BodyPart.LEFT_ELBOW),
          new Pair(BodyPart.LEFT_ELBOW, BodyPart.LEFT_SHOULDER),
          new Pair(BodyPart.LEFT_SHOULDER, BodyPart.RIGHT_SHOULDER),
          new Pair(BodyPart.RIGHT_SHOULDER, BodyPart.RIGHT_ELBOW),
          new Pair(BodyPart.RIGHT_ELBOW, BodyPart.RIGHT_WRIST),
          new Pair(BodyPart.LEFT_SHOULDER, BodyPart.LEFT_HIP),
          new Pair(BodyPart.LEFT_HIP, BodyPart.RIGHT_HIP),
          new Pair(BodyPart.RIGHT_HIP, BodyPart.RIGHT_SHOULDER),
          new Pair(BodyPart.LEFT_HIP, BodyPart.LEFT_KNEE),
          new Pair(BodyPart.LEFT_KNEE, BodyPart.LEFT_ANKLE),
          new Pair(BodyPart.RIGHT_HIP, BodyPart.RIGHT_KNEE),
          new Pair(BodyPart.RIGHT_KNEE, BodyPart.RIGHT_ANKLE)
  );


  public MultiBoxTracker(final Context context) {
    for (final int color : COLORS) {
      availableColors.add(color);
    }

    boxPaint.setColor(Color.RED);
    boxPaint.setStyle(Style.STROKE);
    boxPaint.setStrokeWidth(10.0f);
    boxPaint.setStrokeCap(Cap.ROUND);
    boxPaint.setStrokeJoin(Join.ROUND);
    boxPaint.setStrokeMiter(100);

    textSizePx =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, context.getResources().getDisplayMetrics());
    borderedText = new BorderedText(textSizePx);
  }

  public synchronized void setFrameConfiguration(
      final int width, final int height, final int sensorOrientation) {
    frameWidth = width;
    frameHeight = height;
    this.sensorOrientation = sensorOrientation;
  }

  public synchronized void drawDebug(final Canvas canvas) {
    final Paint textPaint = new Paint();
    textPaint.setColor(Color.WHITE);
    textPaint.setTextSize(60.0f);

    final Paint boxPaint = new Paint();
    boxPaint.setColor(Color.RED);
    boxPaint.setAlpha(200);
    boxPaint.setStyle(Style.STROKE);

    for (final Pair<Float, RectF> detection : screenRects) {
      final RectF rect = detection.second;
      canvas.drawRect(rect, boxPaint);
      Paint paint = new Paint();
      paint.setColor(Color.RED);
//      paint.setTextSize(80.0f);
      paint.setStrokeWidth(8.0f);
      canvas.drawCircle(50, 50, 60f, paint);
      canvas.drawText("" + detection.first, rect.left, rect.top, textPaint);
      borderedText.drawText(canvas, rect.centerX(), rect.centerY(), "" + detection.first);
    }
  }

  public synchronized void trackResults(final List<Recognition> results, final long timestamp) {
    logger.i("Processing %d results from %d", results.size(), timestamp);
    processResults(results);
  }

  public synchronized void trackKeypoint(Person person, final long timestamp) {
    logger.i("Processing keypoint from %d", timestamp);

    processKeypoint(person);
  }

  private Matrix getFrameToCanvasMatrix() {
    return frameToCanvasMatrix;
  }

  private void updateFrameToCanvasMatrix(int canvasHeight, int canvasWidth) {
    final boolean rotated = sensorOrientation % 180 == 90;
    final float multiplier =
        Math.min(
            canvasHeight / (float) (rotated ? frameWidth : frameHeight),
            canvasWidth / (float) (rotated ? frameHeight : frameWidth));
    frameToCanvasMatrix =
        ImageUtils.getTransformationMatrix(
            frameWidth,
            frameHeight,
            (int) (multiplier * (rotated ? frameHeight : frameWidth)),
            (int) (multiplier * (rotated ? frameWidth : frameHeight)),
            sensorOrientation,
            new RectF(0, 0, 0, 0),
            false);
  }

  public synchronized ControlSignal updateTarget() {
    if (!trackedObjects.isEmpty()) {
      // Pick person with highest probability
      final RectF trackedPos = new RectF(trackedObjects.get(0).location);
      final boolean rotated = sensorOrientation % 180 == 90;
      float imgWidth = (float) (rotated ? frameHeight : frameWidth);
      float centerX = (rotated ? trackedPos.centerY() : trackedPos.centerX());
      // Make sure object center is in frame
      centerX = Math.max(0.0f, Math.min(centerX, imgWidth));
      // Scale relative position along x-axis between -1 and 1
      float x_pos_norm = 1.0f - 2.0f * centerX / imgWidth;
      // Scale to control signal and account for rotation
      float x_pos_scaled = rotated ? -x_pos_norm * 1.0f : x_pos_norm * 1.0f;
      //// Scale by "exponential" function: y = x / sqrt(1-x^2)
      // Math.max (Math.min(x_pos_norm / Math.sqrt(1 - x_pos_norm * x_pos_norm),2),-2) * 255.0f;

      if (x_pos_scaled < 0) {
        leftControl = 1.0f;
        rightControl = 1.0f + x_pos_scaled;
      } else {
        leftControl = 1.0f - x_pos_scaled;
        rightControl = 1.0f;
      }
    } else {
      leftControl = 0.0f;
      rightControl = 0.0f;
    }
    return new ControlSignal(
        (0 > sensorOrientation) ? rightControl : leftControl,
        (0 > sensorOrientation) ? leftControl : rightControl);
  }

  public synchronized void draw(final Canvas canvas) {
    updateFrameToCanvasMatrix(canvas.getHeight(), canvas.getWidth());

    for (final TrackedRecognition recognition : trackedObjects) {
      final RectF trackedPos = new RectF(recognition.location);

      getFrameToCanvasMatrix().mapRect(trackedPos);
      boxPaint.setColor(recognition.color);

      float cornerSize = Math.min(trackedPos.width(), trackedPos.height()) / 8.0f;
      canvas.drawRoundRect(trackedPos, cornerSize, cornerSize, boxPaint);
      // Just for testing
//      Paint paint = new Paint();
//      paint.setColor(Color.RED);
//      paint.setStrokeWidth(8.0f);
//      canvas.drawCircle(50, 50, 60f, paint);

      final String labelString =
          !TextUtils.isEmpty(recognition.title)
              ? String.format(
                  Locale.US, "%s %.2f", recognition.title, (100 * recognition.detectionConfidence))
              : String.format(Locale.US, "%.2f", 100 * recognition.detectionConfidence);
      borderedText.drawText(
          canvas, trackedPos.left + cornerSize, trackedPos.top, labelString + "%", boxPaint);

      //      if (recognition == trackedObjects.get(0)) {
      //        borderedText.drawText(
      //                canvas,
      //                trackedPos.left + cornerSize,
      //                trackedPos.top + 40.0f,
      //                String.format(Locale.US, "%.2f", leftControl) + "," + String.format("%.2f",
      // rightControl),
      //                boxPaint);
      //      }
      if (person != null) {
        drawKeypoints(canvas);
      }
    }
  }

  public void clearTrackedObjects() {
    trackedObjects.clear();
  }

  private void processKeypoint(Person person) {
    final Matrix rgbFrameToScreen = new Matrix(getFrameToCanvasMatrix());
    this.person = person;
  }

  private void processResults(final List<Recognition> results) {
    final List<Pair<Float, Recognition>> rectsToTrack = new LinkedList<Pair<Float, Recognition>>();

    screenRects.clear();
    final Matrix rgbFrameToScreen = new Matrix(getFrameToCanvasMatrix());

    for (final Recognition result : results) {
      if (result.getLocation() == null) {
        continue;
      }
      final RectF detectionFrameRect = new RectF(result.getLocation());

      final RectF detectionScreenRect = new RectF();
      rgbFrameToScreen.mapRect(detectionScreenRect, detectionFrameRect);

      logger.v(
          "Result! Frame: " + result.getLocation() + " mapped to screen:" + detectionScreenRect);

      screenRects.add(new Pair<Float, RectF>(result.getConfidence(), detectionScreenRect));

      if (detectionFrameRect.width() < MIN_SIZE || detectionFrameRect.height() < MIN_SIZE) {
        logger.w("Degenerate rectangle! " + detectionFrameRect);
        continue;
      }

      rectsToTrack.add(new Pair<Float, Recognition>(result.getConfidence(), result));
    }

    // Clear so objects don't stay if nothing detected.
    trackedObjects.clear();

    if (rectsToTrack.isEmpty()) {
      logger.v("Nothing to track, aborting.");
      return;
    }

    // trackedObjects.clear();
    for (final Pair<Float, Recognition> potential : rectsToTrack) {
      final TrackedRecognition trackedRecognition = new TrackedRecognition();
      trackedRecognition.detectionConfidence = potential.first;
      trackedRecognition.location = new RectF(potential.second.getLocation());
      trackedRecognition.title = potential.second.getTitle();
      trackedRecognition.color = COLORS[trackedObjects.size()];
      trackedObjects.add(trackedRecognition);

      if (trackedObjects.size() >= COLORS.length) {
        break;
      }
    }
  }

  private static class TrackedRecognition {
    RectF location;
    float detectionConfidence;
    int color;
    String title;
  }

  private void drawKeypoints(Canvas canvas) {
//    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    // Draw `bitmap` and `person` in square canvas.
    int screenWidth;
    int screenHeight;
    int left;
    int right;
    int top;
    int bottom;
    if (canvas.getHeight() > canvas.getWidth()) {
      screenWidth = canvas.getWidth();
      screenHeight = canvas.getHeight();
      left = 0;
      top = (canvas.getHeight() - canvas.getWidth()) / 2;
    } else {
      screenWidth = canvas.getWidth();
      screenHeight = canvas.getHeight();
      left = (canvas.getWidth() - canvas.getHeight()) / 2;
      top = 0;
    }
    right = left + screenWidth;
    bottom = top + screenHeight;

    setPaint();
//    canvas.drawBitmap(
//            bitmap,
//            new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
//            new Rect(left, top, right, bottom),
//            paint
//    );

    float widthRatio = (float) screenWidth / MODEL_WIDTH;
    float heightRatio = (float) screenHeight / MODEL_HEIGHT;

    // Draw key points over the image.
    for (KeyPoint keyPoint : person.getKeyPoints()) {
      if (keyPoint.getScore() > minConfidencePose) {
        Position position = keyPoint.getPosition();
        float adjustedX = position.getX() * widthRatio + left;
        float adjustedY = position.getY() * heightRatio + top;
        canvas.drawCircle(adjustedX, adjustedY, circleRadius, paint);
      }
    }

    int offsetY = 300;
    for (Pair<BodyPart, BodyPart> line : bodyJoints) {
      if ((person.getKeyPoints().get(line.first.ordinal()).getScore() > minConfidencePose) &&
              (person.getKeyPoints().get(line.second.ordinal()).getScore() > minConfidencePose)
      ) {
        canvas.drawLine(
                person.getKeyPoints().get(line.first.ordinal()).getPosition().getX() * widthRatio + left,
                person.getKeyPoints().get(line.first.ordinal()).getPosition().getY() * heightRatio + top,
                person.getKeyPoints().get(line.second.ordinal()).getPosition().getX() * widthRatio + left,
                person.getKeyPoints().get(line.second.ordinal()).getPosition().getY() * heightRatio + top,
                paint
        );
      }
    }
//
//    canvas.drawText(
//            String.format("Score: %.2f", person.getScore()),
//            (15.0f * widthRatio),
//            (30.0f * heightRatio + bottom),
//            paint
//    );
//    canvas.drawText(
//            String.format("Device: %s", posenet.getDevice()),
//            (15.0f * widthRatio),
//            (50.0f * heightRatio + bottom),
//            paint
//    );
//    canvas.drawText(
//            String.format("Time: %d ms", lastProcessingTimeMs),
//            (15.0f * widthRatio),
//            (70.0f * heightRatio + bottom),
//            paint
//    );

    // Draw!
//        surfaceHolder.unlockCanvasAndPost(canvas);
  }

  protected void setPaint() {
    paint.setColor(Color.RED);
    paint.setTextSize(80.0f);
    paint.setStrokeWidth(8.0f);
  }

  private Bitmap cropBitmap(Bitmap rotatedBitmap) {
//    Matrix matrix = new Matrix();
//    matrix.preRotate(180f);
//    matrix.preScale(-1f, 1f);
//    Bitmap rotatedBitmap = Bitmap.createBitmap(croppedBitmap, 0, 0, croppedBitmap.getWidth(), croppedBitmap.getHeight(), matrix, false);
    float bitmapRatio = (float) rotatedBitmap.getHeight() / rotatedBitmap.getWidth();
    float modelInputRatio = (float) MODEL_HEIGHT / MODEL_WIDTH;
    Bitmap croppedBitmap;
    // Acceptable difference between the modelInputRatio and bitmapRatio to skip cropping.
    double maxDifference = 1e-5;

    // Checks if the bitmap has similar aspect ratio as the required model input.
    if(abs(modelInputRatio - bitmapRatio) < maxDifference)
      return rotatedBitmap;
    else if (modelInputRatio < bitmapRatio) {
      float cropHeight = (float) rotatedBitmap.getHeight() - (rotatedBitmap.getWidth() / modelInputRatio);
      croppedBitmap = Bitmap.createBitmap(
              rotatedBitmap,
              0,
              (int) (cropHeight / 2),
              rotatedBitmap.getWidth(),
              (int) (rotatedBitmap.getHeight() - cropHeight));
    }
    else{
      float cropWidth = (float) rotatedBitmap.getWidth() - (rotatedBitmap.getHeight() * modelInputRatio);
      croppedBitmap = Bitmap.createBitmap(
              rotatedBitmap,
              (int) (cropWidth / 2),
              0,
              (int) (rotatedBitmap.getWidth() - cropWidth),
              rotatedBitmap.getHeight());
    }
    return croppedBitmap;

  }
}
