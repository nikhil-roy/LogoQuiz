
package net.nikhilroy.logoquiz.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogoQuizModel {

    @SerializedName("imgUrl")
    @Expose
    private String imgUrl;
    @SerializedName("name")
    @Expose
    private String name;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
