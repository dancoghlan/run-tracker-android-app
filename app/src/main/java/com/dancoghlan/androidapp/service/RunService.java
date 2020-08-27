package com.dancoghlan.androidapp.service;

import android.database.Cursor;

import com.dancoghlan.androidapp.model.RunContext;

import java.util.List;

public interface RunService {

    void insert(RunContext runContext);

    RunContext get(long id);

    List<RunContext> getAll();

    Cursor getAllAsCursor();

    void delete(long id);

    int update(long id, RunContext runContext);

    void truncate();

}
