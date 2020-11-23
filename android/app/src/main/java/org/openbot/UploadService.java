package org.openbot;

import android.content.Context;
import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;
import java.io.File;
import java.io.FileNotFoundException;

class UploadService {

  private final AsyncHttpClient client;
  private final Context context;

  public UploadService(Context context) {
    this.client = new AsyncHttpClient();
    this.context = context;
  }

  public void upload(String url, File file) {
    Log.d("Upload", "Start: " + url);

    RequestParams params = new RequestParams();
    try {
      params.put("file", file);
    } catch (FileNotFoundException e) {
      Log.e("Upload", "File not found: " + file.getAbsolutePath());
      return;
    }

    client.post(
        context,
        url + "/upload",
        params,
        new AsyncHttpResponseHandler() {
          @Override
          public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            // called when response HTTP status is "200 OK"
          }

          @Override
          public void onFailure(
              int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
          }
        });
  }

  public void stop() {
    client.cancelRequests(context, true);
  }
}
