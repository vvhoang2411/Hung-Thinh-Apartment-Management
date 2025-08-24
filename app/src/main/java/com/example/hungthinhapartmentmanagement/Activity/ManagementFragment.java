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


public class ManagementFragment extends Fragment {

    Button btnResManage, btnApartManage, btnDeviceManage;
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
        mAuth = FirebaseAuth.getInstance();
        ibtnSignOut = view.findViewById(R.id.ibtnSignOut);

        ibtnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(getContext(), "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        btnApartManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ApartmentManagement.class);
                startActivity(intent);
            }
        });

        btnResManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResidentManagement.class);
                startActivity(intent);
            }
        });

        return view;
    }
}