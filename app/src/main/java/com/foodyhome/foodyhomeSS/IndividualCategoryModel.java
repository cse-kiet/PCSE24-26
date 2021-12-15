package com.foodyhome.foodyhomeSS;

public class IndividualCategoryModel {
    String Name,Image,Price;

    IndividualCategoryModel(){

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

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public IndividualCategoryModel(String name, String image, String price) {
        Name = name;
        Image = image;
        Price = price;
    }
}
