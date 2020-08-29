package com.dancoghlan.androidapp.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.dancoghlan.androidapp.database.DBManager;
import com.dancoghlan.androidapp.model.RunContext;
import com.dancoghlan.androidapp.model.mapper.RunContextMapper;

import java.util.ArrayList;
import java.util.List;

import static com.dancoghlan.androidapp.database.DatabaseHelper.DATE;
import static com.dancoghlan.androidapp.database.DatabaseHelper.DESCRIPTION;
import static com.dancoghlan.androidapp.database.DatabaseHelper.DISTANCE;
import static com.dancoghlan.androidapp.database.DatabaseHelper.PACE;
import static com.dancoghlan.androidapp.database.DatabaseHelper.SUM_DISTANCE;
import static com.dancoghlan.androidapp.database.DatabaseHelper.TABLE_NAME;
import static com.dancoghlan.androidapp.database.DatabaseHelper.TIME;
import static com.dancoghlan.androidapp.database.DatabaseHelper.TITLE;
import static com.dancoghlan.androidapp.database.DatabaseHelper._ID;

public class SQLiteRunPersistenceDao implements RunPersistenceDao {
    private final DBManager dbManager;

    public SQLiteRunPersistenceDao(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public void insert(RunContext runContext) {
        checkDBOpen();

        SQLiteDatabase database = dbManager.getDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(TITLE, runContext.getTitle());
        contentValue.put(DESCRIPTION, runContext.getDescription());
        contentValue.put(DATE, runContext.getDate().toString());
        contentValue.put(TIME, runContext.getTime());
        contentValue.put(DISTANCE, runContext.getDistance());
        contentValue.put(PACE, runContext.getPace());
        database.insert(TABLE_NAME, null, contentValue);
    }

    @Override
    public long getCount() {
        checkDBOpen();
        SQLiteDatabase database = this.dbManager.getDatabase();
        return DatabaseUtils.queryNumEntries(database, TABLE_NAME);
    }

    @Override
    public List<RunContext> getAll() {
        checkDBOpen();
        Cursor cursor = getAllAsCursor();
        List<RunContext> runContexts = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                runContexts.add(new RunContextMapper().map(cursor));
            }
        }
        return runContexts;
    }

    @Override
    public Cursor getAllAsCursor() {
        checkDBOpen();
        SQLiteDatabase database = this.dbManager.getDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY _id DESC;";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    @Override
    public RunContext get(long id) {
        checkDBOpen();

        SQLiteDatabase database = this.dbManager.getDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE _id = " + id;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return new RunContextMapper().map(cursor);
        }
        return null;
    }

    @Override
    public RunContext getLast() {
        checkDBOpen();
        SQLiteDatabase database = this.dbManager.getDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY _id DESC LIMIT 1;";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return new RunContextMapper().map(cursor);
        }
        return null;
    }

    @Override
    public Cursor getLastCursor() {
        checkDBOpen();
        SQLiteDatabase database = this.dbManager.getDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY _id DESC LIMIT 1;";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor;
        }
        return null;
    }

    @Override
    public List<String> getTimes() {
        checkDBOpen();
        SQLiteDatabase database = this.dbManager.getDatabase();
        String query = "SELECT " + TIME + " FROM " + TABLE_NAME;
        Cursor cursor = database.rawQuery(query, null);
        List<String> times = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                times.add(cursor.getString(0));
            }
        }
        return times;
    }

    @Override
    public Double getSumDouble(String columnName) {
        checkDBOpen();
        SQLiteDatabase database = this.dbManager.getDatabase();
        String query = "SELECT SUM(" + columnName + ") as " + SUM_DISTANCE + " FROM " + TABLE_NAME;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getDouble(0);
        }
        return null;
    }

    @Override
    public int update(long id, RunContext runContext) {
        checkDBOpen();
        SQLiteDatabase database = this.dbManager.getDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, runContext.getTitle());
        contentValues.put(DESCRIPTION, runContext.getDescription());
        contentValues.put(DATE, runContext.getDate().toString());
        contentValues.put(TIME, runContext.getTitle());
        contentValues.put(DISTANCE, runContext.getDistance());
        contentValues.put(PACE, runContext.getPace());
        int i = database.update(TABLE_NAME, contentValues, _ID + " = " + id, null);
        return i;
    }

    @Override
    public void delete(long id) {
        checkDBOpen();
        SQLiteDatabase database = this.dbManager.getDatabase();
        String where = "_id = ?";
        String[] whereArgs = { Long.toString(id) };
        database.delete(TABLE_NAME, where, whereArgs);
        dbManager.close();
    }

    @Override
    public void truncate() {
        checkDBOpen();
        SQLiteDatabase database = this.dbManager.getDatabase();
        database.delete(TABLE_NAME, null, null);
    }

    private void checkDBOpen() {
        if (!this.dbManager.isOpen()) {
            throw new RuntimeException("DB not open");
        }
    }

}
