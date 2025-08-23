package com.example.hungthinhapartmentmanagement.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hungthinhapartmentmanagement.Helper.ResidentHelper;
import com.example.hungthinhapartmentmanagement.Model.Resident;
import com.example.hungthinhapartmentmanagement.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etFullName, etPhone, etBirthday;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale;
    private Button btnSave;
    private ImageButton btnBack;
    private String email;
    private ResidentHelper residentHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        // Ánh xạ các thành phần
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etBirthday = findViewById(R.id.etBirthday);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        // Nhận email từ Intent
        email = getIntent().getStringExtra("email");
        residentHelper = new ResidentHelper();

        // Load dữ liệu ban đầu
        loadResidentInfo();

        // Xử lý DatePicker cho etBirthday
        etBirthday.setOnClickListener(v -> showDatePickerDialog());

        // Xử lý sự kiện btnSave
        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                String fullName = etFullName.getText().toString().trim();
                String gender = rbMale.isChecked() ? "Nam" : "Nữ";
                String phone = etPhone.getText().toString().trim();
                Date birthday = parseDate(etBirthday.getText().toString().trim());
                residentHelper.updateResidentInfo(email, fullName, gender, phone, birthday);
                Toast.makeText(this, "Thông tin đã được cập nhật", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainResidentActivity.class);
                intent.putExtra("fragment", "HomeResident"); // Chuyển về HomeResidentFragment
                startActivity(intent);
                finish();
            }
        });

        // Xử lý sự kiện btnBack và btnCancel
        btnBack.setOnClickListener(v -> finish());
        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());
    }

    private void loadResidentInfo() {
        residentHelper.getResidentByEmail(email, new ResidentHelper.OnResidentLoadedListener() {
            @Override
            public void onResidentsLoaded(List<Resident> residents) {
                if (!residents.isEmpty()) {
                    Resident resident = residents.get(0);
                    etFullName.setText(resident.getFullName());
                    etPhone.setText(resident.getPhone());
                    if ("Nam".equals(resident.getGender())) rbMale.setChecked(true);
                    else rbFemale.setChecked(true);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    if (resident.getBirthday() != null) {
                        etBirthday.setText(sdf.format(resident.getBirthday()));
                    }
                }
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        etBirthday.setText(String.format("%02d/%02d/%d", dayOfMonth, month + 1, year));
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private boolean validateInputs() {
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String birthday = etBirthday.getText().toString().trim();

        if (fullName.isEmpty()) {
            etFullName.setError("Họ và tên không được để trống");
            return false;
        }
        if (phone.isEmpty() || !phone.matches("\\d{10}")) {
            etPhone.setError("Số điện thoại phải là 10 chữ số");
            return false;
        }
        if (birthday.isEmpty() || !isValidDate(birthday)) {
            etBirthday.setError("Ngày sinh không hợp lệ (dd/MM/yyyy)");
            return false;
        }
        return true;
    }

    private Date parseDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    private boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}