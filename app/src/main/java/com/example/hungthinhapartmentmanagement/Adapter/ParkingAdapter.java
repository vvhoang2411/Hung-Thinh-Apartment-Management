package com.example.hungthinhapartmentmanagement.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungthinhapartmentmanagement.Model.ParkingModel;
import com.example.hungthinhapartmentmanagement.R;

import java.util.List;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.ParkingViewHolder> {
    private final List<ParkingModel> parkingList;

    public ParkingAdapter(List<ParkingModel> parkingList) {
        this.parkingList = parkingList;
    }

    @NonNull
    @Override
    public ParkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_parking, parent, false);
        return new ParkingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingViewHolder holder, int position) {
        ParkingModel model = parkingList.get(position);
        holder.imgType.setImageResource(model.getImageResId());
        holder.tvName.setText(model.getName());
        holder.tvMax.setText(model.getMax());
        holder.tvSlot.setText(model.getSlot());
    }

    @Override
    public int getItemCount() {
        return parkingList.size();
    }

    static class ParkingViewHolder extends RecyclerView.ViewHolder {
        ImageView imgType;
        TextView tvName, tvMax, tvSlot;

        public ParkingViewHolder(@NonNull View itemView) {
            super(itemView);
            imgType = itemView.findViewById(R.id.imgType);
            tvName = itemView.findViewById(R.id.tvName);
            tvMax = itemView.findViewById(R.id.tvMax);
            tvSlot = itemView.findViewById(R.id.tvSlot);
        }
    }
}

