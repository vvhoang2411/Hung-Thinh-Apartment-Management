package com.example.hungthinhapartmentmanagement.Helper;

import android.util.Log;

import com.example.hungthinhapartmentmanagement.Model.Announcement;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class AnnouncementHelper {
    private static final String TAG = "AnnouncementHelper";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference announcementsRef = db.collection("announcements");
    private final CollectionReference apartmentsRef = db.collection("apartments");

    public List<Announcement> getAnnouncementsByTarget(String apartmentId) throws ExecutionException, InterruptedException {
        List<Announcement> announcements = new ArrayList<>();

        if (apartmentId == null || apartmentId.isEmpty()) {
            throw new IllegalArgumentException("apartmentId cannot be null or empty");
        }

        Log.d(TAG, "Fetching apartment for apartment_number: " + apartmentId);
        // Query để tìm document trong apartments dựa trên field apartment_number
        Task<QuerySnapshot> apartmentQueryTask = apartmentsRef
                .whereEqualTo("apartment_number", apartmentId)
                .limit(1) // Chỉ lấy 1 document (giả định apartment_number là duy nhất)
                .get();
        QuerySnapshot apartmentSnapshot = Tasks.await(apartmentQueryTask);

        String floor = "";
        String apartmentNumber = apartmentId; // Sử dụng apartmentId làm apartment_number
        if (!apartmentSnapshot.isEmpty()) {
            DocumentSnapshot apartmentDoc = apartmentSnapshot.getDocuments().get(0);
            floor = Objects.toString(apartmentDoc.getString("floor"), "");
            Log.d(TAG, "Apartment found: floor=" + floor + ", apartment_number=" + apartmentNumber);
        } else {
            Log.e(TAG, "Apartment with apartment_number " + apartmentId + " does not exist.");
        }

        // Tập hợp các targetValues để tránh trùng lặp
        Set<String> targetValues = new HashSet<>();
        targetValues.add(apartmentNumber);
        if (!floor.isEmpty()) targetValues.add(floor);
        Log.d(TAG, "Target values for whereIn: " + targetValues);

        // Truy vấn cho targetType = "all"
        Task<QuerySnapshot> allQueryTask = announcementsRef
                .whereEqualTo("targetType", "all")
                .orderBy("createAt", Query.Direction.DESCENDING)
                .get();
        QuerySnapshot allSnapshot = Tasks.await(allQueryTask);
        Log.d(TAG, "All query returned " + allSnapshot.size() + " documents.");
        addAnnouncementsFromSnapshot(allSnapshot, announcements);

        // Truy vấn whereIn cho targetValue khớp với apartment_number hoặc floor
        if (!targetValues.isEmpty()) {
            Task<QuerySnapshot> nonNullQueryTask = announcementsRef
                    .whereIn("targetValue", new ArrayList<>(targetValues))
                    .orderBy("createAt", Query.Direction.DESCENDING)
                    .get();
            QuerySnapshot nonNullSnapshot = Tasks.await(nonNullQueryTask);
            Log.d(TAG, "Non-null query returned " + nonNullSnapshot.size() + " documents.");
            addAnnouncementsFromSnapshot(nonNullSnapshot, announcements);
        }

        // Truy vấn whereEqualTo null (nếu cần)
        Task<QuerySnapshot> nullQueryTask = announcementsRef
                .whereEqualTo("targetValue", null)
                .orderBy("createAt", Query.Direction.DESCENDING)
                .get();
        QuerySnapshot nullSnapshot = Tasks.await(nullQueryTask);
        Log.d(TAG, "Null query returned " + nullSnapshot.size() + " documents.");
        addAnnouncementsFromSnapshot(nullSnapshot, announcements);

        // Loại bỏ trùng lặp (nếu có) và sort
        Set<Announcement> uniqueAnnouncements = new HashSet<>(announcements);
        announcements.clear();
        announcements.addAll(uniqueAnnouncements);
        Collections.sort(announcements, (a1, a2) -> a2.getCreateAt().compareTo(a1.getCreateAt()));
        Log.d(TAG, "Total loaded " + announcements.size() + " announcements.");

        return announcements;
    }

    private void addAnnouncementsFromSnapshot(QuerySnapshot snapshot, List<Announcement> announcements) {
        for (DocumentSnapshot document : snapshot.getDocuments()) {
            try {
                Announcement announcement = document.toObject(Announcement.class);
                if (announcement != null) {
                    announcement.setId(document.getId());
                    announcements.add(announcement);
                    Log.d(TAG, "Added announcement ID: " + document.getId());
                } else {
                    Log.e(TAG, "Failed to map document " + document.getId());
                }
            } catch (Exception e) {
                Log.e(TAG, "Error mapping document " + document.getId() + ": " + e.getMessage());
            }
        }
    }

    public boolean isEmailInReadBy(String announcementId, String email) throws ExecutionException, InterruptedException {
        Task<DocumentSnapshot> task = announcementsRef.document(announcementId).get();
        DocumentSnapshot document = Tasks.await(task);

        if (document.exists()) {
            List<String> readBy = (List<String>) document.get("readBy");
            return readBy != null && readBy.contains(email);
        }
        return false;
    }

    public boolean addEmailToReadBy(String announcementId, String email) throws ExecutionException, InterruptedException {
        DocumentReference docRef = announcementsRef.document(announcementId);
        Task<DocumentSnapshot> task = docRef.get();
        DocumentSnapshot document = Tasks.await(task);

        if (document.exists()) {
            List<String> readBy = (List<String>) document.get("readBy");
            if (readBy == null) {
                readBy = new ArrayList<>();
            }
            if (!readBy.contains(email)) {
                readBy.add(email);
                Task<Void> updateTask = docRef.update("readBy", readBy);
                Tasks.await(updateTask);
                return true;
            }
            return false;
        }
        return false;
    }
}