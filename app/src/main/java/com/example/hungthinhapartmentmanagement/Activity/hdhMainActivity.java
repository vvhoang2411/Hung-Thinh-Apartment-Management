package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.hungthinhapartmentmanagement.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class hdhMainActivity extends AppCompatActivity {
    BottomNavigationView bottomNav;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            Bundle args = new Bundle();
            args.putString("email", email); // Prepare email argument for fragments

            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                selectedFragment = new hdhManagementFragment();
            } else if (itemId == R.id.nav_finance) {
                selectedFragment = new hdhFinanceFragment();
            } else if (itemId == R.id.nav_notification) {
                selectedFragment = new nmtNotificationFragment();
            }

            if (selectedFragment != null) {
                selectedFragment.setArguments(args); // Set email argument to fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commitNow(); // Use commitNow for immediate replacement
            }
            return true;
        });

        // Kiểm tra intent ban đầu
        handleIntent(getIntent());

        // Nếu không có savedInstanceState, load tab home mặc định
        if (savedInstanceState == null) {
            Fragment defaultFragment = new hdhManagementFragment();
            Bundle args = new Bundle();
            args.putString("email", email);
            defaultFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, defaultFragment)
                    .commitNow();
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
            Fragment selectedFragment = null;
            Bundle args = new Bundle();
            args.putString("email", email); // Prepare email argument for fragments

            if (tabIndex == 0) {
                bottomNav.setSelectedItemId(R.id.nav_home);
                selectedFragment = new hdhManagementFragment();
            } else if (tabIndex == 1) {
                bottomNav.setSelectedItemId(R.id.nav_finance);
                selectedFragment = new hdhFinanceFragment();
            } else if (tabIndex == 2) {
                bottomNav.setSelectedItemId(R.id.nav_notification);
                selectedFragment = new nmtNotificationFragment();
            }

            if (selectedFragment != null) {
                selectedFragment.setArguments(args); // Set email argument to fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commitNow(); // Ensure immediate fragment replacement
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Kiểm tra lại intent khi activity được resume để đảm bảo fragment đúng
        handleIntent(getIntent());
    }
}