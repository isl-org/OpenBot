package org.openbot.utils;

import android.app.Activity;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Type;
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
    String configFile = "config.json";
    Gson gson = new GsonBuilder().registerTypeAdapterFactory(new PostProcessingEnabler()).create();
    JsonElement jsonElement;
    Type listType = new TypeToken<List<Model>>() {}.getType();

    boolean fileExists = checkFileExistence(activity, configFile);
    if (fileExists) {
      try {
        jsonElement =
            gson.fromJson(
                    new FileReader(activity.getFilesDir() + File.separator + configFile),
                    JsonElement.class)
                .getAsJsonArray();

        return gson.fromJson(jsonElement, listType);

      } catch (FileNotFoundException e) {
        e.printStackTrace();
        return null;
      }
    }

    try {
      copyFile(
          activity.getAssets().open(configFile),
          configFile,
          activity.getFilesDir().getAbsolutePath());
      jsonElement =
          gson.fromJson(
                  new InputStreamReader(activity.getAssets().open(configFile)), JsonElement.class)
              .getAsJsonArray();
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }

    return gson.fromJson(jsonElement, listType);
  }

  public static boolean updateModelConfig(Activity activity, List<Model> modelList) {
    String configFile = "config.json";

    try {
      Writer writer = new FileWriter(activity.getFilesDir() + File.separator + configFile);
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(modelList, writer);
      writer.flush();
      writer.close();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  public static String nameWithoutExtension(String name) {
    return name.replaceFirst("[.][^.]+$", "");
  }
}
