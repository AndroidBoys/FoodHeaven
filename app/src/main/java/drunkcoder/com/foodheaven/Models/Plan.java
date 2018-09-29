package drunkcoder.com.foodheaven.Models;

import java.io.Serializable;

public class Plan implements Serializable {
    public String planName;
    public String description;
    public String planImageUrl;
    public String noOfDays;
    public String oneTimePrice;
    public String twoTimePrice;
    public String threeTimePrice;
    public boolean includesBreakFast;
    public boolean includesLunch;
    public boolean includesDinner;

    public Plan() {
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanImageUrl() {
        return planImageUrl;
    }

    public void setPlanImageUrl(String planImageUrl) {
        this.planImageUrl = planImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Plan(String planName, String description, String planImageUrl) {

        this.planName = planName;
        this.description = description;
        this.planImageUrl = planImageUrl;
    }

    public String getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(String noOfDays) {
        this.noOfDays = noOfDays;
    }

    public boolean isIncludesBreakFast() {
        return includesBreakFast;
    }

    public void setIncludesBreakFast(boolean includesBreakFast) {
        this.includesBreakFast = includesBreakFast;
    }

    public boolean isIncludesLunch() {
        return includesLunch;
    }

    public void setIncludesLunch(boolean includesLunch) {
        this.includesLunch = includesLunch;
    }

    public boolean isIncludesDinner() {
        return includesDinner;
    }

    public void setIncludesDinner(boolean includesDinner) {
        this.includesDinner = includesDinner;
    }

    public String getOneTimePrice() {
        return oneTimePrice;
    }

    public void setOneTimePrice(String oneTimePrice) {
        this.oneTimePrice = oneTimePrice;
    }

    public String getTwoTimePrice() {
        return twoTimePrice;
    }

    public void setTwoTimePrice(String twoTimePrice) {
        this.twoTimePrice = twoTimePrice;
    }

    public String getThreeTimePrice() {
        return threeTimePrice;
    }

    public void setThreeTimePrice(String threeTimePrice) {
        this.threeTimePrice = threeTimePrice;
    }
}
