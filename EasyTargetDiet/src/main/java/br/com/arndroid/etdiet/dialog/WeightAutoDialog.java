package br.com.arndroid.etdiet.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.provider.weights.WeightsEntity;
import br.com.arndroid.etdiet.provider.weights.WeightsManager;
import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.utils.WeightUtils;

public class WeightAutoDialog extends DialogFragment implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    /**
     * The auto means the dialog is responsible for data actualization, in others words,
     * doesn't need listeners.
     */

    private static final String TITLE_KEY = "TITLE_KEY";
    private static final String MIN_KEY = "MIN_KEY";
    private static final String MAX_KEY = "MAX_KEY";
    private static final String WEIGHT_KEY = "WEIGHT_KEY";

    private String mTitle;
    private int mMinIntegerValue;
    private int mMaxIntegerValue;
    private WeightsEntity mWeightEntity;
    private Button mBtnDate;
    private Button mBtnTime;
    private EditText mEdtNote;
    private NumberPicker mPickerInteger;
    private NumberPicker mPickerDecimal1;
    private NumberPicker mPickerDecimal2;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.weight_auto_dialog, null);
        builder.setView(view);

        bindScreen(view);

        if (savedInstanceState != null) {
            setTitle(savedInstanceState.getString(TITLE_KEY));
            setMinIntegerValue(savedInstanceState.getInt(MIN_KEY));
            setMaxIntegerValue(savedInstanceState.getInt(MAX_KEY));
            setWeightEntity((WeightsEntity) savedInstanceState.getParcelable(WEIGHT_KEY));
        }

        setupScreen();
        refreshScreen();

        builder.setTitle(getTitle());
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                fromScreenToEntity();
                mWeightEntity.validateOrThrow();
                final WeightsManager manager = new WeightsManager(getActivity().getApplicationContext());
                if (manager.entityWillCauseConstraintViolation(mWeightEntity)) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(
                            WeightAutoDialog.this.getActivity());
                    builder.setTitle(WeightAutoDialog.this.getTitle());
                    final String message = getString(R.string.weight_duplicated_message);
                    builder.setMessage(message);
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.create().show();
                } else {
                    manager.refresh(mWeightEntity);
                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(@SuppressWarnings("NullableProblems") Bundle outState) {
        fromScreenToEntity();
        outState.putString(TITLE_KEY, getTitle());
        outState.putInt(MIN_KEY, getMinIntegerValue());
        outState.putInt(MAX_KEY, getMaxIntegerValue());
        outState.putParcelable(WEIGHT_KEY, getWeightEntity());
        super.onSaveInstanceState(outState);
    }

    private void bindScreen(View rootView) {
        mBtnDate = (Button) rootView.findViewById(R.id.btnDate);
        mBtnTime = (Button) rootView.findViewById(R.id.btnTime);
        mEdtNote = (EditText) rootView.findViewById(R.id.edtNote);
        mPickerInteger = (NumberPicker) rootView.findViewById(R.id.pickerInteger);
        mPickerDecimal1 = (NumberPicker) rootView.findViewById(R.id.pickerDecimal1);
        mPickerDecimal2 = (NumberPicker) rootView.findViewById(R.id.pickerDecimal2);
    }

    private void setupScreen() {
        mBtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(WeightAutoDialog.this.getActivity(),
                        WeightAutoDialog.this, DateUtils.getYearFromDateId(mWeightEntity.getDateId()),
                        DateUtils.getMonthFromDateId(mWeightEntity.getDateId()) - 1,
                        DateUtils.getDayFromDateId(mWeightEntity.getDateId()));
                dialog.show();
            }
        });
        mBtnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(WeightAutoDialog.this.getActivity(),
                        WeightAutoDialog.this, DateUtils.getHoursFromTimeAsInt(mWeightEntity.getTime()),
                        DateUtils.getMinutesFromTimeAsInt(mWeightEntity.getTime()), true);
                dialog.show();
            }
        });
        mPickerInteger.setMinValue(getMinIntegerValue());
        mPickerInteger.setMaxValue(getMaxIntegerValue());
        mPickerDecimal1.setMinValue(0);
        mPickerDecimal1.setMaxValue(9);
        mPickerDecimal2.setMinValue(0);
        mPickerDecimal2.setMaxValue(9);
    }

    private void refreshScreen() {
        fromEntityToScreen();
    }

    private void fromEntityToScreen() {
        mBtnDate.setText(DateUtils.dateIdToFormattedString(mWeightEntity.getDateId()));
        mBtnTime.setText(DateUtils.timeToFormattedString(mWeightEntity.getTime()));
        mEdtNote.setText(mWeightEntity.getNote());
        WeightUtils.weightToPickers(mWeightEntity.getWeight(), mPickerInteger, mPickerDecimal1,
                mPickerDecimal2);
    }

    private void fromScreenToEntity() {
        // Date id and time are up to date by onDateSet() and onTimeSet()...
        mWeightEntity.setNote(mEdtNote.getText().toString());
        mWeightEntity.setWeight(WeightUtils.PickersToWeight(mPickerInteger, mPickerDecimal1,
                mPickerDecimal2));
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

    public WeightsEntity getWeightEntity() {
        return mWeightEntity;
    }

    public void setWeightEntity(WeightsEntity weightsEntity) {
        this.mWeightEntity = weightsEntity;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mWeightEntity.setDateId(DateUtils.datePickerToDateId(view));
        // We cannot call refreshScreen() here or we will lose other fields changed...
        mBtnDate.setText(DateUtils.dateIdToFormattedString(mWeightEntity.getDateId()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mWeightEntity.setTime(DateUtils.timePickerToTimeAsInt(view));
        // We cannot call refreshScreen() here or we will lose other fields changed...
        mBtnTime.setText(DateUtils.timeToFormattedString(mWeightEntity.getTime()));
    }
}
