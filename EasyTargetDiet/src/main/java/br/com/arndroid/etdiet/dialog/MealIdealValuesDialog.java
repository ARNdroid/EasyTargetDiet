package br.com.arndroid.etdiet.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.utils.DateUtils;

public class MealIdealValuesDialog extends DialogFragment {
    /**
     * The callback used to indicate the user is done filling in the ideal meal's values.
     */
    public interface OnMealIdealValuesSetListener {
        void onMealIdealValuesSet(String tag, int actualStartTime, int actualEndTime,
                                  float actualValue);
    }

    private static final String TITLE_KEY = "TITLE_KEY";
    private static final String MIN_KEY = "MIN_KEY";
    private static final String MAX_KEY = "MAX_KEY";
    private static final String INITIAL_IDEAL_VALUE_KEY = "INITIAL_IDEAL_VALUE_KEY";
    private static final String ACTUAL_IDEAL_VALUE_KEY = "ACTUAL_IDEAL_VALUE_KEY";
    private static final String INITIAL_START_TIME_KEY = "INITIAL_START_TIME_KEY";
    private static final String INITIAL_END_TIME_KEY = "INITIAL_END_TIME_KEY";
    private static final String ACTUAL_START_TIME_KEY = "ACTUAL_START_TIME_KEY";
    private static final String ACTUAL_END_TIME_KEY = "ACTUAL_END_TIME_KEY";

