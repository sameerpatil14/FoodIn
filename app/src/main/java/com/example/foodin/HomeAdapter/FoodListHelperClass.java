package com.example.foodin.HomeAdapter;

public class FoodListHelperClass {

    String foodName, restaurant, foodPrice, foodImage;

    public FoodListHelperClass() {
    }

    public FoodListHelperClass(String foodName, String restaurant, String foodPrice, String foodImage) {
        this.foodName = foodName;
        this.restaurant = restaurant;
        this.foodPrice = foodPrice;
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

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }
}
