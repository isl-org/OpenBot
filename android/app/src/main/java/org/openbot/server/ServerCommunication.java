package org.openbot.server;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Environment;
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
import timber.log.Timber;

public class ServerCommunication {

  private final AsyncHttpClient client;
  private final Context context;
  private final NsdService nsdService;
  private final NsdManager.ResolveListener resolveListener =
      new NsdManager.ResolveListener() {
        @Override
        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
          // Called when the resolve fails.  Use the error code to debug.
          Timber.e("Resolve failed %s", errorCode);
        }

        @Override
        public void onServiceResolved(NsdServiceInfo serviceInfo) {
          nsdService.stop();
          serverUrl =
              "http://" + serviceInfo.getHost().getHostAddress() + ":" + serviceInfo.getPort();
          Timber.d("Resolved address: %s", serverUrl);

          client.get(context, serverUrl + "/test", testResponseHandler);
          serverListener.onConnectionEstablished(serverUrl);
        }
      };
  private final JsonHttpResponseHandler testResponseHandler =
      new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
          Timber.d("Server found: %s", response.toString());
          try {
            uploadAll();
          } catch (Exception e) {
            Timber.w(e);
          }
        }

        @Override
        public void onFailure(
            int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
          Timber.d("Server error: %s", throwable.toString());
        }
      };
  private final JsonHttpResponseHandler modelListHandler =
      new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
          File dir = context.getFilesDir();

          if (!dir.exists()) {
            if (!dir.mkdirs()) {
              Timber.i("Make dir failed");
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
                Timber.d("Update model: %s", name);
                Timber.d("File times: %d < %d", localFileTime, serverFileTime);
              } else {
                Timber.d("Download new model: %s", name);
              }

              client.get(
                  context,
                  serverUrl + "/models/" + name,
                  new FileAsyncHttpResponseHandler(toFile) {
                    @Override
                    public void onFailure(
                        int statusCode, Header[] headers, Throwable throwable, File file) {
                      Timber.e(throwable, "Download error: %s", name);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, File file) {
                      serverListener.onAddModel(name);
                      if (file.setLastModified(serverFileTime)) {
                        Timber.i("Successful download: %s", name);
                      } else {
                        Timber.e("Set file time error: %s", name);
                      }
                    }
                  });
            } catch (JSONException e) {
              Timber.e(e, "JSON error");
            }
          }

          String[] list = dir.list((dir1, name) -> name.endsWith(".tflite"));
          if (list != null) {
            for (String name : list) {
              if (!valid.contains(name)) {
                File file = new File(dir + File.separator + name);
                if (file.delete()) {
                  serverListener.onRemoveModel(name);
                  Timber.d("deleted: %s", name);
                } else {
                  Timber.e("delete error: %s", name);
                }
              }
            }
          }
        }
      };
  private final Timer timer;
  private final ServerListener serverListener;

  private String serverUrl;

  public ServerCommunication(Context context, ServerListener serverListener) {
    this.client = new AsyncHttpClient();
    this.context = context;
    this.nsdService = new NsdService();
    this.serverListener = serverListener;
    this.timer = new Timer();
  }

  public void start() {
    Timber.d("service started");
    nsdService.start(context, resolveListener);
    timer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            if (serverUrl != null) {
              Timber.d("Check for new models");
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
    Timber.d("Start upload %s (%d MB)", file.getName(), size);

    RequestParams params = new RequestParams();
    try {
      params.put("file", file);
    } catch (FileNotFoundException e) {
      Timber.e("File not found: %s", file.getAbsolutePath());
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
        Timber.d("uploaded: %s", file.getName());
      } else {
        Timber.e("delete error: %s", file.getName());
      }
    }
  }
}
