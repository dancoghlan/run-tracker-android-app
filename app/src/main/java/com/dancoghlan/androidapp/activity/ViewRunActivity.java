package com.dancoghlan.androidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dancoghlan.androidapp.R;
import com.dancoghlan.androidapp.database.DBManager;
import com.dancoghlan.androidapp.database.SQLiteDBManager;
import com.dancoghlan.androidapp.database.dao.RunPersistenceDao;
import com.dancoghlan.androidapp.database.dao.SQLiteRunPersistenceDao;
import com.dancoghlan.androidapp.database.service.RunPersistenceService;
import com.dancoghlan.androidapp.database.service.RunPersistenceServiceImpl;
import com.dancoghlan.androidapp.model.RunContext;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;

import static com.dancoghlan.androidapp.util.ProjectConstants.RUN_KEY;

public class ViewRunActivity extends AppCompatActivity {
    private RunPersistenceService runPersistenceService;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_run);

        this.dbManager = new SQLiteDBManager(this);
        RunPersistenceDao runPersistenceDao = new SQLiteRunPersistenceDao(dbManager);
        this.runPersistenceService = new RunPersistenceServiceImpl(runPersistenceDao);

        // Open DB
        this.dbManager.open();

        // Get RunContext from previous activity
        Intent intent = getIntent();
        String json = intent.getStringExtra(RUN_KEY);
        RunContext runContext = new Gson().fromJson(json, RunContext.class);

        // Get views
        TextView textViewTitle = findViewById(R.id.view_title);
        TextView textViewDescription = findViewById(R.id.view_description);
        TextView textViewDate = findViewById(R.id.view_date);
        TextView textViewTime = findViewById(R.id.view_time);
        TextView textViewDistance = findViewById(R.id.view_distance);
        TextView textViewPace = findViewById(R.id.view_pace);

        // Load RunContext values into corresponding views
        textViewTitle.setText(runContext.getTitle());
        textViewDescription.setText(runContext.getDescription());
        textViewDate.setText(null);
        textViewTime.setText(runContext.getTime());
        textViewDistance.setText(String.valueOf(runContext.getDistance()));
        textViewPace.setText(runContext.getPace());

        // Floating button to create new run
        ExtendedFloatingActionButton floatingActionButton = findViewById(R.id.btn_delete_run);
        floatingActionButton.setOnClickListener(v -> {
            runPersistenceService.delete(runContext.getId());
            Toast.makeText(getApplicationContext(), "Deleted run!", Toast.LENGTH_SHORT).show();
            Intent newIntent = new Intent(ViewRunActivity.this, MainActivity.class);
            startActivity(newIntent);
            finishAffinity();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //this.dbManager.close();
    }

}
