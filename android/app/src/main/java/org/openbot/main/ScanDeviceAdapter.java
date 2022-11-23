package org.openbot.main;

import android.content.Context;
import android.util.SparseArray;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ficat.easyble.BleDevice;

import org.openbot.R;

import java.util.List;

public class ScanDeviceAdapter extends CommonRecyclerViewAdapter<BleDevice>{
    public ScanDeviceAdapter(@NonNull Context context, @NonNull List<BleDevice> dataList, @NonNull SparseArray<int[]> resLayoutAndViewIds) {
        super(context, dataList, resLayoutAndViewIds);
    }

    @Override
    public int getItemResLayoutType(int position) {
        return R.layout.ble_listview_tv;
    }

    @Override
    public void bindDataToItem(CommonRecyclerViewAdapter.MyViewHolder holder, BleDevice data, int position) {
        TextView tvName = (TextView) holder.mViews.get(R.id.tv_name);
        TextView tvAddress = (TextView) holder.mViews.get(R.id.tv_address);
        tvName.setText(data.name);
        tvAddress.setText(data.address);
    }
}
