package com.example.hungthinhapartmentmanagement.Model;

public class ParkingModel {
    private int imageResId;
    private String name, max, slot;

    public ParkingModel(int imageResId, String name, String max, String slot) {
        this.imageResId = imageResId;
        this.name = name;
        this.max = max;
        this.slot = slot;
    }

    public int getImageResId() { return imageResId; }
    public String getName() { return name; }
    public String getMax() { return max; }
    public String getSlot() { return slot; }
}
