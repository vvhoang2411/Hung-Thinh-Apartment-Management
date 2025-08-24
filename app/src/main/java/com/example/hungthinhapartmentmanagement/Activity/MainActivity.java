package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.hungthinhapartmentmanagement.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                selectedFragment = new ManagementFragment();
            } else if (itemId == R.id.nav_finance) {
                selectedFragment = new FinanceFragment();
            } else if (itemId == R.id.nav_notification) {
                selectedFragment = new NotificationFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        // Khi activity khởi tạo, kiểm tra intent ban đầu
        handleIntent(getIntent());

        // Nếu không có intent đặc biệt thì load mặc định tab home
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ManagementFragment())
                    .commit();
        }
    }

    // Nhận intent mới khi activity đã tồn tại
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent != null && intent.hasExtra("returnTab")) {
            int tabIndex = intent.getIntExtra("returnTab", 0);

            if (tabIndex == 0) {
                bottomNav.setSelectedItemId(R.id.nav_home);
            } else if (tabIndex == 1) {
                bottomNav.setSelectedItemId(R.id.nav_finance);
            } else if (tabIndex == 2) {
                bottomNav.setSelectedItemId(R.id.nav_notification);
            }
        }
    }

}