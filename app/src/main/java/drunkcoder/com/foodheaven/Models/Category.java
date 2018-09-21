package drunkcoder.com.foodheaven.Models;

import java.util.ArrayList;

public class Category {
    private ArrayList<Food> foodArrayList=new ArrayList<>();
    private String categoryName;
    private int maxSelect;

    public Category() {
    }

    public ArrayList<Food> getFoodArrayList() {

        return foodArrayList;
    }

    public void setFoodArrayList(ArrayList<Food> foodArrayList) {
        this.foodArrayList = foodArrayList;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getMaxSelect() {
        return maxSelect;
    }

    public void setMaxSelect(int maxSelect) {
        this.maxSelect = maxSelect;
    }

    public Category(ArrayList<Food> foodArrayList, String categoryName, int maxSelect) {

        this.foodArrayList = foodArrayList;
        this.categoryName = categoryName;
        this.maxSelect = maxSelect;
    }
}
