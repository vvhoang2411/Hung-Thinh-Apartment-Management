package com.example.hungthinhapartmentmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hungthinhapartmentmanagement.Activity.RepairRequestListActivity;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerApartment;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Khởi tạo Spinner với mảng từ strings.xml
        spinnerApartment = findViewById(R.id.spinnerApartment);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.apartment_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerApartment.setAdapter(adapter);

        // Thiết lập sự kiện cho Button
        Button btnReport = findViewById(R.id.btnReport);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy giá trị được chọn từ Spinner
                String apartmentId = spinnerApartment.getSelectedItem().toString();

                // Tạo Intent và mở RepairRequestListActivity
                Intent intent = new Intent(MainActivity.this, RepairRequestListActivity.class);
                intent.putExtra("apartmentId", apartmentId);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}