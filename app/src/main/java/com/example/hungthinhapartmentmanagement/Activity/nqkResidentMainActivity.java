package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.hungthinhapartmentmanagement.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class nqkResidentMainActivity extends AppCompatActivity {

    private static final String TAG = "ResidentMainActivity";
    private BottomNavigationView bottomNav;
    public String APARTMENT_ID;
    public String PHONE;
    public String FULL_NAME;
    public String EMAIL;

    private int currentSelectedItemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_resident);

        bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav == null) {
            Log.e(TAG, "BottomNavigationView not found. Check R.id.bottom_navigation in layout.");
            return;
        }
        bottomNav.setOnItemSelectedListener(this::onNavigationItemSelected);

        Intent intent = getIntent();
        APARTMENT_ID = intent.getStringExtra("apartmentId");
        PHONE = intent.getStringExtra("phone");
        FULL_NAME = intent.getStringExtra("fullName");
        EMAIL = intent.getStringExtra("email");

        // Kiểm tra extra từ Intent để load fragment phù hợp
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // Cập nhật Intent mới
        handleIntent(intent); // Xử lý Intent mới
    }

    private void handleIntent(Intent intent) {
        String fragmentToLoad = intent.getStringExtra("fragment");
        Log.d(TAG, "Received fragment extra: " + fragmentToLoad);
        Fragment defaultFragment = new nqkHomeResidentFragment();
        int selectedItemId = R.id.nav_home;

        if ("HomeResident".equals(fragmentToLoad)) {
            Log.d(TAG, "Loading HomeResidentFragment");
            selectedItemId = R.id.nav_home;
            defaultFragment = new nqkHomeResidentFragment();
        } else if ("NotificationResident".equals(fragmentToLoad)) {
            Log.d(TAG, "Loading NotificationResidentFragment");
            selectedItemId = R.id.nav_notification;
            defaultFragment = new nqkNotificationResidentFragment();
        } else if ("InformationResident".equals(fragmentToLoad)) {
            Log.d(TAG, "Loading InfomationResidentFragment");
            selectedItemId = R.id.nav_information;
            defaultFragment = new vvhInfomationResidentFragment();
        } else {
            Log.w(TAG, "No valid fragment extra, defaulting to HomeResidentFragment");
        }

        if (!loadFragment(defaultFragment, selectedItemId, true)) {
            Log.e(TAG, "Failed to load default fragment");
        }
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();

        if (itemId == currentSelectedItemId) {
            return true;
        }

        if (itemId == R.id.nav_home) {
            selectedFragment = new nqkHomeResidentFragment();
        } else if (itemId == R.id.nav_notification) {
            selectedFragment = new nqkNotificationResidentFragment();
        } else if (itemId == R.id.nav_information) {
            selectedFragment = new vvhInfomationResidentFragment();
        } else {
            Log.w(TAG, "Unknown navigation item ID: " + itemId);
        }

        return loadFragment(selectedFragment, itemId, false);
    }

    private boolean loadFragment(Fragment fragment, int selectedItemId, boolean isInitialLoad) {
        if (fragment == null) {
            Log.e(TAG, "Fragment is null, cannot load.");
            return false;
        }

        try {
            Bundle bundle = new Bundle();
            bundle.putString("apartmentId", APARTMENT_ID);
            bundle.putString("phone", PHONE);
            bundle.putString("fullName", FULL_NAME);
            bundle.putString("email", EMAIL);
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

            if (isInitialLoad && bottomNav != null) {
                bottomNav.setSelectedItemId(selectedItemId);
            }

            currentSelectedItemId = selectedItemId;
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error loading fragment: " + e.getMessage(), e);
            return false;
        }
    }
}