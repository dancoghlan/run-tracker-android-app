package com.dancoghlan.androidapp.database.dao;

import android.database.Cursor;

import com.dancoghlan.androidapp.model.RunContext;

import java.util.List;

public interface RunPersistenceDao {

    void insert(RunContext runContext);

    long getCount();

    RunContext get(long id);

    RunContext getLast();

    Cursor getLastCursor();

    List<String> getTimes();

    Double getSumDouble(String columnName);

    List<RunContext> getAll();

    Cursor getAllAsCursor();

    void delete(long id);

    int update(long id, RunContext runContext);

    void truncate();

}
