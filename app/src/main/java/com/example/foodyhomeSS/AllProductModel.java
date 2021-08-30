package com.example.foodyhomeSS;

public class AllProductModel {
    String Name,Image,MRP,Price,Rating,Discount;
    AllProductModel(){

    }

    public AllProductModel(String name, String image, String MRP, String price, String rating, String discount) {
        Name = name;
        Image = image;
        this.MRP = MRP;
        Price = price;
        Rating = rating;
        Discount = discount;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
