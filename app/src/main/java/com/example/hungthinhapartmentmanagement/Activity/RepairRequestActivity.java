package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hungthinhapartmentmanagement.Activity.RepairRequestListActivity;
import com.google.firebase.Timestamp;
import com.example.hungthinhapartmentmanagement.Helper.RepairRequestHelper;
import com.example.hungthinhapartmentmanagement.Model.RepairRequest;
import com.example.hungthinhapartmentmanagement.R;

import java.util.Date;

public class RepairRequestActivity extends AppCompatActivity {

    private String apartmentId;
    private String fullName;
    private String phone;
    private String email;
    private EditText edtProblem, edtNote;
    private Button btnSend;
    private ImageButton btnBack;
    private RepairRequestHelper repairRequestHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_repair_request);

        // Nhận apartmentId, fullName, phone từ Intent
        apartmentId = getIntent().getStringExtra("apartmentId");
        fullName = getIntent().getStringExtra("fullName");
        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");
        if (apartmentId == null || apartmentId.trim().isEmpty()) {
            Toast.makeText(this, "Không tìm thấy apartmentId", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            Toast.makeText(this, "Không tìm thấy fullName", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (phone == null || phone.trim().isEmpty()) {
            Toast.makeText(this, "Không tìm thấy phone", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (email == null || email.trim().isEmpty()) {
            Toast.makeText(this, "Không tìm thấy email", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Khởi tạo các thành phần giao diện
        edtProblem = findViewById(R.id.edtProblem);
        edtNote = findViewById(R.id.edtNote);
        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);
        repairRequestHelper = new RepairRequestHelper();

        // Xử lý WindowInsets để đẩy giao diện xuống dưới thanh trạng thái
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(RepairRequestActivity.this, RepairRequestListActivity.class);
            intent.putExtra("apartmentId", apartmentId);
            intent.putExtra("fullName", fullName);
            intent.putExtra("phone", phone);
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
        });

        // Xử lý sự kiện khi nhấn nút Send
        btnSend.setOnClickListener(v -> {
            String title = edtProblem.getText().toString().trim();
            String description = edtNote.getText().toString().trim();

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(RepairRequestActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo đối tượng RepairRequest với fullName và phone
            RepairRequest request = new RepairRequest(
                    apartmentId,
                    title,
                    description,
                    "pending",
                    true,
                    new Timestamp(new Date()),
                    new Timestamp(new Date()),
                    fullName,
                    phone,
                    email
            );

            // Gửi yêu cầu lên Firestore
            repairRequestHelper.createRepairRequest(request, new RepairRequestHelper.OnOperationListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(RepairRequestActivity.this, "Gửi yêu cầu thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RepairRequestActivity.this, RepairRequestListActivity.class);
                    intent.putExtra("apartmentId", apartmentId);
                    intent.putExtra("fullName", fullName);
                    intent.putExtra("phone", phone);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(RepairRequestActivity.this, "Lỗi khi gửi yêu cầu: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}