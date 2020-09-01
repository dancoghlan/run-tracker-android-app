package com.dancoghlan.androidapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dancoghlan.androidapp.R;
import com.dancoghlan.androidapp.adapter.ViewRunsGridCardRecyclerViewAdapter;
import com.dancoghlan.androidapp.model.Pace;
import com.dancoghlan.androidapp.model.RunContext;
import com.dancoghlan.androidapp.model.Statistic;
import com.dancoghlan.androidapp.util.DateUtils;

import org.joda.time.Duration;

import java.util.ArrayList;
import java.util.List;

import static com.dancoghlan.androidapp.util.GeneralUtils.calculatePace;
import static com.dancoghlan.androidapp.util.GeneralUtils.getRunContexts;
import static com.dancoghlan.androidapp.util.ProjectConstants.RUN_CONTEXTS_KEY;

public class HomeFragment extends Fragment {
    private RecyclerView gridRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        // Get runContexts from main activity
        String runContextJson = getArguments().getString(RUN_CONTEXTS_KEY);
        List<RunContext> runContexts = getRunContexts(runContextJson);

        // Load statistics from runs
        List<Statistic> statistics = getStatistics(runContexts);

        // Add values to statistics gridView
        this.gridRecyclerView = view.findViewById(R.id.recyclerView);
        ViewRunsGridCardRecyclerViewAdapter gridCardRecyclerViewAdapter = new ViewRunsGridCardRecyclerViewAdapter(getContext(), statistics);
        gridCardRecyclerViewAdapter.setClickListener((position) -> {
            // Add onClick actions
        });
        this.gridRecyclerView.setAdapter(gridCardRecyclerViewAdapter);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        gridRecyclerView.setLayoutManager(manager);
    }

    private List<Statistic> getStatistics(List<RunContext> runContexts) {
        List<Statistic> statistics = new ArrayList<>();

        // Set total runs
        long totalRuns = getTotalRuns(runContexts);
        Statistic totalRunsStatistic = new Statistic("Total Runs", String.valueOf(totalRuns), R.drawable.running_24dp);
        statistics.add(totalRunsStatistic);

        // Set total distance
        Double totalDistance = getTotalDistance(runContexts);
        if (totalDistance != null) {
            Statistic totalDistanceStatistic = new Statistic("Total Distance", String.valueOf(totalDistance).concat("km"), R.drawable.distance_24dp);
            statistics.add(totalDistanceStatistic);
        }

        // Set total time
        Duration totalTime = getTotalTime(runContexts);
        if (totalTime != null) {
            String totalTimeStr = DateUtils.durationToString(totalTime);
            Statistic totalTimeStatistic = new Statistic("Total Time", totalTimeStr, R.drawable.clock_24dp);
            statistics.add(totalTimeStatistic);
        }

        // Set average pace
        Pace averagePace = calculatePace(totalTime, totalDistance);
        if (averagePace != null) {
            Statistic averagePaceStatistic = new Statistic("Average Pace", averagePace.toString().concat("/km"), R.drawable.timer_24dp);
            statistics.add(averagePaceStatistic);
        }

        return statistics;
    }

    private Double getTotalDistance(List<RunContext> runContexts) {
        double totalValue = 0;
        for (RunContext runContext : runContexts) {
            Double distance = runContext.getDistance();
            if (distance != null && distance > 0) {
                totalValue += distance;
            }
        }
        return totalValue;
    }

    private Duration getTotalTime(List<RunContext> runContexts) {
        Duration totalDuration = new Duration(Duration.ZERO);
        for (RunContext runContext : runContexts) {
            Duration time = runContext.getTime();
            if (time != null) {
                totalDuration = totalDuration.plus(time);
            }
        }
        return totalDuration;
    }

    private int getTotalRuns(List<RunContext> runContexts) {
        return runContexts.size();
    }

}
