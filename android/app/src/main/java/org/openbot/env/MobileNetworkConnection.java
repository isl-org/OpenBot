package org.openbot.env;

import static timber.log.Timber.e;
import static timber.log.Timber.i;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.openbot.utils.ConnectionUtils;
import timber.log.Timber;

public class MobileNetworkConnection implements ILocalConnection {

  private static final String TAG = "MobileNetworkConn";
  private Context context;

  private String HOST;
  private int PORT;
  private IDataReceived dataReceivedCallback;
  private SocketHandler socketHandler;
  private BlockingQueue<String> messageQueue = new ArrayBlockingQueue<>(25);
  private boolean stopped = true;

  @Override
  public void init(Context context) {
    socketHandler = new SocketHandler(messageQueue);
  }

  @Override
  public void setDataCallback(IDataReceived dataCallback) {
    this.dataReceivedCallback = dataCallback;
  }

  @Override
  public void connect(Context context) {
    this.context = context;
    start();
    runConnection();
  }

  @Override
  public void disconnect(Context context) {
    stop();

    if (socketHandler == null) {
      return;
    }
    socketHandler.close();
  }

  @Override
  public void stop() {
    stopped = true;
    BotToControllerEventBus.emitEvent(ConnectionUtils.createStatus("CONNECTION_ACTIVE", false));
  }

  @Override
  public void start() {

    stopped = false;
    BotToControllerEventBus.emitEvent(ConnectionUtils.createStatus("CONNECTION_ACTIVE", true));
  }

  @Override
  public void setServerAddress(String ip, String port) {
    this.HOST = ip;
    this.PORT = Integer.parseInt(port);
  }

  @Override
  public boolean isVideoCapable() {
    return true;
  }

  @Override
  public boolean isConnected() {
    return socketHandler != null && socketHandler.isConnected();
  }

  @Override
  public void sendMessage(String message) {
    if (socketHandler != null) {
      socketHandler.put(message);
    }
  }
  // end of interface

  private void runConnection() {
    Timber.d("PORT: " + PORT + ", address: " + HOST);

    ((Activity) context)
        .runOnUiThread(
            () -> {
              ControllerToBotEventBus.emitEvent("{command: \"CONNECTED\"}");
            });

    new Thread("Receiver Thread") {
      public void run() {
        SocketHandler.ClientInfo clientInfo = socketHandler.connect(HOST, PORT);
        if (clientInfo == null) {
          Timber.d("Could not get a connection");
          return;
        }
        startReceiver(socketHandler, clientInfo.reader);
        startSender(socketHandler, clientInfo.writer);
      }
    }.start();
  }

  private void startReceiver(SocketHandler socketHandler, Scanner reader) {
    new Thread("startReceiver Thread") {
      public void run() {
        socketHandler.runReceiver(reader);
      }
    }.start();
  }

  private void startSender(SocketHandler socketHandler, OutputStream writer) {
    new Thread("startSender Thread") {
      public void run() {
        try {
          socketHandler.runSender(writer);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }.start();
  }

  class SocketHandler {
    private BlockingQueue<String> messageQueue;
    private Socket client;

    boolean isConnected() {
      return client != null && !client.isClosed();
    }

    SocketHandler(BlockingQueue<String> messageQueue) {
      this.messageQueue = messageQueue;
    }

    class ClientInfo {
      Scanner reader;
      OutputStream writer;

      ClientInfo(Scanner reader, OutputStream writer) {
        this.reader = reader;
        this.writer = writer;
      }
    }

    ClientInfo connect(String host, int port) {
      ClientInfo clientInfo;

      try {
        client = new Socket(host, port);
        clientInfo =
            new ClientInfo(
                new Scanner(new DataInputStream(new BufferedInputStream(client.getInputStream()))),
                client.getOutputStream());
      } catch (Exception e) {
        return null;
      }

      return clientInfo;
    }

    void runReceiver(Scanner reader) {
      try {
        while (true) {
          String msg = reader.nextLine().trim();

          if (!stopped) {
            ((Activity) context).runOnUiThread(() -> dataReceivedCallback.dataReceived(msg));
          }
        }
      } catch (Exception e) {
        close();
      }
    }

    void put(String message) {
      try {
        this.messageQueue.put(message);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    @SuppressLint("TimberArgCount")
    void runSender(OutputStream writer) {
      while (true) {
        try {
          String message = messageQueue.take();
          i(TAG, "queue capacity: " + messageQueue.remainingCapacity());
          writer.write((message + "\n").getBytes(StandardCharsets.UTF_8));
        } catch (InterruptedException | IOException e) {
          i(TAG, "runSender got exception: " + e);
          close();

          // reconnect again
          if (isConnected()) {
            runConnection();
          }
          break;
        }
      }
    }

    void close() {
      try {
        if (client == null || client.isClosed()) {
          return;
        }
        client.close();

        ((Activity) context)
            .runOnUiThread(
                () -> {
                  ControllerToBotEventBus.emitEvent("{command: \"DISCONNECTED\"}");
                });
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
