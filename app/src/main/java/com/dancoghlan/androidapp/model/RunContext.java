package com.dancoghlan.androidapp.model;

import com.dancoghlan.androidapp.rest.serialize.DurationDeserializer;
import com.dancoghlan.androidapp.rest.serialize.DurationSerializer;
import com.dancoghlan.androidapp.rest.serialize.PaceDeserializer;
import com.dancoghlan.androidapp.rest.serialize.PaceSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateSerializer;

import org.joda.time.Duration;
import org.joda.time.LocalDate;

import static com.dancoghlan.androidapp.util.DateUtils.formatDuration;

public class RunContext {
    private long id;
    private String title;
    private String description;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;
    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration time;
    private double distance;
    @JsonSerialize(using = PaceSerializer.class)
    @JsonDeserialize(using = PaceDeserializer.class)
    private Pace pace;

    public RunContext() {

    }

    public RunContext(long id, String title, String description, LocalDate date, Duration time, double distance, Pace pace) {
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

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Duration getTime() {
        return time;
    }

    public void setTime(Duration time) {
        this.time = time;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Pace getPace() {
        return pace;
    }

    public void setPace(Pace pace) {
        this.pace = pace;
    }

    @JsonIgnore
    public String getTimeAsString() {
        return formatDuration(time);
    }

    @Override
    public String toString() {
        return "RunContext{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", distance=" + distance +
                ", pace=" + pace +
                '}';
    }

    public static class Builder {
        private long id;
        private String title;
        private String description;
        private LocalDate date;
        private Duration time;
        private double distance;
        private Pace pace;

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

        public Builder setTime(Duration time) {
            this.time = time;
            return this;
        }

        public Builder setDistance(double distance) {
            this.distance = distance;
            return this;
        }

        public Builder setPace(Pace pace) {
            this.pace = pace;
            return this;
        }

        public RunContext build() {
            return new RunContext(id, title, description, date, time, distance, pace);
        }
    }

}
