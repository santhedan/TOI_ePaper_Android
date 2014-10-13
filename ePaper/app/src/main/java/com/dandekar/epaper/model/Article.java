package com.dandekar.epaper.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by sanjay_dandekar on 02/10/14.
 */
public class Article implements Serializable {

    private String name;

    private String title;

    private String body;

    private String imageUrl;

    private Bitmap articleImage;

    public Article(String name, String title, String body, String imageUrl) {
        this.name = name;
        this.title = title;
        this.body = body;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Bitmap getArticleImage() {
        return articleImage;
    }

    public void setArticleImage(Bitmap articleImage) {
        this.articleImage = articleImage;
    }

    @Override
    public String toString() {
        return "Article{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
