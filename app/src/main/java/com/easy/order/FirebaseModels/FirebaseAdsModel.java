package com.easy.order.FirebaseModels;

public class FirebaseAdsModel {
    private String CommercialId, CommercialImage, Users;

    public FirebaseAdsModel() {
    }

    public FirebaseAdsModel(String commercialId, String commercialImage, String users) {
        CommercialId = commercialId;
        CommercialImage = commercialImage;
        Users = users;
    }

    public String getCommercialId() {
        return CommercialId;
    }

    public void setCommercialId(String commercialId) {
        CommercialId = commercialId;
    }

    public String getCommercialImage() {
        return CommercialImage;
    }

    public void setCommercialImage(String commercialImage) {
        CommercialImage = commercialImage;
    }

    public String getUsers() {
        return Users;
    }

    public void setUsers(String users) {
        Users = users;
    }
}
