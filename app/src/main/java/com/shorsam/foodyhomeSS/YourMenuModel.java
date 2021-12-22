package com.shorsam.foodyhomeSS;

public class YourMenuModel {
    String Image;
    String Name;
    String AddOn1;
    String AddOn2;
    String AddOn3;
    String AddOn0;
    String AddOn4;
    String MRP;
    String Price;
    String Size;

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public YourMenuModel(String QTY) {
        this.QTY = QTY;
    }

    String QTY;
    YourMenuModel(){

    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddOn1() {
        return AddOn1;
    }

    public void setAddOn1(String addOn1) {
        AddOn1 = addOn1;
    }

    public String getAddOn2() {
        return AddOn2;
    }

    public void setAddOn2(String addOn2) {
        AddOn2 = addOn2;
    }

    public String getAddOn3() {
        return AddOn3;
    }

    public void setAddOn3(String addOn3) {
        AddOn3 = addOn3;
    }

    public String getAddOn0() {
        return AddOn0;
    }

    public void setAddOn0(String addOn0) {
        AddOn0 = addOn0;
    }

    public String getAddOn4() {
        return AddOn4;
    }

    public void setAddOn4(String addOn4) {
        AddOn4 = addOn4;
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

    public YourMenuModel(String image, String name, String addOn1, String addOn2, String addOn3, String addOn0, String addOn4, String MRP, String price,String size) {
        Image = image;
        Name = name;
        AddOn1 = addOn1;
        AddOn2 = addOn2;
        AddOn3 = addOn3;
        AddOn0 = addOn0;
        AddOn4 = addOn4;
        this.MRP = MRP;
        Price = price;
        Size=size;
    }
}
