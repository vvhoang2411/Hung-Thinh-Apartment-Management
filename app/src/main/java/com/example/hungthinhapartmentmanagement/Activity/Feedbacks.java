package com.example.hungthinhapartmentmanagement.Activity;




import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hungthinhapartmentmanagement.R;

public class Feedbacks extends AppCompatActivity {

    private EditText editTextFeedback;
    private RatingBar ratingBar;
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedbacks); // Giao diện XML đã tạo

        editTextFeedback = findViewById(R.id.editTextFeedback);
        ratingBar = findViewById(R.id.ratingBar);
        buttonSend = findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(v -> sendFeedback());
    }

    private void sendFeedback() {
        String feedback = editTextFeedback.getText().toString().trim();
        int rating = (int) ratingBar.getRating();

        if (feedback.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập nội dung phản hồi!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Xử lý gửi phản hồi đến server hoặc lưu trữ tại đây
        // Ví dụ: hiện thông báo đã gửi thành công
        Toast.makeText(this, "Phản hồi đã được gửi!\nĐánh giá: " + rating + " sao", Toast.LENGTH_LONG).show();

        // Có thể reset lại dữ liệu nếu muốn
        // editTextFeedback.setText("");
        // ratingBar.setRating(0);
    }
}


