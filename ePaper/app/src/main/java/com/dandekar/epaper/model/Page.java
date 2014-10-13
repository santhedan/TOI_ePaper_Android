package com.dandekar.epaper.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sanjay_dandekar on 02/10/14.
 */
public class Page implements Serializable {

    private String name;

    private String title;

    private String thumbnailUrl;

    private List<Article> articles;

    private transient Bitmap thumbnailImage;

    public Page(String name, String title, String thumbnailUrl, List<Article> articles, Bitmap thumbnailImage) {
        this.name = name;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.articles = articles;
        this.thumbnailImage = thumbnailImage;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public Bitmap getThumbnailImage() {
        return thumbnailImage;
    }

    @Override
    public String toString() {
        return "Page{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                '}';
    }
}
