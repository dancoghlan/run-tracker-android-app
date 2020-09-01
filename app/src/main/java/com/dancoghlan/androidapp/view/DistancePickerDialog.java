package com.dancoghlan.androidapp.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.dancoghlan.androidapp.R;


public class DistancePickerDialog extends AlertDialog implements OnClickListener, DistancePicker.OnDistanceChangedListener {

    public interface OnDistanceSetListener {
        void onDistanceSet(DistancePicker view, int kilometer, int metre);
    }

    private static final String KILOMETER = "kilometer";
    private static final String METRE = "metre";

    private final DistancePicker mDistancePicker;
    private final OnDistanceSetListener mCallback;

    private int mInitialKilometerOfDay;
    private int mInitialMetre;

    public DistancePickerDialog(Context context, OnDistanceSetListener callBack, int kilometer, int metre) {
        this(context, 0, callBack, kilometer, metre);
    }

    public DistancePickerDialog(Context context, int theme, OnDistanceSetListener callBack, int kilometer, int metre) {
        super(context, theme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCallback = callBack;
        mInitialKilometerOfDay = kilometer;
        mInitialMetre = metre;

        updateTitle(mInitialKilometerOfDay, mInitialMetre);

        setButton(context.getText(R.string.time_set), this);
        setButton2(context.getText(R.string.cancel), (OnClickListener) null);
        //setIcon(android.R.drawable.ic_dialog_time);

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.distance_picker_dialog, null);
        setView(view);
        mDistancePicker = view.findViewById(R.id.distancePicker);

        // initialize state
        mDistancePicker.setCurrentKilometer(mInitialKilometerOfDay);
        mDistancePicker.setCurrentMetre(mInitialMetre);
        mDistancePicker.setOnDistanceChangedListener(this);
    }

    public void onClick(DialogInterface dialog, int which) {
        if (mCallback != null) {
            mDistancePicker.clearFocus();
            mCallback.onDistanceSet(mDistancePicker, mDistancePicker.getCurrentKilometer(),
                    mDistancePicker.getCurrentMetre());
        }
    }

    public void onDistanceChanged(DistancePicker view, int kilometre, int metre) {
        updateTitle(kilometre, metre);
    }

    public void updateDistance(int kilometer, int metre) {
        mDistancePicker.setCurrentKilometer(kilometer);
        mDistancePicker.setCurrentMetre(metre);
    }

    private void updateTitle(int kilometre, int metre) {
        String sKilometer = String.format("%d", kilometre);
        String sMetre = String.format("%02d", metre);
        String value = new StringBuilder()
                .append(sKilometer)
                .append(".")
                .append(sMetre)
                .append("km")
                .toString();
        setTitle(value);
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(KILOMETER, mDistancePicker.getCurrentKilometer());
        state.putInt(METRE, mDistancePicker.getCurrentMetre());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int kilometer = savedInstanceState.getInt(KILOMETER);
        int metre = savedInstanceState.getInt(METRE);
        mDistancePicker.setCurrentKilometer(kilometer);
        mDistancePicker.setCurrentMetre(metre);
        mDistancePicker.setOnDistanceChangedListener(this);
        updateTitle(kilometer, metre);
    }

}
