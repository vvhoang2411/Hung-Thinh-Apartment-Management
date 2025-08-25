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
import com.example.hungthinhapartmentmanagement.Adapter.nqkManagementInvoiceAdapter;
import com.example.hungthinhapartmentmanagement.Model.Invoice;
import com.example.hungthinhapartmentmanagement.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class nqkManagementInvoiceListActivity extends AppCompatActivity {
    private String apartmentId;
    private Spinner spinnerLoaiHoaDon, spinnerTrangThai;
    private Button btnTimKiem;
    private ImageButton btnBack, btnAdd;
    private RecyclerView recyclerView;
    private nqkManagementInvoiceAdapter invoiceAdapter;
    private String selectedFeeType = "all";
    private String selectedStatus = "all";
    private static final String TAG = "ManagementInvoiceListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_management_invoice_list);

        // Initialize views
        spinnerLoaiHoaDon = findViewById(R.id.spinnerLoaiHoaDon);
        spinnerTrangThai = findViewById(R.id.spinnerTrangThai);
        btnTimKiem = findViewById(R.id.btnTimKiem);
        btnBack = findViewById(R.id.btnBack);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);

        // Check if recyclerView is null
        if (recyclerView == null) {
            Log.e(TAG, "RecyclerView is null. Check if ID 'recyclerView' exists in activity_management_invoice_list.xml");
            Toast.makeText(this, "Error: RecyclerView not found", Toast.LENGTH_LONG).show();
            return;
        }

        // Get apartmentId from Intent
        apartmentId = getIntent().getStringExtra("apartmentId");
        if (apartmentId == null || apartmentId.isEmpty()) {
            Log.e(TAG, "apartmentId is null or empty");
            Toast.makeText(this, "Error: No apartment ID provided", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Setup RecyclerView with an empty list initially
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        invoiceAdapter = new nqkManagementInvoiceAdapter(this, new ArrayList<>());
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
            Intent intent = new Intent(nqkManagementInvoiceListActivity.this, nqkManagementFindApartmentActivity.class);
            startActivity(intent);
        });

        // Setup add button listener
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(nqkManagementInvoiceListActivity.this, nqkManagementInvoiceCreateActivity.class);
            intent.putExtra("apartmentId", apartmentId);
            startActivity(intent);
        });

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadInvoices(String apartmentId, String feeType, String status) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("invoices").whereEqualTo("apartmentId", apartmentId);

        // Apply feeType filter if not "all"
        if (!feeType.equals("all")) {
            query = query.whereEqualTo("feeType", feeType);
        }

        // Apply status filter if not "all"
        if (!status.equals("all")) {
            query = query.whereEqualTo("status", Boolean.parseBoolean(status));
        }

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Invoice> invoices = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Invoice invoice = document.toObject(Invoice.class);
                    invoice.setDocumentID(document.getId());
                    invoices.add(invoice);
                }
                Log.d(TAG, "Loaded " + invoices.size() + " invoices");
                invoiceAdapter.updateInvoices(invoices);
            } else {
                Log.e(TAG, "Failed to load invoices: " + task.getException());
                Toast.makeText(this, "Failed to load invoices", Toast.LENGTH_SHORT).show();
            }
        });
    }
}