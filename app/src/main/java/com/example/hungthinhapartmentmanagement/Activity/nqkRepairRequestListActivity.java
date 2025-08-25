package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungthinhapartmentmanagement.Adapter.nqkRepairRequestListAdapter;
import com.example.hungthinhapartmentmanagement.Helper.RepairRequestHelper;
import com.example.hungthinhapartmentmanagement.Model.RepairRequest;
import com.example.hungthinhapartmentmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class nqkRepairRequestListActivity extends AppCompatActivity {

    private String apartmentId;
    private String fullName;
    private String phone;
    private String email;
    private RecyclerView recyclerRequests;
    private nqkRepairRequestListAdapter adapter;
    private List<RepairRequest> repairRequestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_repair_request_list);

        // Nhận apartmentId, fullName, phone từ Intent
        apartmentId = getIntent().getStringExtra("apartmentId");
        fullName = getIntent().getStringExtra("fullName");
        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");
        if (apartmentId == null || apartmentId.trim().isEmpty()) {
            Toast.makeText(this, "Không tìm thấy apartmentId", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            Toast.makeText(this, "Không tìm thấy fullName", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (phone == null || phone.trim().isEmpty()) {
            Toast.makeText(this, "Không tìm thấy phone", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (email == null || email.trim().isEmpty()) {
            Toast.makeText(this, "Không tìm thấy email", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Khởi tạo các thành phần giao diện
        recyclerRequests = findViewById(R.id.recyclerRequests);
        recyclerRequests.setLayoutManager(new LinearLayoutManager(this));
        repairRequestList = new ArrayList<>();
        adapter = new nqkRepairRequestListAdapter(this, repairRequestList);
        recyclerRequests.setAdapter(adapter);

        // Xử lý WindowInsets để đẩy giao diện xuống dưới thanh trạng thái
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadRepairRequests();

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(this, nqkResidentMainActivity.class);
            backIntent.putExtra("apartmentId", apartmentId);
            backIntent.putExtra("fullName", fullName);
            backIntent.putExtra("phone", phone);
            backIntent.putExtra("email", email);
            backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(backIntent);
            finish();
        });

        Button btnAddRequest = findViewById(R.id.btnAddRequest);
        btnAddRequest.setOnClickListener(v -> {
            Intent intent = new Intent(nqkRepairRequestListActivity.this, nqkRepairRequestActivity.class);
            intent.putExtra("apartmentId", apartmentId);
            intent.putExtra("fullName", fullName);
            intent.putExtra("phone", phone);
            intent.putExtra("email", email);
            startActivity(intent);
        });
    }

    private void loadRepairRequests() {
        RepairRequestHelper helper = new RepairRequestHelper();
        helper.getActiveRequestsByApartmentId(apartmentId, email, new RepairRequestHelper.OnDataRetrievedListener() {
            @Override
            public void onDataRetrieved(List<RepairRequest> repairRequests) {
                repairRequestList.clear();
                repairRequestList.addAll(repairRequests);
                adapter.notifyDataSetChanged();
                if (repairRequests.isEmpty()) {
                    Toast.makeText(nqkRepairRequestListActivity.this, "Không có yêu cầu sửa chữa nào.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(nqkRepairRequestListActivity.this, "Lỗi khi tải dữ liệu: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}