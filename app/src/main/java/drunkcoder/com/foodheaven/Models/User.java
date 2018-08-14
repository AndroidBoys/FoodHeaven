package drunkcoder.com.foodheaven.Models;

import java.util.ArrayList;

public class User {

    public String email;
    public String phoneNumber;
    public String password;
    public boolean isSubscribed;
    public ArrayList<OurPlans> subscribePlan;

    public ArrayList<OurPlans> getSubscribePlan() {
        return subscribePlan;
    }

    public void setSubscribePlan(ArrayList<OurPlans> subscribePlan) {
        this.subscribePlan = subscribePlan;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }
}
