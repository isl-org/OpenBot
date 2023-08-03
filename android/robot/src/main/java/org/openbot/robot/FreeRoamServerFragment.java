package org.openbot.robot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.openbot.R;
import org.openbot.common.ControlsFragment;
import org.openbot.databinding.FragmentFreeRoamServerBinding;
import org.openbot.env.ControllerToBotEventBus;
import org.openbot.utils.Constants;
import org.openbot.utils.Enums;

import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import timber.log.Timber;

public class FreeRoamServerFragment extends ControlsFragment {

    private FragmentFreeRoamServerBinding binding;

    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFreeRoamServerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            WebSocket webSocket;
//            String serverUrl = "ws://inconclusive-warm-shamrock.glitch.me";
            String serverUrl = "ws://192.168.1.11:8080";

        OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(serverUrl).build();

            WebSocketListener webSocketListener = new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                    // Called when the connection is established
                    System.out.println("WebSocket connection opened");
                    ControllerToBotEventBus.emitEvent("{command: \"CONNECTED\"}");
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    // Called when a message is received from the server
                    System.out.println("Received message: " + text);
                    ControllerToBotEventBus.emitEvent(text);
                }

                @Override
                public void onMessage(WebSocket webSocket, ByteString bytes) {
                    // Called when binary message is received from the server
                    System.out.println("Received binary message");
                }

                @Override
                public void onClosing(WebSocket webSocket, int code, String reason) {
                    // Called when the connection is closing
                    System.out.println("WebSocket connection closing");
                }

                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
                    // Called when the connection is closed
                    System.out.println("WebSocket connection closed");
                }

                @Override
                public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                    // Called when an error occurs
                    System.err.println("WebSocket error: " + t.getMessage());
                }
            };

            webSocket = client.newWebSocket(request, webSocketListener);
    }

    @Override
    protected void processControllerKeyData(String commandType) {
        switch (commandType) {
            case Constants.CMD_DRIVE:
//                handleDriveCommand();
                break;

            case Constants.CMD_INDICATOR_LEFT:
//                toggleIndicator(Enums.VehicleIndicator.LEFT.getValue());
                break;

            case Constants.CMD_INDICATOR_RIGHT:
//                toggleIndicator(Enums.VehicleIndicator.RIGHT.getValue());
                break;

            case Constants.CMD_INDICATOR_STOP:
//                toggleIndicator(Enums.VehicleIndicator.STOP.getValue());
                break;

            case Constants.CMD_DRIVE_MODE:
                setDriveMode(Enums.switchDriveMode(vehicle.getDriveMode()));
                break;

            case Constants.CMD_DISCONNECTED:
//                handleDriveCommand();
//                setControlMode(Enums.ControlMode.GAMEPAD);
                break;

            case Constants.CMD_SPEED_DOWN:
                setSpeedMode(
                        Enums.toggleSpeed(
                                Enums.Direction.DOWN.getValue(),
                                Enums.SpeedMode.getByID(preferencesManager.getSpeedMode())));
                break;

            case Constants.CMD_SPEED_UP:
                setSpeedMode(
                        Enums.toggleSpeed(
                                Enums.Direction.UP.getValue(),
                                Enums.SpeedMode.getByID(preferencesManager.getSpeedMode())));
                break;
        }

    }

    @Override
    protected void processUSBData(String data) {

    }

    protected void setDriveMode(Enums.DriveMode driveMode) {
        if (driveMode != null) {
            switch (driveMode) {
                case DUAL:
//                    binding.controllerContainer.driveMode.setImageResource(R.drawable.ic_dual);
                    break;
                case GAME:
//                    binding.controllerContainer.driveMode.setImageResource(R.drawable.ic_game);
                    break;
                case JOYSTICK:
//                    binding.controllerContainer.driveMode.setImageResource(R.drawable.ic_joystick);
                    break;
            }

            Timber.d("Updating  driveMode: %s", driveMode);
            vehicle.setDriveMode(driveMode);
            preferencesManager.setDriveMode(driveMode.getValue());
        }
    }

    private void setSpeedMode(Enums.SpeedMode speedMode) {
        if (speedMode != null) {
            switch (speedMode) {
                case SLOW:
//                    binding.controllerContainer.speedMode.setImageResource(R.drawable.ic_speed_low);
                    break;
                case NORMAL:
//                    binding.controllerContainer.speedMode.setImageResource(R.drawable.ic_speed_medium);
                    break;
                case FAST:
//                    binding.controllerContainer.speedMode.setImageResource(R.drawable.ic_speed_high);
                    break;
            }

            Timber.d("Updating  controlSpeed: %s", speedMode);
            preferencesManager.setSpeedMode(speedMode.getValue());
            vehicle.setSpeedMultiplier(speedMode.getValue());
        }
    }
}
