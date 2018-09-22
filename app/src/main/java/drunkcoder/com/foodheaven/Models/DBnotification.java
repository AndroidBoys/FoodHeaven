package drunkcoder.com.foodheaven.Models;

import java.util.Map;

public class DBnotification {

    public String deadline;
    public Map<String,String> timeStamp;
    public String mealTime;

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public Map<String, String> getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Map<String, String> timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMealTime() {
        return mealTime;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }

    public DBnotification() {

    }

    public DBnotification(String deadline, Map<String, String> timeStamp, String mealTime) {

        this.deadline = deadline;
        this.timeStamp = timeStamp;
        this.mealTime = mealTime;
    }
}
