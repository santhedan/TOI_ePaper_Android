package com.dandekar.epaper.model;

import java.util.List;

/**
 * Created by sanjay_dandekar on 02/10/14.
 */
public class GetDayIndexTaskOutputEvent {

    private List<Page> pages;

    public GetDayIndexTaskOutputEvent(List<Page> pages) {
        this.pages = pages;
    }

    public List<Page> getPages() {
        return pages;
    }
}
