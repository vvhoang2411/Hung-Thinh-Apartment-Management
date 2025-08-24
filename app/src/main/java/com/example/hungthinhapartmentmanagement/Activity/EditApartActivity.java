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

public class EditApartActivity extends AppCompatActivity {
    private LinearLayout editApartLayout;
    private Spinner spinnerBuilding, spinnerFloor;
    private EditText editTextNumber, editTextArea, editTextDesc;
    private RadioGroup radioGroupStatus;
    private Button buttonUpdate;
    private ImageButton btnBack;
    private FirebaseFirestore db;
    private String apartmentId; // ID của document trong Firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_apart);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Ánh xạ các thành phần giao diện
        spinnerFloor = findViewById(R.id.spinFloApt);
        editTextNumber = findViewById(R.id.edtNumberApt);
        editTextArea = findViewById(R.id.edtAreaApt);
        editTextDesc = findViewById(R.id.edtDescApt);
        radioGroupStatus = findViewById(R.id.rgStatus);
        buttonUpdate = findViewById(R.id.btnUpdate);
        btnBack = findViewById(R.id.btnBack);
        editApartLayout = findViewById(R.id.editApartLayout);

        editApartLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditApartActivity.this, ApartmentManagement.class);
                startActivity(intent);
                finish();
            }
        });

        // Lấy dữ liệu từ Intent
        apartmentId = getIntent().getStringExtra("apartmentId");
        String apartmentNumber = getIntent().getStringExtra("apartmentNumber");
        String floor = getIntent().getStringExtra("floor");
        String area = getIntent().getStringExtra("area");
        String desc = getIntent().getStringExtra("desc");
        String status = getIntent().getStringExtra("status");

        // Thiết lập Spinner cho Tầng (1-15)
        String[] floors = new String[15];
        for (int i = 1; i <= 15; i++) {
            floors[i - 1] = String.valueOf(i);
        }
        ArrayAdapter<String> floorAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, floors);
        floorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFloor.setAdapter(floorAdapter);
        spinnerFloor.setSelection(Integer.parseInt(floor) - 1); // Đặt giá trị mặc định

        // Điền dữ liệu vào các trường
        editTextNumber.setText(apartmentNumber);
        editTextArea.setText(area);
        editTextDesc.setText(desc);
        if ("Còn trống".equals(status)) {
            radioGroupStatus.check(R.id.rbUnpaid);
        } else if ("Đã ở".equals(status)) {
            radioGroupStatus.check(R.id.rbPaid);
        }

        // Xử lý sự kiện nút Cập nhật
        buttonUpdate.setOnClickListener(v -> {
            String newFloor = spinnerFloor.getSelectedItem().toString();
            String newNumber = editTextNumber.getText().toString().trim();
            String newArea = editTextArea.getText().toString().trim();
            String newDesc = editTextDesc.getText().toString().trim();
            int selectedRadioButtonId = radioGroupStatus.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            String newStatus = selectedRadioButton.getText().toString();

            if (newNumber.isEmpty() || newArea.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            String newApartmentNumber = newNumber;

            // Kiểm tra trùng lặp trước khi cập nhật (trừ chính nó)
            checkApartmentNumberExistsForUpdate(apartmentId, newApartmentNumber, newFloor, newNumber, newArea, newDesc, newStatus);
        });
    }

    // Hàm kiểm tra trùng lặp khi cập nhật
    private void checkApartmentNumberExistsForUpdate(String currentId, String apartmentNumber, String floor, String number, String area, String desc, String status) {
        db.collection("apartments")
                .whereEqualTo("apartment_number", apartmentNumber)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Kiểm tra xem document trùng có phải là chính nó không
                            for (com.google.firebase.firestore.QueryDocumentSnapshot document : querySnapshot) {
                                if (!document.getId().equals(currentId)) {
                                    Toast.makeText(EditApartActivity.this, "Số căn hộ đã tồn tại!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        }
                        // Nếu không trùng hoặc trùng với chính nó, thực hiện cập nhật
                        updateApartmentInFirestore(currentId, floor, number, area, desc, status);
                    } else {
                        Toast.makeText(EditApartActivity.this, "Lỗi khi kiểm tra: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Hàm cập nhật căn hộ trong Firestore
    private void updateApartmentInFirestore(String id, String floor, String number, String area, String desc, String status) {
        Map<String, Object> apartment = new HashMap<>();
        apartment.put("apartment_number", number);
        apartment.put("area", area);
        apartment.put("desc", desc);
        apartment.put("floor", floor);
        apartment.put("status", status);

        db.collection("apartments")
                .document(id)
                .update(apartment)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditApartActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditApartActivity.this, ApartmentManagement.class);
                    startActivity(intent);
                    finish(); // Đóng activity sau khi cập nhật
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditApartActivity.this, "Cập nhật thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
