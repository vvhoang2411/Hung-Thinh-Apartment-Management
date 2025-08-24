package com.example.hungthinhapartmentmanagement.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hungthinhapartmentmanagement.Helper.ResidentHelper;
import com.example.hungthinhapartmentmanagement.Model.Resident;
import com.example.hungthinhapartmentmanagement.R;

import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";
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
                String birthday = etBirthday.getText().toString().trim();

                residentHelper.updateResidentInfo(email, fullName, gender, phone, birthday);
                Log.d(TAG, "Updating resident info with email: " + email + ", birthday: " + birthday);
                Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();

                // Chuyển về ResidentMainActivity và load HomeResidentFragment
                Intent intent = new Intent(this, ResidentMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("fragment", "HomeResident");
                Log.d(TAG, "Navigating to ResidentMainActivity with fragment: HomeResident");
                startActivity(intent);
                finish();
            }
        });

        // Xử lý sự kiện btnBack
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadResidentInfo() {
        Intent intent = getIntent();
        if (intent != null) {
            etFullName.setText(intent.getStringExtra("fullName"));
            etPhone.setText(intent.getStringExtra("phone"));
            String gender = intent.getStringExtra("gender");
            if ("Nam".equals(gender)) {
                rbMale.setChecked(true);
            } else if ("Nữ".equals(gender)) {
                rbFemale.setChecked(true);
            }
            String birthday = intent.getStringExtra("birthday");
            if (birthday != null) {
                etBirthday.setText(birthday);
            }
        }
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

    private boolean isValidDate(String dateStr) {
        if (dateStr.matches("\\d{2}/\\d{2}/\\d{4}")) {
            try {
                String[] parts = dateStr.split("/");
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]) - 1; // Tháng bắt đầu từ 0
                int year = Integer.parseInt(parts[2]);
                Calendar calendar = Calendar.getInstance();
                calendar.setLenient(false);
                calendar.set(year, month, day);
                calendar.getTime(); // Kiểm tra tính hợp lệ
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}