package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.hungthinhapartmentmanagement.R;

public class ResidentPaymentActivity extends AppCompatActivity {
    private static final String TAG = "ResidentPaymentActivity";
    private TextView tvAmount, tvContent;
    private ImageButton btnBack;
    private String apartmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resident_payment);

        // Initialize views
        tvAmount = findViewById(R.id.tvAmount);
        tvContent = findViewById(R.id.tvContent);
        btnBack = findViewById(R.id.btnBack);

        // Get data from Intent
        Intent intent = getIntent();
        apartmentId = intent.getStringExtra("apartmentId");
        String billPrice = intent.getStringExtra("billPrice");
        String billType = intent.getStringExtra("billType");

        // Check for null values
        if (apartmentId == null || billPrice == null || billType == null) {
            Log.e(TAG, "Missing Intent extras: apartmentId=" + apartmentId + ", billPrice=" + billPrice + ", billType=" + billType);
            Toast.makeText(this, "Error: Missing payment details", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Set text for tvAmount and tvContent
        tvAmount.setText("Số tiền: " + billPrice);
        tvContent.setText("Nội dung: Căn hộ " + apartmentId + " thanh toán " + billType);

        // Setup back button listener
        btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(this, ResidentInvoiceActivity.class);
            backIntent.putExtra("apartmentId", apartmentId);
            startActivity(backIntent);
            finish();
        });
    }
}