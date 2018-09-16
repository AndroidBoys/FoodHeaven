package drunkcoder.com.foodheaven.Models;

import java.util.ArrayList;

public class Food{

    public String imageUrl;
    public String foodName;
    public String foodDescription;
    public boolean byDefault;//if true food will be available for all users
    public boolean marked; //this is required to maintain checkboxes inside expandable list view;

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

    public boolean isByDefault() {
        return byDefault;
    }

    public void setByDefault(boolean byDefault) {
        this.byDefault = byDefault;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public Food(String imageUrl, String foodName, String foodDescription, boolean byDefault, boolean marked) {

        this.imageUrl = imageUrl;
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.byDefault = byDefault;
        this.marked = marked;
    }
}
