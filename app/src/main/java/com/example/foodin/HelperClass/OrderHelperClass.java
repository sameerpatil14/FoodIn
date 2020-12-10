package com.example.foodin.HelperClass;

public class OrderHelperClass {
    String foodImage, foodName, restaurant, foodPrice, foodQuantity, foodDeliveryAddress, foodDeliveryPhoneno, foodDeliveryStatus, currentTimeMillis;

    public OrderHelperClass() {
    }

    public OrderHelperClass(String foodImage, String foodName, String restaurant, String foodPrice, String foodQuantity, String foodDeliveryAddress, String foodDeliveryPhoneno, String foodDeliveryStatus, String currentTimeMillis) {
        this.foodImage = foodImage;
        this.foodName = foodName;
        this.restaurant = restaurant;
        this.foodPrice = foodPrice;
        this.foodQuantity = foodQuantity;
        this.foodDeliveryAddress = foodDeliveryAddress;
        this.foodDeliveryPhoneno = foodDeliveryPhoneno;
        this.foodDeliveryStatus = foodDeliveryStatus;
        this.currentTimeMillis = currentTimeMillis;
    }

    public String getCurrentTimeMillis() {
        return currentTimeMillis;
    }

    public void setCurrentTimeMillis(String currentTimeMillis) {
        this.currentTimeMillis = currentTimeMillis;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(String foodQuantity) {
        this.foodQuantity = foodQuantity;
    }

    public String getFoodDeliveryAddress() {
        return foodDeliveryAddress;
    }

    public void setFoodDeliveryAddress(String foodDeliveryAddress) {
        this.foodDeliveryAddress = foodDeliveryAddress;
    }

    public String getFoodDeliveryPhoneno() {
        return foodDeliveryPhoneno;
    }

    public void setFoodDeliveryPhoneno(String foodDeliveryPhoneno) {
        this.foodDeliveryPhoneno = foodDeliveryPhoneno;
    }

    public String getFoodDeliveryStatus() {
        return foodDeliveryStatus;
    }

    public void setFoodDeliveryStatus(String foodDeliveryStatus) {
        this.foodDeliveryStatus = foodDeliveryStatus;
    }
}
