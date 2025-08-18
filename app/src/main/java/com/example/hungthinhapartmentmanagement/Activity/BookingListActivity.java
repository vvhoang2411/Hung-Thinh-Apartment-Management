package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hungthinhapartmentmanagement.R;

public class BookingListActivity extends AppCompatActivity {

    private TextView tvName, tvPhone, tvFacility, tvDate, tvDuration;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        tvName = findViewById(R.id.tvSuccessName);
        tvPhone = findViewById(R.id.tvSuccessPhone);
        tvFacility = findViewById(R.id.tvSuccessFacility);
        tvDate = findViewById(R.id.tvSuccessDate);
        tvDuration = findViewById(R.id.tvSuccessDuration);
        btnBack = findViewById(R.id.btnBack);

        // Kiểm tra nếu bạn có thêm TextView facility, date, duration thì thêm ánh xạ ở đây
        // Nếu chưa có thì bỏ qua hoặc thêm vào XML như hướng dẫn

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");
        String facility = intent.getStringExtra("facility");
        String date = intent.getStringExtra("date");
        String duration = intent.getStringExtra("duration");

        if (name != null) tvName.setText("Tên: " + name);
        if (phone != null) tvPhone.setText("SĐT: " + phone);

        // Nếu có các TextView này, thì mới setText, không sẽ lỗi
        if (facility != null && tvFacility != null)
            tvFacility.setText("Tiện ích: " + facility);
        if (date != null && tvDate != null)
            tvDate.setText("Ngày đặt lịch: " + date);
        if (duration != null && tvDuration != null)
            tvDuration.setText("Thời hạn: " + duration);

        btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(BookingListActivity.this, BookingActivity.class);
            // Nếu muốn truyền lại dữ liệu thì putExtra vào đây
            startActivity(backIntent);
            finish();
        });
    }
}
