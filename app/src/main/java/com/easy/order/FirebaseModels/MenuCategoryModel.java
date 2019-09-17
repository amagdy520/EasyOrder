package com.easy.order.FirebaseModels;

public class MenuCategoryModel {
    private String CategoryName;

    public MenuCategoryModel() {
    }

    public MenuCategoryModel(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
}
