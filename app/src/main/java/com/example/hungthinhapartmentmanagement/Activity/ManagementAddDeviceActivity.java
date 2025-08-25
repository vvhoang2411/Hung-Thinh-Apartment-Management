package com.example.hungthinhapartmentmanagement.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hungthinhapartmentmanagement.Helper.DeviceHelper;
import com.example.hungthinhapartmentmanagement.Model.Device;
import com.example.hungthinhapartmentmanagement.R;
import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ManagementAddDeviceActivity extends AppCompatActivity {

    private EditText edtManufacturer, edtMfg, edtModel, edtName, edtPosition, edtInspectionDate;
    private AutoCompleteTextView actDeviceType;
    private Button btnSave;
    private ImageButton btnBack;
    private DeviceHelper deviceHelper;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.mt_activity_management_add_device);

        // Initialize views
        edtManufacturer = findViewById(R.id.edtManufacturer);
        edtMfg = findViewById(R.id.edtManufactureYear);
        edtModel = findViewById(R.id.edtSerialModel);
        edtName = findViewById(R.id.edtDeviceName);
        edtPosition = findViewById(R.id.edtInstallationLocation);
        actDeviceType = findViewById(R.id.actDeviceType);
        edtInspectionDate = findViewById(R.id.edtInspectionDate);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        // Initialize DeviceHelper
        deviceHelper = new DeviceHelper();

        // Initialize date format
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Load device types into AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.device_types));
        actDeviceType.setAdapter(adapter);

        // Set up DatePicker for edtInspectionDate
        edtInspectionDate.setOnClickListener(v -> showDatePickerDialog());

        // Handle Save button click
        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                saveDevice();
            }
        });

        // Handle Back button click
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ManagementAddDeviceActivity.this, ManagementControlDeviceActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    edtInspectionDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private boolean validateInputs() {
        String manufacturer = edtManufacturer.getText().toString().trim();
        String mfg = edtMfg.getText().toString().trim();
        String model = edtModel.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String position = edtPosition.getText().toString().trim();
        String type = actDeviceType.getText().toString().trim();
        String inspectionDate = edtInspectionDate.getText().toString().trim();

        if (manufacturer.isEmpty()) {
            edtManufacturer.setError("Vui lòng nhập hãng sản xuất");
            return false;
        }
        if (mfg.isEmpty()) {
            edtMfg.setError("Vui lòng nhập năm sản xuất");
            return false;
        }
        // Validate manufacture year format
        if (!mfg.matches("\\d{4}")) {
            edtMfg.setError("Năm sản xuất phải là 4 chữ số");
            return false;
        }
        try {
            int year = Integer.parseInt(mfg);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (year < 1900 || year > currentYear) {
                edtMfg.setError("Năm sản xuất phải từ 1900 đến " + currentYear);
                return false;
            }
        } catch (NumberFormatException e) {
            edtMfg.setError("Năm sản xuất không hợp lệ");
            return false;
        }
        if (model.isEmpty()) {
            edtModel.setError("Vui lòng nhập model");
            return false;
        }
        if (name.isEmpty()) {
            edtName.setError("Vui lòng nhập tên thiết bị");
            return false;
        }
        if (position.isEmpty()) {
            edtPosition.setError("Vui lòng nhập vị trí");
            return false;
        }
        if (type.isEmpty()) {
            actDeviceType.setError("Vui lòng chọn loại thiết bị");
            return false;
        }
        if (inspectionDate.isEmpty()) {
            edtInspectionDate.setError("Vui lòng chọn ngày kiểm tra");
            return false;
        }

        return true;
    }

    private void saveDevice() {
        String manufacturer = edtManufacturer.getText().toString().trim();
        String mfg = edtMfg.getText().toString().trim();
        String model = edtModel.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String position = edtPosition.getText().toString().trim();
        String type = actDeviceType.getText().toString().trim();
        String inspectionDate = edtInspectionDate.getText().toString().trim();

        Timestamp dateCheck;
        try {
            Date date = dateFormat.parse(inspectionDate);
            dateCheck = new Timestamp(date);
        } catch (ParseException e) {
            Toast.makeText(this, "Lỗi định dạng ngày: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        Device device = new Device(manufacturer, mfg, model, name, position, type, dateCheck);
        deviceHelper.addDevice(device).addOnSuccessListener(documentReference -> {
            Toast.makeText(this, "Thêm thiết bị thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ManagementAddDeviceActivity.this, ManagementControlDeviceActivity.class);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Lỗi khi thêm thiết bị: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}