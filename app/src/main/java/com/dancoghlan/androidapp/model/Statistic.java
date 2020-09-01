package com.dancoghlan.androidapp.model;

public class Statistic {
    private final String label;
    private final String value;
    private final int imageId;

    public Statistic(String label, String value, int imageId) {
        this.label = label;
        this.value = value;
        this.imageId = imageId;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public int getImageId() {
        return imageId;
    }

}
