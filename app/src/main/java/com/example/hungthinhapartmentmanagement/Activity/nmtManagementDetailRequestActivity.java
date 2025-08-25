package com.example.hungthinhapartmentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hungthinhapartmentmanagement.Helper.RepairRequestHelper;
import com.example.hungthinhapartmentmanagement.R;

public class nmtManagementDetailRequestActivity extends AppCompatActivity {

    private TextView tvVanDe, tvGhiChu, tvNguoiLienHe, tvSoDienThoai, tvDiaChi;
    private RadioGroup rgTrangThai;
    private RadioButton rbChuaTiepNhan, rbDaTiepNhan, rbDaHoanThanh;
    private Button btnCapNhat;
    private ImageButton btnBack;
    private RepairRequestHelper repairRequestHelper;
    private String documentId;
    private static final String TAG = "ManagementDetailRequestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_detail_request);

        // Ánh xạ các view
        tvVanDe = findViewById(R.id.tv_van_de);
        tvGhiChu = findViewById(R.id.tv_ghi_chu);
        tvNguoiLienHe = findViewById(R.id.tv_nguoi_lien_he);
        tvSoDienThoai = findViewById(R.id.tv_sdt);
        tvDiaChi = findViewById(R.id.tv_dia_chi);
        rgTrangThai = findViewById(R.id.rg_trang_thai);
        rbChuaTiepNhan = findViewById(R.id.rb_chua_tiep_nhan);
        rbDaTiepNhan = findViewById(R.id.rb_da_tiep_nhan);
        rbDaHoanThanh = findViewById(R.id.rb_da_hoan_thanh);
        btnCapNhat = findViewById(R.id.btn_cap_nhat);
        btnBack = findViewById(R.id.btnBack);

        // Kiểm tra null views
        if (tvVanDe == null || tvGhiChu == null || tvNguoiLienHe == null || tvSoDienThoai == null || tvDiaChi == null ||
                rgTrangThai == null || rbChuaTiepNhan == null || rbDaTiepNhan == null || rbDaHoanThanh == null ||
                btnCapNhat == null || btnBack == null) {
            Log.e(TAG, "One or more views are null - Check activity_management_detail_request.xml");
            Toast.makeText(this, "Lỗi: Không tìm thấy view", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        documentId = intent.getStringExtra("documentId");
        String apartmentId = intent.getStringExtra("apartmentId");
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String status = intent.getStringExtra("status");
        String fullName = intent.getStringExtra("fullName");
        String phone = intent.getStringExtra("phone");

        // Ánh xạ dữ liệu lên các trường
        tvVanDe.setText(title != null ? title : "Chưa có tiêu đề");
        tvGhiChu.setText(description != null ? description : "Chưa có ghi chú");
        tvNguoiLienHe.setText(fullName != null ? fullName : "Chưa có thông tin");
        tvSoDienThoai.setText(phone != null ? phone : "Chưa có số điện thoại");
        tvDiaChi.setText(apartmentId != null ? "Căn hộ " + apartmentId + " Chung Cư Hưng Thịnh" : "Chưa có địa chỉ");

        // Thiết lập trạng thái RadioButton dựa trên status
        if (status != null) {
            switch (status.toLowerCase()) {
                case "pending":
                    rbChuaTiepNhan.setChecked(true);
                    break;
                case "received":
                    rbDaTiepNhan.setChecked(true);
                    break;
                case "completed":
                    rbDaHoanThanh.setChecked(true);
                    break;
                default:
                    Log.w(TAG, "Unknown status: " + status);
                    rbChuaTiepNhan.setChecked(true); // Mặc định nếu status không hợp lệ
                    break;
            }
        } else {
            rbChuaTiepNhan.setChecked(true); // Mặc định nếu status null
        }

        // Khởi tạo RepairRequestHelper
        repairRequestHelper = new RepairRequestHelper();

        // Xử lý sự kiện nút Cập nhật
        btnCapNhat.setOnClickListener(v -> {
            String newStatus = null;
            if (rbChuaTiepNhan.isChecked()) {
                newStatus = "pending";
            } else if (rbDaTiepNhan.isChecked()) {
                newStatus = "received";
            } else if (rbDaHoanThanh.isChecked()) {
                newStatus = "completed";
            }

            if (newStatus == null || documentId == null) {
                Toast.makeText(this, "Lỗi: Vui lòng chọn trạng thái hoặc documentId không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            repairRequestHelper.updateRequestStatus(documentId, newStatus, new RepairRequestHelper.OnOperationListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(nmtManagementDetailRequestActivity.this, "Cập nhật trạng thái thành công", Toast.LENGTH_SHORT).show();
                    // Chuyển về ManagementRepairRequestActivity
                    Intent returnIntent = new Intent(nmtManagementDetailRequestActivity.this, nmtManagementRepairRequestActivity.class);
                    startActivity(returnIntent);
                    finish();
                }

                @Override
                public void onError(String errorMessage) {
                    Log.e(TAG, "Lỗi khi cập nhật trạng thái: " + errorMessage);
                    Toast.makeText(nmtManagementDetailRequestActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Xử lý sự kiện nút Back
        btnBack.setOnClickListener(v -> {
            Intent returnIntent = new Intent(nmtManagementDetailRequestActivity.this, nmtManagementRepairRequestActivity.class);
            startActivity(returnIntent);
            finish();
        });
    }
}