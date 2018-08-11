package drunkcoder.com.foodheaven.Models;

public class TodayMenu {
    public String imageUrl;
    public String foodName;
    public String foodDescription;
    public String foodQuantity;

    public TodayMenu() {
    }

    public TodayMenu(String imageUrl, String foodName, String foodDescription, String foodQuantity) {
        this.imageUrl = imageUrl;
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.foodQuantity = foodQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public String getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(String foodQuantity) {
        this.foodQuantity = foodQuantity;
    }
}
