package com.dancoghlan.androidapp.view;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;

import com.dancoghlan.androidapp.R;

public class DistancePicker extends FrameLayout {

    private static final OnDistanceChangedListener NO_OP_CHANGE_LISTENER = new OnDistanceChangedListener() {
        public void onDistanceChanged(DistancePicker view, int kilometers, int metres) {
        }
    };

    public static final Formatter TWO_DIGIT_FORMATTER = new Formatter() {
                @Override
                public String format(int value) {
                    return String.format("%02d", value);
                }
            };

    public static final Formatter ONE_DIGIT_FORMATTER = new Formatter() {
        @Override
        public String format(int value) {
            return String.format("%d", value);
        }
    };

    // state
    private int mKilometers = 0;
    private int mMetres = 01;

    // ui components
    private final NumberPicker mKilometerPicker;
    private final NumberPicker mMetrePicker;

    // callbacks
    private OnDistanceChangedListener mOnDistanceChangedListener;

    /**
     * The callback interface used to indicate the time has been adjusted.
     */
    public interface OnDistanceChangedListener {
        void onDistanceChanged(DistancePicker view, int kilometers, int metres);
    }

    public DistancePicker(Context context) {
        this(context, null);
    }

    public DistancePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DistancePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.distance_picker_widget, this, true);

        mKilometerPicker = findViewById(R.id.kilometer);
        mKilometerPicker.setMinValue(0);
        mKilometerPicker.setMaxValue(99);
        mKilometerPicker.setFormatter(ONE_DIGIT_FORMATTER);
        mKilometerPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            mKilometers = newVal;
            onDistanceChanged();
        });

        mMetrePicker = findViewById(R.id.metre);
        mMetrePicker.setMinValue(0);
        mMetrePicker.setMaxValue(99);
        mMetrePicker.setFormatter(TWO_DIGIT_FORMATTER);
        mMetrePicker.setOnValueChangedListener((spinner, oldVal, newVal) -> {
            mMetres = newVal;
            onDistanceChanged();
        });

        setOnDistanceChangedListener(NO_OP_CHANGE_LISTENER);

        setCurrentKilometer(0);
        setCurrentMetre(0);

        if (!isEnabled()) {
            setEnabled(false);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mMetrePicker.setEnabled(enabled);
        mKilometerPicker.setEnabled(enabled);
    }

    /**
     * Used to save / restore state of time picker
     */
    private static class SavedState extends BaseSavedState {
        private final int mKilometer;
        private final int mMetre;

        private SavedState(Parcelable superState, int hour, int minute) {
            super(superState);
            mKilometer = hour;
            mMetre = minute;
        }

        private SavedState(Parcel in) {
            super(in);
            mKilometer = in.readInt();
            mMetre = in.readInt();
        }

        public int getKilometer() {
            return mKilometer;
        }

        public int getMetre() {
            return mMetre;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mKilometer);
            dest.writeInt(mMetre);
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<DistancePicker.SavedState>() {
            public DistancePicker.SavedState createFromParcel(Parcel in) {
                return new DistancePicker.SavedState(in);
            }

            public DistancePicker.SavedState[] newArray(int size) {
                return new DistancePicker.SavedState[size];
            }
        };
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, mKilometers, mMetres);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setCurrentKilometer(ss.getKilometer());
        setCurrentMetre(ss.getMetre());
    }

    /**
     * Set the callback that indicates the time has been adjusted by the user.
     * @param onDistanceChangedListener the callback, should not be null.
     */
    public void setOnDistanceChangedListener(OnDistanceChangedListener onDistanceChangedListener) {
        mOnDistanceChangedListener = onDistanceChangedListener;
    }
    public Integer getCurrentKilometer() {
        return mKilometers;
    }

    public void setCurrentKilometer(Integer currentKilometer) {
        this.mKilometers = currentKilometer;
        updateKilometerDisplay();
    }

    public Integer getCurrentMetre() {
        return mMetres;
    }

    public void setCurrentMetre(Integer currentMetre) {
        this.mMetres = currentMetre;
        updateMetreDisplay();
    }

    @Override
    public int getBaseline() {
        return mKilometerPicker.getBaseline();
    }

    private void updateKilometerDisplay() {
        int currentKilometer = mKilometers;
        mKilometerPicker.setValue(currentKilometer);
        onDistanceChanged();
    }

    private void onDistanceChanged() {
        mOnDistanceChangedListener.onDistanceChanged(this, getCurrentKilometer(), getCurrentMetre());
    }

    private void updateMetreDisplay() {
        mMetrePicker.setValue(mMetres);
        mOnDistanceChangedListener.onDistanceChanged(this, getCurrentKilometer(), getCurrentMetre());
    }

}