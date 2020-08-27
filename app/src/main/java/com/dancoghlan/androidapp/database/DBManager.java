package com.dancoghlan.androidapp.database;

import android.database.sqlite.SQLiteDatabase;

public interface DBManager {

    SQLiteDatabase getDatabase();

    DBManager open();

    void close();

    boolean isOpen();

}
