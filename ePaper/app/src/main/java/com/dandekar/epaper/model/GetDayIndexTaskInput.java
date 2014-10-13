package com.dandekar.epaper.model;

/**
 * Created by sanjay_dandekar on 02/10/14.
 */
public class GetDayIndexTaskInput {

    private String edition;

    private String cityName;

    private String date;

    public GetDayIndexTaskInput(String edition, String cityName, String date) {
        this.edition = edition;
        this.cityName = cityName;
        this.date = date;
    }

    public String getEdition() {
        return edition;
    }

    public String getCityName() {
        return cityName;
    }

    public String getDate() {
        return date;
    }
}
