package com.example.hungthinhapartmentmanagement.Model;

public class Apartment {
    private String id;
    private String apartmentNumber;
    private String status;
    private String area;
    private String desc;

    // Constructors, getters, setters
    public Apartment() {}
    public Apartment(String id, String apartmentNumber, String status, String area, String desc) {
        this.id = id;
        this.apartmentNumber = apartmentNumber;
        this.status = status;
        this.area = area;
        this.desc = desc;
    }
    // Getters and setters...


    public String getId() {
        return id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
