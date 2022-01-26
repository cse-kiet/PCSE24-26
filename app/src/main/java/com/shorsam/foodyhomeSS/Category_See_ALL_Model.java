package com.shorsam.foodyhomeSS;
public class Category_See_ALL_Model {
    String Name,Image,Description,Price,Discount;
    Category_See_ALL_Model(){

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) { Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Category_See_ALL_Model(String name,String price ,String description,String discount,String image) {
        Name = name;
        Price=price;
        Description=description;
        Discount=discount;
        Price=price;
        Image = image;
    }
}
