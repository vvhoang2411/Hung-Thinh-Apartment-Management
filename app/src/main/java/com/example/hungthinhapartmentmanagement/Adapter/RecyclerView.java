package com.example.hungthinhapartmentmanagement.Adapter;

public class RecyclerView {
    public String facility;
    public String date;
    public String duration;
    public String name;
    public String phone;

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public RecyclerView(String facility, String date, String duration, String name, String phone) {
        this.facility = facility;
        this.date = date;
        this.duration = duration;
        this.name = name;
        this.phone = phone;
    }

    // Constructor, getter, setter...
}

