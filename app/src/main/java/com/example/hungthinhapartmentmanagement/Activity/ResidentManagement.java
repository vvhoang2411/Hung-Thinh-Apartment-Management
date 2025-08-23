package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.example.hungthinhapartmentmanagement.Model.Resident;
import com.example.hungthinhapartmentmanagement.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ResidentManagement extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private ResidentAdapter adapter;
    private List<Resident> residentList = new ArrayList<>();
    private ConstraintLayout residentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resident_management);

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
        adapter = new ResidentAdapter(residentList);
        recyclerView.setAdapter(adapter);

        fetchResidents(); // Đọc dữ liệu khi khởi động
    }

    private void fetchResidents() {
        db.collection("residents").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                residentList.clear();
                QuerySnapshot querySnapshot = task.getResult();
                List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                for (DocumentSnapshot doc : documents) {
                    String resId = doc.getId();
                    String userId = doc.getString("user_id");
                    String aptId = doc.getString("apartment_id");

                    // Fetch user
                    db.collection("users").document(userId).get().addOnSuccessListener(userDoc -> {
                        String name = userDoc.getString("name");
                        String email = userDoc.getString("email");
                        String phone = userDoc.getString("phone");

                        // Fetch apartment
                        db.collection("apartments").document(aptId).get().addOnSuccessListener(aptDoc -> {
                            String aptNumber = aptDoc.getString("apartment_number");

                            // Tạo Resident object
                            Resident resident = new Resident();
                            resident.setId(resId);
                            resident.setUserId(userId);
                            resident.setApartmentId(aptId);
                            resident.setName(name);
                            resident.setEmail(email);
                            resident.setPhone(phone);
                            resident.setApartmentNumber(aptNumber);

                            residentList.add(resident);
                            adapter.notifyDataSetChanged();
                        });
                    });
                }
            } else {
                Log.e("Firestore", "Error getting residents", task.getException());
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