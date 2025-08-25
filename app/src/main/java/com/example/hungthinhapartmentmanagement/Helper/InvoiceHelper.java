package com.example.hungthinhapartmentmanagement.Helper;

import com.example.hungthinhapartmentmanagement.Model.Invoice;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class InvoiceHelper {
    private FirebaseFirestore db;
    private static final String TAG = "InvoiceHelper";

    public InvoiceHelper() {
        db = FirebaseFirestore.getInstance();
    }

    public Task<List<Invoice>> getInvoices(String apartmentId, String feeType, String status) {
        // If apartmentId is null or empty, return empty list
        if (apartmentId == null || apartmentId.isEmpty()) {
            return com.google.android.gms.tasks.Tasks.forResult(new ArrayList<>());
        }

        // Create base query for invoices collection with apartmentId filter
        Query query = db.collection("invoices")
                .whereEqualTo("apartmentId", apartmentId);

        // Add filters based on feeType and status
        if (!feeType.equals("all") && !status.equals("all")) {
            // Both feeType and status are provided
            query = query.whereEqualTo("feeType", feeType)
                    .whereEqualTo("status", Boolean.parseBoolean(status));
        } else if (!feeType.equals("all") && status.equals("all")) {
            // Only feeType is provided
            query = query.whereEqualTo("feeType", feeType);
        } else if (!status.equals("all") && feeType.equals("all")) {
            // Only status is provided
            query = query.whereEqualTo("status", Boolean.parseBoolean(status));
        }

        // Add sorting by dueDate in descending order (farthest to nearest)
        query = query.orderBy("dueDate", Query.Direction.DESCENDING);

        // Execute query and map results to Invoice objects
        return query.get().continueWith(task -> {
            List<Invoice> invoices = new ArrayList<>();
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                for (com.google.firebase.firestore.DocumentSnapshot document : snapshot.getDocuments()) {
                    Invoice invoice = document.toObject(Invoice.class);
                    invoice.setDocumentID(document.getId()); // Set documentID for each invoice
                    invoices.add(invoice);
                }
            }
            return invoices;
        });
    }

    public Task<String> addInvoice(Invoice invoice) {
        // Validate input
        if (invoice == null || invoice.getApartmentId() == null || invoice.getApartmentId().isEmpty()) {
            return com.google.android.gms.tasks.Tasks.forException(new IllegalArgumentException("Invalid invoice or apartmentId"));
        }

        // Save to Firestore
        return db.collection("invoices")
                .add(invoice)
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        String documentId = task.getResult().getId();
                        // Update documentID in the Invoice object
                        invoice.setDocumentID(documentId);
                        db.collection("invoices").document(documentId).set(invoice);
                        return documentId;
                    } else {
                        throw task.getException();
                    }
                });
    }

    public Task<Void> updateInvoice(String documentID, Invoice invoice) {
        // Validate input
        if (documentID == null || documentID.isEmpty() || invoice == null) {
            return com.google.android.gms.tasks.Tasks.forException(new IllegalArgumentException("Invalid documentID or invoice"));
        }

        // Ensure documentID is set in the Invoice object
        invoice.setDocumentID(documentID);

        // Update in Firestore
        return db.collection("invoices").document(documentID)
                .set(invoice);
    }

    public Task<Void> deleteInvoice(String documentID) {
        // Validate input
        if (documentID == null || documentID.isEmpty()) {
            return com.google.android.gms.tasks.Tasks.forException(new IllegalArgumentException("Invalid documentID"));
        }

        // Delete from Firestore
        return db.collection("invoices").document(documentID)
                .delete();
    }
}