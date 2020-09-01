package com.dancoghlan.androidapp.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dancoghlan.androidapp.R;
import com.dancoghlan.androidapp.model.Pace;
import com.dancoghlan.androidapp.model.RunContext;
import com.dancoghlan.androidapp.rest.service.RunRestService;
import com.dancoghlan.androidapp.rest.service.RunRestServiceImpl;
import com.dancoghlan.androidapp.view.DistancePickerDialog;
import com.dancoghlan.androidapp.view.MyTimePickerDialogWithSeconds;
import com.dancoghlan.androidapp.view.TimePicker;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.springframework.web.client.ResourceAccessException;

import java.util.Calendar;

import static com.dancoghlan.androidapp.util.DateUtils.formatDuration;
import static com.dancoghlan.androidapp.util.DateUtils.timeToDuration;
import static com.dancoghlan.androidapp.util.GeneralUtils.calculatePace;
import static com.dancoghlan.androidapp.util.GeneralUtils.createConnectivityFailureDialog;
import static com.dancoghlan.androidapp.util.ProjectConstants.ERROR_EMPTY_FIELD;

public class AddRunActivity extends AppCompatActivity {
    private static final String CONNECTIVITY_FAILED = "failed";
    private TextView dateView;
    private EditText titleEditText;
    private Button timeButton, distanceButton, paceButton;
    private int day, month, year, hourOfDay, minute, second, kilometer = 0, metre = 00;
    private LocalDate date;
    private Duration time;
    private Pace pace;
    private Double distance;

    private MyTimePickerDialogWithSeconds.OnTimeSetListener timeSetListener = new MyTimePickerDialogWithSeconds.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds) {
            showTime(timeButton, hourOfDay, minute, seconds);
            setPace();
        }
    };

    private DatePickerDialog.OnDateSetListener myDateListener = (datePicker, year, month, day) -> showDate(year, month + 1, day);

    private DistancePickerDialog.OnDistanceSetListener distanceSetListener = (view, kilometers, meters) -> {
        showDistance(kilometers, meters);
        setPace();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_run);

        // Get views
        this.timeButton = findViewById(R.id.btn_time);
        this.distanceButton = findViewById(R.id.btn_distance);
        this.paceButton = findViewById(R.id.btn_pace);
        this.titleEditText = findViewById(R.id.input_title);

        this.titleEditText.setText("My Run");

        displayTodayDate();

        // Create dialog for distance value
        this.distanceButton.setOnClickListener(view -> {
            this.distanceButton.setError(null);
            new DistancePickerDialog(view.getContext(), distanceSetListener, kilometer, metre).show();
            setPace();
        });

        // Add on click listeners for buttons
        this.timeButton.setOnClickListener(view -> {
            this.timeButton.setError(null);
            new MyTimePickerDialogWithSeconds(view.getContext(), timeSetListener, hourOfDay, minute, second).show();
            setPace();
        });
        this.paceButton.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Pace is calculated from Time and Distance", Toast.LENGTH_SHORT).show();
        });
        this.dateView.setOnClickListener(view -> {
            this.paceButton.setError(null);
            new DatePickerDialog(view.getContext(), myDateListener, year, month, day).show();
        });

        // Submit button
        ExtendedFloatingActionButton floatingActionButton = findViewById(R.id.btn_submit_run);
        floatingActionButton.setOnClickListener(view -> submitInput());
    }

    private void submitInput() {
        // Check for empty fields
        boolean emptyFields = false;

        // Title
        String title = titleEditText.getText().toString();
        if (StringUtils.isEmpty(title)) {
            emptyFields = true;
            titleEditText.setError(ERROR_EMPTY_FIELD);
        }
        // Description
        EditText descriptionEditText = findViewById(R.id.input_description);
        String description = descriptionEditText.getText().toString();
        // Date
        if (this.date == null) {
            emptyFields = true;
            this.dateView.setError(ERROR_EMPTY_FIELD);
        }
        // Time
        if (this.time == null) {
            emptyFields = true;
            this.timeButton.setError(ERROR_EMPTY_FIELD);
        }
        // Distance
        if (this.distance == null) {
            emptyFields = true;
            this.distanceButton.setError(ERROR_EMPTY_FIELD);
        }

        if (!emptyFields) {
            setPace();
            // Save run to DB
            RunContext run = new RunContext.Builder()
                    .setTitle(title)
                    .setDescription(description)
                    .setDate(this.date)
                    .setTime(this.time)
                    .setDistance(this.distance)
                    .setPace(this.pace)
                    .build();
            saveRun(run);
        }
    }

    private void saveRun(RunContext run) {
        new RestAsyncTask().execute(run);
    }

    private void displayTodayDate() {
        // Set today's date on Date view
        this.dateView = findViewById(R.id.input_date);
        Calendar calendar = Calendar.getInstance();
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(this.year, this.month + 1, this.day);
    }

    private void showDistance(int kilometer, int distance) {
        String distanceStr = String.format("%d.%02d", kilometer, distance);
        this.distance = Double.valueOf(distanceStr);
        this.distanceButton.setText(distanceStr.concat("km"));
    }

    private void showTime(Button button, int hourOfDay, int minute, int seconds) {
        this.time = timeToDuration(hourOfDay, minute, seconds);
        button.setText(formatDuration(this.time));
    }

    private void showDate(int year, int month, int day) {
        String dateStr = new StringBuilder()
                .append(day < 10 ? "0" + day : day)
                .append("/")
                .append(month < 10 ? "0" + month : month)
                .append("/")
                .append(year)
                .toString();
        this.date = LocalDate.parse(dateStr, DateTimeFormat.forPattern("dd/MM/yyyy"));
        this.dateView.setText(dateStr);
    }

    private void setPace() {
        Pace calculatedPace = calculatePace(this.time, this.distance);
        if (calculatedPace != null) {
            this.pace = calculatedPace;
            this.paceButton.setText(this.pace.toString().concat("/km"));
        }
    }

    private class RestAsyncTask extends AsyncTask<RunContext, String, String> {
        private ProgressDialog progressDialog;
        private RunRestService runRestService;

        @Override
        protected String doInBackground(RunContext... params) {
            publishProgress("Saving...");
            RunContext run = params[0];
            try {
                runRestService.save(run);
                return StringUtils.EMPTY;
            } catch (ResourceAccessException e) {
                return CONNECTIVITY_FAILED;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals(CONNECTIVITY_FAILED)) {
                createConnectivityFailureDialog(AddRunActivity.this).show();
            } else {
                // Close activity once run saved to DB
                Intent intent = new Intent(AddRunActivity.this, MainActivity.class);
                Toast.makeText(getApplicationContext(), "Saved run!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finishAffinity();
            }
            this.progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            this.progressDialog = ProgressDialog.show(AddRunActivity.this, "Saving", "Saving run...");
            this.runRestService = new RunRestServiceImpl();
        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

}
