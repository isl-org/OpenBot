package org.openbot.main;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;

import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Element;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ficat.easyble.BleDevice;
import com.ficat.easyble.BleManager;
import com.ficat.easyble.Logger;
import com.ficat.easyble.gatt.bean.CharacteristicInfo;
import com.ficat.easyble.gatt.bean.ServiceInfo;
import com.ficat.easyble.gatt.callback.BleNotifyCallback;
import com.ficat.easyble.scan.BleScanCallback;

import org.openbot.R;
import org.openbot.databinding.FragmentBluetoothBinding;

import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothFragment extends Fragment {
//    boolean bluetoothActive = false;
//    private FragmentBluetoothBinding binding;
//    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//    private BluetoothLeScanner bluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
//
//    private boolean scanning;
//    private Handler handler = new Handler();
//
//    private static final long SCAN_PERIOD = 10000;
//
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//
//        bluetoothActive = PermissionUtils.getBluetoothStatus();
//        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//        List<String> s = new ArrayList<String>();
//        for (BluetoothDevice bt : pairedDevices)
//            s.add(bt.getName());
//        System.out.println("BluetoothAdapter paired devices == " + pairedDevices);
//        System.out.println("BluetoothAdapter devices == " + s);
//        System.out.println("list = " + list);
//
//        if (!bluetoothActive) {
//            //turn on bluetooth dialog
//            showBluetoothDialog();
//        } else scanLeDevice();
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(
//            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        binding = FragmentBluetoothBinding.inflate(inflater, container, false);
//        return binding.getRoot();
//    }
//
//    private void scanLeDevice() {
//        System.out.println("scanning" + scanning);
//        if (!scanning) {
//            // Stops scanning after a predefined scan period.
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    scanning = false;
//                    System.out.println("SCAN_PERIOD chala");
//                    bluetoothLeScanner.stopScan(leScanCallback);
//                }
//            }, SCAN_PERIOD);
//
//            scanning = true;
//            System.out.println("startScan chala");
//            bluetoothLeScanner.startScan(leScanCallback);
//        } else {
//            scanning = false;
//            System.out.println("stopScan chala");
//            bluetoothLeScanner.stopScan(leScanCallback);
//        }
//    }
//
//    private ArrayList<BluetoothDevice> list;
//
//    //  Device scan callback.
//    private final ScanCallback leScanCallback =
//            new ScanCallback() {
//                @Override
//                public void onScanResult(int callbackType, ScanResult result) {
//                    System.out.println("leScanCallback chala");
//                    super.onScanResult(callbackType, result);
//                    list.add(result.getDevice());
//                }
//
//                @Override
//                public void onScanFailed(int errorCode) {
//                    super.onScanFailed(errorCode);
//                    System.out.println("failed Scan");
//                }
//            };
//
//    private void showBluetoothDialog() {
////        if (bluetoothAdapter != null) {
////
////            if (!bluetoothAdapter.isEnabled) {
////                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
////                startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST_CODE)
////            }
////        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
//        builder.setMessage("Turn on Bluetooth request").setPositiveButton("Allow", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                //turn on bluetooth
//                BluetoothAdapter.getDefaultAdapter().enable();
//                Toast.makeText(requireContext().getApplicationContext(),
//                        "Bluetooth turn on", Toast.LENGTH_SHORT).show();
//            }
//        }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // User cancelled the dialog
//                //Toaster can't turn on bluetooth
//                Toast.makeText(requireContext().getApplicationContext(),
//                        "Can't turn on Bluetooth", Toast.LENGTH_SHORT).show();
//            }
//        });
//        builder.show();
//    }

    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private BluetoothGatt mGatt;
    private FragmentBluetoothBinding binding;
    private BluetoothGattCharacteristic bluetoothGattCharacteristic;
    private BleManager bleManager;
    private int ReadQueueIndex;
    private List<BluetoothGattCharacteristic> ReadQueue;
    private BleDevice device;
    private ServiceInfo curService;
    private CharacteristicInfo curCharacteristic;
    private List<String> notifySuccessUuids = new ArrayList<>();
    private TextView tvConnectionState, tvReadResult, tvWriteResult,
            tvNotify, tvInfoCurrentUuid, tvInfoNotification;


    Set<String> DeviceList = new HashSet<>();
    ArrayList<BluetoothDevice> BLE_Devices = new ArrayList<BluetoothDevice>();
    //ArrayList<String> DeviceList = new ArrayList<String>();
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(
                    requireContext().getApplicationContext(),
                    "BLE Not Supported",
                    Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBluetoothBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.ble_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice selectedItem = (BluetoothDevice) parent.getItemAtPosition(position);
                Toast.makeText(requireContext().getApplicationContext(),
                        selectedItem.getName() + selectedItem.getBondState(), Toast.LENGTH_SHORT).show();
                System.out.println("selected = " + selectedItem);
                connectToDevice(selectedItem);
            }
        });
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
//            scanLeDevice(false);
//        }
//    }

