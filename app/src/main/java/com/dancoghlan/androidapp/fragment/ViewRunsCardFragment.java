package com.dancoghlan.androidapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dancoghlan.androidapp.R;
import com.dancoghlan.androidapp.activity.ViewRunActivity;
import com.dancoghlan.androidapp.adapter.ViewRunsCardRecyclerViewAdapter;
import com.dancoghlan.androidapp.database.DBManager;
import com.dancoghlan.androidapp.database.SQLiteDBManager;
import com.dancoghlan.androidapp.database.dao.RunPersistenceDao;
import com.dancoghlan.androidapp.database.dao.SQLiteRunPersistenceDao;
import com.dancoghlan.androidapp.database.service.RunPersistenceService;
import com.dancoghlan.androidapp.database.service.RunPersistenceServiceImpl;
import com.dancoghlan.androidapp.model.RunContext;
import com.google.gson.Gson;

import java.util.List;

import static com.dancoghlan.androidapp.util.ProjectConstants.RUN_KEY;

public class ViewRunsCardFragment extends Fragment {
    private RunPersistenceService runPersistenceService;
    private DBManager dbManager;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_runs_card, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        // Setup persistence classes
        this.dbManager = new SQLiteDBManager(getContext());
        RunPersistenceDao runPersistenceDao = new SQLiteRunPersistenceDao(dbManager);
        this.runPersistenceService = new RunPersistenceServiceImpl(runPersistenceDao);

        // Open DB
        this.dbManager.open();

        // Load runs from DB into listView
        List<RunContext> runContexts = runPersistenceService.getAll();

        recyclerView = view.findViewById(R.id.recyclerView);
        ViewRunsCardRecyclerViewAdapter recyclerViewAdapter = new ViewRunsCardRecyclerViewAdapter(runContexts);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter.setOnItemClickListener(runContext -> {
            if (runContext != null) {
                // Open new activity when item selected
                Intent intent = new Intent(getContext(), ViewRunActivity.class);
                intent.putExtra(RUN_KEY, new Gson().toJson(runContext));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //this.dbManager.close();
    }

    private class PersistenceAsyncTask extends AsyncTask<String, String, Cursor> {
        private ProgressDialog progressDialog;

        @Override
        protected Cursor doInBackground(String... params) {
            publishProgress("Loading...");
            return null;
        }

        @Override
        protected void onPostExecute(Cursor cursorResult) {
            this.progressDialog.dismiss();


        }

        @Override
        protected void onPreExecute() {
            this.progressDialog = ProgressDialog.show(getContext(), "Loading", "Loading runs...");
        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

}
