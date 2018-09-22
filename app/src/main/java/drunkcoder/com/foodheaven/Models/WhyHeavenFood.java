package drunkcoder.com.foodheaven.Models;

public class WhyHeavenFood {
    public String about,imageUrl;

    public WhyHeavenFood() {
    }

    public String getAbout() {

        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public WhyHeavenFood(String about, String imageUrl) {

        this.about = about;
        this.imageUrl = imageUrl;
    }
}
