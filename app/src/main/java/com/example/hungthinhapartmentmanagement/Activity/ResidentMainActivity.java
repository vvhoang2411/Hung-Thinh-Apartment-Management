package com.example.hungthinhapartmentmanagement.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.hungthinhapartmentmanagement.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ResidentMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_resident);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(this::onNavigationItemSelected);

        // Xử lý Intent khi quay lại từ EditProfileActivity
        String fragmentToLoad = getIntent().getStringExtra("fragment");
        if ("HomeResident".equals(fragmentToLoad)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeResidentFragment())
                    .commit();
            bottomNav.setSelectedItemId(R.id.nav_home); // Chọn tab Home
        } else {
            // Load default fragment nếu không có extra
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeResidentFragment())
                    .commit();
        }
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        Fragment selectedFragment = null;

        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            selectedFragment = new HomeResidentFragment();
        } else if (itemId == R.id.nav_notification) {
            selectedFragment = new NotificationResidentFragment();
        } else if (itemId == R.id.nav_information) {
            selectedFragment = new InfomationResidentFragment();
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
        }
        return true;
    }
}