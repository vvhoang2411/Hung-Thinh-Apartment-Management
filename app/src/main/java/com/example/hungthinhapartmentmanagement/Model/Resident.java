package com.example.hungthinhapartmentmanagement.Model;

public class Resident {
    private String id;
    private String userId;
    private String apartmentId;
    private String name; // Từ user
    private String email; // Từ user
    private String phone; // Từ user
    private String apartmentNumber; // Từ apartment

    // Constructors, getters, setters
    public Resident() {}
    // Getters and setters...


    public String getApartmentId() {
        return apartmentId;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setApartmentId(String apartmentId) {
        this.apartmentId = apartmentId;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
