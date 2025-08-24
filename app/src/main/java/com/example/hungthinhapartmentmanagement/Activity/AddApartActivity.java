package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hungthinhapartmentmanagement.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AddApartActivity extends AppCompatActivity {
    private LinearLayout addApartLayout;
    private Spinner spinnerBuilding, spinnerFloor;
    private EditText editTextNumber, editTextArea, editTextDesc;
    private RadioGroup radioGroupStatus;
    private Button buttonAdd;
    private ImageButton buttonBack;
    private FirebaseFirestore db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_apart);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Ánh xạ các thành phần giao diện
        spinnerBuilding = findViewById(R.id.edtBuiltApt);
        spinnerFloor = findViewById(R.id.spinFloApt);
        editTextNumber = findViewById(R.id.edtNumberApt);
        editTextArea = findViewById(R.id.edtAreaApt);
        editTextDesc = findViewById(R.id.edtDescApt);
        radioGroupStatus = findViewById(R.id.rgStatus);
        buttonAdd = findViewById(R.id.btnAdd);
        buttonBack = findViewById(R.id.btnBack);
        addApartLayout = findViewById(R.id.addApartLayout);

        addApartLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddApartActivity.this, ApartmentManagement.class);
                startActivity(intent);
                finish();
            }
        });


        // Thiết lập Spinner cho Tòa (A, B)
        ArrayAdapter<CharSequence> buildingAdapter = ArrayAdapter.createFromResource(this,
                R.array.building_array, android.R.layout.simple_spinner_item);
        buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBuilding.setAdapter(buildingAdapter);

        // Thiết lập Spinner cho Tầng (1-15)
        String[] floors = new String[15];
        for (int i = 1; i <= 15; i++) {
            floors[i - 1] = String.valueOf(i);
        }
        ArrayAdapter<String> floorAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, floors);
        floorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFloor.setAdapter(floorAdapter);

        // Xử lý sự kiện nút Thêm mới
        buttonAdd.setOnClickListener(v -> {
            // Lấy dữ liệu từ các trường nhập liệu
            String building = spinnerBuilding.getSelectedItem().toString();
            String floor = spinnerFloor.getSelectedItem().toString();
            String number = editTextNumber.getText().toString().trim();
            String area = editTextArea.getText().toString().trim();
            String desc = editTextDesc.getText().toString().trim();
            int selectedRadioButtonId = radioGroupStatus.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            String status = selectedRadioButton.getText().toString();

            // Kiểm tra các trường bắt buộc
            if (number.isEmpty() || area.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra trùng lặp trước khi thêm
            checkApartmentNumberExists(number, building, floor, number, area, desc, status);
        });
    }

    // Hàm kiểm tra xem apartment_number đã tồn tại chưa
    private void checkApartmentNumberExists(String apartmentNumber, String building, String floor, String number, String area, String desc, String status) {
        db.collection("apartments")
                .whereEqualTo("apartment_number", apartmentNumber)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Nếu có document trùng, thông báo lỗi
                            Toast.makeText(AddApartActivity.this, "Số căn hộ đã tồn tại!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Nếu không trùng, thêm vào Firestore
                            addApartmentToFirestore(building, floor, number, area, desc, status);
                        }
                    } else {
                        Toast.makeText(AddApartActivity.this, "Lỗi khi kiểm tra: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    // Hàm thêm căn hộ vào Firestore
    private void addApartmentToFirestore(String building, String floor, String number, String area, String desc, String status) {
        // Tạo dữ liệu để thêm vào Firestore
        Map<String, Object> apartment = new HashMap<>();
        apartment.put("apartment_number", number);
        apartment.put("area", area);
        apartment.put("building", building);
        apartment.put("desc", desc);
        apartment.put("floor", floor);
        apartment.put("status", status);

        // Thêm dữ liệu vào Firestore
        db.collection("apartments")
                .add(apartment)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddApartActivity.this, "Thêm căn hộ thành công!", Toast.LENGTH_SHORT).show();
                    // Xóa các trường nhập liệu sau khi thêm thành công
                    editTextNumber.setText("");
                    editTextArea.setText("");
                    editTextDesc.setText("");
                    radioGroupStatus.check(R.id.rbUnpaid); // Đặt lại RadioButton mặc định
                    Intent intent = new Intent(AddApartActivity.this, ApartmentManagement.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddApartActivity.this, "Thêm căn hộ thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }

}
