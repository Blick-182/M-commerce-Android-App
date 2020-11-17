package com.example.blacklight.Models;

public class Customers {

    private String username, phoneNumber, password, firstName, lastName, county, subCounty, city, pickUpSpot;

    public Customers(){

    }


    public Customers(String username, String phoneNumber, String password, String firstName, String lastName, String county, String subCounty, String city, String pickUpSpot) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.county = county;
        this.subCounty = subCounty;
        this.city = city;
        this.pickUpSpot = pickUpSpot;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getSubCounty() {
        return subCounty;
    }

    public void setSubCounty(String subCounty) {
        this.subCounty = subCounty;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPickUpSpot() {
        return pickUpSpot;
    }

    public void setPickUpSpot(String pickUpSpot) {
        this.pickUpSpot = pickUpSpot;
    }
}
