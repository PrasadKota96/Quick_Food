package uk.ac.tees.aad.w9316578.Model;

public class CartFood {
    int id;
    private String date, foodId;
    private String foodName, foodPrice;
    private String foodDesc, foodImageUri,foodItems;

    public String getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(String foodItems) {
        this.foodItems = foodItems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
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

    public CartFood(int id, String date, String foodId, String foodName, String foodPrice, String foodDesc, String foodImageUri, String foodItems) {
        this.id = id;
        this.date = date;
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodDesc = foodDesc;
        this.foodImageUri = foodImageUri;
        this.foodItems = foodItems;
    }
}
