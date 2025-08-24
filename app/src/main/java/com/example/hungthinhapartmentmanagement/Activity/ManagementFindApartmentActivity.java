package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hungthinhapartmentmanagement.Adapter.FindApartmentAdapter;
import com.example.hungthinhapartmentmanagement.Helper.FindAparmentHelper;
import com.example.hungthinhapartmentmanagement.Model.Resident;
import com.example.hungthinhapartmentmanagement.R;
import java.util.List;

public class ManagementFindApartmentActivity extends AppCompatActivity {
    private EditText edtSearch;
    private ImageButton btnBack;
    private RecyclerView recyclerView;
    private FindApartmentAdapter adapter;
    private FindAparmentHelper findAparmentHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_management_find_apartment);

        // Initialize views
        edtSearch = findViewById(R.id.edtSearch);
        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.recyclerView);

        // Initialize adapter and helper
        findAparmentHelper = new FindAparmentHelper();
        adapter = new FindApartmentAdapter(this, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load initial resident list
        loadResidents("");

        // Set up search functionality
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                loadResidents(keyword);
            }
        });

        // Set up back button
//        btnBack.setOnClickListener(v -> {
//            Intent intent = new Intent(ManagementFindApartmentActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        });
    }

    private void loadResidents(String keyword) {
        findAparmentHelper.findResidentsByKeyword(keyword, new FindAparmentHelper.OnResidentsFetchedListener() {
            @Override
            public void onSuccess(List<Resident> residents) {
                adapter.updateResidents(residents);
            }

            @Override
            public void onError(Exception e) {
                Log.e("ManagementFindApartment", "Error fetching residents", e);
                // Optionally show error message to user
            }
        });
    }
}