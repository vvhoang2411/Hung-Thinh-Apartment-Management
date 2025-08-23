package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hungthinhapartmentmanagement.Helper.ResidentHelper;
import com.example.hungthinhapartmentmanagement.Model.Resident;
import com.example.hungthinhapartmentmanagement.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class InfomationResidentFragment extends Fragment {

    private ImageButton btnEditProfile;
    private TextView tvApartmentId, tvFullName, tvGender, tvPhone, tvEmail, tvRelationship, tvBirthday;
    private String email = "vvh@gmail.com";
    private ResidentHelper residentHelper;

    // Biến để lưu resident hiện tại
    private Resident currentResident;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_infomation_resident, container, false);

        // Ánh xạ các thành phần
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        tvApartmentId = view.findViewById(R.id.tvApartmentId);
        tvFullName = view.findViewById(R.id.tvFullName);
        tvGender = view.findViewById(R.id.tvGender);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvRelationship = view.findViewById(R.id.tvRelationship);
        tvBirthday = view.findViewById(R.id.tvBirthday);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo ResidentHelper
        residentHelper = new ResidentHelper();

        // Load data
        loadResidentInfo();

        // Bắt sự kiện click
        btnEditProfile.setOnClickListener(v -> {
            if (currentResident != null) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("fullName", currentResident.getFullName());
                intent.putExtra("phone", currentResident.getPhone());
                intent.putExtra("gender", currentResident.getGender());
                intent.putExtra("email", currentResident.getEmail());

                if (currentResident.getBirthday() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    intent.putExtra("birthday", sdf.format(currentResident.getBirthday()));
                }

                startActivity(intent);
            }
        });
    }

    private void loadResidentInfo() {
        residentHelper.getResidentByEmail(email, new ResidentHelper.OnResidentLoadedListener() {
            @Override
            public void onResidentsLoaded(List<Resident> residents) {
                if (!residents.isEmpty() && getActivity() != null) {
                    Resident resident = residents.get(0);
                    currentResident = resident; // lưu lại resident

                    tvApartmentId.setText("Căn hộ: " + resident.getApartmentId());
                    tvFullName.setText("Họ tên: " + resident.getFullName());
                    tvGender.setText("Giới tính: " + resident.getGender());
                    tvPhone.setText("Số điện thoại: " + resident.getPhone());
                    tvEmail.setText("Email: " + resident.getEmail());
                    tvRelationship.setText(resident.isRelationship() ? "Quan hệ: Chủ hộ" : "Quan hệ: Thành viên");

                    if (resident.getBirthday() != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        tvBirthday.setText("Ngày sinh: " + sdf.format(resident.getBirthday()));
                    } else {
                        tvBirthday.setText("");
                    }
                }
            }
        });
    }
}
