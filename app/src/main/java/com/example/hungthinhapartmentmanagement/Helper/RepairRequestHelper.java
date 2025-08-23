package com.example.hungthinhapartmentmanagement.Helper;

import android.util.Log;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.example.hungthinhapartmentmanagement.Model.RepairRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RepairRequestHelper {

    private static final String COLLECTION_NAME = "repair_requests";
    private static final String TAG = "RepairRequestHelper";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference repairRequestsRef = db.collection(COLLECTION_NAME);

    public void getActiveRequestsByApartmentId(String apartmentId, String email, OnDataRetrievedListener listener) {
        if (apartmentId == null || apartmentId.trim().isEmpty()) {
            Log.e(TAG, "apartmentId is null or empty");
            listener.onError("apartmentId không hợp lệ");
            return;
        }
        if (email == null || email.trim().isEmpty()) {
            Log.e(TAG, "email is null or empty");
            listener.onError("email không hợp lệ");
            return;
        }

        repairRequestsRef
                .whereEqualTo("apartmentId", apartmentId)
                .whereEqualTo("email", email) // Thêm điều kiện lọc theo email
                .whereEqualTo("isActive", true)
                .orderBy("createdAt", Query.Direction.DESCENDING) // Sắp xếp từ mới nhất đến cũ nhất
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<RepairRequest> repairRequests = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        RepairRequest request = document.toObject(RepairRequest.class);
                        request.setCreatedAt(document.getTimestamp("createdAt"));
                        request.setUpdatedAt(document.getTimestamp("updatedAt"));
                        request.setDocumentId(document.getId()); // Gán documentId
                        request.setFullName(document.getString("fullName")); // Ánh xạ fullName
                        request.setPhone(document.getString("phone"));  //Ánh xạ phone
                        request.setEmail(document.getString("email")); // Ánh xạ email
                        repairRequests.add(request);
                    }
                    listener.onDataRetrieved(repairRequests);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching requests: " + e.getMessage());
                    if (Objects.requireNonNull(e.getMessage()).contains("The query requires an index")) {
                        listener.onError("Chỉ mục đang được xây dựng. Vui lòng đợi hoặc kiểm tra Firebase Console.");
                    } else {
                        listener.onError("Lỗi khi tải dữ liệu: " + e.getMessage());
                    }
                });
    }

    public void createRepairRequest(RepairRequest request, OnOperationListener listener) {
        Map<String, Object> data = new HashMap<>();
        data.put("apartmentId", request.getApartmentId());
        data.put("title", request.getTitle());
        data.put("description", request.getDescription());
        data.put("status", request.getStatus());
        data.put("isActive", request.isActive());
        data.put("createdAt", request.getCreatedAt());
        data.put("updatedAt", request.getUpdatedAt());
        data.put("fullName", request.getFullName()); // Thêm fullName
        data.put("phone", request.getPhone());       // Thêm phone
        data.put("email", request.getEmail());       // Thêm email

        repairRequestsRef.add(data)
                .addOnSuccessListener(documentReference -> listener.onSuccess())
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error creating request: " + e.getMessage());
                    listener.onError(e.getMessage());
                });
    }

    public void deleteRepairRequest(String documentId, OnOperationListener listener) {
        repairRequestsRef.document(documentId)
                .update("isActive", false, "updatedAt", new Timestamp(new Date()))
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error deleting request: " + e.getMessage());
                    listener.onError(e.getMessage());
                });
    }

    public interface OnDataRetrievedListener {
        void onDataRetrieved(List<RepairRequest> repairRequests);
        void onError(String errorMessage);
    }

    public interface OnOperationListener {
        void onSuccess();
        void onError(String errorMessage);
    }
}