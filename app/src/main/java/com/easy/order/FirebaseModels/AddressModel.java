package com.easy.order.FirebaseModels;

public class AddressModel {
    private String Data;

    public AddressModel() {
    }

    public AddressModel(String data) {
        Data = data;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
