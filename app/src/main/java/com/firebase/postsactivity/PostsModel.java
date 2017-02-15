package com.firebase.postsactivity;

import android.widget.ImageView;

/**
 * Created by user on 24-09-2016.
 */
public class PostsModel {

    private String title,imgUrl,description,postedtime;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostedtime() {
        return postedtime;
    }

    public void setPostedtime(String postedtime) {
        this.postedtime = postedtime;
    }
}
