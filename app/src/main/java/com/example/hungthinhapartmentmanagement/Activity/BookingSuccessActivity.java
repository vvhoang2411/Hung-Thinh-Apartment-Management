package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hungthinhapartmentmanagement.R;

public class BookingSuccessActivity extends AppCompatActivity {

    private TextView tvFacility, tvDate, tvDuration, tvName, tvPhone;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_success);

        tvName = findViewById(R.id.tvSuccessName);
        tvPhone = findViewById(R.id.tvSuccessPhone);
        tvFacility = findViewById(R.id.tvSuccessFacility);
        tvDate = findViewById(R.id.tvSuccessDate);
        tvDuration = findViewById(R.id.tvSuccessDuration);
        btnDone = findViewById(R.id.btnDone);

        Intent intent = getIntent();
        String facility = intent.getStringExtra("facility");
        String date = intent.getStringExtra("date");
        String duration = intent.getStringExtra("duration");
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");

        if (name != null) {
            tvName.setText("Tên: " + name);
        }
        if (phone != null) {
            tvPhone.setText("SĐT: " + phone);
        }
        tvFacility.setText("Tiện ích: " + facility);
        tvDate.setText("Ngày đặt lịch: " + date);
        tvDuration.setText("Thời hạn: " + duration);

        btnDone.setOnClickListener(v -> {
            Intent goListIntent = new Intent(BookingSuccessActivity.this, BookingListActivity.class);

            // Truyền dữ liệu (nếu cần)
            goListIntent.putExtra("name", name);
            goListIntent.putExtra("phone", phone);
            goListIntent.putExtra("facility", facility);
            goListIntent.putExtra("date", date);
            goListIntent.putExtra("duration", duration);

            startActivity(goListIntent);
            finish();
        });

    }
}
