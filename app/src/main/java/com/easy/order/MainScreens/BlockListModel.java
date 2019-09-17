package com.easy.order.MainScreens;

public class BlockListModel {

    private String Client;

    public BlockListModel(String client) {
        Client = client;
    }

    public BlockListModel() {
    }

    public String getClient() {
        return Client;
    }

    public void setClient(String client) {
        Client = client;
    }
}
