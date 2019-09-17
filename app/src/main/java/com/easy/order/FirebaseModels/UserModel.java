package com.easy.order.FirebaseModels;

public class UserModel {

    private String Address, Email, Name, ProfilePicture;

    public UserModel() {
    }

    public UserModel(String address, String email, String name, String profilePicture) {
        Address = address;
        Email = email;
        Name = name;
        ProfilePicture = profilePicture;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        ProfilePicture = profilePicture;
    }
}
