package org.openbot.utils;

import android.app.Activity;
import android.util.Log;
import android.util.Size;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import org.openbot.tflite.Model;

public class FileUtils {

  public static void copyFile(InputStream inputFile, String name, String outputPath) {

    InputStream in = null;
    OutputStream out = null;
    try {

      // create output directory if it doesn't exist
      File dir = new File(outputPath);
      if (!dir.exists()) {
        dir.mkdirs();
      }

      in = inputFile;
      out = new FileOutputStream(outputPath + '/' + name);

      byte[] buffer = new byte[1024];
      int read;
      while ((read = in.read(buffer)) != -1) {
        out.write(buffer, 0, read);
      }
      in.close();
      in = null;

      // write the output file (You have now copied the file)
      out.flush();
      out.close();
      out = null;

    } catch (FileNotFoundException fnfe1) {
      Log.e("tag", fnfe1.getMessage());
    } catch (Exception e) {
      Log.e("tag", e.getMessage());
    }
  }

  public static boolean checkFileExistence(Activity activity, String name) {
    boolean found = false;
    for (String s : Objects.requireNonNull(activity.getFilesDir().list()))
      if (s.equals(name)) {
        found = true;
        break;
      }
    return found;
  }

  public static List<Model> loadConfigJSONFromAsset(Activity activity) {
    String json;
    try {
      InputStream is = activity.getAssets().open("config.json");
      int size = is.available();
      byte[] buffer = new byte[size];
      is.read(buffer);
      is.close();
      json = new String(buffer, StandardCharsets.UTF_8);
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
    Gson gson = new GsonBuilder().registerTypeAdapterFactory(new PostProcessingEnabler()).create();

    JsonElement jsonElement =
        gson.fromJson(json, JsonElement.class).getAsJsonObject().get("models");

    Type listType = new TypeToken<List<Model>>() {}.getType();

    return gson.fromJson(jsonElement, listType);
  }
}
