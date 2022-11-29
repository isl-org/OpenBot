package org.openbot.main;

import android.Manifest;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import org.openbot.OpenBotApplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ficat.easyble.BleDevice;
import com.ficat.easyble.BleManager;
import com.ficat.easypermissions.EasyPermissions;
import com.ficat.easypermissions.RequestExecutor;
import com.ficat.easypermissions.bean.Permission;

import org.openbot.R;
import org.openbot.databinding.FragmentBluetoothBinding;
import org.openbot.vehicle.Vehicle;

import java.util.List;

public class BluetoothFragment extends Fragment {
    private RecyclerView rv;
    private FragmentBluetoothBinding binding;
    private Vehicle vehicle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vehicle = OpenBotApplication.vehicle;
        binding = FragmentBluetoothBinding.inflate(inflater, container, false);
        rv = binding.rv;
        showDevicesByRv();
        return binding.getRoot();
    }

    private void showDevicesByRv() {
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        SparseArray<int[]> res = new SparseArray<>();
        res.put(R.layout.ble_listview_tv, new int[]{R.id.ble_name, R.id.ble_address, R.id.ble_connection_state});
        vehicle.setBleAdapter(new ScanDeviceAdapter(getActivity(), vehicle.getDeviceList(), res),new CommonRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                BleDevice device = vehicle.getDeviceList().get(position);
                if (vehicle.getBleDevice() == null || vehicle.getBleDevice().address.equals(device.address)) {
                    vehicle.setBleDevice(device);
                }
                vehicle.toggleConnection(position);
            }
        });
        rv.setAdapter(vehicle.getBleAdapter());
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!BleManager.isBluetoothOn()) BleManager.toggleBluetooth(true);
        //for most devices whose version is over Android6,scanning may need GPS permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !isGpsOn()) {
            Toast.makeText(getActivity(), "Please turn on GPS before scanning", Toast.LENGTH_LONG).show();
            return;
        }
        EasyPermissions
                .with(getActivity())
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .autoRetryWhenUserRefuse(true, null)
                .result(new RequestExecutor.ResultReceiver() {
                    @Override
                    public void onPermissionsRequestResult(boolean grantAll, List<Permission> results) {
                        TextView tv = getView().findViewById(R.id.btn_refresh);

                        if (grantAll) {
                            tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startScan();
                                }
                            });
                            startScan();
                        } else {
                            Toast.makeText(getActivity(), "Please go to settings to grant location permission manually",
                                    Toast.LENGTH_LONG).show();
                            EasyPermissions.goToSettingsActivity(getActivity());
                        }
                    }
                });
    }

    private void startScan() {
        ProgressBar pb = getView().findViewById(R.id.progress_bar);
        TextView tv = getView().findViewById(R.id.btn_refresh);
        vehicle.startScan();
        pb.setVisibility(View.VISIBLE);
        tv.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        pb.setVisibility(View.INVISIBLE);
                        tv.setVisibility(View.VISIBLE);
                    }
                }, 4000);
    }

    private boolean isGpsOn() {
        LocationManager locationManager
                = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (manager != null) {
//            manager.destroy();
//        }
    }
}
