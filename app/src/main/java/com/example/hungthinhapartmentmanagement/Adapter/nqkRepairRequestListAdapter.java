package com.example.hungthinhapartmentmanagement.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungthinhapartmentmanagement.Helper.RepairRequestHelper;
import com.example.hungthinhapartmentmanagement.Model.RepairRequest;
import com.example.hungthinhapartmentmanagement.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class nqkRepairRequestListAdapter extends RecyclerView.Adapter<nqkRepairRequestListAdapter.ViewHolder> {

    private final List<RepairRequest> repairRequestList;
    private final Context context;
    private final RepairRequestHelper repairRequestHelper;

    public nqkRepairRequestListAdapter(Context context, List<RepairRequest> repairRequestList) {
        this.context = context;
        this.repairRequestList = repairRequestList;
        this.repairRequestHelper = new RepairRequestHelper();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RepairRequest request = repairRequestList.get(position);

        holder.tvTitle.setText("Vấn đề: " + request.getTitle());
        holder.tvDescription.setText("Ghi chú: " + request.getDescription());
        holder.tvFullName.setText("Người gửi: " + request.getFullName());

        // Chuyển đổi Timestamp thành chuỗi ngày tháng
        if (request.getCreatedAt() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String formattedDate = sdf.format(request.getCreatedAt().toDate());
            holder.tvCreateAt.setText("Ngày gửi: " + formattedDate);
        } else {
            holder.tvCreateAt.setText("Ngày gửi: Chưa có thông tin");
        }

        bindStatus(holder.tvStatus, holder.btnCancel, request.getStatus());

        holder.btnCancel.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                RepairRequest requestToDelete = repairRequestList.get(adapterPosition);
                String documentId = requestToDelete.getDocumentId();
                if (documentId != null) {
                    showDeleteConfirmationDialog(adapterPosition, documentId);
                } else {
                    Toast.makeText(context, "Không tìm thấy documentId để xóa yêu cầu.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDeleteConfirmationDialog(int position, String documentId) {
        new AlertDialog.Builder(context)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa yêu cầu này không?")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    repairRequestHelper.deleteRepairRequest(documentId, new RepairRequestHelper.OnOperationListener() {
                        @Override
                        public void onSuccess() {
                            repairRequestList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, repairRequestList.size());
                            Toast.makeText(context, "Xóa yêu cầu thành công", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Toast.makeText(context, "Lỗi khi xóa yêu cầu: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }

    private void bindStatus(TextView tvStatus, ImageButton btnCancel, String status) {
        switch (status.toLowerCase()) {
            case "pending":
                tvStatus.setText(context.getString(R.string.status_pending));
                tvStatus.setTextColor(ContextCompat.getColor(context, R.color.gray));
                btnCancel.setVisibility(View.VISIBLE);
                break;
            case "received":
                tvStatus.setText(context.getString(R.string.status_received));
                tvStatus.setTextColor(ContextCompat.getColor(context, R.color.orange_500));
                btnCancel.setVisibility(View.GONE);
                break;
            case "completed":
                tvStatus.setText(context.getString(R.string.status_completed));
                tvStatus.setTextColor(ContextCompat.getColor(context, R.color.green_500));
                btnCancel.setVisibility(View.GONE);
                break;
            default:
                tvStatus.setText(context.getString(R.string.status_unknown));
                tvStatus.setTextColor(ContextCompat.getColor(context, R.color.gray));
                btnCancel.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return repairRequestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvStatus, tvCreateAt, tvFullName;
        ImageButton btnCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvCreateAt = itemView.findViewById(R.id.tvCreateAt);
            tvFullName = itemView.findViewById(R.id.tvFullName); // Thêm ánh xạ cho tvFullName
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}