package com.shorsam.foodyhomeSS;

public class SelectStoreModel {
    String Name,Address,Rating;
    SelectStoreModel(){

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public SelectStoreModel(String name, String address, String rating) {
        Name = name;
        Address = address;
        Rating = rating;
    }
}
