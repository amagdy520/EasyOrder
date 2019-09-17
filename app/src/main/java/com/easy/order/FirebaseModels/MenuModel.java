package com.easy.order.FirebaseModels;

public class MenuModel {
    private String Category, CategoryImage, CategoryName, CategoryPrice;

    public MenuModel() {
    }

    public MenuModel(String category, String categoryImage, String categoryName, String categoryPrice) {
        Category = category;
        CategoryImage = categoryImage;
        CategoryName = categoryName;
        CategoryPrice = categoryPrice;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getCategoryImage() {
        return CategoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        CategoryImage = categoryImage;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryPrice() {
        return CategoryPrice;
    }

    public void setCategoryPrice(String categoryPrice) {
        CategoryPrice = categoryPrice;
    }
}
