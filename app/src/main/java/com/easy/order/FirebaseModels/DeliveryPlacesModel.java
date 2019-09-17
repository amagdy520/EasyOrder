package com.easy.order.FirebaseModels;

public class DeliveryPlacesModel {

    private String Place;

    public DeliveryPlacesModel() {
    }

    public DeliveryPlacesModel(String place) {
        Place = place;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }
}
