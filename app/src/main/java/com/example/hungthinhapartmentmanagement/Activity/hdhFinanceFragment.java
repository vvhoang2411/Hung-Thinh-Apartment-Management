package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.hungthinhapartmentmanagement.R;

public class hdhFinanceFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_finance, container, false);

        // Lấy email từ arguments
        String email = getArguments() != null ? getArguments().getString("email", "default@example.com") : "default@example.com";

        // Xử lý nút btnInvoiceManage
        view.findViewById(R.id.btnInvoiceManage).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), nqkManagementFindApartmentActivity.class);
            startActivity(intent);
        });

        view.findViewById(R.id.btnFinanManage).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), vvhManagementStatisticalActivity.class);
            startActivity(intent);
        });

        return view;
    }
}