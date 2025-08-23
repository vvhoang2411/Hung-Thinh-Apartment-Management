package com.example.hungthinhapartmentmanagement.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungthinhapartmentmanagement.Adapter.ManagementRepairRequestAdapter;
import com.example.hungthinhapartmentmanagement.Helper.RepairRequestHelper;
import com.example.hungthinhapartmentmanagement.Model.RepairRequest;
import com.example.hungthinhapartmentmanagement.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ManagementRepairRequestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ManagementRepairRequestAdapter adapter;
    private List<RepairRequest> repairRequestList;
    private RepairRequestHelper repairRequestHelper;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private static final String TAG = "ManagementRepairRequestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt_activity_management_repair_request);

        // Ánh xạ các thành phần
        toolbar = findViewById(R.id.toolbar);
        if (toolbar == null) {
            Log.e(TAG, "toolbar is null - Check activity_management_repair_request.xml");
            Toast.makeText(this, "Lỗi: Không tìm thấy Toolbar", Toast.LENGTH_SHORT).show();
            return;
        }
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Danh sách phản ánh");
        }

        tabLayout = findViewById(R.id.tabLayout);
        if (tabLayout == null) {
            Log.e(TAG, "tabLayout is null - Check activity_management_repair_request.xml");
            Toast.makeText(this, "Lỗi: Không tìm thấy TabLayout", Toast.LENGTH_SHORT).show();
            return;
        }

        recyclerView = findViewById(R.id.recyclerView);
        if (recyclerView == null) {
            Log.e(TAG, "recyclerView is null - Check activity_management_repair_request.xml");
            Toast.makeText(this, "Lỗi: Không tìm thấy RecyclerView", Toast.LENGTH_SHORT).show();
            return;
        }

        // Khởi tạo RecyclerView
        repairRequestList = new ArrayList<>();
        adapter = new ManagementRepairRequestAdapter(this, repairRequestList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        repairRequestHelper = new RepairRequestHelper();

        // Thiết lập sự kiện chọn tab
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        loadRequestsByStatus("pending");
                        break;
                    case 1:
                        loadRequestsByStatus("received");
                        break;
                    case 2:
                        loadRequestsByStatus("completed");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Load mặc định với status "pending"
        loadRequestsByStatus("pending");
    }

    private void loadRequestsByStatus(String status) {
        repairRequestHelper.getActiveRequestsByStatus(status, new RepairRequestHelper.OnDataRetrievedListener() {
            @Override
            public void onDataRetrieved(List<RepairRequest> repairRequests) {
                Log.d(TAG, "Lấy được " + repairRequests.size() + " yêu cầu cho status: " + status);
                for (RepairRequest request : repairRequests) {
                    Log.d(TAG, "Yêu cầu: " + request.getTitle() + ", Trạng thái: " + request.getStatus());
                }
                repairRequestList.clear();
                repairRequestList.addAll(repairRequests);
                adapter.notifyDataSetChanged();
                if (repairRequests.isEmpty()) {
                    Toast.makeText(ManagementRepairRequestActivity.this, "Không có yêu cầu nào với trạng thái " + status, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.e(TAG, "Lỗi khi tải dữ liệu: " + errorMessage);
                Toast.makeText(ManagementRepairRequestActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}