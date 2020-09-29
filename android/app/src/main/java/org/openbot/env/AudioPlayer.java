// Created by Matthias Mueller - Intel Intelligent Systems Lab - 2020

package org.openbot.env;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import java.io.File;
import java.io.IOException;

// Convert text to speech
// https://ttsmp3.com

public class AudioPlayer {
  private MediaPlayer mp;
  private Context mContext;

  public AudioPlayer(Context context) {
    mp = new MediaPlayer();
    mContext = context;
  }

  // Play from a resource file
  public void play(int id) {
    try {
      mp.reset();
      mp = mp.create(mContext, id);
      mp.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // Play from a file in storage
  public void play(String path) {
    try {
      mp.reset();
      mp.setDataSource(path);
      mp.prepare();
      mp.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // Play from media asset
  public void play(String assetFolder, String fileName) {
    try {
      AssetFileDescriptor afd =
          mContext
              .getAssets()
              .openFd("media" + File.separator + assetFolder + File.separator + fileName);
      mp.reset();
      mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
      mp.prepare();
      mp.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
