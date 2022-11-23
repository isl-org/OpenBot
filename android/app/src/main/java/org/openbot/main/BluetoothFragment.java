package org.openbot.main;


import android.Manifest;
import android.content.Context;
import android.graphics.Rect;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ficat.easyble.BleDevice;
import com.ficat.easyble.BleManager;
import com.ficat.easyble.Logger;
import com.ficat.easyble.gatt.bean.CharacteristicInfo;
import com.ficat.easyble.gatt.bean.ServiceInfo;
import com.ficat.easyble.gatt.callback.BleConnectCallback;
import com.ficat.easyble.gatt.callback.BleNotifyCallback;
import com.ficat.easyble.gatt.callback.BleWriteCallback;
import com.ficat.easyble.scan.BleScanCallback;
import com.ficat.easypermissions.EasyPermissions;
import com.ficat.easypermissions.RequestExecutor;
import com.ficat.easypermissions.bean.Permission;

import org.openbot.R;
import org.openbot.databinding.FragmentBluetoothBinding;
import org.openbot.utils.ByteUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BluetoothFragment extends Fragment {
    public static BluetoothFragment shared = new BluetoothFragment();
    private RecyclerView rv;
    private BleManager manager;
    private List<BleDevice> deviceList = new ArrayList<>();
    private ScanDeviceAdapter adapter;
    private FragmentBluetoothBinding binding;
    private CharacteristicInfo notifyCharacteristic;
    private CharacteristicInfo writeCharacteristic;
    private BleDevice connectedDevice;
    private List<ServiceInfo> groupList = new ArrayList<>();
    private List<List<CharacteristicInfo>> childList = new ArrayList<>();
    private List<String> notifySuccessUuids = new ArrayList<>();
    private View selectedViewItem;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBleManager();
    }

    private void initBleManager() {
        //check if this android device supports ble
        if (!BleManager.supportBle(getActivity())) {
            return;
        }
        //open bluetooth without a request dialog
        BleManager.toggleBluetooth(true);

        BleManager.ScanOptions scanOptions = BleManager.ScanOptions
                .newInstance()
                .scanPeriod(8000)
                .scanDeviceName(null);

        BleManager.ConnectOptions connectOptions = BleManager.ConnectOptions
                .newInstance()
                .connectTimeout(12000);

        manager = BleManager
                .getInstance()
                .setScanOptions(scanOptions)
                .setConnectionOptions(connectOptions)
                .setLog(true, "EasyBle")
                .init(this.getActivity());
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBluetoothBinding.inflate(inflater, container, false);
        rv = binding.rv;
        showDevicesByRv();
        return binding.getRoot();
    }

    private void showDevicesByRv() {
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 3;
            }
        });
        SparseArray<int[]> res = new SparseArray<>();
        res.put(R.layout.ble_listview_tv, new int[]{R.id.tv_name, R.id.tv_address, R.id.tv_connection_state});
        adapter = new ScanDeviceAdapter(getActivity(), deviceList, res);
        adapter.setOnItemClickListener(new CommonRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                System.out.println("pos"+position);
                manager.stopScan();
                BleDevice bleDevice = deviceList.get(position);
                if(connectedDevice == null || !bleDevice.address.equals(connectedDevice.address)) {
                    connectedDevice = bleDevice;
                }
                selectedViewItem = itemView;
                updateConnectionStateUi();
                addDeviceInfoDataAndUpdate();
                connectToDevice();
            }
        });
        rv.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView refreshButton = (TextView) getView().findViewById(R.id.btn_refresh);
        if (!BleManager.isBluetoothOn()) {
            BleManager.toggleBluetooth(true);
        }
        //for most devices whose version is over Android6,scanning may need GPS permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !isGpsOn()) {
            Toast.makeText(getActivity(), "Please turn on GPS before scanning", Toast.LENGTH_LONG).show();
            return;
        }
