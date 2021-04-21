package org.openbot.env;

public class ConnectionManager {
    ILocalConnection getConnection() {
        return new NetworkServiceConnection();
    }
}
