package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungthinhapartmentmanagement.Adapter.ResidentAdapter;
import com.example.hungthinhapartmentmanagement.Model.Apartment;
import com.example.hungthinhapartmentmanagement.Model.Resident;
import com.example.hungthinhapartmentmanagement.R;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ResidentManagement extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private ResidentAdapter adapter;
    private List<Resident> residentList;
    private List<Resident> filteredList; // Danh sách đã lọc
    private ConstraintLayout residentLayout;
    private ImageButton btnTransAdd, btnBack;
    private EditText edtSearchRes;
    private Button btnSearchRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resident_management);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResidentManagement.this, MainActivity.class);
                intent.putExtra("returnTab", 0); // tab1
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        btnTransAdd = findViewById(R.id.btnTransAdd);
        btnTransAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResidentManagement.this, AddResidentActivity.class);
                startActivity(intent);
            }
        });

        residentLayout = findViewById(R.id.residentLayout);
        residentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.rvResidents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        residentList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new ResidentAdapter(this, filteredList);
        recyclerView.setAdapter(adapter);

        btnSearchRes = findViewById(R.id.btnSearchRes);
        btnSearchRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = edtSearchRes.getText().toString().trim();
                filterList(searchText);
            }
        });

        edtSearchRes = findViewById(R.id.edtSearchRes);
        edtSearchRes.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }
        });

        fetchResidents(); // Đọc dữ liệu khi khởi động
    }

    private void fetchResidents() {
        db.collection("residents")
                .orderBy("fullName", com.google.firebase.firestore.Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        residentList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String apartmentId = document.getString("apartmentId");
                            String birthday = document.getString("birthday");
                            String email = document.getString("email");
                            String fullName = document.getString("fullName");
                            String gender = document.getString("gender");
                            String phone = document.getString("phone");
                            Boolean relationship = document.getBoolean("relationship");

                            Resident resident = new Resident(apartmentId, birthday, email, fullName,
                                    gender, phone, relationship, document.getId());
                            residentList.add(resident);
                            filteredList.add(resident);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        // Xử lý lỗi
                    }
                });
    }

    private void filterList(String searchText) {
        filteredList.clear();
        if (searchText.isEmpty()) {
            filteredList.addAll(residentList); // Nếu trống, hiển thị toàn bộ
        } else {
            String searchQuery = searchText;
            for (Resident resident : residentList) {
                if (resident.getFullName().contains(searchQuery)) {
                    filteredList.add(resident);
                }
            }
        }
        adapter.notifyDataSetChanged();
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