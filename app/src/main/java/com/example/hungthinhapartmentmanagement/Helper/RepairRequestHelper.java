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
                .whereEqualTo("email", email)
                .whereEqualTo("isActive", true)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<RepairRequest> repairRequests = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        RepairRequest request = document.toObject(RepairRequest.class);
                        request.setCreatedAt(document.getTimestamp("createdAt"));
                        request.setUpdatedAt(document.getTimestamp("updatedAt"));
                        request.setDocumentId(document.getId());
                        request.setFullName(document.getString("fullName"));
                        request.setPhone(document.getString("phone"));
                        request.setEmail(document.getString("email"));
                        repairRequests.add(request);
                        Log.d(TAG, "Document: " + document.getId() + ", Title: " + request.getTitle());
                    }
                    Log.d(TAG, "Lấy được " + repairRequests.size() + " yêu cầu cho apartmentId: " + apartmentId);
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

    public void getActiveRequestsByStatus(String status, OnDataRetrievedListener listener) {
        if (status == null || status.trim().isEmpty()) {
            Log.e(TAG, "status is null or empty");
            listener.onError("status không hợp lệ");
            return;
        }

        repairRequestsRef
                .whereEqualTo("status", status)
                .whereEqualTo("isActive", true)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<RepairRequest> repairRequests = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        RepairRequest request = document.toObject(RepairRequest.class);
                        request.setCreatedAt(document.getTimestamp("createdAt"));
                        request.setUpdatedAt(document.getTimestamp("updatedAt"));
                        request.setDocumentId(document.getId());
                        request.setFullName(document.getString("fullName"));
                        request.setPhone(document.getString("phone"));
                        request.setEmail(document.getString("email"));
                        repairRequests.add(request);
                        Log.d(TAG, "Document: " + document.getId() + ", Title: " + request.getTitle() + ", Status: " + request.getStatus());
                    }
                    Log.d(TAG, "Lấy được " + repairRequests.size() + " yêu cầu cho status: " + status);
                    listener.onDataRetrieved(repairRequests);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching requests by status: " + e.getMessage());
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
        data.put("fullName", request.getFullName());
        data.put("phone", request.getPhone());
        data.put("email", request.getEmail());

        repairRequestsRef.add(data)
                .addOnSuccessListener(documentReference -> listener.onSuccess())
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error creating request: " + e.getMessage());
                    listener.onError(e.getMessage());
                });
    }

    public void updateRequestStatus(String documentId, String status, OnOperationListener listener) {
        if (documentId == null || documentId.trim().isEmpty()) {
            Log.e(TAG, "documentId is null or empty");
            listener.onError("documentId không hợp lệ");
            return;
        }
        if (status == null || status.trim().isEmpty()) {
            Log.e(TAG, "status is null or empty");
            listener.onError("status không hợp lệ");
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("status", status);
        updates.put("updatedAt", new Timestamp(new Date()));

        repairRequestsRef.document(documentId)
                .update(updates)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating status: " + e.getMessage());
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