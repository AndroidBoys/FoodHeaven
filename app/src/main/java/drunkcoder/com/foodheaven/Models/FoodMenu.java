package drunkcoder.com.foodheaven.Models;

public class FoodMenu {
    public String imageUrl;
    public String foodName;
    public String foodDescription;


    public FoodMenu() {
    }

    public FoodMenu(String imageUrl, String foodName, String foodDescription) {
        this.imageUrl = imageUrl;
        this.foodName = foodName;
        this.foodDescription = foodDescription;
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

}