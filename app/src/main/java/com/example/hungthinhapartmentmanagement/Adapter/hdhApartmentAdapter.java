package com.example.hungthinhapartmentmanagement.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungthinhapartmentmanagement.Activity.hdhEditApartActivity;
import com.example.hungthinhapartmentmanagement.Model.Apartments;
import com.example.hungthinhapartmentmanagement.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class hdhApartmentAdapter extends RecyclerView.Adapter<hdhApartmentAdapter.ApartmentViewHolder> {
    private List<Apartments> apartmentsList;
    private Context context;
    private FirebaseFirestore db;

    public hdhApartmentAdapter(Context context, List<Apartments> apartmentsList) {
        this.context = context;
        this.apartmentsList = apartmentsList;
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public ApartmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apart, parent, false);
        return new ApartmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ApartmentViewHolder holder, int position) {
        Apartments apartments = apartmentsList.get(position);
        holder.tvApartmentNumber.setText(apartments.getApartmentNumber());
        holder.tvArea.setText(apartments.getArea() + " m²");
        holder.tvDesc.setText(apartments.getDesc());
        holder.tvFloor.setText(apartments.getFloor());
        holder.tvStatus.setText("● " + apartments.getStatus());

        // Đặt màu cho tvStatus dựa trên giá trị
        if ("Còn trống".equals(apartments.getStatus())) {
            holder.tvStatus.setTextColor(Color.GREEN);
        } else if ("Đã ở".equals(apartments.getStatus())) {
            holder.tvStatus.setTextColor(Color.RED);
        } else {
            holder.tvStatus.setTextColor(Color.BLACK); // Mặc định là màu đen nếu không khớp
        }

        // Xử lý nút Edit
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, hdhEditApartActivity.class);
            intent.putExtra("apartmentId", apartments.getId()); // Truyền document ID
            intent.putExtra("apartmentNumber", apartments.getApartmentNumber());
            intent.putExtra("floor", apartments.getFloor());
            intent.putExtra("area", apartments.getArea());
            intent.putExtra("desc", apartments.getDesc());
            intent.putExtra("status", apartments.getStatus());
            context.startActivity(intent);
        });

        // Xử lý nút Delete
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có muốn xóa căn hộ " + apartments.getApartmentNumber() + " không?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        // Xóa document trong Firestore
                        db.collection("apartments")
                                .document(apartments.getId())
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    // Xóa thành công, cập nhật danh sách
                                    apartmentsList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, apartmentsList.size());
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
        return apartmentsList.size();
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
