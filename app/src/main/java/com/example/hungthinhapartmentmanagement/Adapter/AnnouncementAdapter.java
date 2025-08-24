package com.example.hungthinhapartmentmanagement.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hungthinhapartmentmanagement.Helper.AnnouncementHelper;
import com.example.hungthinhapartmentmanagement.Model.Announcement;
import com.example.hungthinhapartmentmanagement.Activity.NotificationDetailActivity;
import com.example.hungthinhapartmentmanagement.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    private final Context context;
    private final List<Announcement> announcements;
    private final AnnouncementHelper helper;
    private final String currentEmail;
    private final String apartmentId;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    // Định dạng thời gian với múi giờ +07
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    {
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
    }

    public AnnouncementAdapter(Context context, List<Announcement> announcements, String currentEmail, String apartmentId) {
        this.context = context;
        this.announcements = announcements;
        this.helper = new AnnouncementHelper();
        this.currentEmail = currentEmail;
        this.apartmentId = apartmentId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Announcement announcement = announcements.get(position);
        holder.tvTitle.setText(announcement.getTitle());

        // Format và hiển thị ngày gửi
        String formattedDate = announcement.getCreateAt() != null
                ? dateFormat.format(announcement.getCreateAt().toDate())
                : "Không có ngày";
        holder.tvDate.setText(formattedDate);

        // Ẩn dấu chấm đỏ mặc định
        holder.unreadIndicator.setVisibility(View.GONE);

        // Chạy kiểm tra isEmailInReadBy trên thread nền
        executorService.execute(() -> {
            try {
                boolean isRead = helper.isEmailInReadBy(announcement.getId(), currentEmail);
                // Cập nhật UI trên main thread
                uiHandler.post(() -> {
                    if (!isRead) {
                        holder.unreadIndicator.setVisibility(View.VISIBLE); // Hiển thị dấu chấm đỏ nếu chưa đọc
                    } else {
                        holder.unreadIndicator.setVisibility(View.GONE); // Ẩn nếu đã đọc
                    }
                });
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                // Mặc định ẩn nếu lỗi
                uiHandler.post(() -> holder.unreadIndicator.setVisibility(View.GONE));
            }
        });

        holder.cardView.setOnClickListener(v -> {
            // Chạy addEmailToReadBy trên thread nền
            executorService.execute(() -> {
                try {
                    helper.addEmailToReadBy(announcement.getId(), currentEmail);
                    // Cập nhật UI (chuyển sang activity) trên main thread
                    uiHandler.post(() -> {
                        Intent intent = new Intent(context, NotificationDetailActivity.class);
                        intent.putExtra("content", announcement.getContent());
                        intent.putExtra("createAt", formattedDate); // Sử dụng formattedDate đã tính toán
                        intent.putExtra("title", announcement.getTitle());
                        intent.putExtra("email", currentEmail);
                        intent.putExtra("announcementId", announcement.getId());
                        intent.putExtra("apartmentId", apartmentId);
                        context.startActivity(intent);
                    });
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    // Hiển thị lỗi trên UI nếu cần
                    uiHandler.post(() -> {
                        // Có thể thêm Toast hoặc log lỗi
                    });
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDate;
        CardView cardView;
        View unreadIndicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            cardView = itemView.findViewById(R.id.cardView); // Ánh xạ CardView
            unreadIndicator = itemView.findViewById(R.id.unreadIndicator); // Ánh xạ dấu chấm đỏ
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        executorService.shutdown(); // Đóng Executor khi adapter bị detach
    }
}