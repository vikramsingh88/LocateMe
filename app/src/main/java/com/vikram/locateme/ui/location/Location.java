package com.vikram.locateme.ui.location;

/**
 * Created by M1032130 on 7/3/2017.
 */

public class Location {
    private String latlon;
    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getLatlon() {
        return latlon;
    }

    public void setLatlon(String latlon) {
        this.latlon = latlon;
    }
}
