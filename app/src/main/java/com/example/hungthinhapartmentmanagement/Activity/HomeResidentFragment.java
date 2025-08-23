package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.hungthinhapartmentmanagement.R;

public class HomeResidentFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout cho fragment
        return inflater.inflate(R.layout.fragment_home_resident, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lấy tham chiếu đến nút Feedback
        ImageButton btnFeedback = view.findViewById(R.id.btnFeedback);

        // Gắn sự kiện click
        btnFeedback.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RepairRequestListActivity.class);
            intent.putExtra("apartmentId", "A101");
            intent.putExtra("phone", "0987654321");
            intent.putExtra("fullName", "Ninh Quang Khải");
            intent.putExtra("email", "nqk@gmail.com");
            startActivity(intent);
        });
    }
}