package com.example.hungthinhapartmentmanagement.Model;

import com.google.firebase.Timestamp;

public class Device {
    private String manufacturer;
    private String mfg;
    private String model;
    private String name;
    private String position;
    private String type;
    private Timestamp date_check;

    // Default constructor for Firebase
    public Device() {
    }

    // Parameterized constructor
    public Device(String manufacturer, String mfg, String model, String name, String position, String type, Timestamp date_check) {
        this.manufacturer = manufacturer;
        this.mfg = mfg;
        this.model = model;
        this.name = name;
        this.position = position;
        this.type = type;
        this.date_check = date_check;
    }

    // Getter and Setter for manufacturer
    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    // Getter and Setter for mfg
    public String getMfg() {
        return mfg;
    }

    public void setMfg(String mfg) {
        this.mfg = mfg;
    }

    // Getter and Setter for model
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for position
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    // Getter and Setter for type
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Getter and Setter for date_check
    public Timestamp getDate_check() {
        return date_check;
    }

    public void setDate_check(Timestamp date_check) {
        this.date_check = date_check;
    }
}