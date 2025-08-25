package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hungthinhapartmentmanagement.Helper.AnnouncementHelper;
import com.example.hungthinhapartmentmanagement.Model.Announcement;
import com.example.hungthinhapartmentmanagement.R;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ManagementNotificationCreateActivity extends AppCompatActivity {

    private static final String TAG = "ManagementNotificationCreateActivity";
    private RadioGroup recipientOptions;
    private RadioButton radioAllResidents, radioSpecificFloor, radioSpecificApartment;
    private EditText floorInput, apartmentInput, editTitle, editContent;
    private Button btnSubmit;
    private ImageButton btnBack;
    private String senderEmail;
    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.mt_activity_management_notification_create);

        // Ánh xạ các thành phần
        recipientOptions = findViewById(R.id.recipient_options);
        radioAllResidents = findViewById(R.id.radio_all_residents);
        radioSpecificFloor = findViewById(R.id.radio_specific_floor);
        radioSpecificApartment = findViewById(R.id.radio_specific_apartment);
        floorInput = findViewById(R.id.floor_input);
        apartmentInput = findViewById(R.id.apartment_input);
        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);

        // Nhận email từ Intent
        senderEmail = getIntent().getStringExtra("email");
        if (senderEmail == null || senderEmail.isEmpty()) {
            Toast.makeText(this, "Không có email người gửi", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Ẩn các input ban đầu
        floorInput.setVisibility(View.GONE);
        apartmentInput.setVisibility(View.GONE);

        // Xử lý RadioGroup để hiển thị/ẩn các input
        recipientOptions.setOnCheckedChangeListener((group, checkedId) -> {
            floorInput.setVisibility(View.GONE);
            apartmentInput.setVisibility(View.GONE);

            if (checkedId == R.id.radio_specific_floor) {
                floorInput.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.radio_specific_apartment) {
                apartmentInput.setVisibility(View.VISIBLE);
            }
        });

        // Xử lý nút Back
        btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(this, ManagementNotificationActivity.class);
            startActivity(backIntent);
            finish();
        });

        // Xử lý nút Submit
        btnSubmit.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String content = editContent.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Tiêu đề và nội dung không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            String targetType = "";
            String targetValue ="";

            int checkedId = recipientOptions.getCheckedRadioButtonId();
            if (checkedId == R.id.radio_all_residents) {
                targetType = "all";
                targetValue = "";
            } else if (checkedId == R.id.radio_specific_floor) {
                targetType = "floor";
                targetValue = floorInput.getText().toString().trim();
                if (targetValue.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập số tầng", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (checkedId == R.id.radio_specific_apartment) {
                targetType = "apartment";
                targetValue = apartmentInput.getText().toString().trim();
                if (targetValue.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập số căn hộ", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                Toast.makeText(this, "Vui lòng chọn đối tượng gửi", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo đối tượng Announcement
            Announcement newAnnouncement = new Announcement();
            newAnnouncement.setTitle(title);
            newAnnouncement.setContent(content);
            newAnnouncement.setCreateAt(Timestamp.now());
            newAnnouncement.setSenderEmail(senderEmail);
            newAnnouncement.setTargetType(targetType);
            newAnnouncement.setTargetValue(targetValue);
            newAnnouncement.setReadBy(new ArrayList<>()); // Danh sách rỗng

            // Thêm thông báo lên Firestore trên thread nền
            new Thread(() -> {
                try {
                    AnnouncementHelper helper = new AnnouncementHelper();
                    helper.addNewAnnouncement(newAnnouncement);
                    uiHandler.post(() -> {
                        Toast.makeText(this, "Thêm thông báo thành công", Toast.LENGTH_SHORT).show();
                        Intent backIntent = new Intent(this, ManagementNotificationActivity.class);
                        startActivity(backIntent);
                        finish();
                    });
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, "Error adding announcement: " + e.getMessage());
                    uiHandler.post(() -> Toast.makeText(this, "Thêm thông báo thất bại", Toast.LENGTH_SHORT).show());
                }
            }).start();
        });
    }
}