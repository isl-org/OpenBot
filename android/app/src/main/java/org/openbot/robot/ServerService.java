package org.openbot.robot;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Environment;
import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openbot.R;

class ServerService {

  private static final String TAG = "Server";

  public interface ServerListener {
    void onAddModel(String model);

    void onRemoveModel(String model);
  }

  private final AsyncHttpClient client;
  private final Context context;
  private final NsdService nsdService;
  private final NsdManager.ResolveListener resolveListener =
      new NsdManager.ResolveListener() {
        @Override
        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
          // Called when the resolve fails.  Use the error code to debug.
          Log.e(TAG, "Resolve failed " + errorCode);
        }

        @Override
        public void onServiceResolved(NsdServiceInfo serviceInfo) {
          nsdService.stop();
          serverUrl =
              "http://" + serviceInfo.getHost().getHostAddress() + ":" + serviceInfo.getPort();
          Log.d(TAG, "Resolved address: " + serverUrl);

          client.get(context, serverUrl + "/test", testResponseHandler);
        }
      };
  private final JsonHttpResponseHandler testResponseHandler =
      new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
          Log.d(TAG, "Server found: " + response.toString());
          try {
            uploadAll();
          } catch (Exception e) {
            Log.w(TAG, e);
          }
        }

        @Override
        public void onFailure(
            int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
          Log.d(TAG, "Server error: " + throwable.toString());
        }
      };
  private final JsonHttpResponseHandler modelListHandler =
      new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
          File dir = context.getFilesDir();

          if (!dir.exists()) {
            if (!dir.mkdirs()) {
              Log.i(TAG, "Make dir failed");
            }
          }

          HashSet<String> valid = new HashSet<>();
          for (int i = 0; i < response.length(); i++) {
            try {
              String name = response.optJSONObject(i).getString("name");
              valid.add(name);
              long serverFileTime = response.optJSONObject(i).getLong("mtime") * 1000;
              File toFile = new File(dir + File.separator + name);
              if (toFile.exists()) {
                long localFileTime = toFile.lastModified();
                if (localFileTime >= serverFileTime) {
                  continue;
                }
                Log.d(TAG, "Update model: " + name);
                Log.d(TAG, String.format("File times: %d < %d", localFileTime, serverFileTime));
              } else {
                Log.d(TAG, "Download new model: " + name);
              }

              client.get(
                  context,
                  serverUrl + "/models/" + name,
                  new FileAsyncHttpResponseHandler(toFile) {
                    @Override
                    public void onFailure(
                        int statusCode, Header[] headers, Throwable throwable, File file) {
                      Log.e(TAG, "Download error: " + name, throwable);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, File file) {
                      serverListener.onAddModel(name);
                      if (file.setLastModified(serverFileTime)) {
                        Log.i(TAG, "Successful download: " + name);
                      } else {
                        Log.e(TAG, "Set file time error: " + name);
                      }
                    }
                  });
            } catch (JSONException e) {
              Log.e(TAG, "JSON error", e);
            }
          }

          String[] list = dir.list((dir1, name) -> name.endsWith(".tflite"));
          if (list != null) {
            for (String name : list) {
              if (!valid.contains(name)) {
                File file = new File(dir + File.separator + name);
                if (file.delete()) {
                  serverListener.onRemoveModel(name);
                  Log.d(TAG, "deleted: " + name);
                } else {
                  Log.e(TAG, "delete error: " + name);
                }
              }
            }
          }
        }
      };
  private final Timer timer;
  private final ServerListener serverListener;

  private String serverUrl;

  public ServerService(Context context, ServerListener serverListener) {
    this.client = new AsyncHttpClient();
    this.context = context;
    this.nsdService = new NsdService();
    this.serverListener = serverListener;
    this.timer = new Timer();
  }

  public void start() {
    Log.d(TAG, "service started");
    nsdService.start(context, resolveListener);
    timer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            if (serverUrl != null) {
              Log.d(TAG, "Check for new models");
              client.get(context, serverUrl + "/models", modelListHandler);
            }
          }
        },
        0,
        10000);
  }

  public void upload(File file) {
    if (serverUrl == null) {
      return;
    }
    if (serverUrl.isEmpty()) {
      return;
    }
    long size = file.length() / 1024 / 1024;
    Log.d(TAG, String.format("Start upload %s (%d MB)", file.getName(), size));

    RequestParams params = new RequestParams();
    try {
      params.put("file", file);
    } catch (FileNotFoundException e) {
      Log.e(TAG, "File not found: " + file.getAbsolutePath());
      return;
    }

    client.post(context, serverUrl + "/upload", params, new UploadResponseHandler(file));
  }

  public void uploadAll() {
    String logDir =
        Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator
            + context.getString(R.string.app_name);
    File directory = new File(logDir);
    File[] files = directory.listFiles();
    if (files != null) {
      for (File file : files) {
        if (file.getName().endsWith(".zip")) {
          upload(file);
        }
      }
    }
  }

  public void stop() {
    client.cancelRequests(context, true);
    timer.cancel();
  }

  static class UploadResponseHandler extends JsonHttpResponseHandler {
    private final File file;

    public UploadResponseHandler(File file) {
      super();
      this.file = file;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
      if (file.delete()) {
        Log.d(TAG, "uploaded: " + file.getName());
      } else {
        Log.e(TAG, "delete error: " + file.getName());
      }
    }
  }
}
