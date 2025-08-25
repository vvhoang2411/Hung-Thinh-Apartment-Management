package com.example.hungthinhapartmentmanagement.Model;

public class User {
    private String email;
    private String name;
    private String phone;
    private String role;

    public User() {
        // Constructor rỗng cần cho Firestore
    }

    public User(String email, String name, String phone, String role) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    // Getters và Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
