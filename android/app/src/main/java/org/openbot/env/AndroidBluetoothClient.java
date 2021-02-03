package org.openbot.env;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Set;
import java.util.UUID;

public class AndroidBluetoothClient {
    final String command = "HELO\n";
    final UUID sppUuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");

    void run() {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            return;
        }

        if (!btAdapter.isEnabled()) {
            return;
        }

        BluetoothDevice btDevice = null;
        Set<BluetoothDevice> bondedDevices = btAdapter.getBondedDevices();
        for (BluetoothDevice dev : bondedDevices) {

            if (dev.getName().equals("MyDeviceName")) {
                btDevice = dev;
            }
        }

        if (btDevice == null) {
            return;
        }

        BluetoothSocket btSocket;
        try {
            btSocket = btDevice.createRfcommSocketToServiceRecord(sppUuid);
        } catch (IOException ex) {
            return;
        }

        for (int i = 0; ; i++) {
            try {
                btSocket.connect();
            } catch (IOException ex) {
                if (i < 5) {
                    continue;
                }

                return;
            }
            break;
        }

        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(btSocket.getOutputStream(), "ASCII"));
            writer.write(command);
            writer.flush();
        } catch (IOException ex) {
            return;
        }

        String output;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(btSocket.getInputStream(), "ASCII"));
            output = reader.readLine();
        } catch (IOException ex) {
            return;
        }

        try {
            btSocket.close();
        } catch (IOException ex) {
            return;
        }
    }
}