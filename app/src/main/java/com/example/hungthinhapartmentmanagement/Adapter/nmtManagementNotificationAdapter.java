package com.example.hungthinhapartmentmanagement.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungthinhapartmentmanagement.Model.Announcement;
import com.example.hungthinhapartmentmanagement.R;
import com.example.hungthinhapartmentmanagement.Activity.nmtManagementNotificationDetailActivity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class nmtManagementNotificationAdapter extends RecyclerView.Adapter<nmtManagementNotificationAdapter.ViewHolder> {

    private List<Announcement> announcementList;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private final Context context;

    public nmtManagementNotificationAdapter(Context context, List<Announcement> announcementList) {
        this.context = context;
        this.announcementList = announcementList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notify, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Announcement announcement = announcementList.get(position);

        // Đặt dữ liệu vào các TextView
        holder.tvTitle.setText(announcement.getTitle());
        if (announcement.getCreateAt() != null) {
            holder.tvDate.setText(dateFormat.format(announcement.getCreateAt().toDate()));
        } else {
            holder.tvDate.setText("Không có ngày");
        }

        // Xử lý sự kiện click trên item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, nmtManagementNotificationDetailActivity.class);
            intent.putExtra("documentId", announcement.getId());
            intent.putExtra("title", announcement.getTitle());
            intent.putExtra("content", announcement.getContent());
            if (announcement.getCreateAt() != null) {
                intent.putExtra("createAt", dateFormat.format(announcement.getCreateAt().toDate()));
            } else {
                intent.putExtra("createAt", "Không có ngày");
            }
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return announcementList != null ? announcementList.size() : 0;
    }

    public void updateData(List<Announcement> newList) {
        this.announcementList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title); // ID cho tiêu đề
            tvDate = itemView.findViewById(R.id.tv_timestamp);   // ID cho thời gian
        }
    }
}