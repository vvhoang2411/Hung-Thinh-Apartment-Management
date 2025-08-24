package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungthinhapartmentmanagement.Adapter.ManagementNotificationAdapter;
import com.example.hungthinhapartmentmanagement.Helper.AnnouncementHelper;
import com.example.hungthinhapartmentmanagement.Model.Announcement;
import com.example.hungthinhapartmentmanagement.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ManagementNotificationActivity extends AppCompatActivity {

    private static final String TAG = "ManagementNotificationActivity";
    private RecyclerView rvNotifications;
    private ManagementNotificationAdapter adapter;
    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private String currentEmail = "nqk@gmail.com"; // Giả định email hiện tại, thay bằng logic thực tế

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.mt_activity_management_notification);

        // Ánh xạ RecyclerView
        rvNotifications = findViewById(R.id.rvNotifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ManagementNotificationAdapter(this, new ArrayList<>());
        rvNotifications.setAdapter(adapter);

        // Load danh sách thông báo
        loadAnnouncements();

        // Xử lý nút Add
        findViewById(R.id.btnAdd).setOnClickListener(v -> {
            Intent intent = new Intent(ManagementNotificationActivity.this, ManagementNotificationCreateActivity.class);
            intent.putExtra("email", currentEmail); // Chỉ truyền email
            startActivity(intent);
        });
    }

    private void loadAnnouncements() {
        new Thread(() -> {
            try {
                AnnouncementHelper helper = new AnnouncementHelper();
                List<Announcement> announcements = helper.getAllAnnouncements(); // Load tất cả thông báo
                uiHandler.post(() -> {
                    if (!announcements.isEmpty()) {
                        adapter.updateData(announcements); // Cập nhật dữ liệu vào adapter
                    } else {
                        Toast.makeText(this, "Không có thông báo nào", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Error loading announcements: " + e.getMessage());
                uiHandler.post(() -> Toast.makeText(this, "Lỗi khi tải thông báo", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tải lại danh sách khi quay lại activity
        loadAnnouncements();
    }
}