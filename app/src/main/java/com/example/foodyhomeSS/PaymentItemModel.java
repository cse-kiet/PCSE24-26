package com.example.foodyhomeSS;

public class PaymentItemModel {
    PaymentItemModel(){

    }
    String Name;
    String QTY;
    String AddOn0;
    String AddOn1;
    String AddOn2;





    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getAddOn0() {
        return AddOn0;
    }

    public void setAddOn0(String addOn0) {
        AddOn0 = addOn0;
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

    public String getAddOn4() {
        return AddOn4;
    }

    public void setAddOn4(String addOn4) {
        AddOn4 = addOn4;
    }

    public PaymentItemModel( String name, String QTY, String addOn0, String addOn1, String addOn2, String addOn3, String addOn4) {

        Name = name;
        this.QTY = QTY;
        AddOn0 = addOn0;
        AddOn1 = addOn1;
        AddOn2 = addOn2;
        AddOn3 = addOn3;
        AddOn4 = addOn4;
    }

    String AddOn3;
    String AddOn4;

}
