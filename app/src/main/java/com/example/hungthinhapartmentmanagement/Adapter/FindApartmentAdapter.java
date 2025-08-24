package com.example.hungthinhapartmentmanagement.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hungthinhapartmentmanagement.Model.Resident;
import com.example.hungthinhapartmentmanagement.R;
import com.example.hungthinhapartmentmanagement.Activity.ManagementInvoiceListActivity;
import java.util.ArrayList;
import java.util.List;

public class FindApartmentAdapter extends RecyclerView.Adapter<FindApartmentAdapter.ResidentViewHolder> {
    private List<Resident> residentList;
    private Context context;

    public FindApartmentAdapter(Context context, List<Resident> residentList) {
        this.context = context;
        this.residentList = (residentList != null) ? residentList : new ArrayList<>();
    }

    @NonNull
    @Override
    public ResidentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_find_apartment, parent, false);
        return new ResidentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResidentViewHolder holder, int position) {
        Resident resident = residentList.get(position);

        // Set apartmentId
        String apartmentIdText = "Mã căn hộ: " + (resident.getApartmentId() != null ? resident.getApartmentId() : "");
        holder.tvApartmentId.setText(apartmentIdText);

        // Set fullName
        String fullNameText = "Chủ căn hộ: " + (resident.getFullName() != null ? resident.getFullName() : "");
        holder.tvFullName.setText(fullNameText);

        // Handle item click to open ManagementInvoiceListActivity
        holder.itemView.setOnClickListener(v -> {
            String apartmentId = resident.getApartmentId();
            if (apartmentId != null) {
                Intent intent = new Intent(context, ManagementInvoiceListActivity.class);
                intent.putExtra("apartmentId", apartmentId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return residentList.size();
    }

    public void updateResidents(List<Resident> newResidents) {
        this.residentList = (newResidents != null) ? newResidents : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class ResidentViewHolder extends RecyclerView.ViewHolder {
        TextView tvApartmentId, tvFullName;

        public ResidentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvApartmentId = itemView.findViewById(R.id.tvApartmentId);
            tvFullName = itemView.findViewById(R.id.tvFullName);
        }
    }
}