package drunkcoder.com.foodheaven.Models;

public class Assistance {
    String center,phoneNo;

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Assistance() {

    }

    public Assistance(String center, String phoneNo) {

        this.center = center;
        this.phoneNo = phoneNo;
    }
}
