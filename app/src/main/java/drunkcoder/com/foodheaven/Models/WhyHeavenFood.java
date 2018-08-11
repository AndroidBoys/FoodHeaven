package drunkcoder.com.foodheaven.Models;

public class WhyHeavenFood {
    public String about1,about2,image1,image2;

    public String getAbout1() {
        return about1;
    }

    public void setAbout1(String about1) {
        this.about1 = about1;
    }

    public String getAbout2() {
        return about2;
    }

    public WhyHeavenFood() {
    }

    public void setAbout2(String about2) {
        this.about2 = about2;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public WhyHeavenFood(String about1, String about2, String image1, String image2) {

        this.about1 = about1;
        this.about2 = about2;
        this.image1 = image1;
        this.image2 = image2;
    }
}
