package com.example.hungthinhapartmentmanagement.Model;

import com.google.firebase.Timestamp;

public class RepairRequest {
    private String documentId;
    private String apartmentId;
    private String title;
    private String description;
    private String status;
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String fullName; // Thêm trường fullName
    private String phone;    // Thêm trường phone
    private String email; //Thêm trường email

    public RepairRequest() {
    }

    public RepairRequest(String apartmentId, String title, String description, String status, boolean isActive, Timestamp createdAt, Timestamp updatedAt, String fullName, String phone, String email) {
        this.apartmentId = apartmentId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
    }

    public String getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(String apartmentId) {
        if (apartmentId == null || apartmentId.trim().isEmpty()) {
            throw new IllegalArgumentException("apartmentId cannot be null or empty");
        }
        this.apartmentId = apartmentId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}