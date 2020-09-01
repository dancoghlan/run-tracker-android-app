package com.dancoghlan.androidapp.model.mapper;

import android.database.Cursor;

import com.dancoghlan.androidapp.model.Pace;
import com.dancoghlan.androidapp.model.RunContext;

import org.joda.time.Duration;
import org.joda.time.LocalDate;

import static com.dancoghlan.androidapp.util.DateUtils.timeToDuration;

public class RunContextMapper implements Mapper<RunContext> {

    @Override
    public RunContext map(Cursor cursor) {
        long id = cursor.getLong(0);
        String title = cursor.getString(1);
        String description = cursor.getString(2);
        LocalDate date = null;
        String time = cursor.getString(4);
        Duration timeDuration = timeToDuration(time);
        double distance = cursor.getDouble(5);
        String pace = cursor.getString(6);
        Pace pacePojo = new Pace(pace);
        return new RunContext.Builder()
                .setId(id)
                .setTitle(title)
                .setDescription(description)
                .setDate(date)
                .setTime(timeDuration)
                .setDistance(distance)
                .setPace(pacePojo)
                .build();
    }

}
