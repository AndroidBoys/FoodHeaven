package drunkcoder.com.foodheaven.Models;

import java.io.Serializable;

public class OurPlans implements Serializable {
    public String packName,
                    description,
                    packImageUrl;

    public OurPlans() {
    }

    public String getPackName() {
        return packName;

    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPackImageUrl() {
        return packImageUrl;
    }

    public void setPackImageUrl(String packImageUrl) {
        this.packImageUrl = packImageUrl;
    }

    public OurPlans(String packName, String description, String packImageUrl) {

        this.packName = packName;
        this.description = description;
        this.packImageUrl = packImageUrl;
    }
}
