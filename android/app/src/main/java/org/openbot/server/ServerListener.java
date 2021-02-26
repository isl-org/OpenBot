package org.openbot.server;

public interface ServerListener {
  void onAddModel(String model);

  void onRemoveModel(String model);

  void onConnectionEstablished(String ipAddress);
}
