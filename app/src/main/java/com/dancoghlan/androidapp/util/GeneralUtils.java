package com.dancoghlan.androidapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AlertDialog;

import com.dancoghlan.androidapp.model.Pace;
import com.dancoghlan.androidapp.model.RunContext;

import org.joda.time.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneralUtils {

    public static Pace calculatePace(Duration time, Double distance) {
        if (time != null && distance != null) {
            long seconds = time.getStandardSeconds();
            long absSeconds = Math.abs(seconds);
            double div = absSeconds / distance;

            int mins = (int) (div % 3600) / 60;
            int secs = (int) div % 60;
            return new Pace(mins, secs);
        }
        return null;
    }

    public static List<RunContext> getRunContexts(String json) {
        List<RunContext> runContexts = new ArrayList<>();
        RunContext[] runContextsArray = new RunContextObjectMapper().jsonToRunContextArray(json);
        if (runContextsArray != null && runContextsArray.length > 0) {
            runContexts = Arrays.asList(runContextsArray);
        }
        return runContexts;
    }

    public static boolean isConnectedToInternet(ConnectivityManager connectivityManager) {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static AlertDialog createNeutralAlertDialog(Context context, String title, String message) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton("OK", (dialog, id) -> dialog.dismiss()).create();
    }

    public static final AlertDialog createConnectivityFailureDialog(Context context) {
        return createNeutralAlertDialog(context, "Connectivity Failure", "Failed to connect to remove server");
    }

}
