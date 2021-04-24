package org.openbot.env;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionManager {
  private static final String TAG = "ConnectionManager";
  private static ConnectionManager _connectionManager;
  private static Context _context;
  private ILocalConnection connection;

  private final ILocalConnection networkConnection = new NetworkServiceConnection();
  private final ILocalConnection nearbyConnection = new NearbyConnection();

  private ConnectionManager() {
    if (_connectionManager != null) {
      throw new RuntimeException(
          "Use getInstance() method to get the single instance of this class.");
    }
  }

  public static ConnectionManager getInstance(Context context) {
    _context = context;
    if (_connectionManager == null) {

      synchronized (ConnectionManager.class) {
        if (_connectionManager == null) _connectionManager = new ConnectionManager();
      }
    }

    return _connectionManager;
  }

  ILocalConnection getConnection() {
    if (connection != null) {
      return connection;
    }

    if (isConnectedViaWifi()) {
      connection = networkConnection;
    } else {
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
}
