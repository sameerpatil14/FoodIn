package com.example.foodin.HomeAdapter;

public class SuggestedHelperClass {


    String suggestedImage, suggestedPrice, suggestedTitle, suggestedDesc;

    public SuggestedHelperClass() {
    }

    public SuggestedHelperClass(String suggestedImage, String suggestedPrice, String suggestedTitle, String suggestedDesc) {
        this.suggestedImage = suggestedImage;
        this.suggestedPrice = suggestedPrice;
        this.suggestedTitle = suggestedTitle;
        this.suggestedDesc = suggestedDesc;
    }

    public String getSuggestedImage() {
        return suggestedImage;
    }

    public void setSuggestedImage(String suggestedImage) {
        this.suggestedImage = suggestedImage;
    }

    public String getSuggestedPrice() {
        return suggestedPrice;
    }

    public void setSuggestedPrice(String suggestedPrice) {
        this.suggestedPrice = suggestedPrice;
    }

    public String getSuggestedTitle() {
        return suggestedTitle;
    }

    public void setSuggestedTitle(String suggestedTitle) {
        this.suggestedTitle = suggestedTitle;
    }

    public String getSuggestedDesc() {
        return suggestedDesc;
    }

    public void setSuggestedDesc(String suggestedDesc) {
        this.suggestedDesc = suggestedDesc;
    }
}
