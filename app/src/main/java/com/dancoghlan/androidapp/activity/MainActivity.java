package com.dancoghlan.androidapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.dancoghlan.androidapp.R;
import com.dancoghlan.androidapp.adapter.MainActivityTabAdapter;
import com.dancoghlan.androidapp.model.RunContext;
import com.dancoghlan.androidapp.rest.service.RunRestService;
import com.dancoghlan.androidapp.rest.service.RunRestServiceImpl;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.springframework.web.client.ResourceAccessException;

import java.util.List;

import static com.dancoghlan.androidapp.util.GeneralUtils.createConnectivityFailureDialog;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get views
        this.tabLayout = findViewById(R.id.tabLayout);
        this.viewPager = findViewById(R.id.viewPager);

        // Execute AsyncTask to get runs from DB via REST
        loadRuns();

        // Floating button to create new run
        FloatingActionButton floatingActionButton = findViewById(R.id.btn_add_new_run);
        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddRunActivity.class);
            startActivity(intent);
        });
    }

    private void loadRuns() {
        new GetRunsAsyncTask().execute();
    }

    private class GetRunsAsyncTask extends AsyncTask<String, String, List<RunContext>> {
        private ProgressDialog progressDialog;
        private RunRestService runRestService;

        @Override
        protected List<RunContext> doInBackground(String... params) {
            publishProgress("Loading...");
//            boolean isConnectedToInternet = isConnectedToInternet((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
            try {
                return runRestService.getAll();
            } catch (ResourceAccessException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<RunContext> runContexts) {
            if (runContexts == null) {
                createConnectivityFailureDialog(MainActivity.this).show();
            }
            // Create tab layout
            final MainActivityTabAdapter adapter = new MainActivityTabAdapter(MainActivity.this,
                    getSupportFragmentManager(), tabLayout.getTabCount(), runContexts);
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            this.progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            this.progressDialog = ProgressDialog.show(MainActivity.this, "Loading", "Loading runs...");
            this.runRestService = new RunRestServiceImpl();
        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

}
