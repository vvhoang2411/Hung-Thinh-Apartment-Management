package com.example.hungthinhapartmentmanagement.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hungthinhapartmentmanagement.R;

public class BookingDeltailActivity extends AppCompatActivity {

    private Spinner spinnerStartDate, spinnerDuration;
    private TextView tvName, tvPhone;
    private ImageButton btnBack;
    private Button btnBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        // Ánh xạ các view
        tvName = findViewById(R.id.etName); // hoặc TextView nếu bạn dùng TextView
        tvPhone = findViewById(R.id.etPhone);
        spinnerStartDate = findViewById(R.id.spinnerStartDate);
        spinnerDuration = findViewById(R.id.spinnerDuration);
        btnBack = findViewById(R.id.btnBack);
        btnBook = findViewById(R.id.btnBook);

        // Thiết lập Spinner data...
        ArrayAdapter<String> adapterStartDate = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                new String[]{"29/07/2025", "30/07/2025", "31/07/2025"});
        adapterStartDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStartDate.setAdapter(adapterStartDate);

        ArrayAdapter<String> adapterDuration = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                new String[]{"1 tháng", "3 tháng", "6 tháng"});
        adapterDuration.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDuration.setAdapter(adapterDuration);

        btnBack.setOnClickListener(v -> finish());

        btnBook.setOnClickListener(v -> {
            String name = tvName.getText().toString().trim();
            String phone = tvPhone.getText().toString().trim();
            String startDate = spinnerStartDate.getSelectedItem().toString();
            String duration = spinnerDuration.getSelectedItem().toString();
            String facility = "Gym";

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên và số điện thoại", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Đặt lịch thành công:\nNgày bắt đầu: " + startDate + "\nThời hạn: " + duration, Toast.LENGTH_LONG).show();

            Intent goSuccessIntent = new Intent(BookingDeltailActivity.this, BookingSuccessActivity.class);
            goSuccessIntent.putExtra("facility", facility);
            goSuccessIntent.putExtra("date", startDate);
            goSuccessIntent.putExtra("duration", duration);
            goSuccessIntent.putExtra("name", name);
            goSuccessIntent.putExtra("phone", phone);
            startActivity(goSuccessIntent);
        });
    }
}

