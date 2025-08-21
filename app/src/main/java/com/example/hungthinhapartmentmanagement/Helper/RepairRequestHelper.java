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

public class    RepairRequestHelper {

    private static final String COLLECTION_NAME = "repair_requests";
    private static final String TAG = "RepairRequestHelper";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference repairRequestsRef = db.collection(COLLECTION_NAME);

    public void getActiveRequestsByApartmentId(String apartmentId, OnDataRetrievedListener listener) {
        if (apartmentId == null || apartmentId.trim().isEmpty()) {
            Log.e(TAG, "apartmentId is null or empty");
            listener.onError("apartmentId không hợp lệ");
            return;
        }

        repairRequestsRef
                .whereEqualTo("apartmentId", apartmentId)
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
                        repairRequests.add(request);
                    }
                    listener.onDataRetrieved(repairRequests);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching requests: " + e.getMessage());
                    if (e.getMessage().contains("The query requires an index")) {
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