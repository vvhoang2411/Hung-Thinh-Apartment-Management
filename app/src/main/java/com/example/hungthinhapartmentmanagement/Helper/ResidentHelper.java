package com.example.hungthinhapartmentmanagement.Helper;

import com.example.hungthinhapartmentmanagement.Model.Resident;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ResidentHelper {
    private static final String COLLECTION_NAME = "residents";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference residentsRef = db.collection(COLLECTION_NAME);

    public interface OnResidentLoadedListener {
        void onResidentsLoaded(List<Resident> residents);
    }

    public void getResidentByEmail(String email, OnResidentLoadedListener listener) {
        residentsRef.whereEqualTo("email", email).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Resident> residents = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Resident resident = document.toObject(Resident.class);
                            residents.add(resident);
                        }
                        listener.onResidentsLoaded(residents);
                    }
                });
    }

    public void updateResidentInfo(String email, String fullName, String gender, String phone, String birthday) {
        residentsRef.whereEqualTo("email", email).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        document.getReference().update(
                                "fullName", fullName,
                                "gender", gender,
                                "phone", phone,
                                "birthday", birthday
                        );
                    }
                });
    }
}