package com.easy.order.FirebaseModels;

public class SponsorModel {

    private String Image, User;

    public SponsorModel(String image, String user) {
        Image = image;
        User = user;
    }

    public SponsorModel() {
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }
}
