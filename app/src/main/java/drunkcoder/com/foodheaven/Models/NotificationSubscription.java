package drunkcoder.com.foodheaven.Models;

public class NotificationSubscription {

    public String token;
    public boolean subscribedToBreakFast;
    public boolean subscribedToLunch;
    public boolean subscribedToDinner;

    public NotificationSubscription() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isSubscribedToBreakFast() {
        return subscribedToBreakFast;
    }

    public void setSubscribedToBreakFast(boolean subscribedToBreakFast) {
        this.subscribedToBreakFast = subscribedToBreakFast;
    }

    public boolean isSubscribedToLunch() {
        return subscribedToLunch;
    }

    public void setSubscribedToLunch(boolean subscribedToLunch) {
        this.subscribedToLunch = subscribedToLunch;
    }

    public boolean isSubscribedToDinner() {
        return subscribedToDinner;
    }

    public void setSubscribedToDinner(boolean subscribedToDinner) {
        this.subscribedToDinner = subscribedToDinner;
    }
}
