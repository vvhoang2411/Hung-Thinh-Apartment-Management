package com.example.hungthinhapartmentmanagement.Model;

public class Resident {
    private String apartmentId;
    private String birthday;
    private String email;
    private String fullName;
    private String gender;
    private String phone;
    private boolean relationship;
    private String documentId;

    public Resident() {
        // Constructor rỗng cần cho Firestore
    }
    public Resident(String apartmentId, String birthday, String email, String fullName,
                    String gender, String phone, boolean relationship) {
        this.apartmentId = apartmentId;
        this.birthday = birthday;
        this.email = email;
        this.fullName = fullName;
        this.gender = gender;
        this.phone = phone;
        this.relationship = relationship;
    }

    public Resident(String apartmentId, String birthday, String email, String fullName,
                    String gender, String phone, boolean relationship, String documentId) {
        this.apartmentId = apartmentId;
        this.birthday = birthday;
        this.email = email;
        this.fullName = fullName;
        this.gender = gender;
        this.phone = phone;
        this.relationship = relationship;
        this.documentId = documentId;
    }

    // Getters và Setters
    public String getApartmentId() { return apartmentId; }
    public void setApartmentId(String apartmentId) { this.apartmentId = apartmentId; }
    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public boolean isRelationship() { return relationship; }
    public void setRelationship(boolean relationship) { this.relationship = relationship; }
    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }
}
