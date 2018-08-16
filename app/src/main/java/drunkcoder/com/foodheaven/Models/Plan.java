package drunkcoder.com.foodheaven.Models;

import java.io.Serializable;

public class Plan implements Serializable {
    public String planName;
    public String description;
    public String planImageUrl;
    public String noOfDays;
    public String singleTimePrice;
    public String frequencyPerDay;
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

    public String getSingleTimePrice() {
        return singleTimePrice;
    }

    public void setSingleTimePrice(String singleTimePrice) {
        this.singleTimePrice = singleTimePrice;
    }

    public String getFrequencyPerDay() {
        return frequencyPerDay;
    }

    public void setFrequencyPerDay(String frequencyPerDay) {
        this.frequencyPerDay = frequencyPerDay;
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
}
