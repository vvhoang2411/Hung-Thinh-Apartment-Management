package com.example.hungthinhapartmentmanagement.Helper;

import com.example.hungthinhapartmentmanagement.Model.Device;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.Task;
import java.util.ArrayList;
import java.util.List;

public class DeviceHelper {
    private final FirebaseFirestore db;
    private final CollectionReference deviceCollection;

    // Constructor
    public DeviceHelper() {
        db = FirebaseFirestore.getInstance();
        deviceCollection = db.collection("devices");
    }

    // Get all devices
    public Task<QuerySnapshot> getAllDevices() {
        return deviceCollection.get();
    }

    // Add new device
    public Task<DocumentReference> addDevice(Device device) {
        return deviceCollection.add(device);
    }

    // Update device by documentId
    public Task<Void> updateDevice(String documentId, Device device) {
        return deviceCollection.document(documentId).set(device);
    }

    // Delete device by documentId
    public Task<Void> deleteDevice(String documentId) {
        return deviceCollection.document(documentId).delete();
    }
}