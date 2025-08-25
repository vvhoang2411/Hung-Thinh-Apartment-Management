package com.example.hungthinhapartmentmanagement.Activity;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hungthinhapartmentmanagement.Adapter.ApartmentAdapter;
import com.example.hungthinhapartmentmanagement.Model.Apartment;
import com.example.hungthinhapartmentmanagement.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Calendar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ApartmentManagement extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private ApartmentAdapter adapter;
    private List<Apartment> apartmentList; // Danh sách gốc
    private List<Apartment> filteredList; // Danh sách đã lọc
    private EditText editTextSearch;
    private Button buttonSearch;
    private ConstraintLayout appartLayout;

    private ImageButton btnTransAdd, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_management);

        appartLayout = findViewById(R.id.appartLayout);
        appartLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        btnTransAdd = findViewById(R.id.btnTransAdd);
        btnTransAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApartmentManagement.this, AddApartActivity.class);
                startActivity(intent);
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApartmentManagement.this, MainActivity.class);
                intent.putExtra("returnTab", 0); // tab1
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        recyclerView = findViewById(R.id.rvApartments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        apartmentList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new ApartmentAdapter(this, filteredList);
        recyclerView.setAdapter(adapter);
        editTextSearch = findViewById(R.id.edtSearchApt);
        buttonSearch = findViewById(R.id.btnSearchApt);

        db = FirebaseFirestore.getInstance();

        fetchApartment();

        editTextSearch.addTextChangedListener(new TextWatcher() {
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

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = editTextSearch.getText().toString().trim();
                filterList(searchText);
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

    public void fetchApartment() {
        db.collection("apartments")
                .orderBy("apartment_number", com.google.firebase.firestore.Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        apartmentList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String apartmentNumber = document.getString("apartment_number");
                            String area = document.getString("area");
                            String desc = document.getString("desc");
                            String floor = document.getString("floor");
                            String status = document.getString("status");

                            Apartment apartment = new Apartment(id, apartmentNumber, area, desc, floor, status);
                            apartmentList.add(apartment);
                            filteredList.add(apartment);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        // Xử lý lỗi
                    }
                });
    }

    // Phương thức lọc danh sách
    private void filterList(String searchText) {
        filteredList.clear();
        if (searchText.isEmpty()) {
            filteredList.addAll(apartmentList); // Nếu trống, hiển thị toàn bộ
        } else {
            String searchQuery = searchText;
            for (Apartment apartment : apartmentList) {
                if (apartment.getApartmentNumber().contains(searchQuery)) {
                    filteredList.add(apartment);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}