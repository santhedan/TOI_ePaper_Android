package com.dandekar.epaper.model;

import java.io.Serializable;

/**
 * Created by sanjay_dandekar on 02/10/14.
 */
public class Edition implements Serializable {

    private String editionName;

    private String editionId;

    private int color;

    public Edition(String editionName, String editionId, int color) {
        this.editionName = editionName;
        this.editionId = editionId;
        this.color = color;
    }

    public String getEditionName() {
        return editionName;
    }

    public String getEditionId() {
        return editionId;
    }

    public int getColor() {
        return  this.color;
    }
}
