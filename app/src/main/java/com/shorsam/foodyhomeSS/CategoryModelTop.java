package com.shorsam.foodyhomeSS;

public class CategoryModelTop {
    String Name,Image,Discount;
    CategoryModelTop(){

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

//    public String getDiscount() {
//        return Discount;
//    }
//
//    public void setDiscount(String discount) {
//        Discount = discount;}


    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public CategoryModelTop(String name, String image,String discount) {
        Discount=discount;
        Name = name;
        Image = image;
    }
}
