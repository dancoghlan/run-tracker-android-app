package com.dancoghlan.androidapp.fragment;

import android.content.Intent;
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
import com.dancoghlan.androidapp.model.RunContext;
import com.dancoghlan.androidapp.util.RunContextObjectMapper;

import java.util.List;

import static com.dancoghlan.androidapp.util.GeneralUtils.getRunContexts;
import static com.dancoghlan.androidapp.util.ProjectConstants.RUN_CONTEXTS_KEY;
import static com.dancoghlan.androidapp.util.ProjectConstants.RUN_KEY;

public class ViewRunsCardFragment extends Fragment {
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_runs_card, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        this.recyclerView = view.findViewById(R.id.recyclerView);

        String runContextJson = getArguments().getString(RUN_CONTEXTS_KEY);
        List<RunContext> runContexts = getRunContexts(runContextJson);

        // Load runs from DB into listView
        ViewRunsCardRecyclerViewAdapter recyclerViewAdapter = new ViewRunsCardRecyclerViewAdapter(runContexts);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter.setOnItemClickListener(runContext -> {
            if (runContext != null) {
                // Open new activity when item selected
                Intent intent = new Intent(getContext(), ViewRunActivity.class);
                intent.putExtra(RUN_KEY, new RunContextObjectMapper().writeValueAsString(runContext));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);
    }

}
