package com.example.blacklight.Models;

public class Orders {
    private String firstName, lastName, phoneNumber, pickUpSpot, totalPrice, state, date, time;

    public Orders (){}

    public Orders(String firstName, String lastName, String phoneNumber, String pickUpSpot, String totalPrice, String state, String date, String time) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.pickUpSpot = pickUpSpot;
        this.totalPrice = totalPrice;
        this.state = state;
        this.date = date;
        this.time = time;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPickUpSpot() {
        return pickUpSpot;
    }

    public void setPickUpSpot(String pickUpSpot) {
        this.pickUpSpot = pickUpSpot;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
