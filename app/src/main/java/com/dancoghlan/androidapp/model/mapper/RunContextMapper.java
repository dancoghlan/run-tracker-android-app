package com.dancoghlan.androidapp.model.mapper;

import android.database.Cursor;

import com.dancoghlan.androidapp.model.RunContext;

import org.joda.time.LocalDate;

public class RunContextMapper implements Mapper<RunContext> {

    @Override
    public RunContext map(Cursor cursor) {
        long id = cursor.getLong(0);
        String title = cursor.getString(1);
        String description = cursor.getString(2);
        LocalDate date = null;
        String time = cursor.getString(4);
        double distance = cursor.getDouble(4);
        String pace = cursor.getString(6);

        return new RunContext.Builder()
                .setId(id)
                .setTitle(title)
                .setDescription(description)
                .setDate(date)
                .setTime(time)
                .setDistance(distance)
                .setPace(pace)
                .build();
    }

}
