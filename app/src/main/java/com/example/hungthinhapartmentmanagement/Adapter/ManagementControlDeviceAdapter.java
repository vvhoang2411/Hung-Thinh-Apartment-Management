package com.example.hungthinhapartmentmanagement.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungthinhapartmentmanagement.Model.Device;
import com.example.hungthinhapartmentmanagement.R;
import com.example.hungthinhapartmentmanagement.Activity.ManagementEditDeviceActivity;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ManagementControlDeviceAdapter extends RecyclerView.Adapter<ManagementControlDeviceAdapter.DeviceViewHolder> {

    private final Context context;
    private final List<Device> deviceList;
    private final List<String> documentIds;

    public ManagementControlDeviceAdapter(Context context, List<Device> deviceList, List<String> documentIds) {
        this.context = context;
        this.deviceList = deviceList;
        this.documentIds = documentIds;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_device, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        Device device = deviceList.get(position);
        String documentId = documentIds.get(position);

        // Set data to TextViews
        holder.textViewDeviceName.setText(device.getName());
        holder.textViewDeviceLocation.setText("Vị trí: " + device.getPosition());

        // Format Timestamp to readable date
        Timestamp timestamp = device.getDate_check();
        String formattedDate = "N/A";
        if (timestamp != null) {
            Date date = timestamp.toDate();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            formattedDate = "Ngày kiểm tra: " + sdf.format(date);
        }
        holder.textViewInspectionDate.setText(formattedDate);

        // Set click listener to open ManagementEditDeviceActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ManagementEditDeviceActivity.class);
            intent.putExtra("documentId", documentId);
            intent.putExtra("manufacturer", device.getManufacturer());
            intent.putExtra("mfg", device.getMfg());
            intent.putExtra("model", device.getModel());
            intent.putExtra("name", device.getName());
            intent.putExtra("position", device.getPosition());
            intent.putExtra("type", device.getType());
            intent.putExtra("date_check", device.getDate_check());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDeviceName;
        TextView textViewDeviceLocation;
        TextView textViewInspectionDate;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDeviceName = itemView.findViewById(R.id.textViewDeviceName);
            textViewDeviceLocation = itemView.findViewById(R.id.textViewDeviceLocation);
            textViewInspectionDate = itemView.findViewById(R.id.textViewInspectionDate);
        }
    }
}