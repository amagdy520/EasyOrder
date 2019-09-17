package com.easy.order.FirebaseModels;

public class TestModel {
    private String Username;

    public TestModel() {
    }

    public TestModel(String username) {
        Username = username;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
