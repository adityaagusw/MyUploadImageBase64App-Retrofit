package com.example.imageuploadapi;

import com.google.gson.annotations.SerializedName;

public class ImageClass {

    @SerializedName("title")
    private String Title;

    @SerializedName("image")
    private String Image;

    @SerializedName("response")
    private String Response;

    public String getResponse() {
        return Response;
    }

    /*
    public String getTitle() {
        return Title;
    }

    public String getImage() {
        return Image;
    }*/

}
