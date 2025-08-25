package com.example.hungthinhapartmentmanagement.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hungthinhapartmentmanagement.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class hdhEditResidentActivity extends AppCompatActivity {
    private EditText edtName, edtBirth, edtPhone, edtEmail;
    private Spinner spinnerRoom;
    private RadioGroup rgGender, rgRelation;
    private Button btnEdit;
    private ImageButton btnBack;
    private FirebaseFirestore db;
    private List<String> availableRooms;
    private String residentId;
    private ScrollView editResLayout;
    private static final String PHONE_PATTERN = "^0[0-9]{9,10}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_resident); // Sử dụng layout tương tự add, nhưng đổi tên nếu cần

        // Khởi tạo Firebase
        db = FirebaseFirestore.getInstance();

        // Ánh xạ các view
        edtName = findViewById(R.id.edtName);
        edtBirth = findViewById(R.id.edtBirth);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        spinnerRoom = findViewById(R.id.spinnerRoom);
        rgGender = findViewById(R.id.rgGender);
        rgRelation = findViewById(R.id.rgRelation);
        btnEdit = findViewById(R.id.btnEdit);
        btnBack = findViewById(R.id.btnBack);
        editResLayout = findViewById(R.id.editResLayout);
        editResLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        View.OnClickListener dateClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(hdhEditResidentActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                edtBirth.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        };

        edtBirth.setOnClickListener(dateClickListener);

        // Lấy ID từ Intent
        residentId = getIntent().getStringExtra("residentId");
        if (residentId == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID cư dân", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Khởi tạo danh sách phòng
        availableRooms = new ArrayList<>();
        loadAvailableRooms();

        // Tải dữ liệu hiện tại
//        loadResidentData();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResident();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(hdhEditResidentActivity.this, hdhResidentManagement.class);
                startActivity(intent);
            }
        });
    }

    private void loadAvailableRooms() {
        db.collection("apartments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        availableRooms.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String apartmentId = document.getString("apartment_number");
                            if (apartmentId != null) {
                                availableRooms.add(apartmentId);
                            }
                        }
                        if (availableRooms.isEmpty()) {
                            availableRooms.add("Không có phòng");
                        } else {
                            // Sắp xếp số phòng theo thứ tự số tăng dần
                            Collections.sort(availableRooms, new Comparator<String>() {
                                @Override
                                public int compare(String room1, String room2) {
                                    try {
                                        // Chuyển chuỗi thành số để so sánh
                                        int num1 = Integer.parseInt(room1);
                                        int num2 = Integer.parseInt(room2);
                                        return Integer.compare(num1, num2);
                                    } catch (NumberFormatException e) {
                                        // Nếu không phải số, so sánh chuỗi
                                        return room1.compareTo(room2);
                                    }
                                }
                            });
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, availableRooms);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerRoom.setAdapter(adapter);

                        // Tải dữ liệu cư dân sau khi có danh sách phòng
                        loadResidentData();
                    } else {
                        Toast.makeText(this, "Lỗi khi tải danh sách phòng", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadResidentData() {
        db.collection("residents").document(residentId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String apartmentId = documentSnapshot.getString("apartmentId");
                        String birthday = documentSnapshot.getString("birthday");
                        String email = documentSnapshot.getString("email");
                        String fullName = documentSnapshot.getString("fullName");
                        String gender = documentSnapshot.getString("gender");
                        String phone = documentSnapshot.getString("phone");
                        boolean relationship = documentSnapshot.getBoolean("relationship") != null ? documentSnapshot.getBoolean("relationship") : false;

                        // Điền dữ liệu vào form
                        edtName.setText(fullName);
                        edtBirth.setText(birthday);
                        edtPhone.setText(phone);
                        edtEmail.setText(email);

                        // Chọn giá trị hiện tại
                        int spinnerPosition = availableRooms.indexOf(apartmentId);
                        if (spinnerPosition >= 0) {
                            spinnerRoom.setSelection(spinnerPosition);
                        } else {
                            Toast.makeText(hdhEditResidentActivity.this, "Phòng hiện tại không còn trong danh sách 'Đã có'", Toast.LENGTH_SHORT).show();
                            spinnerRoom.setSelection(0); // Chọn giá trị mặc định
                        }

                        // Chọn RadioButton
                        if (gender.equals("Nam")) {
                            rgGender.check(R.id.rbUnpaid);
                        } else if (gender.equals("Nữ")) {
                            rgGender.check(R.id.rbPaid);
                        }

                        if (relationship) {
                            rgRelation.check(R.id.rbRelation);
                        } else {
                            rgRelation.check(R.id.rbUnrelation);
                        }
                    } else {
                        Toast.makeText(this, "Không tìm thấy dữ liệu cư dân", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void updateResident() {
        String name = edtName.getText().toString().trim();
        String birth = edtBirth.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String apartmentId = spinnerRoom.getSelectedItem().toString();
        String gender = ((RadioButton) findViewById(rgGender.getCheckedRadioButtonId())).getText().toString();
        boolean relationship = ((RadioButton) findViewById(rgRelation.getCheckedRadioButtonId())).getText().toString().equals("Có");

        if (name.isEmpty() || birth.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPhone(phone)) {
            Toast.makeText(this, "Số điện thoại không hợp lệ (phải bắt đầu bằng 0 và có 10-11 chữ số)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật "residents"
        Map<String, Object> residentUpdates = new HashMap<>();
        residentUpdates.put("apartmentId", apartmentId);
        residentUpdates.put("birthday", birth);
        residentUpdates.put("fullName", name);
        residentUpdates.put("gender", gender);
        residentUpdates.put("phone", phone);
        residentUpdates.put("relationship", relationship);

        db.collection("residents").document(residentId)
                .update(residentUpdates)
                .addOnSuccessListener(aVoid -> {
                    // Cập nhật "users" (chỉ name và phone, role giữ nguyên)
                    Map<String, Object> userUpdates = new HashMap<>();
                    userUpdates.put("name", name);
                    userUpdates.put("phone", phone);

                    db.collection("users").document(residentId)
                            .update(userUpdates)
                            .addOnSuccessListener(aVoid1 -> {
                                Toast.makeText(hdhEditResidentActivity.this, "Cập nhật cư dân thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(hdhEditResidentActivity.this, hdhResidentManagement.class);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> Toast.makeText(hdhEditResidentActivity.this, "Lỗi khi cập nhật users: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> Toast.makeText(hdhEditResidentActivity.this, "Lỗi khi cập nhật residents: " + e.getMessage(), Toast.LENGTH_SHORT).show());

    }

    private boolean isValidPhone(String phone) {
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        return pattern.matcher(phone).matches();
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
