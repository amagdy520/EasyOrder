package com.easy.order.FirebaseModels;

public class ClientsModel {
    private String
            ClientImageCover,
            ClientName,
            ClientAvailability,
            ClientImageProfile,
            Username,
            ClientCategory;

    public ClientsModel() {
    }

    public ClientsModel(String clientImageCover, String clientName, String clientAvailability, String clientImageProfile, String username, String clientCategory) {
        ClientImageCover = clientImageCover;
        ClientName = clientName;
        ClientAvailability = clientAvailability;
        ClientImageProfile = clientImageProfile;
        Username = username;
        ClientCategory = clientCategory;
    }

    public String getClientImageCover() {
        return ClientImageCover;
    }

    public void setClientImageCover(String clientImageCover) {
        ClientImageCover = clientImageCover;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getClientAvailability() {
        return ClientAvailability;
    }

    public void setClientAvailability(String clientAvailability) {
        ClientAvailability = clientAvailability;
    }

    public String getClientImageProfile() {
        return ClientImageProfile;
    }

    public void setClientImageProfile(String clientImageProfile) {
        ClientImageProfile = clientImageProfile;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getClientCategory() {
        return ClientCategory;
    }

    public void setClientCategory(String clientCategory) {
        ClientCategory = clientCategory;
    }
}
