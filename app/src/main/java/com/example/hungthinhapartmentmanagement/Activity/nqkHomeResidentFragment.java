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

public class nqkHomeResidentFragment extends Fragment {
    private String apartmentId;
    private String fullName;
    private String phone;
    private String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout for the fragment
        return inflater.inflate(R.layout.fragment_home_resident, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve data from the Bundle passed to the Fragment
        Bundle bundle = getArguments();
        if (bundle != null) {
            apartmentId = bundle.getString("apartmentId");
            fullName = bundle.getString("fullName");
            phone = bundle.getString("phone");
            email = bundle.getString("email");
        }

        // Get reference to the Feedback button
        ImageButton btnFeedback = view.findViewById(R.id.btnFeedback);
        // Get reference to the Invoice button
        ImageButton btnInvoice = view.findViewById(R.id.btnInvoice);

        // Set click listener for Feedback button
        btnFeedback.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), nqkRepairRequestListActivity.class);
            intent.putExtra("apartmentId", apartmentId);
            intent.putExtra("fullName", fullName);
            intent.putExtra("phone", phone);
            intent.putExtra("email", email);
            startActivity(intent);
        });

        // Set click listener for Invoice button
        btnInvoice.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), vvhResidentInvoiceActivity.class);
            intent.putExtra("apartmentId", apartmentId);
            intent.putExtra("fullName", fullName);
            intent.putExtra("phone", phone);
            intent.putExtra("email", email);
            startActivity(intent);
        });
    }
}