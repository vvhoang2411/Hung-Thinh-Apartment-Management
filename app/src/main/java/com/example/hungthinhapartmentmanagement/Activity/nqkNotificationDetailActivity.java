package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hungthinhapartmentmanagement.R;

public class nqkNotificationDetailActivity extends AppCompatActivity {

    private static final String TAG = "NotificationDetailActivity";
    private TextView tvTitle, tvContent, tvCreateAt;
    private ImageButton btnBack;
    private String apartmentId, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification_detail);

        tvTitle = findViewById(R.id.tvDetailTitle);
        tvContent = findViewById(R.id.tvDetailContent);
        tvCreateAt = findViewById(R.id.tvDetailDate);
        btnBack = findViewById(R.id.btnBack);

        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            String createAt = intent.getStringExtra("createAt");
            email = intent.getStringExtra("email");
            apartmentId = intent.getStringExtra("apartmentId");
            String announcementId = intent.getStringExtra("announcementId");

            if (title != null) tvTitle.setText(title);
            else tvTitle.setText("Không có tiêu đề");

            if (content != null) tvContent.setText(content);
            else tvContent.setText("Không có nội dung");

            if (createAt != null) tvCreateAt.setText(createAt);
            else tvCreateAt.setText("Không có ngày tạo");
        } else {
            Log.e(TAG, "Intent is null");
            Toast.makeText(this, "Không thể tải dữ liệu thông báo", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(this, nqkResidentMainActivity.class);
            backIntent.putExtra("fragment", "NotificationResident"); // Quay lại tab Notification
            backIntent.putExtra("apartmentId", apartmentId);
            backIntent.putExtra("email", email);
            startActivity(backIntent);
            finish();
        });
    }
}