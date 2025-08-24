package com.example.hungthinhapartmentmanagement.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungthinhapartmentmanagement.Activity.EditApartActivity;
import com.example.hungthinhapartmentmanagement.Model.Apartment;
import com.example.hungthinhapartmentmanagement.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ApartmentAdapter extends RecyclerView.Adapter<ApartmentAdapter.ApartmentViewHolder> {
    private List<Apartment> apartmentList;
    private Context context;
    private FirebaseFirestore db;

    public ApartmentAdapter(Context context, List<Apartment> apartmentList) {
        this.context = context;
        this.apartmentList = apartmentList;
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public ApartmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apart, parent, false);
        return new ApartmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ApartmentViewHolder holder, int position) {
        Apartment apartment = apartmentList.get(position);
        holder.tvApartmentNumber.setText(apartment.getApartmentNumber());
        holder.tvArea.setText(apartment.getArea() + " m²");
        holder.tvDesc.setText(apartment.getDesc());
        holder.tvFloor.setText(apartment.getFloor());
        holder.tvStatus.setText("● " + apartment.getStatus());

        // Đặt màu cho tvStatus dựa trên giá trị
        if ("Còn trống".equals(apartment.getStatus())) {
            holder.tvStatus.setTextColor(Color.GREEN);
        } else if ("Đã ở".equals(apartment.getStatus())) {
            holder.tvStatus.setTextColor(Color.RED);
        } else {
            holder.tvStatus.setTextColor(Color.BLACK); // Mặc định là màu đen nếu không khớp
        }

        // Xử lý nút Edit
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditApartActivity.class);
            intent.putExtra("apartmentId", apartment.getId()); // Truyền document ID
            intent.putExtra("apartmentNumber", apartment.getApartmentNumber());
            intent.putExtra("floor", apartment.getFloor());
            intent.putExtra("area", apartment.getArea());
            intent.putExtra("desc", apartment.getDesc());
            intent.putExtra("status", apartment.getStatus());
            context.startActivity(intent);
        });

        // Xử lý nút Delete
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có muốn xóa căn hộ " + apartment.getApartmentNumber() + " không?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        // Xóa document trong Firestore
                        db.collection("apartments")
                                .document(apartment.getId())
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    // Xóa thành công, cập nhật danh sách
                                    apartmentList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, apartmentList.size());
                                    Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Xóa thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("Không", (dialog, which) -> {
                        // Đóng popup nếu chọn Không
                        dialog.dismiss();
                    })
                    .setCancelable(true) // Cho phép hủy bằng nút Back
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return apartmentList.size();
    }

    public static class ApartmentViewHolder extends RecyclerView.ViewHolder {
        public TextView tvApartmentNumber, tvArea, tvDesc, tvFloor, tvStatus;
        public ImageButton btnEdit, btnDelete;

        public ApartmentViewHolder(View itemView) {
            super(itemView);
            tvApartmentNumber = itemView.findViewById(R.id.tvNumberApt);
            tvArea = itemView.findViewById(R.id.tvAreaApt);
            tvDesc = itemView.findViewById(R.id.tvDescApt);
            tvFloor = itemView.findViewById(R.id.tvFloApt);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
