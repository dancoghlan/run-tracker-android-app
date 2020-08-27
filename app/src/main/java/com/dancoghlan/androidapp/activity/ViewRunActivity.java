package com.dancoghlan.androidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dancoghlan.androidapp.R;
import com.dancoghlan.androidapp.model.RunContext;
import com.google.gson.Gson;

import static com.dancoghlan.androidapp.util.ProjectConstants.RUN_KEY;

public class ViewRunActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_run);

        // Get selected RunContext from previous activity
        Intent intent = getIntent();
        String json = intent.getStringExtra(RUN_KEY);
        RunContext runContext = new Gson().fromJson(json, RunContext.class);

        // Get views from layout
        TextView textViewTitle = findViewById(R.id.view_title);
        TextView textViewDescription = findViewById(R.id.view_description);
        TextView textViewDate = findViewById(R.id.view_date);
        TextView textViewTime = findViewById(R.id.view_time);
        TextView textViewDistance = findViewById(R.id.view_distance);
        TextView textViewPace = findViewById(R.id.view_pace);

        // Load Run Context values into corresponding views
        textViewTitle.setText(runContext.getTitle());
        textViewDescription.setText(runContext.getDescription());
        textViewDate.setText(null);
        textViewTime.setText(runContext.getTime());
        textViewDistance.setText(String.valueOf(runContext.getDistance()));
        textViewPace.setText(runContext.getPace());
    }

}
