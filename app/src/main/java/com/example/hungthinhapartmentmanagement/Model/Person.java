package com.example.hungthinhapartmentmanagement.Activity;

public class Person {

    private String name;
    private String room;
    private String phone;
    private String email;
    private String password;

    public Person(String email, String name, String password, String phone, String room) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.room = room;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getRoom() {
        return room;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
