package com.boat.app.boatapp;

/**
 * Created by rvoor on 3-4-2018.
 */

class Data {
    private String name;
    private String status;

    public Data(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}
