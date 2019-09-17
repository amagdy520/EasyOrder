package com.easy.order.FirebaseModels;

public class OffersModel {

    private String OfferImage, OfferTitle, OfferContent, Client;

    public OffersModel() {
    }

    public OffersModel(String offerImage, String offerTitle, String offerContent, String client) {
        OfferImage = offerImage;
        OfferTitle = offerTitle;
        OfferContent = offerContent;
        Client = client;
    }

    public String getOfferImage() {
        return OfferImage;
    }

    public void setOfferImage(String offerImage) {
        OfferImage = offerImage;
    }

    public String getOfferTitle() {
        return OfferTitle;
    }

    public void setOfferTitle(String offerTitle) {
        OfferTitle = offerTitle;
    }

    public String getOfferContent() {
        return OfferContent;
    }

    public void setOfferContent(String offerContent) {
        OfferContent = offerContent;
    }

    public String getClient() {
        return Client;
    }

    public void setClient(String client) {
        Client = client;
    }
}
