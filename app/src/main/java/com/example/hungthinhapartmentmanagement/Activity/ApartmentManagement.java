package com.example.hungthinhapartmentmanagement.Activity;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

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
    private List<Apartment> apartmentList;
    private ConstraintLayout appartLayout;

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

        recyclerView = findViewById(R.id.rvApartments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        apartmentList = new ArrayList<>();
        adapter = new ApartmentAdapter(apartmentList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        fetchApartment();

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
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        apartmentList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId(); // Lấy document ID làm id
                            String apartmentNumber = document.getString("apartment_number");
                            String status = document.getString("status");
                            String area = document.getString("area");
                            String desc = document.getString("desc");

                            Apartment apartment = new Apartment(id, apartmentNumber, status, area, desc);
                            apartmentList.add(apartment);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("Firestore", "Error getting documents: ", task.getException());
                    }
                });
    }
}