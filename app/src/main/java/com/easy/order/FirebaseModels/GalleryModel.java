package com.easy.order.FirebaseModels;

public class GalleryModel {
    private String Image;

    public GalleryModel() {
    }

    public GalleryModel(String image) {
        Image = image;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
