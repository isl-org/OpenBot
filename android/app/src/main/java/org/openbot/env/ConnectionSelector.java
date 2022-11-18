package org.openbot.env;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionSelector {
  private static final String TAG = "ConnectionManager";
  private static ConnectionSelector _connectionSelector;
  private static Context _context;
  private ILocalConnection connection;

  private final ILocalConnection networkConnection = new NetworkServiceConnection();
  private final ILocalConnection mobileConnection = new MobileNetworkConnection();
  private final ILocalConnection nearbyConnection = new NearbyConnection();

  private ConnectionSelector() {
    if (_connectionSelector != null) {
      throw new RuntimeException(
          "Use getInstance() method to get the single instance of this class.");
    }
  }

  public static ConnectionSelector getInstance(Context context) {
    _context = context;
    if (_connectionSelector == null) {

      synchronized (ConnectionSelector.class) {
        if (_connectionSelector == null) _connectionSelector = new ConnectionSelector();
      }
    }

    return _connectionSelector;
  }

  ILocalConnection getConnection() {
    if (connection != null) {
      return connection;
    }

    if (isConnectedViaWifi()) {
      Log.i(TAG, "Connected via Wifi");
      connection = networkConnection;
    } else if (isConnectedViaMobile()) {
      Log.i(TAG, "Connected via mobile network");
      connection = mobileConnection;
    }
    else {
      Log.i(TAG, "Connected via Peer-to-Peer");
      connection = nearbyConnection;
    }

    return connection;
  }

  private boolean isConnectedViaWifi() {
    ConnectivityManager connectivityManager =
        (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    return mWifi.isConnected();
  }

  private boolean isConnectedViaMobile() {
    ConnectivityManager connectivityManager =
            (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo mMobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    return mMobile.isConnected();
  }
}
