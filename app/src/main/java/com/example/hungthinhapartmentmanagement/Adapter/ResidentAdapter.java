package com.example.hungthinhapartmentmanagement.Adapter;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungthinhapartmentmanagement.Activity.EditResidentActivity;
import com.example.hungthinhapartmentmanagement.Model.Resident;
import com.example.hungthinhapartmentmanagement.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ResidentAdapter extends RecyclerView.Adapter<ResidentAdapter.ResidentViewHolder> {
    private List<Resident> residentList;
    private Context context;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public ResidentAdapter(Context context, List<Resident> residentList) {
        this.context = context;
        this.residentList = residentList;
        this.db = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public ResidentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resident, parent, false);
        return new ResidentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResidentViewHolder holder, int position) {
        Resident resident = residentList.get(position);
        holder.tvApartmentNumber.setText(resident.getApartmentId());
        holder.tvFullName.setText(resident.getFullName());
        holder.tvGender.setText(resident.getGender());
        holder.tvPhone.setText(resident.getPhone());
        holder.tvBirthday.setText(resident.getBirthday());
        holder.tvEmail.setText(resident.getEmail());
        holder.tvRelationship.setText(resident.isRelationship() ? "Có" : "Không");

        // Xử lý nút Edit
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditResidentActivity.class);
            intent.putExtra("residentId", residentList.get(position).getDocumentId()); // Giả định Resident có getter cho documentId
            context.startActivity(intent);
        });

        // Xử lý nút Delete với popup xác nhận
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có muốn xóa cư dân " + resident.getFullName() + " không?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        int currentPosition = holder.getAdapterPosition();
                        if (currentPosition != RecyclerView.NO_POSITION) {
                            deleteResident(resident.getDocumentId(), currentPosition);
                        }
                    })
                    .setNegativeButton("Không", (dialog, which) -> {
                        dialog.dismiss(); // Đóng popup nếu chọn Không
                    })
                    .setCancelable(true) // Cho phép hủy bằng nút Back
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return residentList.size();
    }

    public static class ResidentViewHolder extends RecyclerView.ViewHolder {
        public TextView tvApartmentNumber, tvFullName, tvGender, tvPhone, tvBirthday, tvEmail, tvRelationship;
        public ImageButton btnEdit, btnDelete;

        public ResidentViewHolder(View itemView) {
            super(itemView);
            tvApartmentNumber = itemView.findViewById(R.id.tvNumberApt);
            tvFullName = itemView.findViewById(R.id.tvName);
            tvGender = itemView.findViewById(R.id.tvGender);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvBirthday = itemView.findViewById(R.id.tvBirth);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvRelationship = itemView.findViewById(R.id.tvRelation);
            btnEdit = itemView.findViewById(R.id.btnTransEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    private void deleteResident(String documentId, int position) {
        // Xóa từ collection "residents"
        db.collection("residents").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Xóa từ collection "users"
                    db.collection("users").document(documentId)
                            .delete()
                            .addOnSuccessListener(aVoid1 -> {
                                residentList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, residentList.size());
                                Toast.makeText(context, "Xóa cư dân thành công", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(context, "Xóa user thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Xóa cư dân thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
