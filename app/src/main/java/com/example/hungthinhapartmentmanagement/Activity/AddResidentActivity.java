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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hungthinhapartmentmanagement.Model.Resident;
import com.example.hungthinhapartmentmanagement.Model.User;
import com.example.hungthinhapartmentmanagement.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AddResidentActivity extends AppCompatActivity {
    private EditText edtName, edtBirth, edtPhone, edtEmail, edtPassword;
    private Spinner spinnerRoom;
    private RadioGroup rgGender, rgRelation;
    private Button btnAdd;
    private ImageButton btnBack;
    private ScrollView addResLayout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private List<String> availableRooms;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resident);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        edtName = findViewById(R.id.edtName);
        edtBirth = findViewById(R.id.edtBirth);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        spinnerRoom = findViewById(R.id.spinnerRoom);
        rgGender = findViewById(R.id.rgGender);
        rgRelation = findViewById(R.id.rgRelation);
        btnAdd = findViewById(R.id.btnAdd);
        addResLayout = findViewById(R.id.addResLayout);
        addResLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddResidentActivity.this, ResidentManagement.class);
                startActivity(intent);
                finish();
            }
        });

        View.OnClickListener dateClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddResidentActivity.this,
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

        // Khởi tạo danh sách phòng
        availableRooms = new ArrayList<>();
        loadAvailableRooms();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addResident();
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
                            String apartmentNumber = document.getString("apartment_number");
                            if (apartmentNumber != null) {
                                availableRooms.add(apartmentNumber);
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
                        // Cấu hình Spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, availableRooms);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerRoom.setAdapter(adapter);
                    } else {
                        Toast.makeText(this, "Lỗi khi tải danh sách phòng", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addResident() {
        String name = edtName.getText().toString().trim();
        String birth = edtBirth.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String apartmentNumber = spinnerRoom.getSelectedItem().toString();
        String gender = ((RadioButton) findViewById(rgGender.getCheckedRadioButtonId())).getText().toString();
        boolean relationship = ((RadioButton) findViewById(rgRelation.getCheckedRadioButtonId())).getText().toString().equals("Có");

        if (name.isEmpty() || birth.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo tài khoản trên Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();

                            // Thêm vào collection "users"
                            User newUser = new User(email, name, phone, "Cư dân");
                            db.collection("users").document(uid)
                                    .set(newUser)
                                    .addOnSuccessListener(aVoid -> {
                                        // Thêm vào collection "residents" với đầy đủ thông tin
                                        Resident resident = new Resident(apartmentNumber, birth, email, name,
                                                gender, phone, relationship, uid);
                                        db.collection("residents").document(uid)
                                                .set(resident)
                                                .addOnSuccessListener(aVoid1 -> {
                                                    Toast.makeText(AddResidentActivity.this, "Thêm cư dân thành công", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(AddResidentActivity.this, ResidentManagement.class);
                                                    startActivity(intent);
                                                    finish();
                                                })
                                                .addOnFailureListener(e -> Toast.makeText(AddResidentActivity.this, "Lỗi khi thêm cư dân", Toast.LENGTH_SHORT).show());
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(AddResidentActivity.this, "Lỗi khi thêm user", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        Toast.makeText(AddResidentActivity.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
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
