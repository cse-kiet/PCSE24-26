package com.shorsam.foodyhomeSS;

public class AllAddressModel {
    String Name,Address,Phone;
    AllAddressModel(){

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public AllAddressModel(String name, String address, String phone) {
        Name = name;
        Address = address;
        Phone = phone;
    }
}
