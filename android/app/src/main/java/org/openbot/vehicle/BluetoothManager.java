package org.openbot.vehicle;

import android.content.Context;
import android.widget.Toast;

import com.ficat.easyble.BleDevice;
import com.ficat.easyble.BleManager;
import com.ficat.easyble.Logger;
import com.ficat.easyble.gatt.bean.CharacteristicInfo;
import com.ficat.easyble.gatt.bean.ServiceInfo;
import com.ficat.easyble.gatt.callback.BleConnectCallback;
import com.ficat.easyble.gatt.callback.BleNotifyCallback;
import com.ficat.easyble.gatt.callback.BleWriteCallback;
import com.ficat.easyble.scan.BleScanCallback;

import org.openbot.main.ScanDeviceAdapter;
import org.openbot.utils.ByteUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BluetoothManager {
    private BleManager manager;
    private CharacteristicInfo notifyCharacteristic;
    private CharacteristicInfo writeCharacteristic;
    private ServiceInfo writeServiceInfo;
    private ServiceInfo notifyServiceInfo;
    public List<BleDevice> deviceList = new ArrayList<>();
    public List<String> notifySuccessUuids = new ArrayList<>();
    public BleDevice bleDevice;
    private Context context;
    public ScanDeviceAdapter adapter;
    private int indexValue;

    public BluetoothManager(Context context) {
        this.context = context;
        initBleManager();
    }

    public void initBleManager() {
        //check if this android device supports ble
        if (!BleManager.supportBle(this.context)) {
            return;
        }
        //open bluetooth without a request dialog
        BleManager.toggleBluetooth(true);

        BleManager.ScanOptions scanOptions = BleManager.ScanOptions
                .newInstance()
                .scanPeriod(4000)
                .scanDeviceName(null);

        BleManager.ConnectOptions connectOptions = BleManager.ConnectOptions
                .newInstance()
                .connectTimeout(12000);

        manager = BleManager
                .getInstance()
                .setScanOptions(scanOptions)
                .setConnectionOptions(connectOptions)
                .setLog(true, "Bluetooth_Connection")
                .init(this.context);
    }

    public void startScan() {
        manager.startScan(new BleScanCallback() {
            @Override
            public void onLeScan(BleDevice device, int rssi, byte[] scanRecord) {
                for (BleDevice d : deviceList) {
                    if (device.address.equals(d.address)) {
                        return;
                    }
                }
                if (!device.name.equals("unknown")) {
                    deviceList.add(device);
                }
                if (bleDevice != null && !deviceList.contains(bleDevice) && bleDevice.connected) {
                    deviceList.add(bleDevice);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onStart(boolean startScanSuccess, String info) {
//                Log.e("TAG", "start scan = " + startScanSuccess + "   info: " + info);
                if (startScanSuccess) {
                    deviceList.clear();
                }
            }

            @Override
            public void onFinish() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void toggleConnection(int position) {
        indexValue = position;
        if (bleDevice != null && bleDevice.connected) {
            BleManager.getInstance().disconnect(bleDevice.address);
            bleDevice = null;
        } else {
            BleManager.getInstance().connect(bleDevice.address, connectCallback);
        }
    }

    public BleConnectCallback connectCallback = new BleConnectCallback() {
        @Override
        public void onStart(boolean startConnectSuccess, String info, BleDevice device) {
            bleDevice = device;
            deviceList.remove(indexValue);
            deviceList.add(indexValue, device);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onConnected(BleDevice device) {
            bleDevice = device;
            deviceList.remove(indexValue);
            deviceList.add(indexValue, device);
            adapter.notifyDataSetChanged();
            addDeviceInfoDataAndUpdate();
            Logger.e("Successfully connected: " + " " + device);
        }

        @Override
        public void onDisconnected(String info, int status, BleDevice device) {
            bleDevice = null;
            deviceList.remove(indexValue);
            deviceList.add(indexValue, device);
            Logger.e("disconnected!");
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int failCode, String info, BleDevice device) {
            Logger.e("connect fail:" + info);
            bleDevice = null;
            deviceList.remove(indexValue);
            deviceList.add(indexValue, device);
            Toast.makeText(context, "Connection fail: " + info, Toast.LENGTH_LONG).show();
            adapter.notifyDataSetChanged();
        }
    };

    public void addDeviceInfoDataAndUpdate() {
        if (bleDevice == null) return;
        Map<ServiceInfo, List<CharacteristicInfo>> deviceInfo = BleManager.getInstance().getDeviceServices(bleDevice.address);
        if (deviceInfo == null) {
            return;
        }
        for (Map.Entry<ServiceInfo, List<CharacteristicInfo>> e : deviceInfo.entrySet()) {
            for (CharacteristicInfo characteristicInfo : e.getValue()) {
                if (characteristicInfo.notify) {
                    notifyCharacteristic = characteristicInfo;
                    notifyServiceInfo = e.getKey();
                    read();
                }
                if (characteristicInfo.writable) {
                    writeServiceInfo = e.getKey();
                    writeCharacteristic = characteristicInfo;
                }
            }
        }
    }

    public void read() {
        if (bleDevice != null)
            BleManager.getInstance().notify(bleDevice, notifyServiceInfo.uuid, notifyCharacteristic.uuid, notifyCallback);
    }

    public void write(String msg) {
        if (bleDevice != null)
        BleManager.getInstance().write(bleDevice, writeServiceInfo.uuid, writeCharacteristic.uuid, msg.getBytes(), writeCallback);
    }

    public BleWriteCallback writeCallback = new BleWriteCallback() {
        @Override
        public void onWriteSuccess(byte[] data, BleDevice device) {
            Logger.e("write success:" + ByteUtils.bytes2HexStr(data));
        }

        @Override
        public void onFailure(int failCode, String info, BleDevice device) {
            Logger.e("write fail:" + info + " " + failCode);
        }
    };

    public BleNotifyCallback notifyCallback = new BleNotifyCallback() {
        @Override
        public void onCharacteristicChanged(byte[] data, BleDevice device) {
            String s1 = new String(data);
            System.out.println("testing here output" + s1);
        }

        @Override
        public void onNotifySuccess(String notifySuccessUuid, BleDevice device) {
            if (!notifySuccessUuids.contains(notifySuccessUuid)) {
                notifySuccessUuids.add(notifySuccessUuid);
            }
        }

        @Override
        public void onFailure(int failCode, String info, BleDevice device) {
            Logger.e("notify fail:" + info);
        }
    };

}
