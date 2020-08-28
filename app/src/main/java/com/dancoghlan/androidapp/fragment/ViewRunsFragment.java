package com.dancoghlan.androidapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.fragment.app.Fragment;

import com.dancoghlan.androidapp.R;
import com.dancoghlan.androidapp.activity.ViewRunActivity;
import com.dancoghlan.androidapp.adapter.ViewRunsCursorAdaptor;
import com.dancoghlan.androidapp.database.dao.RunPersistenceDao;
import com.dancoghlan.androidapp.database.dao.SQLiteRunPersistenceDao;
import com.dancoghlan.androidapp.database.DBManager;
import com.dancoghlan.androidapp.database.SQLiteDBManager;
import com.dancoghlan.androidapp.model.RunContext;
import com.dancoghlan.androidapp.model.mapper.RunContextMapper;
import com.dancoghlan.androidapp.database.service.RunPersistenceService;
import com.dancoghlan.androidapp.database.service.RunPersistenceServiceImpl;
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
    private ListView listView;
    boolean runsLoaded;

    private static String[] FROM = new String[]{_ID, TITLE, DATE, TIME, DISTANCE, PACE};
    private static final int[] TO = new int[]{R.id.list_id, R.id.list_title, R.id.list_date, R.id.list_time, R.id.list_distance, R.id.list_pace};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_runs, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        listView = view.findViewById(R.id.list);
    }

    /**
     * Ensures DB is only queried when tab is first selected by user.
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !runsLoaded) {
            new PersistenceAsyncTask().execute();
            runsLoaded = true;
        }
    }

    private class PersistenceAsyncTask extends AsyncTask<String, String, Cursor> {
        private ProgressDialog progressDialog;
        private RunPersistenceService runPersistenceService;
        private DBManager dbManager;

        @Override
        protected Cursor doInBackground(String... params) {
            publishProgress("Loading...");
            return runPersistenceService.getAllAsCursor();
        }

        @Override
        protected void onPostExecute(Cursor cursorResult) {
            this.progressDialog.dismiss();

            if (this.dbManager.isOpen()) {
                this.dbManager.close();
            }

            // Load runs from DB into listView
            adapter = new ViewRunsCursorAdaptor(getContext(), R.layout.list_view_runs, cursorResult, FROM, TO, 0);
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
        protected void onPreExecute() {
            this.progressDialog = ProgressDialog.show(getContext(), "Loading", "Loading runs...");

            // Setup persistence classes
            this.dbManager = new SQLiteDBManager(getContext());
            RunPersistenceDao runPersistenceDao = new SQLiteRunPersistenceDao(dbManager);
            this.runPersistenceService = new RunPersistenceServiceImpl(runPersistenceDao);

            // Open DB
            this.dbManager.open();
        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

}
