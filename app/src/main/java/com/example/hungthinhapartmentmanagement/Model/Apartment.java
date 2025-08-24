package com.example.hungthinhapartmentmanagement.Model;

public class Apartment {
    private String id;
    private String apartmentNumber;
    private String area;
    private String building;
    private String desc;
    private String floor;
    private String status;

    public Apartment() {
        // Required empty constructor for Firestore
    }

    public Apartment(String id, String apartmentNumber, String area, String building, String desc, String floor, String status) {
        this.id = id;
        this.apartmentNumber = apartmentNumber;
        this.area = area;
        this.building = building;
        this.desc = desc;
        this.floor = floor;
        this.status = status;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String apartmentNumber) { this.apartmentNumber = apartmentNumber; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public String getBuilding() { return building; }
    public void setBuilding(String building) { this.building = building; }
    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }
    public String getFloor() { return floor; }
    public void setFloor(String floor) { this.floor = floor; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
