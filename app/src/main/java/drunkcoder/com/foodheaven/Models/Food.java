package drunkcoder.com.foodheaven.Models;

public class Food {

    public String imageUrl;
    public String foodName;
    public String foodDescription;

    public Food() {

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

    public Food(String imageUrl, String foodName, String foodDescription) {
        this.imageUrl = imageUrl;
        this.foodName = foodName;
        this.foodDescription = foodDescription;
    }
}