//        tvConnectionState = getView().findViewById(R.id.tv_connection_state);
        EasyPermissions
                .with(getActivity())
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .autoRetryWhenUserRefuse(true, null)
                .result(new RequestExecutor.ResultReceiver() {
                    @Override
                    public void onPermissionsRequestResult(boolean grantAll, List<Permission> results) {
                        if (grantAll) {
                            refreshButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (!manager.isScanning()) {
                                        startScan();
                                    }
                                    System.out.println("refresh clicked");
                                }
                            });
                            if (!manager.isScanning()) {
                                startScan();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Please go to settings to grant location permission manually",
                                    Toast.LENGTH_LONG).show();
                            EasyPermissions.goToSettingsActivity(getActivity());
                        }
                    }
                });
    }

    private void addDeviceInfoDataAndUpdate() {
        if (connectedDevice == null) return;
        Map<ServiceInfo, List<CharacteristicInfo>> deviceInfo = BleManager.getInstance().getDeviceServices(connectedDevice.address);
        if (deviceInfo == null) {
            return;
        }
        for (Map.Entry<ServiceInfo, List<CharacteristicInfo>> e : deviceInfo.entrySet()) {
            for (CharacteristicInfo characteristicInfo : e.getValue()) {
                if (characteristicInfo.notify) {
                    notifyCharacteristic = characteristicInfo;
                    BleManager.getInstance().notify(connectedDevice, e.getKey().uuid, notifyCharacteristic.uuid, notifyCallback);
                }
                if (characteristicInfo.writable) {
                    writeCharacteristic = characteristicInfo;
                    String str = "c100";
                    BleManager.getInstance().write(connectedDevice, e.getKey().uuid, writeCharacteristic.uuid, ByteUtils.hexStr2Bytes("c100"), writeCallback);
                }
            }
            groupList.add(e.getKey());
            childList.add(e.getValue());
        }
    }

    private void startScan() {
        progressBar = getView().findViewById(R.id.progress_bar);
        TextView refreshButton = getView().findViewById(R.id.btn_refresh);
        manager.startScan(new BleScanCallback() {
            @Override
            public void onLeScan(BleDevice device, int rssi, byte[] scanRecord) {
                refreshButton.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                for (BleDevice d : deviceList) {
                    if (device.address.equals(d.address)) {
                        return;
                    }
                }
                if (device.name != "unknown") {
                    deviceList.add(device);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onStart(boolean startScanSuccess, String info) {
                Log.e("TAG", "start scan = " + startScanSuccess + "   info: " + info);
                if (startScanSuccess) {
                    deviceList.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFinish() {
                Log.e("TAG", "scan finish");
                progressBar.setVisibility(View.INVISIBLE);
                refreshButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void connectToDevice() {
        if(connectedDevice.connected){
            BleManager.getInstance().disconnect(connectedDevice.address);
            connectedDevice = null;
        } else{
            BleManager.getInstance().connect(connectedDevice.address, connectCallback);
        }
    }

    private BleConnectCallback connectCallback = new BleConnectCallback() {
        @Override
        public void onStart(boolean startConnectSuccess, String info, BleDevice device) {
            Logger.e("start connecting:" + startConnectSuccess + "    info=" + info);
            connectedDevice = device;
            updateConnectionStateUi();
            if (!startConnectSuccess) {
                Toast.makeText(getActivity(), "start connecting fail:" + info, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onConnected(BleDevice device) {
            connectedDevice = device;
            addDeviceInfoDataAndUpdate();
            updateConnectionStateUi();
            Logger.e("Successfully connected: " + " BleDevice=" + device);
        }

        @Override
        public void onDisconnected(String info, int status, BleDevice device) {
            connectedDevice = device;
            groupList.clear();
            childList.clear();
            adapter.notifyDataSetChanged();
            updateConnectionStateUi();
            Logger.e("disconnected!");
        }

        @Override
        public void onFailure(int failCode, String info, BleDevice device) {
            Logger.e("connect fail:" + info);
            Toast.makeText(getActivity(),
                    getResources().getString(failCode == BleConnectCallback.FAIL_CONNECT_TIMEOUT ?
                            R.string.tips_connect_timeout : R.string.tips_connect_fail), Toast.LENGTH_LONG).show();

            groupList.clear();
            childList.clear();
            adapter.notifyDataSetChanged();
            updateConnectionStateUi();
        }
    };

    private BleWriteCallback writeCallback = new BleWriteCallback() {
        @Override
        public void onWriteSuccess(byte[] data, BleDevice device) {
            Logger.e("write success:" + ByteUtils.bytes2HexStr(data));
        }

        @Override
        public void onFailure(int failCode, String info, BleDevice device) {
            Logger.e("write fail:" + info);
            Logger.e("write failCode:" + failCode);
        }
    };

    private BleNotifyCallback notifyCallback = new BleNotifyCallback() {
        @Override
        public void onCharacteristicChanged(byte[] data, BleDevice device) {
            String s1 = new String(data);
            System.out.println("testing here output" + s1);
        }

        @Override
        public void onNotifySuccess(String notifySuccessUuid, BleDevice device) {
            Logger.e("notify success uuid:" + notifySuccessUuid);
            if (!notifySuccessUuids.contains(notifySuccessUuid)) {
                notifySuccessUuids.add(notifySuccessUuid);
            }
        }

        @Override
        public void onFailure(int failCode, String info, BleDevice device) {
            Logger.e("notify fail:" + info);
        }
    };

    private void updateConnectionStateUi() {
        String state;
        TextView tvConnectionState = (TextView) selectedViewItem.findViewById(R.id.tv_connection_state);
        if (connectedDevice.connected) {
            state = "Disconnect";
            tvConnectionState.setTextColor(getResources().getColor(R.color.red));
        } else if (connectedDevice.connecting) {
            state = "Connecting";
            tvConnectionState.setTextColor(getResources().getColor(R.color.green));
        } else {
            state = "Connect";
            tvConnectionState.setTextColor(getResources().getColor(R.color.semi_transparent));
        }
        tvConnectionState.setText(state);
    }

    private boolean isGpsOn() {
        LocationManager locationManager
                = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (manager != null) {
            manager.destroy();
        }
    }
}
