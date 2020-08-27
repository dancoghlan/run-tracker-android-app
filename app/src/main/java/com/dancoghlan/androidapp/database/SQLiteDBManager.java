package com.dancoghlan.androidapp.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteDBManager implements DBManager {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public SQLiteDBManager(Context context) {
        this.context = context;
    }

    @Override
    public DBManager open() throws SQLException {
        dbHelper = DatabaseHelper.getInstance(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    @Override
    public void close() {
        this.dbHelper.close();
    }

    @Override
    public SQLiteDatabase getDatabase() {
        return this.database;
    }

    @Override
    public boolean isOpen() {
        return this.database.isOpen();
    }

}
