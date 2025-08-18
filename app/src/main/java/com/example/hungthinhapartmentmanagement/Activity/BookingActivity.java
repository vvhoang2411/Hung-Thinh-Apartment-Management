package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hungthinhapartmentmanagement.R;

public class BookingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking); // Sửa lỗi dấu * và tên đúng
        LinearLayout studioItem = findViewById(R.id.itemFacilityLayout);

        // Ánh xạ nút Back
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent chuyển sang ResidentHomeActivity
                Intent intent = new Intent(BookingActivity.this, ResidentHomeActivity.class);
                startActivity(intent);
                finish(); // Kết thúc BookingActivity để không bị phép quay ngược lại
            }
        });

        // Ví dụ: ánh xạ item_facility (bạn cần set id trong file item_facility.xml)
        LinearLayout facilityItem = findViewById(R.id.itemFacilityLayout);
        if (facilityItem != null) {
            facilityItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(BookingActivity.this, "Bạn đã chọn studio!", Toast.LENGTH_SHORT).show();
                    // TODO: Có thể mở màn hình chi tiết hoặc chuyển bước tiếp theo
                    Intent intent = new Intent(BookingActivity.this, BookingDeltailActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
