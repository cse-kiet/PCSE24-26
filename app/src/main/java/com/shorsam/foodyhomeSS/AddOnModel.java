package com.shorsam.foodyhomeSS;

public class AddOnModel {
    AddOnModel(){

    }
    String Name,Price;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public AddOnModel(String name, String price) {
        Name = name;
        Price = price;
    }
}
