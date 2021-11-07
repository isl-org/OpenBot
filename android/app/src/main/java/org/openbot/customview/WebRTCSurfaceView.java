/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openbot.customview;

import android.content.Context;
import android.util.AttributeSet;

public class WebRTCSurfaceView extends org.webrtc.SurfaceViewRenderer {

  private int ratioWidth = 0;
  private int ratioHeight = 0;

  public WebRTCSurfaceView(final Context context) {
    this(context, null);
  }

  public WebRTCSurfaceView(final Context context, final AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public WebRTCSurfaceView(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs);
  }
}
