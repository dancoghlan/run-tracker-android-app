package com.dancoghlan.androidapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper mInstance = null;

    public static final String TABLE_NAME = "RUN";
    public static final String DB_NAME = "RUNS.DB";
    public static final int DB_VERSION = 3;

    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String DISTANCE = "distance";
    public static final String PACE = "pace";

    public static final String SUM_DISTANCE = "sum_distance";

    private static final String CREATE_TABLE =
            "create table " + TABLE_NAME +
            "("
                + _ID +         " TEXT, "
                + TITLE +       " TEXT NOT NULL, "
                + DESCRIPTION + " TEXT, "
                + DATE +        " DATE NOT NULL, "
                + TIME +        " TEXT NOT NULL, "
                + DISTANCE +    " DOUBLE NOT NULL, "
                + PACE +        " TEXT NOT NULL" +
            ");";

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}