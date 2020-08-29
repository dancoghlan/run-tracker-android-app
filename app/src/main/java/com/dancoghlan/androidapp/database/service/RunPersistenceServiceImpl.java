package com.dancoghlan.androidapp.database.service;

import android.database.Cursor;

import com.dancoghlan.androidapp.database.dao.RunPersistenceDao;
import com.dancoghlan.androidapp.model.RunContext;

import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.List;

import static com.dancoghlan.androidapp.database.DatabaseHelper.DISTANCE;

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
    public Double getTotalDistance() {
        return runPersistenceDao.getSumDouble(DISTANCE);
    }

    @Override
    public Duration getTotalTime() {
        List<String> times = runPersistenceDao.getTimes();
        Duration totalDuration = new Duration(Duration.ZERO);
        if (CollectionUtils.isNotEmpty(times)) {
            for (String timeStr : times) {
                PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
                        .appendHours()
                        .appendSeparator(":")
                        .appendMinutes()
                        .appendSeparator(":")
                        .appendSeconds()
                        .toFormatter();
                Period period = periodFormatter.parsePeriod(timeStr);
                totalDuration = totalDuration.plus(period.toStandardDuration());
            }
        }
        return totalDuration;
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
