package com.example.hungthinhapartmentmanagement.Adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungthinhapartmentmanagement.Model.Resident;
import com.example.hungthinhapartmentmanagement.R;

import java.util.List;

public class ResidentAdapter extends RecyclerView.Adapter<ResidentAdapter.ViewHolder>{
    private List<Resident> residentList;

    public ResidentAdapter(List<Resident> residentList) {
        this.residentList = residentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resident, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Resident resident = residentList.get(position);
        holder.tvName.setText(resident.getName());
        holder.tvEmail.setText(resident.getEmail());
        holder.tvPhone.setText(resident.getPhone());
        holder.tvApartmentNumber.setText(resident.getApartmentNumber());
    }

    @Override
    public int getItemCount() {
        return residentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvPhone, tvApartmentNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvApartmentNumber = itemView.findViewById(R.id.tvRoomRes);
        }
    }
}
