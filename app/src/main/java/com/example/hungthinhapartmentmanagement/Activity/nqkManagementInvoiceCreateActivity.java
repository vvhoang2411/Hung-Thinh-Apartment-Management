package com.example.hungthinhapartmentmanagement.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hungthinhapartmentmanagement.Helper.InvoiceHelper;
import com.example.hungthinhapartmentmanagement.Model.Invoice;
import com.example.hungthinhapartmentmanagement.R;
import com.google.firebase.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class nqkManagementInvoiceCreateActivity extends AppCompatActivity {
    private EditText edtApartmentId, edtTotal, edtDate, edtNote;
    private Spinner spinnerType, spinnerMonth, spinnerYear, spinnerStatus;
    private Button btnAdd;
    private ImageButton btnBack;
    private InvoiceHelper invoiceHelper;
    private String apartmentId;
    private static final String TAG = "ManagementInvoiceCreateActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_management_invoice_create);

        // Initialize InvoiceHelper
        invoiceHelper = new InvoiceHelper();

        // Initialize views
        edtApartmentId = findViewById(R.id.edtApartmentId);
        spinnerType = findViewById(R.id.spinnerType);
        edtTotal = findViewById(R.id.edtTotal);
        spinnerMonth = findViewById(R.id.spinnerMonth);
        edtDate = findViewById(R.id.edtDate);
        edtNote = findViewById(R.id.edtNote);
        spinnerYear = findViewById(R.id.spinnerYear);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);

        // Get apartmentId from Intent and set to edtApartmentId
        apartmentId = getIntent().getStringExtra("apartmentId");
        if (apartmentId == null || apartmentId.isEmpty()) {
            Log.e(TAG, "apartmentId is null or empty");
            Toast.makeText(this, "Error: No apartment ID provided", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        edtApartmentId.setText(apartmentId);
        edtApartmentId.setEnabled(false); // Disable editing

        ArrayList<String> years = new ArrayList<>();
        for (int year = 2020; year <= 2050; year++) {
            years.add(String.valueOf(year));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);

        // Setup DatePicker for edtDate
        edtDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
                calendar.set(selectedYear, selectedMonth, selectedDay);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                edtDate.setText(sdf.format(calendar.getTime()));
            }, year, month, day).show();
        });

        // Setup button listeners
        btnAdd.setOnClickListener(v -> addInvoice());
        btnBack.setOnClickListener(v -> navigateBack());
    }

    private void addInvoice() {
        // Get input values
        String feeTypeVietnamese = spinnerType.getSelectedItem().toString();
        String totalStr = edtTotal.getText().toString().trim();
        String month = spinnerMonth.getSelectedItem().toString();
        String year = spinnerYear.getSelectedItem().toString();
        String dueDateStr = edtDate.getText().toString().trim();
        String note = edtNote.getText().toString().trim();
        String statusStr = spinnerStatus.getSelectedItem().toString();

        // Validate inputs
        if (dueDateStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ngày hạn thanh toán", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate total (must be a positive integer)
        int total;
        try {
            total = Integer.parseInt(totalStr);
            if (total <= 0) {
                Toast.makeText(this, "Số tiền phải là số nguyên dương", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số tiền không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse dueDate
        Timestamp dueDate;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date parsedDate = sdf.parse(dueDateStr);
            dueDate = new Timestamp(parsedDate);
        } catch (Exception e) {
            Log.e(TAG, "Invalid date format: " + dueDateStr, e);
            Toast.makeText(this, "Định dạng ngày không hợp lệ (dd/MM/yyyy)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Map feeType
        String feeType;
        switch (feeTypeVietnamese) {
            case "Dịch vụ":
                feeType = "service";
                break;
            case "Nước":
                feeType = "water";
                break;
            case "Điện":
                feeType = "electricity";
                break;
            case "Gửi xe":
                feeType = "parking";
                break;
            case "Khác":
                feeType = "other";
                break;
            default:
                feeType = feeTypeVietnamese;
        }

        // Map status
        boolean status = statusStr.equals("Đã thanh toán");

        // Create Invoice object
        Invoice invoice = new Invoice(apartmentId, feeType, String.valueOf(total), month, note, year, dueDate, status, null);

        // Save to Firestore using InvoiceHelper
        invoiceHelper.addInvoice(invoice)
                .addOnSuccessListener(documentId -> {
                    Log.d(TAG, "Invoice added with ID: " + documentId);
                    Toast.makeText(this, "Thêm hóa đơn thành công", Toast.LENGTH_SHORT).show();
                    navigateBack();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding invoice", e);
                    Toast.makeText(this, "Lỗi khi thêm hóa đơn", Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateBack() {
        Intent intent = new Intent(this, nqkManagementInvoiceListActivity.class);
        intent.putExtra("apartmentId", apartmentId);
        startActivity(intent);
        finish();
    }
}