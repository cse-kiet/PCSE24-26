package com.shorsam.foodyhomeSS;

public class RegularModel {
    String Name,Price,MRP,Discount;
    RegularModel(){

    }

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

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public RegularModel(String name, String price, String MRP, String discount) {
        Name = name;
        Price = price;
        this.MRP = MRP;
        Discount = discount;
    }
}
