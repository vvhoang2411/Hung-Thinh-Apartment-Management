package com.example.hungthinhapartmentmanagement.Model;

public class Apartment {
    private String apartment_number;
    private String area;
    private String desc;
    private String floor;
    private String status;

    // Default constructor required for Firestore
    public Apartment() {
    }

    // Constructor with all fields
    public Apartment(String apartment_number, String area, String desc, String floor, String status) {
        this.apartment_number = apartment_number;
        this.area = area;
        this.desc = desc;
        this.floor = floor;
        this.status = status;
    }

    // Getter and Setter for apartment_number
    public String getApartment_number() {
        return apartment_number;
    }

    public void setApartment_number(String apartment_number) {
        this.apartment_number = apartment_number;
    }

    // Getter and Setter for area
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    // Getter and Setter for desc
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    // Getter and Setter for floor
    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    // Getter and Setter for status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}