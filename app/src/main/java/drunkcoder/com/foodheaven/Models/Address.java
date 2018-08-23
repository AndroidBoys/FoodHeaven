package drunkcoder.com.foodheaven.Models;

import java.io.Serializable;

public class Address implements Serializable {
    public  String address;
    public  String longitude;
    public String latitude;

    public Address() {
    }

    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Address(String address, String longitude, String latitude) {

        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
