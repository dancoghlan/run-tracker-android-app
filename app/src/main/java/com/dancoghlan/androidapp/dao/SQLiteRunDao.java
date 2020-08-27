package com.dancoghlan.androidapp.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.dancoghlan.androidapp.database.DBManager;
import com.dancoghlan.androidapp.model.RunContext;

import org.chalup.microorm.MicroOrm;

import java.util.List;

import static com.dancoghlan.androidapp.database.DatabaseHelper.DATE;
import static com.dancoghlan.androidapp.database.DatabaseHelper.DESCRIPTION;
import static com.dancoghlan.androidapp.database.DatabaseHelper.DISTANCE;
import static com.dancoghlan.androidapp.database.DatabaseHelper.PACE;
import static com.dancoghlan.androidapp.database.DatabaseHelper.TABLE_NAME;
import static com.dancoghlan.androidapp.database.DatabaseHelper.TIME;
import static com.dancoghlan.androidapp.database.DatabaseHelper.TITLE;
import static com.dancoghlan.androidapp.database.DatabaseHelper._ID;

public class SQLiteRunDao implements RunDao {
    private final DBManager dbManager;

    public SQLiteRunDao(DBManager dbManager) {
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

        SQLiteDatabase database = this.dbManager.getDatabase();
        String[] columns = new String[]{_ID, TITLE, DESCRIPTION, DATE, TIME, DISTANCE, PACE};
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return null;
    }

    @Override
    public Cursor getAllAsCursor() {
        checkDBOpen();

        SQLiteDatabase database = this.dbManager.getDatabase();
        String[] columns = new String[]{_ID, TITLE, DESCRIPTION, DATE, TIME, DISTANCE, PACE};
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    @Override
    public RunContext get(long id) {
        checkDBOpen();

        SQLiteDatabase database = this.dbManager.getDatabase();
        String query = "SELECT * FROM LIST WHERE _id = " + id;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            MicroOrm microOrm = new MicroOrm();
            RunContext runContext = microOrm.fromCursor(cursor, RunContext.class);
            return runContext;
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
        database.delete(TABLE_NAME, _ID + "=" + id, null);
    }

    @Override
    public void truncate() {
        checkDBOpen();

        SQLiteDatabase database = this.dbManager.getDatabase();
        database.execSQL("DELETE FROM " + TABLE_NAME);
        database.delete(TABLE_NAME, null, null);
    }

    private void checkDBOpen() {
        if (!this.dbManager.isOpen()) {
            throw new RuntimeException("DB not open");
        }
    }

}
