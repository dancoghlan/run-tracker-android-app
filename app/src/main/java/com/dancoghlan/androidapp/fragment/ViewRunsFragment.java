package com.dancoghlan.androidapp.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.fragment.app.Fragment;

import com.dancoghlan.androidapp.R;
import com.dancoghlan.androidapp.activity.ViewRunActivity;
import com.dancoghlan.androidapp.dao.RunDao;
import com.dancoghlan.androidapp.dao.SQLiteRunDao;
import com.dancoghlan.androidapp.database.DBManager;
import com.dancoghlan.androidapp.database.SQLiteDBManager;
import com.dancoghlan.androidapp.model.RunContext;
import com.dancoghlan.androidapp.model.mapper.RunContextMapper;
import com.dancoghlan.androidapp.service.RunService;
import com.dancoghlan.androidapp.service.RunServiceImpl;
import com.google.gson.Gson;

import static com.dancoghlan.androidapp.database.DatabaseHelper.DATE;
import static com.dancoghlan.androidapp.database.DatabaseHelper.DISTANCE;
import static com.dancoghlan.androidapp.database.DatabaseHelper.PACE;
import static com.dancoghlan.androidapp.database.DatabaseHelper.TIME;
import static com.dancoghlan.androidapp.database.DatabaseHelper.TITLE;
import static com.dancoghlan.androidapp.database.DatabaseHelper._ID;
import static com.dancoghlan.androidapp.util.ProjectConstants.RUN_KEY;


public class ViewRunsFragment extends Fragment {
    private SimpleCursorAdapter adapter;
    private RunService runService;
    private DBManager dbManager;

    private static String[] FROM = new String[]{_ID, TITLE, DATE, TIME, DISTANCE, PACE};
    private static final int[] TO = new int[]{R.id.list_id, R.id.list_title, R.id.list_date, R.id.list_time, R.id.list_distance, R.id.list_pace};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_runs, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        ListView listView = view.findViewById(R.id.list);

        // Setup persistence classes
        dbManager = new SQLiteDBManager(getContext());
        RunDao runDao = new SQLiteRunDao(dbManager);
        runService = new RunServiceImpl(runDao);

        // Open DB
        dbManager.open();
        // Get all runs from DB
        Cursor cursor = runService.getAllAsCursor();

        // Load runs from DB into listView
        adapter = new SimpleCursorAdapter(view.getContext(), R.layout.list_view_runs, cursor, FROM, TO, 0);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, v, position, id) -> {
            Cursor itemCursor = ((SimpleCursorAdapter) parent.getAdapter()).getCursor();
            itemCursor.moveToPosition(position);
            RunContext runContext = new RunContextMapper().map(itemCursor);
            if (runContext != null) {
                // Open new activity when item selected
                Intent intent = new Intent(getContext(), ViewRunActivity.class);
                intent.putExtra(RUN_KEY, new Gson().toJson(runContext));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Close DB
        if (this.dbManager.isOpen()) {
            this.dbManager.close();
        }
    }

}