//    @Override
//    public void onDestroy() {
//        if (mGatt == null) {
//            return;
//        }
//        mGatt.close();
//        mGatt = null;
//        super.onDestroy();
//    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
                settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                        .build();
                filters = new ArrayList<ScanFilter>();
            }
            scanLeDevice(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                //Bluetooth not enabled.
                getActivity().finish();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT < 21) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        System.out.println("DeviceList = " + DeviceList);
                    } else {
                        mLEScanner.stopScan(mScanCallback);
                        System.out.println("DeviceList = " + DeviceList);
                    }
                }
            }, SCAN_PERIOD);
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                mLEScanner.startScan(filters, settings, mScanCallback);
            }
        } else {
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            } else {
                mLEScanner.stopScan(mScanCallback);
            }
        }
    }

    private final ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice btDevice = result.getDevice();
            String deviceName = btDevice.getName();
            if (btDevice.getName() != null) {
                System.out.println("result status = " + result);
                BLE_Devices.add(btDevice);
                DeviceList.add(deviceName);
                ArrayAdapter<BluetoothDevice> adapter = new ArrayAdapter(requireContext(), R.layout.ble_listview_tv, BLE_Devices);
                listView.setAdapter(adapter);
            }
//            connectToDevice(btDevice);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            System.out.println("batchResults = " + results);
            for (int i = 0; i < results.size(); i++) {
                ScanResult result = results.get(i);
                System.out.println("for loop Results = " + result);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            System.out.println("onScanFailed " + errorCode);
        }
    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("mLeScanCallback == " + device.toString());
