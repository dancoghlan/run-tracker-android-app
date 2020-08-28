package com.dancoghlan.androidapp.database.service;

import android.database.Cursor;

import com.dancoghlan.androidapp.database.dao.RunPersistenceDao;
import com.dancoghlan.androidapp.model.RunContext;

import java.util.List;

public class RunPersistenceServiceImpl implements RunPersistenceService {
    private final RunPersistenceDao runPersistenceDao;

    public RunPersistenceServiceImpl(RunPersistenceDao runPersistenceDao) {
        this.runPersistenceDao = runPersistenceDao;
    }

    @Override
    public void insert(RunContext runContext) {
        runPersistenceDao.insert(runContext);
    }

    @Override
    public long getCount() {
        return runPersistenceDao.getCount();
    }

    @Override
    public RunContext get(long id) {
        return runPersistenceDao.get(id);
    }

    @Override
    public RunContext getLast() {
        return runPersistenceDao.getLast();
    }

    @Override
    public Cursor getLastCursor() {
        return runPersistenceDao.getLastCursor();
    }

    @Override
    public Double getSumDouble(String columnName) {
        return runPersistenceDao.getSumDouble(columnName);
    }

    @Override
    public List<RunContext> getAll() {
        return runPersistenceDao.getAll();
    }

    @Override
    public Cursor getAllAsCursor() {
        return runPersistenceDao.getAllAsCursor();
    }

    @Override
    public void delete(long id) {
        runPersistenceDao.delete(id);
    }

    @Override
    public int update(long id, RunContext runContext) {
        return runPersistenceDao.update(id, runContext);
    }

    @Override
    public void truncate() {
        runPersistenceDao.truncate();
    }

}
