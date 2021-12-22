package com.shorsam.foodyhomeSS;

public class YourOrder2Model {
    String Image,Name,Price,MRP,AddOn1,AddOn2,AddOn3,AddOn0,AddOn4,Store,QTY,Size;

    public String getAddOn4() {
        return AddOn4;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public void setAddOn4(String addOn4) {
        AddOn4 = addOn4;
    }

    public YourOrder2Model(String addOn4) {
        AddOn4 = addOn4;
    }

    YourOrder2Model(){

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

    public String getStore() {
        return Store;
    }

    public void setStore(String store) {
        Store = store;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public YourOrder2Model(String image, String name, String price, String MRP, String addOn1, String addOn2, String addOn3, String size, String addOn0, String store) {
        Image = image;
        Name = name;
        Price = price;
        this.MRP = MRP;
        AddOn1 = addOn1;
        AddOn2 = addOn2;
        AddOn3 = addOn3;
        AddOn0 = addOn0;
        Store = store;
        Size=size;
    }
}
