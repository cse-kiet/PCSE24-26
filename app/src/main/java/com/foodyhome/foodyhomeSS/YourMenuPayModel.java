package com.foodyhome.foodyhomeSS;

public class YourMenuPayModel {
    public YourMenuPayModel(String price,String QTY) {
        Price = price;
        this.QTY = QTY;
    }

    String Price,QTY;

    public String getQTY() {
        return QTY;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    YourMenuPayModel(){

    }
}
