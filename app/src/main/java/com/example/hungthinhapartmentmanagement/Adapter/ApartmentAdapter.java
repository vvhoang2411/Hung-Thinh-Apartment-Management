package com.example.hungthinhapartmentmanagement.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungthinhapartmentmanagement.Model.Apartment;
import com.example.hungthinhapartmentmanagement.R;

import java.util.List;

public class ApartmentAdapter extends RecyclerView.Adapter<ApartmentAdapter.ViewHolder> {
    private List<Apartment> apartmentList;

    public ApartmentAdapter(List<Apartment> apartmentList) {
        this.apartmentList = apartmentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Apartment apartment = apartmentList.get(position);
        holder.tvNumber.setText(apartment.getApartmentNumber());
        holder.tvStatus.setText("● " + apartment.getStatus());
        holder.tvArea.setText(apartment.getArea() + " m²");
        holder.tvDesc.setText(apartment.getDesc());

        // Thay đổi màu sắc của text status
        if ("Còn trống".equals(apartment.getStatus())) {
            holder.tvStatus.setTextColor(Color.GREEN);
        } else if ("Đã ở".equals(apartment.getStatus())) {
            holder.tvStatus.setTextColor(Color.RED);
        } else {
            holder.tvStatus.setTextColor(Color.BLACK); // Màu mặc định
        }
    }

    @Override
    public int getItemCount() {
        return apartmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumber, tvStatus, tvArea, tvDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvNumberApt);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvArea = itemView.findViewById(R.id.tvAreaApt);
            tvDesc = itemView.findViewById(R.id.tvDescApt);
        }
    }
}
