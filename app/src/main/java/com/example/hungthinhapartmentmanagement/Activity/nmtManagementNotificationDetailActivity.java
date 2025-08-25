package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hungthinhapartmentmanagement.Helper.AnnouncementHelper;
import com.example.hungthinhapartmentmanagement.R;

import java.util.concurrent.ExecutionException;

public class nmtManagementNotificationDetailActivity extends AppCompatActivity {

    private static final String TAG = "ManagementNotificationDetailActivity";
    private TextView tvDetailTitle, tvDetailDate, tvDetailContent;
    private Button btnDelete;
    private ImageButton btnBack;
    private String documentId;
    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_management_notification_detail);

        // Ánh xạ các thành phần
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailDate = findViewById(R.id.tvDetailDate);
        tvDetailContent = findViewById(R.id.tvDetailContent);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBack);

        // Nhận thông tin từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            documentId = intent.getStringExtra("documentId");
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            String createAt = intent.getStringExtra("createAt");

            // Đặt giá trị vào TextView
            tvDetailTitle.setText(title != null ? title : "Không có tiêu đề");
            tvDetailContent.setText(content != null ? content : "Không có nội dung");
            tvDetailDate.setText(createAt != null ? createAt : "Không có ngày");
        } else {
            Toast.makeText(this, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Xử lý nút Back
        btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(this, hdhMainActivity.class);
            backIntent.putExtra("returnTab", 2); // 2 corresponds to nav_notification
            backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // Reuse existing MainActivity
            startActivity(backIntent);
            finish();
        });

        // Xử lý nút Delete với hộp thoại xác nhận
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa thông báo này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        // Thực hiện xóa trên thread nền
                        new Thread(() -> {
                            try {
                                AnnouncementHelper helper = new AnnouncementHelper();
                                boolean deleted = helper.deleteAnnouncementById(documentId);
                                uiHandler.post(() -> {
                                    if (deleted) {
                                        Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                        Intent backIntent = new Intent(this, hdhMainActivity.class);
                                        backIntent.putExtra("returnTab", 2); // 2 corresponds to nav_notification
                                        backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // Reuse existing MainActivity
                                        startActivity(backIntent);
                                        finish();
                                    } else {
                                        Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (ExecutionException | InterruptedException e) {
                                Log.e(TAG, "Error deleting announcement: " + e.getMessage());
                                uiHandler.post(() -> Toast.makeText(this, "Lỗi khi xóa thông báo", Toast.LENGTH_SHORT).show());
                            }
                        }).start();
                    })
                    .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });
    }
}