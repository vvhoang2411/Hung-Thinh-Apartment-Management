package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungthinhapartmentmanagement.Adapter.nmtManagementNotificationAdapter;
import com.example.hungthinhapartmentmanagement.Helper.AnnouncementHelper;
import com.example.hungthinhapartmentmanagement.Model.Announcement;
import com.example.hungthinhapartmentmanagement.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class nmtNotificationFragment extends Fragment {

    private static final String TAG = "NotificationFragment";
    private RecyclerView rvNotifications;
    private nmtManagementNotificationAdapter adapter;
    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private String currentEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        // Lấy email từ arguments
        currentEmail = getArguments() != null ? getArguments().getString("email", "default@example.com") : "default@example.com";

        // Ánh xạ RecyclerView
        rvNotifications = view.findViewById(R.id.rvNotifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new nmtManagementNotificationAdapter(getContext(), new ArrayList<>());
        rvNotifications.setAdapter(adapter);

        // Load danh sách thông báo
        loadAnnouncements();

        // Xử lý nút Add
        view.findViewById(R.id.btnAdd).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), nmtManagementNotificationCreateActivity.class);
            intent.putExtra("email", currentEmail); // Truyền email
            startActivity(intent);
        });

        return view;
    }

    private void loadAnnouncements() {
        new Thread(() -> {
            try {
                AnnouncementHelper helper = new AnnouncementHelper();
                List<Announcement> announcements = helper.getAllAnnouncements(); // Load tất cả thông báo
                uiHandler.post(() -> {
                    if (!announcements.isEmpty()) {
                        adapter.updateData(announcements); // Cập nhật dữ liệu vào adapter
                    } else {
                        Toast.makeText(getContext(), "Không có thông báo nào", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Error loading announcements: " + e.getMessage());
                uiHandler.post(() -> Toast.makeText(getContext(), "Lỗi khi tải thông báo", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tải lại danh sách khi quay lại fragment
        loadAnnouncements();
    }
}