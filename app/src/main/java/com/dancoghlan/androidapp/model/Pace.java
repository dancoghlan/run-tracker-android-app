package com.dancoghlan.androidapp.model;

import org.joda.time.Hours;
import org.joda.time.Minutes;

public class Pace {
    private final Hours hours;
    private final Minutes minutes;

    public Pace(String time) {
        String[] units = time.split(":");
        this.hours = Hours.hours(Integer.valueOf(units[0]));
        this.minutes = Minutes.minutes(Integer.valueOf(units[1]));

    }

    public Pace(int hours, int minutes) {
        this.hours = Hours.hours(hours);
        this.minutes = Minutes.minutes(minutes);
    }

    public Pace(Hours hours, Minutes minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public Hours getHours() {
        return hours;
    }

    public Minutes getMinutes() {
        return minutes;
    }

    @Override
    public String toString() {
        return String.format("%d:%02d", hours.getHours(), minutes.getMinutes());
    }

}
