package com.dancoghlan.androidapp.model.mapper;

import android.database.Cursor;

public interface Mapper<T> {

    T map(Cursor cursor);

}
