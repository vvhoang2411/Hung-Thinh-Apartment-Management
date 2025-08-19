package com.example.hungthinhapartmentmanagement.Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hungthinhapartmentmanagement.R;

import java.util.Calendar;

public class ApartmentManagement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_apartment_management);

        final EditText dateInput = findViewById(R.id.dateInput);
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateInput.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(ApartmentManagement.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                            // Định dạng ngày tháng thành MM/YYYY
                            String formattedDate = (selectedMonth + 1) + "/" + selectedYear;
                            dateInput.setText(formattedDate);
                        }
                    }, year, month, day);
            datePickerDialog.show();
        });
    }
}