package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hungthinhapartmentmanagement.Adapter.vvhResidentInvoiceAdapter;
import com.example.hungthinhapartmentmanagement.Helper.InvoiceHelper;
import com.example.hungthinhapartmentmanagement.Model.Invoice;
import com.example.hungthinhapartmentmanagement.R;
import java.util.ArrayList;
import java.util.List;

public class vvhResidentInvoiceActivity extends AppCompatActivity {
    private String apartmentId;
    private String fullName;
    private String phone;
    private String email;
    private Spinner spinnerLoaiHoaDon, spinnerTrangThai;
    private Button btnTimKiem;
    private ImageButton btnBack;
    private RecyclerView recyclerView;
    private vvhResidentInvoiceAdapter invoiceAdapter;
    private InvoiceHelper invoiceHelper;
    private String selectedFeeType = "all";
    private String selectedStatus = "all";
    private static final String TAG = "VVHResidentInvoiceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resident_invoice);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        spinnerLoaiHoaDon = findViewById(R.id.spinnerLoaiHoaDon);
        spinnerTrangThai = findViewById(R.id.spinnerTrangThai);
        btnTimKiem = findViewById(R.id.btnTimKiem);
        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.recyclerView);

        // Check if recyclerView is null
        if (recyclerView == null) {
            Log.e(TAG, "RecyclerView is null. Check if ID 'recyclerView' exists in activity_resident_invoice.xml");
            Toast.makeText(this, "Error: RecyclerView not found", Toast.LENGTH_LONG).show();
            return;
        }

        // Initialize InvoiceHelper
        invoiceHelper = new InvoiceHelper();

        // Get apartmentId from Intent
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

        // Setup RecyclerView with an empty list initially
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        invoiceAdapter = new vvhResidentInvoiceAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(invoiceAdapter);

        // Load initial invoices with apartmentId, feeType = "all", status = "all"
        loadInvoices(apartmentId, "all", "all");

        // Setup spinner listeners
        spinnerLoaiHoaDon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                switch (selected) {
                    case "Tất cả":
                        selectedFeeType = "all";
                        break;
                    case "Dịch vụ":
                        selectedFeeType = "service";
                        break;
                    case "Điện":
                        selectedFeeType = "electricity";
                        break;
                    case "Nước":
                        selectedFeeType = "water";
                        break;
                    case "Gửi xe":
                        selectedFeeType = "parking";
                        break;
                    case "Khác":
                        selectedFeeType = "other";
                        break;
                }
                Log.d(TAG, "Selected feeType: " + selectedFeeType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedFeeType = "all";
            }
        });

        spinnerTrangThai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                switch (selected) {
                    case "Tất cả":
                        selectedStatus = "all";
                        break;
                    case "Đã thanh toán":
                        selectedStatus = "true";
                        break;
                    case "Chưa thanh toán":
                        selectedStatus = "false";
                        break;
                }
                Log.d(TAG, "Selected status: " + selectedStatus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedStatus = "all";
            }
        });

        // Setup search button listener
        btnTimKiem.setOnClickListener(v -> {
            Log.d(TAG, "Search button clicked with apartmentId: " + apartmentId + ", feeType: " + selectedFeeType + ", status: " + selectedStatus);
            loadInvoices(apartmentId, selectedFeeType, selectedStatus);
        });

        // Setup back button listener
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
    }

    private void loadInvoices(String apartmentId, String feeType, String status) {
        invoiceHelper.getInvoices(apartmentId, feeType, status).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Invoice> invoices = task.getResult();
                Log.d(TAG, "Loaded " + invoices.size() + " invoices");
                invoiceAdapter.updateInvoices(invoices);
            } else {
                Log.e(TAG, "Failed to load invoices: " + task.getException());
                Toast.makeText(this, "Failed to load invoices", Toast.LENGTH_SHORT).show();
            }
        });
    }
}