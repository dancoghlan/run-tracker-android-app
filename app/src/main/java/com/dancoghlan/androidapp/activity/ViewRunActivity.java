package com.dancoghlan.androidapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dancoghlan.androidapp.R;
import com.dancoghlan.androidapp.model.RunContext;
import com.dancoghlan.androidapp.rest.service.RunRestService;
import com.dancoghlan.androidapp.rest.service.RunRestServiceImpl;
import com.dancoghlan.androidapp.util.RunContextObjectMapper;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import static com.dancoghlan.androidapp.util.ProjectConstants.RUN_KEY;

public class ViewRunActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_run);

        // Get RunContext from previous activity
        Intent intent = getIntent();
        String json = intent.getStringExtra(RUN_KEY);
        RunContext runContext = new RunContextObjectMapper().jsonToRunContext(json);

        // Get views
        TextView textViewId = findViewById(R.id.view_id);
        TextView textViewTitle = findViewById(R.id.view_title);
        TextView textViewDescription = findViewById(R.id.view_description);
        TextView textViewDate = findViewById(R.id.view_date);
        TextView textViewTime = findViewById(R.id.view_time);
        TextView textViewDistance = findViewById(R.id.view_distance);
        TextView textViewPace = findViewById(R.id.view_pace);

        // Load RunContext values into corresponding views
        textViewId.setText(String.valueOf(runContext.getId()));
        textViewTitle.setText(runContext.getTitle());
        textViewDescription.setText(runContext.getDescription());
        textViewDate.setText(null);
        textViewTime.setText(runContext.getTimeAsString());
        textViewDistance.setText(String.valueOf(runContext.getDistance()));
        textViewPace.setText(runContext.getPace().toString());

        // Floating button to create new run
        ExtendedFloatingActionButton floatingActionButton = findViewById(R.id.btn_delete_run);
        floatingActionButton.setOnClickListener(v -> deleteRun(runContext.getId()));
    }

    private void deleteRun(Long id) {
        new DeleteRunAsyncTask().execute(id);
    }

    private class DeleteRunAsyncTask extends AsyncTask<Long, String, String> {
        private ProgressDialog progressDialog;
        private RunRestService runRestService;

        @Override
        protected String doInBackground(Long... params) {
            publishProgress("Loading...");
            runRestService.delete(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // Create tab layout
            Toast.makeText(getApplicationContext(), "Deleted run!", Toast.LENGTH_SHORT).show();
            Intent newIntent = new Intent(ViewRunActivity.this, MainActivity.class);
            startActivity(newIntent);
            finishAffinity();
            this.progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            this.progressDialog = ProgressDialog.show(ViewRunActivity.this, "Loading", "Loading runs...");
            this.runRestService = new RunRestServiceImpl();
        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

}
