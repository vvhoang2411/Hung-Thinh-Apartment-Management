package com.example.hungthinhapartmentmanagement.Model;

public class Resident {
    private String apartmentId;
    private String fullName;
    private String gender;
    private String phone;
    private String email;
    private boolean relationship;
    private String birthday; // Đổi từ Date thành String

    // Default constructor required for Firebase
    public Resident() {}

    // Parameterized constructor
    public Resident(String apartmentId, String fullName, String gender, String phone, String email,
                    boolean relationship, String birthday) {
        this.apartmentId = apartmentId;
        this.fullName = fullName;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.relationship = relationship;
        this.birthday = birthday;
    }

    // Getters and Setters
    public String getApartmentId() {
        return apartmentId;
    }
    public void setApartmentId(String apartmentId) {
        this.apartmentId = apartmentId;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
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

    public boolean isRelationship() {
        return relationship;
    }
    public void setRelationship(boolean relationship) {
        this.relationship = relationship;
    }

    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}