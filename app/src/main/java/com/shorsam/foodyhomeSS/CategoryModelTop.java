package com.shorsam.foodyhomeSS;

public class CategoryModelTop {
    String Name,Image;
    CategoryModelTop(){

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

    public CategoryModelTop(String name, String image) {
        Name = name;
        Image = image;
    }
}
