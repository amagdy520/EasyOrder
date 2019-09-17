package com.easy.order.FirebaseModels;

public class ClientCategoryModel {

    private String CategoryName;

    public ClientCategoryModel() {
    }

    public ClientCategoryModel(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
}
