package org.openbot.projects;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReadFileTask extends AsyncTask<Void, Void, Void> {
  String fileId;
  private ReadFileCallback callback;

  public ReadFileTask(String fileId, ReadFileCallback callback) {
    this.fileId = fileId;
    this.callback = callback;
  }

  protected Void doInBackground(Void... voids) {
    try {
      URL fileUrl =
          new URL("https://drive.google.com/uc?export=download&id=" + fileId + "&confirm=200");
      HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
      conn.setRequestMethod("GET");
      conn.connect();
      InputStream inputStream = conn.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      String line;
      StringBuilder stringBuilder = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        stringBuilder.append(line);
      }
      reader.close();
      inputStream.close();
      String fileContents = stringBuilder.toString();
      onPostExecute(fileContents);
    } catch (IOException e) {
      onPostExecuteFailed(e);
      e.printStackTrace();
    }
    return null;
  }

  protected void onPostExecute(String fileContents) {
    if (callback != null) {
      callback.onFileReadSuccess(fileContents);
    }
  }

  protected void onPostExecuteFailed(IOException e) {
    if (callback != null) {
      callback.onFileReadFailed(e);
    }
  }
}
