package com.dandekar.epaper.utility;

import com.squareup.otto.Bus;

/**
 * Created by sanjay_dandekar on 02/10/14.
 */
public class MyBus {

    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }
}