//                            connectToDevice(device);
                        }
                    });
                }
            };

    public void connectToDevice(BluetoothDevice device) {
        if (mGatt == null) {
            System.out.println("inside mgatt");
            mGatt = device.connectGatt(requireActivity(), false, gattCallback);
//            scanLeDevice(false);// will stop after first device detection
        }
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt discoveredGatt, int status, int newState) {
            BleManager.getInstance().notify(device, curService.uuid, curCharacteristic.uuid, notifyCallback);
            System.out.println("onConnectionStateChange Status: " + status);
            super.onConnectionStateChange(discoveredGatt, status, newState);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    System.out.println("successfully connected to the GATT Server -> " + BluetoothProfile.STATE_CONNECTED);
//                    System.out.println("Connected device -> " + discoveredGatt.getConnectedDevices());
                    System.out.println("Attempts to discover services after successful connection.");
                    discoveredGatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    mGatt = null;
                    System.out.println("disconnected from the GATT Server");
                    break;
                default:
                    System.out.println("gattCallback STATE_OTHER");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt discoveredGatt, int status) {
            super.onServicesDiscovered(discoveredGatt, status);
            List<BluetoothGattService> services = discoveredGatt.getServices();
            System.out.println("onServicesDiscovered => " + services.toString());
            System.out.println("onServicesCharacteristics 0 => " + services.get(0).getCharacteristics().toString());
            System.out.println("onServicesCharacteristics 1 => " + services.get(1).getCharacteristics().toString());
            System.out.println("onServicesCharacteristics 2 => " + services.get(2).getCharacteristics().toString());

//            discoveredGatt.readCharacteristic(services.get(1).getCharacteristics().get(0));
//            for (BluetoothGattService e : elements) {
//                doSomething(e);
//            }
            ReadQueue = new ArrayList<>();

//            ReadQueue.add(services.get(0).getCharacteristics().get(0));

            ReadQueue.add(services.get(1).getCharacteristics().get(0));
            ReadQueue.add(services.get(1).getCharacteristics().get(1));
            ReadQueue.add(services.get(1).getCharacteristics().get(2));
            System.out.println("charType 1 = " + services.get(1).getCharacteristics().get(0).getProperties());
            System.out.println("charType 2 = " + services.get(2).getCharacteristics().get(0).getProperties());
            System.out.println("charType 0 = " + services.get(0).getCharacteristics().get(0).getProperties());



//            ReadQueue.add(services.get(2).getCharacteristics().get(0));
//            ReadQueue.add(services.get(2).getCharacteristics().get(1));

            ReadQueueIndex = 2;
            System.out.println("sizeRead => " + ReadQueue.size());
            System.out.println("readQueue => " + ReadQueue);
            ReadCharacteristics(2);
        }

        private void ReadCharacteristics(int index) {
            mGatt.readCharacteristic(ReadQueue.get(index));
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt discoveredGatt, BluetoothGattCharacteristic characteristic, int status) {
//            List<BluetoothGattService> services = discoveredGatt.getServices();

            String value = Arrays.toString(new String[]{characteristic.getStringValue(0)});
            System.out.println("check -> " + value);
            ReadQueue.remove(ReadQueue.get(ReadQueueIndex));
            if (ReadQueue.size() >= 0) {
                ReadQueueIndex--;
                if (ReadQueueIndex == -1) {
                    Log.i("Read Queue: ", "Complete");
                } else {
                    ReadCharacteristics(ReadQueueIndex);
                }
            }
        }

        private void updateNotificationInfo(String notification) {
            StringBuilder builder = new StringBuilder("Notify Uuid:");
            for (String s : notifySuccessUuids) {
                builder.append("\n");
                builder.append(s);
            }
            if (!TextUtils.isEmpty(notification)) {
                builder.append("\nReceive Data:\n");
                builder.append(notification);
            }
            tvInfoNotification.setText(builder.toString());
        }


        private BleNotifyCallback notifyCallback = new BleNotifyCallback() {
            @Override
            public void onCharacteristicChanged(byte[] data, BleDevice device) {
//            S
                String s = ByteUtils.bytes2HexStr(data);
                Logger.e("onCharacteristicChanged:  " + data);
                updateNotificationInfo(s);
            }

            @Override
            public void onNotifySuccess(String notifySuccessUuid, BleDevice device) {
                Logger.e("notify success uuid:" + notifySuccessUuid);
                tvInfoNotification.setVisibility(View.VISIBLE);
                if (!notifySuccessUuids.contains(notifySuccessUuid)) {
                    notifySuccessUuids.add(notifySuccessUuid);
                }
                updateNotificationInfo("");
            }

            @Override
            public void onFailure(int failCode, String info, BleDevice device) {
                Logger.e("notify fail:" + info);
//                Toast.makeText(OperateActivity.this, "notify fail:" + info, Toast.LENGTH_LONG).show();
            }
        };

//        @Override
//        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//            super.onCharacteristicChanged(gatt, characteristic);
//            System.out.println("characteristics change");
//        }

//        @Override
//        public void onCharacteristicWrite(BluetoothGatt discoveredGatt, BluetoothGattCharacteristic characteristic, int status){
//            System.out.println("charrr111 = " + characteristic);
//            byte[] value = new byte[1];
//            value[0] = (byte) (21 & 0xFF);
//            characteristic.setValue(value);
//            discoveredGatt.writeCharacteristic(characteristic);
//            System.out.println("charrr = " + characteristic);
//        }

    };
}
