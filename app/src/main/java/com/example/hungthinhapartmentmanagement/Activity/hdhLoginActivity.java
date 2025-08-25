package com.example.hungthinhapartmentmanagement.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.hungthinhapartmentmanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class hdhLoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPass;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;
    private ConstraintLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        rootLayout = findViewById(R.id.rootLayout);

        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(hdhLoginActivity.this);
                progressDialog.setMessage("Đang đăng nhập...");
                progressDialog.setCancelable(false); // không cho bấm ra ngoài để tắt
                progressDialog.show();
                String email = edtEmail.getText().toString();
                String password = edtPass.getText().toString();

                if (email.isEmpty()||password.isEmpty()){
                    Toast.makeText(hdhLoginActivity.this, "Không được bỏ trống!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidEmail(email)) {
                    Toast.makeText(hdhLoginActivity.this, "Địa chỉ email không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                checkUserRole(user.getUid());
                            }
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(hdhLoginActivity.this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }

    private void checkUserRole(String uid) {
        db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String role = documentSnapshot.getString("role");
                String email = documentSnapshot.getString("email");
                if ("admin".equals(role)) {
                    // Chuyển sang form quản lý (MainActivity)
                    Intent intent = new Intent(hdhLoginActivity.this, hdhMainActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish(); // Đóng LoginActivity
                } else {
                    // Truy cập bảng residents để lấy thông tin
                    db.collection("residents")
                            .whereEqualTo("email", email)
                            .get()
                            .addOnSuccessListener(querySnapshot -> {
                                if (!querySnapshot.isEmpty()) {
                                    DocumentSnapshot residentDoc = querySnapshot.getDocuments().get(0);
                                    String apartmentId = residentDoc.getString("apartmentId");
                                    String phone = residentDoc.getString("phone");
                                    String fullName = residentDoc.getString("fullName");

                                    // Chuyển sang ResidentMainActivity với 4 giá trị
                                    Intent intent = new Intent(hdhLoginActivity.this, nqkResidentMainActivity.class);
                                    intent.putExtra("email", email);
                                    intent.putExtra("apartmentId", apartmentId);
                                    intent.putExtra("phone", phone);
                                    intent.putExtra("fullName", fullName);
                                    startActivity(intent);
                                    finish(); // Đóng LoginActivity
                                } else {
                                    Toast.makeText(this, "Không tìm thấy thông tin cư dân!", Toast.LENGTH_SHORT).show();
                                    mAuth.signOut();
                                }
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Lỗi truy vấn thông tin cư dân: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                            });
                }
            } else {
                Toast.makeText(this, "Người dùng không tồn tại trong hệ thống!", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Lỗi kiểm tra role: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        });
    }
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}