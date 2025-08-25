package com.example.hungthinhapartmentmanagement.Model;

import com.google.firebase.Timestamp;

public class Invoice {
    private String apartmentId;
    private String feeType;
    private String money;
    private String month;
    private String note;
    private String year;
    private Timestamp dueDate; // Đổi từ Date thành Timestamp
    private boolean status;
    private String documentID;

    // Default constructor required for Firestore
    public Invoice() {}

    // Parameterized constructor
    public Invoice(String apartmentId, String feeType, String money, String month, String note,
                   String year, Timestamp dueDate, boolean status, String documentID) {
        this.apartmentId = apartmentId;
        this.feeType = feeType;
        this.money = money;
        this.month = month;
        this.note = note;
        this.year = year;
        this.dueDate = dueDate;
        this.status = status;
        this.documentID = documentID;
    }

    // Getters and Setters
    public String getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(String apartmentId) {
        this.apartmentId = apartmentId;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }
}