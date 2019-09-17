package com.easy.order.FirebaseModels;

public class PhoneNumberModel {
    private String PhoneNumber;

    public PhoneNumberModel() {
    }

    public PhoneNumberModel(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
}
