package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.hungthinhapartmentmanagement.R;
import com.google.firebase.auth.FirebaseAuth;


public class hdhManagementFragment extends Fragment {

    Button btnResManage, btnApartManage, btnDeviceManage, btnFollowFeedback;
    ImageButton ibtnSignOut;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_management, container, false);

        btnResManage = view.findViewById(R.id.btnResManage);
        btnApartManage = view.findViewById(R.id.btnApartManage);
        btnDeviceManage = view.findViewById(R.id.btnDeviceManage);
        btnFollowFeedback = view.findViewById(R.id.btnFollowFeedback);
        mAuth = FirebaseAuth.getInstance();
        ibtnSignOut = view.findViewById(R.id.ibtnSignOut);

        ibtnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(getContext(), "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), hdhLoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        btnApartManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), hdhApartmentManagement.class);
                startActivity(intent);
            }
        });

        btnResManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), hdhResidentManagement.class);
                startActivity(intent);
            }
        });

        btnDeviceManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), nmtManagementControlDeviceActivity.class);
                startActivity(intent);
            }
        });

        btnFollowFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), nmtManagementRepairRequestActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}