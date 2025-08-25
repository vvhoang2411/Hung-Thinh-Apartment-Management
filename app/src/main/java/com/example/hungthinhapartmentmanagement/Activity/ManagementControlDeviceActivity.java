package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungthinhapartmentmanagement.Adapter.ManagementControlDeviceAdapter;
import com.example.hungthinhapartmentmanagement.Helper.DeviceHelper;
import com.example.hungthinhapartmentmanagement.Model.Device;
import com.example.hungthinhapartmentmanagement.R;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManagementControlDeviceActivity extends AppCompatActivity {

    private RecyclerView recyclerViewDevices;
    private ImageButton btnAdd;
    private ImageButton btnBack;
    private DeviceHelper deviceHelper;
    private ManagementControlDeviceAdapter adapter;
    private List<Device> deviceList;
    private List<String> documentIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.mt_activity_management_control_device);

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
        adapter = new ManagementControlDeviceAdapter(this, deviceList, documentIds);
        recyclerViewDevices.setAdapter(adapter);

        // Load devices from Firestore
        loadDevices();

        // Set click listener for btnAdd
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(ManagementControlDeviceActivity.this, ManagementAddDeviceActivity.class);
            startActivity(intent);
        });

        // Set click listener for btnBack
//        btnBack.setOnClickListener(v -> {
//            Intent intent = new Intent(ManagementControlDeviceActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish(); // Close current activity
//        });
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
            Toast.makeText(ManagementControlDeviceActivity.this, "Lỗi khi tải danh sách thiết bị: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}