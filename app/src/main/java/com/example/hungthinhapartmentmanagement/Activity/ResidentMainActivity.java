package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.hungthinhapartmentmanagement.R;
import com.example.hungthinhapartmentmanagement.Activity.HomeResidentFragment;
import com.example.hungthinhapartmentmanagement.Activity.InfomationResidentFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ResidentMainActivity extends AppCompatActivity {

    private static final String TAG = "ResidentMainActivity";
    private BottomNavigationView bottomNav;
    public static final String APARTMENT_ID = "A101";
    public static final String PHONE = "0987654321";
    public static final String FULL_NAME = "Ninh Quang Khải";
    public static final String EMAIL = "nqk@gmail.com";

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
        Fragment defaultFragment = new HomeResidentFragment();
        int selectedItemId = R.id.nav_home;

        if ("HomeResident".equals(fragmentToLoad)) {
            Log.d(TAG, "Loading HomeResidentFragment");
            selectedItemId = R.id.nav_home;
            defaultFragment = new HomeResidentFragment();
//        } else if ("NotificationResident".equals(fragmentToLoad)) {
//            Log.d(TAG, "Loading NotificationResidentFragment");
//            selectedItemId = R.id.nav_notification;
//            defaultFragment = new NotificationResidentFragment();
        } else if ("InformationResident".equals(fragmentToLoad)) {
            Log.d(TAG, "Loading InfomationResidentFragment");
            selectedItemId = R.id.nav_information;
            defaultFragment = new InfomationResidentFragment();
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
            selectedFragment = new HomeResidentFragment();
//        } else if (itemId == R.id.nav_notification) {
//            selectedFragment = new NotificationResidentFragment();
        } else if (itemId == R.id.nav_information) {
            selectedFragment = new InfomationResidentFragment();
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