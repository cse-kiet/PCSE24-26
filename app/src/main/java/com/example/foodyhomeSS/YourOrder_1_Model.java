package com.example.foodyhomeSS;

public class YourOrder_1_Model {
    String Address;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Integer getTotalPay() {
        return TotalPay;
    }

    public void setTotalPay(Integer totalPay) {
        TotalPay = totalPay;
    }

    public YourOrder_1_Model(String address, String date, Integer totalPay) {
        Address = address;
        Date = date;
        TotalPay = totalPay;
    }

    String Date;
    Integer TotalPay;
    YourOrder_1_Model(){

    }



}
