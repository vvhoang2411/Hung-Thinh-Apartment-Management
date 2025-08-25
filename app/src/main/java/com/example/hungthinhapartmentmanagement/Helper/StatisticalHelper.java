package com.example.hungthinhapartmentmanagement.Helper;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.Task;

public class StatisticalHelper {

    private final FirebaseFirestore db;

    public StatisticalHelper() {
        db = FirebaseFirestore.getInstance();
    }
    public Task<QuerySnapshot> getInvoicesByMonthYearStatus(String month, String year, boolean status) {
        return db.collection("invoices")
                .whereEqualTo("month", month)
                .whereEqualTo("year", year)
                .whereEqualTo("status", status)
                .get();
    }
}