package com.example.foodin.HelperClass;

public class CartHelperClass {
    String foodImage, foodName, restaurant, foodPrice, foodQuantity;
    public CartHelperClass() {
    }

    public CartHelperClass(String foodImage, String foodName, String restaurant, String foodPrice, String foodQuantity) {
        this.foodImage = foodImage;
        this.foodName = foodName;
        this.restaurant = restaurant;
        this.foodPrice = foodPrice;
        this.foodQuantity = foodQuantity;
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

}
