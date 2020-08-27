package com.dancoghlan.androidapp.service;

import android.database.Cursor;

import com.dancoghlan.androidapp.dao.RunDao;
import com.dancoghlan.androidapp.model.RunContext;

import java.util.List;

public class RunServiceImpl implements RunService {
    private final RunDao runDao;

    public RunServiceImpl(RunDao runDao) {
        this.runDao = runDao;
    }

    @Override
    public void insert(RunContext runContext) {
        runDao.insert(runContext);
    }

    @Override
    public RunContext get(long id) {
        return runDao.get(id);
    }

    @Override
    public List<RunContext> getAll() {
        return runDao.getAll();
    }

    @Override
    public Cursor getAllAsCursor() {
        return runDao.getAllAsCursor();
    }

    @Override
    public void delete(long id) {
        runDao.delete(id);
    }

    @Override
    public int update(long id, RunContext runContext) {
        return runDao.update(id, runContext);
    }

    @Override
    public void truncate() {
        runDao.truncate();
    }

}
