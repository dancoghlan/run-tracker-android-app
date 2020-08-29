package com.dancoghlan.androidapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dancoghlan.androidapp.R;
import com.dancoghlan.androidapp.activity.ViewRunActivity;
import com.dancoghlan.androidapp.adapter.ScrollableRecyclerViewAdapter;
import com.dancoghlan.androidapp.database.DBManager;
import com.dancoghlan.androidapp.database.SQLiteDBManager;
import com.dancoghlan.androidapp.database.dao.RunPersistenceDao;
import com.dancoghlan.androidapp.database.dao.SQLiteRunPersistenceDao;
import com.dancoghlan.androidapp.database.service.RunPersistenceService;
import com.dancoghlan.androidapp.database.service.RunPersistenceServiceImpl;
import com.dancoghlan.androidapp.decorator.LinePagerIndicatorDecoration;
import com.dancoghlan.androidapp.model.RunContext;
import com.dancoghlan.androidapp.util.DateUtils;
import com.google.gson.Gson;

import org.joda.time.Duration;

import java.text.DecimalFormat;
import java.util.List;

import static com.dancoghlan.androidapp.util.ProjectConstants.RUN_KEY;

public class HomeFragment extends Fragment {
    private RunPersistenceService runPersistenceService;
    private DBManager dbManager;
    private ScrollableRecyclerViewAdapter scrollableAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        // Setup persistence classes
        this.dbManager = new SQLiteDBManager(getContext());
        RunPersistenceDao runPersistenceDao = new SQLiteRunPersistenceDao(dbManager);
        this.runPersistenceService = new RunPersistenceServiceImpl(runPersistenceDao);

        // Open DB
        this.dbManager.open();

        // Set total runs
        long totalRuns = runPersistenceService.getCount();
        TextView totalRunsTextView = view.findViewById(R.id.view_total_runs);
        totalRunsTextView.setText(String.valueOf(totalRuns));

        // Set total distance
        TextView totalDistanceTextView = view.findViewById(R.id.view_total_distance);
        Double totalDistance = runPersistenceService.getTotalDistance();
        if (totalDistance != null) {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String totalDistanceValue = decimalFormat.format(totalDistance).concat("km");
            totalDistanceTextView.setText(totalDistanceValue);
        }

        // Set total time
        TextView totalTimeTextView = view.findViewById(R.id.view_total_time);
        Duration totalTime = runPersistenceService.getTotalTime();
        if (totalTime != null) {
            String totalTimeStr = DateUtils.durationToString(totalTime);
            totalTimeTextView.setText(totalTimeStr);
        }


        // Get all runs from DB
        List<RunContext> runContexts = runPersistenceService.getAll();

        // Load runs into scrollable list
        RecyclerView recyclerView = view.findViewById(R.id.rv_recent_runs);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        scrollableAdapter = new ScrollableRecyclerViewAdapter(getContext(), runContexts);
        scrollableAdapter.setClickListener((view1, position) -> {
            // Open new activity when item selected
            RunContext runContext = scrollableAdapter.getItem(position);
            Intent intent = new Intent(getContext(), ViewRunActivity.class);
            intent.putExtra(RUN_KEY, new Gson().toJson(runContext));
            startActivity(intent);
        });
        recyclerView.setAdapter(scrollableAdapter);
        recyclerView.addItemDecoration(new LinePagerIndicatorDecoration());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //this.dbManager.close();
    }

}
