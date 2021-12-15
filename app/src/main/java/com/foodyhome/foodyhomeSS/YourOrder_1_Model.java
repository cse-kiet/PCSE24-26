package com.foodyhome.foodyhomeSS;

public class YourOrder_1_Model {
    String Address,Store,OrderId;

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

    public String getStore() {
        return Store;
    }

    public void setStore(String store) {
        Store = store;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public YourOrder_1_Model(String orderId, String address, String store, String date, Integer totalPay) {
        Address = address;
        Date = date;
        Store=store;
        TotalPay = totalPay;
        OrderId=orderId;
    }

    String Date;
    Integer TotalPay;
    YourOrder_1_Model(){

    }



}