    private String mTitle;
    private int mMinIntegerValue;
    private int mMaxIntegerValue;
    private float mInitialIdealValue;
    private int mInitialStartTime;
    private int mInitialEndTime;
    private NumberPicker mPickerInteger;
    private NumberPicker mPickerDecimal;
    private Button mBtnStartTime;
    private Button mBtnEndTime;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.meal_ideal_values_dialog, null);
        builder.setView(view);

        bindScreen(view);

        float actualIdealValue = getInitialIdealValue();
        int actualStartTime = getInitialStartTime();
        int actualEndTime = getInitialEndTime();
        if (savedInstanceState != null) {
            setTitle(savedInstanceState.getString(TITLE_KEY));
            setMinIntegerValue(savedInstanceState.getInt(MIN_KEY));
            setMaxIntegerValue(savedInstanceState.getInt(MAX_KEY));
            setInitialIdealValue(savedInstanceState.getFloat(INITIAL_IDEAL_VALUE_KEY));
            actualIdealValue = savedInstanceState.getFloat(ACTUAL_IDEAL_VALUE_KEY);
            setInitialStartTime(savedInstanceState.getInt(INITIAL_START_TIME_KEY));
            actualStartTime = savedInstanceState.getInt(ACTUAL_START_TIME_KEY);
            setInitialEndTime(savedInstanceState.getInt(INITIAL_END_TIME_KEY));
            actualEndTime = savedInstanceState.getInt(ACTUAL_END_TIME_KEY);
        }

        setupScreen();
        refreshScreen(actualIdealValue, actualStartTime, actualEndTime);

        builder.setTitle(getTitle());
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                ((OnMealIdealValuesSetListener)getActivity()).onMealIdealValuesSet(
                        MealIdealValuesDialog.this.getTag(),
                        DateUtils.formattedStringToTime(mBtnStartTime.getText().toString()),
                        DateUtils.formattedStringToTime(mBtnEndTime.getText().toString()),
                        getActualIdealValueFromPickers());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        return builder.create();
    }

    private float getActualIdealValueFromPickers() {
        return mPickerDecimal.getValue() == 0 ?
                mPickerInteger.getValue() : mPickerInteger.getValue() + 0.5f;
    }

    private void refreshScreen(float actualIdealValue, int actualStartTime, int actualEndTime) {
        mPickerInteger.setValue((int) Math.floor(actualIdealValue));
        mPickerDecimal.setValue(actualIdealValue % 1 == 0 ? 0 : 1);
        mBtnStartTime.setText(DateUtils.timeToFormattedString(actualStartTime));
        mBtnEndTime.setText(DateUtils.timeToFormattedString(actualEndTime));
    }

    private void setupScreen() {
        mPickerInteger.setMinValue(getMinIntegerValue());
        mPickerInteger.setMaxValue(getMaxIntegerValue());
        mPickerDecimal.setDisplayedValues(new String[]{"0", "5"});
        mPickerDecimal.setMinValue(0);
        mPickerDecimal.setMaxValue(1);
        mBtnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                                mBtnStartTime.setText(DateUtils.timeToFormattedString(
                                        DateUtils.hoursToMillis(hours) + DateUtils.minutesToMillis(minutes)));
                            } },
                        DateUtils.getHoursFromTimeAsInt(DateUtils.formattedStringToTime(mBtnStartTime.getText().toString())),
                        DateUtils.getMinutesFromTimeAsInt(DateUtils.formattedStringToTime(mBtnStartTime.getText().toString())),
                        true);
                dialog.setTitle(getResources().getString(R.string.from));
                dialog.show();
            }
        });
        mBtnEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                                mBtnEndTime.setText(DateUtils.timeToFormattedString(
                                        DateUtils.hoursToMillis(hours) + DateUtils.minutesToMillis(minutes)));
                            } },
                        DateUtils.getHoursFromTimeAsInt(DateUtils.formattedStringToTime(mBtnEndTime.getText().toString())),
                        DateUtils.getMinutesFromTimeAsInt(DateUtils.formattedStringToTime(mBtnEndTime.getText().toString())),
                        true);
                dialog.setTitle(getResources().getString(R.string.to));
                dialog.show();
            }
        });
    }

    private void bindScreen(View rootView) {
        mPickerInteger = (NumberPicker) rootView.findViewById(R.id.pickerInteger);
        mPickerDecimal = (NumberPicker) rootView.findViewById(R.id.pickerDecimal);
        mBtnStartTime = (Button) rootView.findViewById(R.id.btnStartTime);
        mBtnEndTime = (Button) rootView.findViewById(R.id.btnEndTime);
    }

    @Override
    public void onSaveInstanceState(@SuppressWarnings("NullableProblems") Bundle outState) {
        outState.putString(TITLE_KEY, getTitle());
        outState.putInt(MIN_KEY, getMinIntegerValue());
        outState.putInt(MAX_KEY, getMaxIntegerValue());
        outState.putFloat(INITIAL_IDEAL_VALUE_KEY, getInitialIdealValue());
        outState.putFloat(ACTUAL_IDEAL_VALUE_KEY, getActualIdealValueFromPickers());
        outState.putInt(INITIAL_START_TIME_KEY, mInitialStartTime);
        outState.putInt(ACTUAL_START_TIME_KEY, DateUtils.formattedStringToTime(
                mBtnStartTime.getText().toString()));
        outState.putInt(INITIAL_END_TIME_KEY, mInitialEndTime);
        outState.putInt(ACTUAL_END_TIME_KEY, DateUtils.formattedStringToTime(
                mBtnEndTime.getText().toString()));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Activity activity) {
        /* We didn't use a callback like:
             OnPointListener mListener
           because we couldn't save the listener with Bundle.setXxx to restore the dialog after,
           for example, a screen rotation.
           Due to it, the attached activity must implement the interface.
         */
        super.onAttach(activity);
        if (!(activity instanceof OnMealIdealValuesSetListener)) {
            throw new ClassCastException(activity.toString() +
                    " must implement MealsIdealValuesDialog.OnMealIdealValuesSetListener");
        }
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public int getMinIntegerValue() {
        return mMinIntegerValue;
    }

    public void setMinIntegerValue(int minIntegerValue) {
        this.mMinIntegerValue = minIntegerValue;
    }

    public int getMaxIntegerValue() {
        return mMaxIntegerValue;
    }

    public void setMaxIntegerValue(int maxIntegerValue) {
        this.mMaxIntegerValue = maxIntegerValue;
    }

    public float getInitialIdealValue() {
        return mInitialIdealValue;
    }

    public void setInitialIdealValue(float currentIdealValue) {
        mInitialIdealValue = currentIdealValue;
    }

    public int getInitialStartTime() {
        return mInitialStartTime;
    }

    public void setInitialStartTime(int initialStartTime) {
        mInitialStartTime = initialStartTime;
    }

    public int getInitialEndTime() {
        return mInitialEndTime;
    }

    public void setInitialEndTime(int initialEndTime) {
        mInitialEndTime = initialEndTime;
    }
}
