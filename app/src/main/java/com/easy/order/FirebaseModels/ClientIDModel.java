package com.easy.order.FirebaseModels;

public class ClientIDModel {
    private String ClientID;

    public ClientIDModel() {
    }

    public ClientIDModel(String clientID) {
        ClientID = clientID;
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }
}
