package drunkcoder.com.foodheaven.Models;

import java.util.ArrayList;

public class SpecialFoodOrder {

    public ArrayList<SpecialFood> specialFoodsArrayList;
    public String mealTime;

    public String getMealTime() {
        return mealTime;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }

    public SpecialFoodOrder(ArrayList<SpecialFood> specialFoodsArrayList, String mealTime) {
        this.specialFoodsArrayList = specialFoodsArrayList;
        this.mealTime = mealTime;
    }

    public SpecialFoodOrder() {
    }

    public ArrayList<SpecialFood> getSpecialFoodsArrayList() {
        return specialFoodsArrayList;
    }

    public void setSpecialFoodsArrayList(ArrayList<SpecialFood> specialFoodsArrayList) {
        this.specialFoodsArrayList = specialFoodsArrayList;
    }
}
