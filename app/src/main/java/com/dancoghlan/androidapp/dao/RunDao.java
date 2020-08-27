package com.dancoghlan.androidapp.dao;

import android.database.Cursor;

import com.dancoghlan.androidapp.model.RunContext;

import java.util.List;

public interface RunDao {

    void insert(RunContext runContext);

    long getCount();

    RunContext get(long id);

    List<RunContext> getAll();

    Cursor getAllAsCursor();

    void delete(long id);

    int update(long id, RunContext runContext);

    void truncate();

}
