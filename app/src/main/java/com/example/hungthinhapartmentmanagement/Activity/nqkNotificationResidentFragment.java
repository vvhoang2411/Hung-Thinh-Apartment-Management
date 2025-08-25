package com.example.hungthinhapartmentmanagement.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungthinhapartmentmanagement.Adapter.nqkAnnouncementAdapter;
import com.example.hungthinhapartmentmanagement.Helper.AnnouncementHelper;
import com.example.hungthinhapartmentmanagement.Model.Announcement;
import com.example.hungthinhapartmentmanagement.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class nqkNotificationResidentFragment extends Fragment {

    private String apartmentId;
    private String email;

    private RecyclerView recyclerView;
    private nqkAnnouncementAdapter nqkAnnouncementAdapter;
    private final AnnouncementHelper announcementHelper = new AnnouncementHelper();
    private static final String TAG = "NotificationResidentFragment";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification_resident, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            apartmentId = bundle.getString("apartmentId");
            email = bundle.getString("email");
        } else {
            Log.e(TAG, "Bundle is null.");
            Toast.makeText(getContext(), "Không thể tải dữ liệu người dùng", Toast.LENGTH_SHORT).show();
            return;
        }

        if (apartmentId == null || apartmentId.isEmpty()) {
            Log.e(TAG, "apartmentId is null or empty.");
            Toast.makeText(getContext(), "ID căn hộ không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Loading with apartmentId: " + apartmentId + ", email: " + email);

        recyclerView = view.findViewById(R.id.rvNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        nqkAnnouncementAdapter = new nqkAnnouncementAdapter(getContext(), new ArrayList<>(), email, apartmentId);
        recyclerView.setAdapter(nqkAnnouncementAdapter);

        loadAnnouncements();
    }

    private void loadAnnouncements() {
        executorService.execute(() -> {
            try {
                List<Announcement> announcements = announcementHelper.getAnnouncementsByTarget(apartmentId);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (announcements.isEmpty()) {
                            Log.d(TAG, "No announcements found for apartmentId: " + apartmentId);
                            Toast.makeText(getContext(), "Không có thông báo nào.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "Found " + announcements.size() + " announcements.");
                            nqkAnnouncementAdapter = new nqkAnnouncementAdapter(getContext(), announcements, email, apartmentId);
                            recyclerView.setAdapter(nqkAnnouncementAdapter);
                        }
                    });
                }
            } catch (Exception e) {
                Log.e(TAG, "Error loading announcements: " + e.getMessage(), e);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Lỗi khi tải thông báo: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}