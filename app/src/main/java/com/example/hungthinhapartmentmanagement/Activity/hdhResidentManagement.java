package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungthinhapartmentmanagement.Adapter.hdhResidentAdapter;
import com.example.hungthinhapartmentmanagement.Model.Residents;
import com.example.hungthinhapartmentmanagement.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class hdhResidentManagement extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private hdhResidentAdapter adapter;
    private List<Residents> residentsList;
    private List<Residents> filteredList; // Danh sách đã lọc
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
                Intent intent = new Intent(hdhResidentManagement.this, hdhMainActivity.class);
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
                Intent intent = new Intent(hdhResidentManagement.this, hdhAddResidentActivity.class);
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
        residentsList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new hdhResidentAdapter(this, filteredList);
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
                        residentsList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String apartmentId = document.getString("apartmentId");
                            String birthday = document.getString("birthday");
                            String email = document.getString("email");
                            String fullName = document.getString("fullName");
                            String gender = document.getString("gender");
                            String phone = document.getString("phone");
                            Boolean relationship = document.getBoolean("relationship");

                            Residents residents = new Residents(apartmentId, birthday, email, fullName,
                                    gender, phone, relationship, document.getId());
                            residentsList.add(residents);
                            filteredList.add(residents);
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
            filteredList.addAll(residentsList); // Nếu trống, hiển thị toàn bộ
        } else {
            String searchQuery = searchText;
            for (Residents residents : residentsList) {
                if (residents.getFullName().contains(searchQuery)) {
                    filteredList.add(residents);
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