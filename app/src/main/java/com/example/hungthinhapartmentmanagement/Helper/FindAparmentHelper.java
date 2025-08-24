package com.example.hungthinhapartmentmanagement.Helper;

import com.example.hungthinhapartmentmanagement.Model.Resident;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class FindAparmentHelper {

    // Hàm lấy danh sách Resident (chỉ lấy apartmentId và fullName) theo relationship = true
    public interface OnResidentsFetchedListener {
        void onSuccess(List<Resident> residents);
        void onError(Exception e);
    }

    public void findResidentsByKeyword(String keyword, OnResidentsFetchedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("residents")
                .whereEqualTo("relationship", true);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Resident> residents = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Chỉ lấy apartmentId và fullName
                    String apartmentId = document.getString("apartmentId");
                    String fullName = document.getString("fullName");

                    // Tạo Resident object với các trường cần thiết
                    Resident resident = new Resident();
                    resident.setApartmentId(apartmentId);
                    resident.setFullName(fullName);

                    // Lọc theo từ khóa (nếu có)
                    if (keyword == null || keyword.trim().isEmpty()) {
                        residents.add(resident);
                    } else {
                        String keywordLower = keyword.trim().toLowerCase(Locale.getDefault());
                        if ((apartmentId != null && apartmentId.toLowerCase(Locale.getDefault()).contains(keywordLower)) ||
                                (fullName != null && fullName.toLowerCase(Locale.getDefault()).contains(keywordLower))) {
                            residents.add(resident);
                        }
                    }
                }

                // Sắp xếp danh sách theo apartmentId (A-Z, 0-9)
                Collections.sort(residents, new Comparator<Resident>() {
                    @Override
                    public int compare(Resident r1, Resident r2) {
                        String id1 = r1.getApartmentId() != null ? r1.getApartmentId() : "";
                        String id2 = r2.getApartmentId() != null ? r2.getApartmentId() : "";
                        return id1.compareTo(id2);
                    }
                });

                listener.onSuccess(residents);
            } else {
                listener.onError(task.getException());
            }
        });
    }
}