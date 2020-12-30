package org.openbot.robot;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Environment;
import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;
import java.io.File;
import java.io.FileNotFoundException;
import org.openbot.R;

class UploadService {

  private final AsyncHttpClient client;
  private final Context context;
  private final NsdService nsdService;
  private final NsdManager.ResolveListener resolveListener =
      new NsdManager.ResolveListener() {
        @Override
        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
          // Called when the resolve fails.  Use the error code to debug.
          Log.e("NSD", "Resolve failed " + errorCode);
        }

        @Override
        public void onServiceResolved(NsdServiceInfo serviceInfo) {
          serverUrl =
              "http://" + serviceInfo.getHost().getHostAddress() + ":" + serviceInfo.getPort();
          Log.d("NSD", "Resolved address = " + serverUrl);

          client.get(
              context,
              serverUrl + "/test",
              new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                  Log.d("Upload", "Server found");
                }

                @Override
                public void onFailure(
                    int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                  Log.d("Upload", "Server error " + error.toString());
                }
              });
        }
      };

  private String serverUrl;

  public UploadService(Context context) {
    this.client = new AsyncHttpClient();
    this.context = context;
    this.nsdService = new NsdService();
  }

  public void start() {
    this.nsdService.start(context, resolveListener);
  }

  public void upload(File file) {
    if (serverUrl == null) {
      return;
    }
    if (serverUrl.isEmpty()) {
      return;
    }
    Log.d("Upload", "Start: " + serverUrl);

    RequestParams params = new RequestParams();
    try {
      params.put("file", file);
    } catch (FileNotFoundException e) {
      Log.e("Upload", "File not found: " + file.getAbsolutePath());
      return;
    }

    client.post(
        context,
        serverUrl + "/upload",
        params,
        new AsyncHttpResponseHandler() {
          @Override
          public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            // called when response HTTP status is "200 OK"
            if (file.delete()) {
              Log.d("Upload", "uploaded: " + file.getName());
            } else {
              Log.e("Upload", "delete error: " + file.getName());
            }
          }

          @Override
          public void onFailure(
              int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
          }
        });
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
  }
}
