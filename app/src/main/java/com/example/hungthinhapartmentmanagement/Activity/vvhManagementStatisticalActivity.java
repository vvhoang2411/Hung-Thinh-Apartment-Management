package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hungthinhapartmentmanagement.Helper.StatisticalHelper;
import com.example.hungthinhapartmentmanagement.R;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class vvhManagementStatisticalActivity extends AppCompatActivity {
    private Spinner spinnerMonth, spinnerYear;
    private TextView tvParkingCollected, tvServiceCollected, tvWaterCollected, tvElectricityCollected, tvOtherCollected, tvTotalCollected;
    private TextView tvParkingUncollected, tvServiceUncollected, tvWaterUncollected, tvElectricityUncollected, tvOtherUncollected, tvTotalUncollected;
    private Button btnTimKiem;
    private ImageButton btnBack;
    private StatisticalHelper statisticalHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_management_statistical);

        // Khởi tạo StatisticalHelper
        statisticalHelper = new StatisticalHelper();
        Log.d("VVHManagementStatisticalActivity", "statisticalHelper initialized");

        // Khởi tạo các view
        btnBack = findViewById(R.id.btnBack);
        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerYear = findViewById(R.id.spinnerYear);
        btnTimKiem = findViewById(R.id.btnTimKiem);
        tvParkingCollected = findViewById(R.id.tvParkingCollected);
        tvServiceCollected = findViewById(R.id.tvServiceCollected);
        tvWaterCollected = findViewById(R.id.tvWaterCollected);
        tvElectricityCollected = findViewById(R.id.tvElectricityCollected);
        tvOtherCollected = findViewById(R.id.tvOtherCollected);
        tvTotalCollected = findViewById(R.id.tvTotalCollected);
        tvParkingUncollected = findViewById(R.id.tvParkingUncollected);
        tvServiceUncollected = findViewById(R.id.tvServiceUncollected);
        tvWaterUncollected = findViewById(R.id.tvWaterUncollected);
        tvElectricityUncollected = findViewById(R.id.tvElectricityUncollected);
        tvOtherUncollected = findViewById(R.id.tvOtherUncollected);
        tvTotalUncollected = findViewById(R.id.tvTotalUncollected);

        // Kiểm tra các view
        if (btnTimKiem == null) {
            Log.e("VVHManagementStatisticalActivity", "btnTimKiem is null. Check layout XML for R.id.btnTimKiem");
            Toast.makeText(this, "Không tìm thấy nút Tìm kiếm", Toast.LENGTH_SHORT).show();
            return;
        }
        if (spinnerMonth == null || spinnerYear == null) {
            Log.e("VVHManagementStatisticalActivity", "Spinner is null. Check layout XML for R.id.spinnerMonth or R.id.spinnerYear");
            Toast.makeText(this, "Không tìm thấy Spinner", Toast.LENGTH_SHORT).show();
            return;
        }
        if (btnBack == null) {
            Log.e("VVHManagementStatisticalActivity", "btnBack is null. Check layout XML for R.id.btnBack");
            Toast.makeText(this, "Không tìm thấy nút Back", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thiết lập adapter cho spinnerYear
        ArrayList<String> years = new ArrayList<>();
        for (int year = 2020; year <= 2050; year++) {
            years.add(String.valueOf(year));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);

        // Xử lý sự kiện nhấn nút Back
        btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(this, hdhMainActivity.class);
            backIntent.putExtra("returnTab", 1);
            backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(backIntent);
            finish();
        });

        // Xử lý sự kiện nhấn nút Tìm kiếm
        btnTimKiem.setOnClickListener(v -> {
            Log.d("VVHManagementStatisticalActivity", "btnTimKiem clicked");
            String selectedMonth = spinnerMonth.getSelectedItem().toString();
            String selectedYear = spinnerYear.getSelectedItem().toString();

            // Reset các giá trị TextView
            resetTextViews();

            // Gọi hàm để lấy dữ liệu với status = true (đã thu)
            statisticalHelper.getInvoicesByMonthYearStatus(selectedMonth, selectedYear, true)
                    .addOnSuccessListener(querySnapshot -> {
                        long parkingCollected = 0, serviceCollected = 0, waterCollected = 0, electricityCollected = 0, otherCollected = 0;

                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String feeType = document.getString("feeType");
                            String moneyStr = document.getString("money");
                            long money = parseMoney(moneyStr);

                            if (feeType != null) {
                                switch (feeType) {
                                    case "parking":
                                        parkingCollected += money;
                                        break;
                                    case "service":
                                        serviceCollected += money;
                                        break;
                                    case "water":
                                        waterCollected += money;
                                        break;
                                    case "electricity":
                                        electricityCollected += money;
                                        break;
                                    case "other":
                                        otherCollected += money;
                                        break;
                                }
                            }
                        }

                        // Cập nhật TextView cho các khoản đã thu
                        tvParkingCollected.setText(formatMoney(parkingCollected));
                        tvServiceCollected.setText(formatMoney(serviceCollected));
                        tvWaterCollected.setText(formatMoney(waterCollected));
                        tvElectricityCollected.setText(formatMoney(electricityCollected));
                        tvOtherCollected.setText(formatMoney(otherCollected));
                        long totalCollected = parkingCollected + serviceCollected + waterCollected + electricityCollected + otherCollected;
                        tvTotalCollected.setText(formatMoney(totalCollected));
                    })
                    .addOnFailureListener(e -> {
                        Log.e("VVHManagementStatisticalActivity", "Error fetching collected data: " + e.getMessage());
                        Toast.makeText(vvhManagementStatisticalActivity.this, "Lỗi khi lấy dữ liệu đã thu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

            // Gọi hàm để lấy dữ liệu với status = false (chưa thu)
            statisticalHelper.getInvoicesByMonthYearStatus(selectedMonth, selectedYear, false)
                    .addOnSuccessListener(querySnapshot -> {
                        long parkingUncollected = 0, serviceUncollected = 0, waterUncollected = 0, electricityUncollected = 0, otherUncollected = 0;

                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String feeType = document.getString("feeType");
                            String moneyStr = document.getString("money");
                            long money = parseMoney(moneyStr);

                            if (feeType != null) {
                                switch (feeType) {
                                    case "parking":
                                        parkingUncollected += money;
                                        break;
                                    case "service":
                                        serviceUncollected += money;
                                        break;
                                    case "water":
                                        waterUncollected += money;
                                        break;
                                    case "electricity":
                                        electricityUncollected += money;
                                        break;
                                    case "other":
                                        otherUncollected += money;
                                        break;
                                }
                            }
                        }

                        // Cập nhật TextView cho các khoản chưa thu
                        tvParkingUncollected.setText(formatMoney(parkingUncollected));
                        tvServiceUncollected.setText(formatMoney(serviceUncollected));
                        tvWaterUncollected.setText(formatMoney(waterUncollected));
                        tvElectricityUncollected.setText(formatMoney(electricityUncollected));
                        tvOtherUncollected.setText(formatMoney(otherUncollected));
                        long totalUncollected = parkingUncollected + serviceUncollected + waterUncollected + electricityUncollected + otherUncollected;
                        tvTotalUncollected.setText(formatMoney(totalUncollected));
                    })
                    .addOnFailureListener(e -> {
                        Log.e("VVHManagementStatisticalActivity", "Error fetching uncollected data: " + e.getMessage());
                        Toast.makeText(vvhManagementStatisticalActivity.this, "Lỗi khi lấy dữ liệu chưa thu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    // Hàm định dạng số tiền: thêm dấu . mỗi 3 số và thêm "đ"
    private String formatMoney(long money) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(money).replace(",", ".") + "đ";
    }

    // Hàm parse tiền từ String sang long
    private long parseMoney(String moneyStr) {
        try {
            return Long.parseLong(moneyStr);
        } catch (NumberFormatException e) {
            Log.e("VVHManagementStatisticalActivity", "Error parsing money: " + moneyStr, e);
            return 0L;
        }
    }

    // Hàm reset các TextView về 0
    private void resetTextViews() {
        String zeroMoney = formatMoney(0);
        tvParkingCollected.setText(zeroMoney);
        tvServiceCollected.setText(zeroMoney);
        tvWaterCollected.setText(zeroMoney);
        tvElectricityCollected.setText(zeroMoney);
        tvOtherCollected.setText(zeroMoney);
        tvTotalCollected.setText(zeroMoney);
        tvParkingUncollected.setText(zeroMoney);
        tvServiceUncollected.setText(zeroMoney);
        tvWaterUncollected.setText(zeroMoney);
        tvElectricityUncollected.setText(zeroMoney);
        tvOtherUncollected.setText(zeroMoney);
        tvTotalUncollected.setText(zeroMoney);
    }
}