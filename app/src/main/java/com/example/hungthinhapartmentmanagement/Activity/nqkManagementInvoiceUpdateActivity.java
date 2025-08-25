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

public class nqkManagementInvoiceUpdateActivity extends AppCompatActivity {
    private EditText edtApartmentId, edtTotal, edtDate, edtNote;
    private Spinner spinnerType, spinnerMonth, spinnerYear, spinnerStatus;
    private Button btnUpdate, btnDelete;
    private ImageButton btnBack;
    private InvoiceHelper invoiceHelper;
    private String apartmentId, documentID;
    private static final String TAG = "ManagementInvoiceUpdateActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_management_invoice_update);

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
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBack);

        // Get data from Intent
        apartmentId = getIntent().getStringExtra("apartmentId");
        documentID = getIntent().getStringExtra("documentID");
        String feeType = getIntent().getStringExtra("feeType");
        String money = getIntent().getStringExtra("money");
        String month = getIntent().getStringExtra("month");
        String year = getIntent().getStringExtra("year");
        long dueDateMillis = getIntent().getLongExtra("dueDate", 0);
        boolean status = getIntent().getBooleanExtra("status", false);
        String note = getIntent().getStringExtra("note");

        if (apartmentId == null || apartmentId.isEmpty() || documentID == null || documentID.isEmpty()) {
            Log.e(TAG, "apartmentId or documentID is null or empty");
            Toast.makeText(this, "Error: Missing required data", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Set apartmentId to EditText and disable editing
        edtApartmentId.setText(apartmentId);
        edtApartmentId.setEnabled(false);

        // Setup spinnerYear (2020 to 2050)
        ArrayList<String> years = new ArrayList<>();
        for (int yearInt = 2020; yearInt <= 2050; yearInt++) {
            years.add(String.valueOf(yearInt));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);

        // Populate fields with Intent data
        populateFields(feeType, money, month, year, dueDateMillis, status, note);

        // Setup DatePicker for edtDate
        edtDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int yearInt = calendar.get(Calendar.YEAR);
            int monthInt = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
                calendar.set(selectedYear, selectedMonth, selectedDay);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                edtDate.setText(sdf.format(calendar.getTime()));
            }, yearInt, monthInt, day).show();
        });

        // Setup button listeners
        btnUpdate.setOnClickListener(v -> updateInvoice());
        btnDelete.setOnClickListener(v -> deleteInvoice());
        btnBack.setOnClickListener(v -> navigateBack());
    }

    private void populateFields(String feeType, String money, String month, String year, long dueDateMillis, boolean status, String note) {
        // Set feeType
        String[] feeTypes = getResources().getStringArray(R.array.loaiHoaDon);
        for (int i = 0; i < feeTypes.length; i++) {
            String feeTypeVietnamese;
            switch (feeType != null ? feeType.toLowerCase() : "") {
                case "service":
                    feeTypeVietnamese = "Dịch vụ";
                    break;
                case "water":
                    feeTypeVietnamese = "Nước";
                    break;
                case "electricity":
                    feeTypeVietnamese = "Điện";
                    break;
                case "parking":
                    feeTypeVietnamese = "Gửi xe";
                    break;
                case "other":
                    feeTypeVietnamese = "Khác";
                    break;
                default:
                    feeTypeVietnamese = feeType;
            }
            if (feeTypes[i].equals(feeTypeVietnamese)) {
                spinnerType.setSelection(i);
                break;
            }
        }

        // Set other fields
        if (money != null) edtTotal.setText(money);
        if (month != null) {
            String[] months = getResources().getStringArray(R.array.thang);
            for (int i = 0; i < months.length; i++) {
                if (months[i].equals(month)) {
                    spinnerMonth.setSelection(i);
                    break;
                }
            }
        }
        if (year != null) {
            ArrayAdapter<String> yearAdapter = (ArrayAdapter<String>) spinnerYear.getAdapter();
            int yearIndex = yearAdapter.getPosition(year);
            if (yearIndex >= 0) spinnerYear.setSelection(yearIndex);
        }
        if (dueDateMillis != 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            edtDate.setText(sdf.format(new Date(dueDateMillis)));
        }
        spinnerStatus.setSelection(status ? 0 : 1); // 0 for "Đã thanh toán", 1 for "Chưa thanh toán"
        if (note != null) edtNote.setText(note);
    }

    private void updateInvoice() {
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

        // Create updated Invoice object
        Invoice invoice = new Invoice(apartmentId, feeType, String.valueOf(total), month, note, year, dueDate, status, documentID);

        // Update in Firestore using InvoiceHelper
        invoiceHelper.updateInvoice(documentID, invoice)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Invoice updated with ID: " + documentID);
                    Toast.makeText(this, "Cập nhật hóa đơn thành công", Toast.LENGTH_SHORT).show();
                    navigateBack();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating invoice", e);
                    Toast.makeText(this, "Lỗi khi cập nhật hóa đơn", Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteInvoice() {
        // Show confirmation dialog
        new android.app.AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa hóa đơn này không?")
                .setPositiveButton("Xác nhận", (dialog, which) -> {
                    // Delete from Firestore using InvoiceHelper
                    invoiceHelper.deleteInvoice(documentID)
                            .addOnSuccessListener(aVoid -> {
                                Log.d(TAG, "Invoice deleted with ID: " + documentID);
                                Toast.makeText(this, "Xóa hóa đơn thành công", Toast.LENGTH_SHORT).show();
                                navigateBack();
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error deleting invoice", e);
                                Toast.makeText(this, "Lỗi khi xóa hóa đơn", Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("Hủy", (dialog, which) -> {
                    // Do nothing or dismiss the dialog
                    dialog.dismiss();
                })
                .setCancelable(true)
                .show();
    }

    private void navigateBack() {
        Intent intent = new Intent(this, nqkManagementInvoiceListActivity.class);
        intent.putExtra("apartmentId", apartmentId);
        startActivity(intent);
        finish();
    }
}