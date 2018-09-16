package drunkcoder.com.foodheaven.Models;

import java.util.ArrayList;

public class Order {
    public User user;
    public int status;
    public ArrayList<Food> foodArrayList=new ArrayList<>();

    public Order() {
    }

    public User getUser() {

        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<Food> getFoodArrayList() {
        return foodArrayList;
    }

    public void setFoodArrayList(ArrayList<Food> foodArrayList) {
        this.foodArrayList = foodArrayList;
    }

    public Order(User user, int status, ArrayList<Food> foodArrayList) {

        this.user = user;
        this.status = status;
        this.foodArrayList = foodArrayList;
    }
}
