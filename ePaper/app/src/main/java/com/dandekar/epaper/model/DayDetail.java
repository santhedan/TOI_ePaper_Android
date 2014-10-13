package com.dandekar.epaper.model;

/**
 * Created by sanjay_dandekar on 02/10/14.
 */
public class DayDetail {

    private String date;

    private String size;

    private String folderPath;

    public DayDetail(String date, String size, String folderPath) {
        this.date = date;
        this.size = size;
        this.folderPath = folderPath;
    }

    public String getDate() {
        return date;
    }

    public String getSize() {
        return size;
    }

    public String getFolderPath() {
        return folderPath;
    }
}
