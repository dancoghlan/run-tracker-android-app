package com.dancoghlan.androidapp.model;

import org.chalup.microorm.annotations.Column;
import org.joda.time.LocalDate;

import static com.dancoghlan.androidapp.database.DatabaseHelper.DATE;
import static com.dancoghlan.androidapp.database.DatabaseHelper.DESCRIPTION;
import static com.dancoghlan.androidapp.database.DatabaseHelper.DISTANCE;
import static com.dancoghlan.androidapp.database.DatabaseHelper.PACE;
import static com.dancoghlan.androidapp.database.DatabaseHelper.TIME;
import static com.dancoghlan.androidapp.database.DatabaseHelper.TITLE;
import static com.dancoghlan.androidapp.database.DatabaseHelper._ID;

public class RunContext {
    @Column(_ID)
    private final long id;
    @Column(TITLE)
    private final String title;
    @Column(DESCRIPTION)
    private final String description;
    @Column(DATE)
    private final LocalDate date;
    @Column(TIME)
    private final String time;
    @Column(DISTANCE)
    private final double distance;
    @Column(PACE)
    private final String pace;

    private RunContext(long id, String title, String description, LocalDate date, String time, double distance, String pace) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.distance = distance;
        this.pace = pace;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public double getDistance() {
        return distance;
    }

    public String getPace() {
        return pace;
    }

    @Override
    public String toString() {
        return "RunContext{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", time='" + time + '\'' +
                ", distance='" + distance + '\'' +
                ", pace='" + pace + '\'' +
                '}';
    }

    public static class Builder {
        private long id;
        private String title;
        private String description;
        private LocalDate date;
        private String time;
        private double distance;
        private String pace;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder setTime(String time) {
            this.time = time;
            return this;
        }

        public Builder setDistance(double distance) {
            this.distance = distance;
            return this;
        }

        public Builder setPace(String pace) {
            this.pace = pace;
            return this;
        }

        public RunContext build() {
            return new RunContext(id, title, description, date, time, distance, pace);
        }

    }

}
