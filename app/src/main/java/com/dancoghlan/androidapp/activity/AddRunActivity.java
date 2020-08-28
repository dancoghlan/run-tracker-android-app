package com.dancoghlan.androidapp.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dancoghlan.androidapp.R;
import com.dancoghlan.androidapp.database.DBManager;
import com.dancoghlan.androidapp.database.SQLiteDBManager;
import com.dancoghlan.androidapp.database.dao.RunPersistenceDao;
import com.dancoghlan.androidapp.database.dao.SQLiteRunPersistenceDao;
import com.dancoghlan.androidapp.database.service.RunPersistenceService;
import com.dancoghlan.androidapp.database.service.RunPersistenceServiceImpl;
import com.dancoghlan.androidapp.model.RunContext;
import com.dancoghlan.androidapp.view.MyTimePickerDialogNoSeconds;
import com.dancoghlan.androidapp.view.MyTimePickerDialogWithSeconds;
import com.dancoghlan.androidapp.view.TimePicker;
import com.dancoghlan.androidapp.view.TimePickerWithSeconds;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.Calendar;
import java.util.List;

public class AddRunActivity extends AppCompatActivity {
    private RunPersistenceService runPersistenceService;
    private DBManager dbManager;
    private TextView dateView;
    private Button timeButton, distanceButton, paceButton;
    private int day, month, year, hourOfDay, minute, second;
    private LocalDate date;
    private String time, pace;
    private double distance;

    private MyTimePickerDialogWithSeconds.OnTimeSetListener timeSetListener = new MyTimePickerDialogWithSeconds.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds) {
            showTime(timeButton, hourOfDay, minute, seconds);
        }
    };

    private MyTimePickerDialogNoSeconds.OnTimeSetListener paceSetListener = new MyTimePickerDialogNoSeconds.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePickerWithSeconds view, int hourOfDay, int minute) {
            showTime(paceButton, hourOfDay, minute);
        }
    };

    private DatePickerDialog.OnDateSetListener myDateListener = (datePicker, year, month, day) -> showDate(year, month + 1, day);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_run);

        // Setup persistence classes
        this.dbManager = new SQLiteDBManager(AddRunActivity.this);
        RunPersistenceDao runPersistenceDao = new SQLiteRunPersistenceDao(dbManager);
        this.runPersistenceService = new RunPersistenceServiceImpl(runPersistenceDao);

        // Open DB
        this.dbManager.open();

        // Get views
        this.timeButton = findViewById(R.id.btn_time);
        this.distanceButton = findViewById(R.id.btn_distance);
        this.paceButton = findViewById(R.id.btn_pace);

        EditText titleEditText = findViewById(R.id.input_title);
        long count = runPersistenceService.getCount();
        titleEditText.setText("Run " + ++count);

        // Set today's date on Date view
        this.dateView = findViewById(R.id.input_date);
        Calendar calendar = Calendar.getInstance();
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(this.year, this.month + 1, this.day);

        // Create dialog for distance value
        distanceButton.setOnClickListener(view -> {
            LayoutInflater layoutInflater = AddRunActivity.this.getLayoutInflater();
            View promptsView = layoutInflater.inflate(R.layout.alert_dialog, null);
            final EditText userInput = promptsView.findViewById(R.id.input_user_input);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
            alertDialogBuilder.setView(promptsView);
            alertDialogBuilder.setTitle("Set Distance");
            AlertDialog alertDialog = alertDialogBuilder
                    .setPositiveButton("Save", (dialog, id) -> {
                                String input = userInput.getText().toString();
                                if (StringUtils.isNotEmpty(input)) {
                                    this.distance = Double.parseDouble(input);
                                    this.distanceButton.setText(distance + "km");
                                }
                            }
                    )
                    .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel()).create();
            alertDialog.show();
        });

        // Add on click listeners for buttons
        this.timeButton.setOnClickListener(view -> new MyTimePickerDialogWithSeconds(view.getContext(), timeSetListener, hourOfDay, minute, second).show());
        this.paceButton.setOnClickListener(view -> new MyTimePickerDialogNoSeconds(view.getContext(), paceSetListener, hourOfDay, minute).show());
        this.dateView.setOnClickListener(view -> new DatePickerDialog(view.getContext(), myDateListener, year, month, day).show());

        // Submit button
        ExtendedFloatingActionButton floatingActionButton = findViewById(R.id.btn_submit_run);
        floatingActionButton.setOnClickListener(view -> {
            EditText descriptionEditText = findViewById(R.id.input_description);

            // Save run to DB
            RunContext runContext = new RunContext.Builder()
                    .setTitle(titleEditText.getText().toString())
                    .setDescription(descriptionEditText.getText().toString())
                    .setDate(this.date)
                    .setTime(this.time)
                    .setDistance(this.distance)
                    .setPace(this.pace)
                    .build();
            new PersistenceAsyncTask().execute(runContext);
        });
    }

    private void showTime(Button button, int hourOfDay, int minute, int seconds) {
        this.time = new StringBuilder()
                .append(hourOfDay)
                .append(":")
                .append(minute < 10 ? "0" + minute : minute)
                .append(":")
                .append(seconds < 10 ? "0" + seconds : seconds)
                .toString();
        button.setText(this.time);
    }

    private void showTime(Button button, int hourOfDay, int minute) {
        this.pace = new StringBuilder()
                .append(hourOfDay)
                .append(":")
                .append(minute < 10 ? "0" + minute : minute)
                .toString();
        button.setText(this.pace.concat("/km"));
    }

    private void showDate(int year, int month, int day) {
        String dateStr = new StringBuilder()
                .append(day < 10 ? "0" + day : day)
                .append("/")
                .append(month < 10 ? "0" + month : month)
                .append("/")
                .append(year)
                .toString();
        date = LocalDate.parse(dateStr, DateTimeFormat.forPattern("dd/MM/yyyy"));
        dateView.setText(dateStr);
    }

    private void alertEmptyFields(List<String> emptyFields) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Empty Fields");
        alertDialog.setMessage("The following mandatory fields are empty:\n" + emptyFields);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    private class PersistenceAsyncTask extends AsyncTask<RunContext, String, String> {
        private String resp;
        private ProgressDialog progressDialog;

        @Override
        protected String doInBackground(RunContext... params) {
            publishProgress("Saving...");
            runPersistenceService.insert(params[0]);
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            this.progressDialog.dismiss();

            // Close DB
            if (dbManager.isOpen()) {
                dbManager.close();
            }

            // Close activity once run saved to DB
            Intent intent = new Intent(AddRunActivity.this, MainActivity.class);
            Toast.makeText(getApplicationContext(), "Saved run!", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        }

        @Override
        protected void onPreExecute() {
            this.progressDialog = ProgressDialog.show(AddRunActivity.this, "Saving", "Saving run...");
        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

}
