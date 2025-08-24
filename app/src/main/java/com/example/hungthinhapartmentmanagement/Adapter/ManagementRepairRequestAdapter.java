package com.example.hungthinhapartmentmanagement.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungthinhapartmentmanagement.Activity.ManagementDetailRequestActivity;
import com.example.hungthinhapartmentmanagement.Model.RepairRequest;
import com.example.hungthinhapartmentmanagement.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ManagementRepairRequestAdapter extends RecyclerView.Adapter<ManagementRepairRequestAdapter.ViewHolder> {

    private final List<RepairRequest> repairRequestList;
    private final Context context;
    private static final String TAG = "ManagementRepairRequestAdapter";

    public ManagementRepairRequestAdapter(Context context, List<RepairRequest> repairRequestList) {
        this.context = context;
        this.repairRequestList = repairRequestList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mt_item_management_repair_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RepairRequest request = repairRequestList.get(position);

        // Ánh xạ dữ liệu vào các view trong item_management_repair_request.xml
        holder.tvHoPhanAnh.setText(request.getApartmentId() != null ? request.getApartmentId() : "Chưa có căn hộ");
        holder.tvNoiDung.setText(request.getTitle() != null ? request.getTitle() : "Chưa có tiêu đề");
        holder.tvTrangThai.setText(request.getStatus() != null ? request.getStatus() : "Không xác định");

        // Chuyển đổi Timestamp thành chuỗi ngày tháng
        if (request.getCreatedAt() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = sdf.format(request.getCreatedAt().toDate());
            holder.tvNgayPhanAnh.setText(formattedDate);
        } else {
            holder.tvNgayPhanAnh.setText("Chưa có thông tin");
        }

        // Cập nhật giao diện trạng thái
        bindStatus(holder.tvTrangThai, request.getStatus());

        holder.btnDetail.setOnClickListener(v -> {
            Intent intent = new Intent(context, ManagementDetailRequestActivity.class);
            intent.putExtra("documentId", request.getDocumentId());
            intent.putExtra("apartmentId", request.getApartmentId());
            intent.putExtra("title", request.getTitle());
            intent.putExtra("description", request.getDescription());
            intent.putExtra("status", request.getStatus());
            intent.putExtra("isActive", request.isActive());
            intent.putExtra("createdAt", request.getCreatedAt() != null ? request.getCreatedAt().getSeconds() : 0);
            intent.putExtra("updatedAt", request.getUpdatedAt() != null ? request.getUpdatedAt().getSeconds() : 0);
            intent.putExtra("fullName", request.getFullName());
            intent.putExtra("phone", request.getPhone());
            intent.putExtra("email", request.getEmail());
            context.startActivity(intent);
        });

        Log.d(TAG, "Bound item at position " + position + ": " + request.getTitle());
    }

    private void bindStatus(TextView tvTrangThai, String status) {
        if (status == null) {
            tvTrangThai.setText("Không xác định");
            tvTrangThai.setTextColor(ContextCompat.getColor(context, R.color.gray));
            return;
        }
        switch (status.toLowerCase()) {
            case "pending":
                tvTrangThai.setText("Chưa phản hồi");
                tvTrangThai.setTextColor(ContextCompat.getColor(context, R.color.gray));
                break;
            case "received":
                tvTrangThai.setText("Đã phản hồi");
                tvTrangThai.setTextColor(ContextCompat.getColor(context, R.color.orange_500));
                break;
            case "completed":
                tvTrangThai.setText("Hoàn tất");
                tvTrangThai.setTextColor(ContextCompat.getColor(context, R.color.green_500));
                break;
            default:
                tvTrangThai.setText("Không xác định");
                tvTrangThai.setTextColor(ContextCompat.getColor(context, R.color.gray));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return repairRequestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHoPhanAnh, tvNgayPhanAnh, tvNoiDung, tvTrangThai;
        ImageButton btnDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHoPhanAnh = itemView.findViewById(R.id.tv_address);
            tvNgayPhanAnh = itemView.findViewById(R.id.tv_date_request);
            tvNoiDung = itemView.findViewById(R.id.tv_problem);
            tvTrangThai = itemView.findViewById(R.id.tv_status);
            btnDetail = itemView.findViewById(R.id.btnDetail);

            // Debug null views
            if (tvHoPhanAnh == null) Log.e(TAG, "tv_ho_phan_anh is null - Check XML");
            if (tvNgayPhanAnh == null) Log.e(TAG, "tv_ngay_phan_anh is null - Check XML");
            if (tvNoiDung == null) Log.e(TAG, "tv_noi_dung is null - Check XML");
            if (tvTrangThai == null) Log.e(TAG, "tv_trang_thai is null - Check XML");
            if (btnDetail == null) Log.e(TAG, "btnDetail is null - Check XML");
        }
    }
}