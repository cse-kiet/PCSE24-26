package com.shorsam.foodyhomeSS;

public class AllProductModel {
    String Name,Image,MRP,Price,Rating,Discount,StoreName,DeliveryStatus;
    AllProductModel(){

    }


    public String getDeliveryStatus() {
        return DeliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        DeliveryStatus = deliveryStatus;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public AllProductModel(String name, String image, String MRP, String price, String rating, String discount, String storeName, String deliveryStatus) {
        Name = name;
        Image = image;
        this.MRP = MRP;
        Price = price;
        Rating = rating;
        Discount = discount;
        DeliveryStatus=deliveryStatus;
        StoreName=storeName;
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
