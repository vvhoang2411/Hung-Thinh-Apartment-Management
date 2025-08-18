package com.example.hungthinhapartmentmanagement.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hungthinhapartmentmanagement.R;

public class RegisterParking extends AppCompatActivity {

    private Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_parking);

        btnConfirm = findViewById(R.id.btnConfirm);

        // Xử lý nút "Xác nhận"
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trả về trang trước đó (ParkingAdapter)
                finish();
            }
        });

        // Xử lý nút Back trong header (nếu có ImageView với id btnBack)
        View btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}