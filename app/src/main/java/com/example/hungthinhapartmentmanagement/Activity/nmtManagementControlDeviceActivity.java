package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungthinhapartmentmanagement.Adapter.nmtManagementControlDeviceAdapter;
import com.example.hungthinhapartmentmanagement.Helper.DeviceHelper;
import com.example.hungthinhapartmentmanagement.Model.Device;
import com.example.hungthinhapartmentmanagement.R;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class nmtManagementControlDeviceActivity extends AppCompatActivity {

    private RecyclerView recyclerViewDevices;
    private ImageButton btnAdd;
    private ImageButton btnBack;
    private DeviceHelper deviceHelper;
    private nmtManagementControlDeviceAdapter adapter;
    private List<Device> deviceList;
    private List<String> documentIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_management_control_device);

        // Initialize views
        recyclerViewDevices = findViewById(R.id.recyclerViewDevices);
        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);

        // Initialize data
        deviceHelper = new DeviceHelper();
        deviceList = new ArrayList<>();
        documentIds = new ArrayList<>();

        // Set up RecyclerView
        recyclerViewDevices.setLayoutManager(new LinearLayoutManager(this));
        adapter = new nmtManagementControlDeviceAdapter(this, deviceList, documentIds);
        recyclerViewDevices.setAdapter(adapter);

        // Load devices from Firestore
        loadDevices();

        // Set click listener for btnAdd
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(nmtManagementControlDeviceActivity.this, nmtManagementAddDeviceActivity.class);
            startActivity(intent);
        });

        // Set click listener for btnBack
        btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(this, hdhMainActivity.class);
            backIntent.putExtra("returnTab", 0); // 2 corresponds to nav_notification
            backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // Reuse existing MainActivity
            startActivity(backIntent);
            finish();
        });
    }

    private void loadDevices() {
        deviceHelper.getAllDevices().addOnSuccessListener(querySnapshot -> {
            deviceList.clear();
            documentIds.clear();
            for (QueryDocumentSnapshot document : querySnapshot) {
                Device device = document.toObject(Device.class);
                deviceList.add(device);
                documentIds.add(document.getId());
            }
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Toast.makeText(nmtManagementControlDeviceActivity.this, "Lỗi khi tải danh sách thiết bị: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}