package com.dancoghlan.androidapp.util;

import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class DateUtils {

    public static Duration timeToDuration(String time) {
        PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
                .appendHours()
                .appendSeparator(":")
                .appendMinutes()
                .appendSeparator(":")
                .appendSeconds()
                .toFormatter();
        Period period = periodFormatter.parsePeriod(time);
        return period.toStandardDuration();
    }

    public static Duration timeToDuration(int hours, int mins, int secs) {
        String time = new StringBuilder()
                .append(hours)
                .append(":")
                .append(mins)
                .append(":")
                .append(secs)
                .toString();
        return timeToDuration(time);
    }

    public static String durationToString(Duration duration) {
        long seconds = duration.getStandardSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format("%dh %02dm", absSeconds / 3600, (absSeconds % 3600) / 60);
        return seconds < 0 ? "-" + positive : positive;
    }

    public static String formatDuration(Duration duration) {
        long seconds = duration.getStandardSeconds();
        long absSeconds = Math.abs(seconds);

        long hours = (absSeconds / 3600);
        long mins = ((absSeconds % 3600) / 60);
        long secs = (absSeconds % 60);

        return String.format("%d:%02d:%02d", hours, mins, secs);
    }

}
