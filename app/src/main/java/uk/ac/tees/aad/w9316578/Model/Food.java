package uk.ac.tees.aad.w9316578.Model;

import java.io.Serializable;

public class Food implements Serializable {
    private String date, foodId;
    private String foodName, foodPrice;
    private String foodDesc, foodImageUri;


    public Food() {
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodDesc() {
        return foodDesc;
    }

    public void setFoodDesc(String foodDesc) {
        this.foodDesc = foodDesc;
    }

    public String getFoodImageUri() {
        return foodImageUri;
    }

    public void setFoodImageUri(String foodImageUri) {
        this.foodImageUri = foodImageUri;
    }


    public Food(String date, String foodId, String foodName, String foodPrice, String foodDesc, String foodImageUri) {
        this.date = date;
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodDesc = foodDesc;
        this.foodImageUri = foodImageUri;
    }
}
