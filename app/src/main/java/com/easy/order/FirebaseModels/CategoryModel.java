package com.easy.order.FirebaseModels;

public class CategoryModel {
    private String CategoryImage, CategoryName;

    public CategoryModel() {
    }

    public CategoryModel(String categoryImage, String categoryName) {
        CategoryImage = categoryImage;
        CategoryName = categoryName;
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
}
