package br.com.arndroid.etdiet.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.meals.MealsAdapter;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageManager;
import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.utils.PointUtils;

public class QuickInsertAutoDialog extends DialogFragment implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    /**
     * The auto means the dialog is responsible for data actualization, in others words,
     * doesn't need listeners.
     */

    private static final String TITLE_KEY = "TITLE_KEY";
    private static final String FOODS_USAGE_KEY = "FOODS_USAGE_KEY";

    private String mTitle;
    private FoodsUsageEntity mFoodsUsageEntity;
    private Button mBtnDate;
    private Spinner mSpnMeal;
    private Button mBtnTime;
    private EditText mEdtDescription;
    private NumberPicker mPickerInteger;
    private NumberPicker mPickerDecimal;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.quick_insert_auto_dialog, null);
        builder.setView(view);

        bindScreen(view);

        if (savedInstanceState != null) {
            setTitle(savedInstanceState.getString(TITLE_KEY));
            setFoodsUsageEntity((FoodsUsageEntity) savedInstanceState.getParcelable(FOODS_USAGE_KEY));
        }

        setupScreen();
        refreshScreen();

        builder.setTitle(getTitle());
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                fromScreenToEntity();
                if (TextUtils.isEmpty(mEdtDescription.getText())) {
                    mFoodsUsageEntity.setDescription(mEdtDescription.getHint().toString());
                }
                mFoodsUsageEntity.validateOrThrow();
                final FoodsUsageManager manager = new FoodsUsageManager(getActivity().getApplicationContext());
                manager.refresh(mFoodsUsageEntity);
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

    @Override
    public void onSaveInstanceState(@SuppressWarnings("NullableProblems") Bundle outState) {
        fromScreenToEntity();
        outState.putString(TITLE_KEY, getTitle());
        outState.putParcelable(FOODS_USAGE_KEY, getFoodsUsageEntity());
        super.onSaveInstanceState(outState);
    }

    private void bindScreen(View rootView) {
        mBtnDate = (Button) rootView.findViewById(R.id.btnDate);
        mSpnMeal = (Spinner) rootView.findViewById(R.id.spnMeal);
        mBtnTime = (Button) rootView.findViewById(R.id.btnTime);
        mEdtDescription = (EditText) rootView.findViewById(R.id.edtDescription);
        mPickerInteger = (NumberPicker) rootView.findViewById(R.id.pickerInteger);
        mPickerDecimal = (NumberPicker) rootView.findViewById(R.id.pickerDecimal);
    }

    private void setupScreen() {
        mBtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(QuickInsertAutoDialog.this.getActivity(),
                        QuickInsertAutoDialog.this, DateUtils.getYearFromDateId(mFoodsUsageEntity.getDateId()),
                        DateUtils.getMonthFromDateId(mFoodsUsageEntity.getDateId()) - 1,
                        DateUtils.getDayFromDateId(mFoodsUsageEntity.getDateId()));
                dialog.show();
            }
        });
        mBtnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(QuickInsertAutoDialog.this.getActivity(),
                        QuickInsertAutoDialog.this, DateUtils.getHoursFromTimeAsInt(mFoodsUsageEntity.getTime()),
                        DateUtils.getMinutesFromTimeAsInt(mFoodsUsageEntity.getTime()), true);
                dialog.show();
            }
        });

        MealsAdapter adapter = new MealsAdapter(getActivity());
        mSpnMeal.setAdapter(adapter);
        mSpnMeal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (TextUtils.isEmpty(mEdtDescription.getText())) {
                    final String mealName = getString(Meals.getMealResourceNameIdFromMealId(
                            Meals.getMealFromPosition(position)));
                    final String hint = String.format(getString(R.string.quick_insert_description), mealName.toLowerCase());
                    mEdtDescription.setHint(hint);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing...
            }
        });

        mPickerInteger.setMinValue(0);
        mPickerInteger.setMaxValue(99);

        PointUtils.setPickerDecimal(mPickerDecimal);
    }

    private void refreshScreen() {
        fromEntityToScreen();
    }

    private void fromEntityToScreen() {
        mBtnDate.setText(DateUtils.dateIdToFormattedString(mFoodsUsageEntity.getDateId()));
        mSpnMeal.setSelection(mFoodsUsageEntity.getMeal());
        mBtnTime.setText(DateUtils.timeToFormattedString(mFoodsUsageEntity.getTime()));
        mEdtDescription.setText(mFoodsUsageEntity.getDescription());
        PointUtils.valueToPickers(mFoodsUsageEntity.getValue(), mPickerInteger, mPickerDecimal);
    }

    private void fromScreenToEntity() {
        // Date id and time are up to date by onDateSet() and onTimeSet()...
        mFoodsUsageEntity.setMeal(Meals.getMealFromPosition(mSpnMeal.getSelectedItemPosition()));
        mFoodsUsageEntity.setDescription(mEdtDescription.getText().toString());
        mFoodsUsageEntity.setValue(PointUtils.pickersToValue(mPickerInteger, mPickerDecimal));
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public FoodsUsageEntity getFoodsUsageEntity() {
        return mFoodsUsageEntity;
    }

    public void setFoodsUsageEntity(FoodsUsageEntity foodsUsageEntity) {
        this.mFoodsUsageEntity = foodsUsageEntity;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mFoodsUsageEntity.setDateId(DateUtils.datePickerToDateId(view));
        // We cannot call refreshScreen() here or we will lose other fields changed...
        mBtnDate.setText(DateUtils.dateIdToFormattedString(mFoodsUsageEntity.getDateId()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mFoodsUsageEntity.setTime(DateUtils.timePickerToTimeAsInt(view));
        // We cannot call refreshScreen() here or we will lose other fields changed...
        mBtnTime.setText(DateUtils.timeToFormattedString(mFoodsUsageEntity.getTime()));
    }
}
