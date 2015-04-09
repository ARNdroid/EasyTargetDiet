package br.com.arndroid.etdiet.dialog.quickinsert;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
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
import br.com.arndroid.etdiet.utils.StringUtils;

public class QuickInsertAutoDialog extends DialogFragment implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        NumberPicker.OnValueChangeListener {
    /**
     * The auto means the dialog is responsible for data actualization, in others words,
     * doesn't need listeners.
     */

    private static final Logger LOG = LoggerFactory.getLogger(QuickInsertAutoDialog.class);

    public static final int ADD_MODE_JOURNAL = 0;
    public static final int ADD_MODE_USAGE_LIST = 1;

    private static final String STATE_KEY_TITLE = "STATE_KEY_TITLE";
    private static final String STATE_KEY_ADD_MODE = "STATE_KEY_ADD_MODE";
    private static final String STATE_KEY_FOODS_USAGE = "STATE_KEY_FOODS_USAGE";

    private String mTitle;
    private int mAddMode;
    private FoodsUsageEntity mFoodsUsageEntity;

    private BaseHintStrategy mHintStrategy;
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
        // We don't have access to root ViewGroup here:
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.quick_insert_auto_dialog, null);
        builder.setView(view);

        bindScreen(view);

        if (savedInstanceState != null) {
            LOG.trace("Inside onCreateDialog: restoring state from savedInstanceState");
            setTitle(savedInstanceState.getString(STATE_KEY_TITLE));
            setAddMode(savedInstanceState.getInt(STATE_KEY_ADD_MODE));
            setFoodsUsageEntity((FoodsUsageEntity) savedInstanceState.getParcelable(STATE_KEY_FOODS_USAGE));
        }

        mHintStrategy = new SimpleHintStrategy();
        final boolean isNew = mFoodsUsageEntity.getId() == null;
        mHintStrategy.setDateAHint(isNew);
        mHintStrategy.setTimeAHint(isNew);
        if (mAddMode == ADD_MODE_JOURNAL) {
            mHintStrategy.setMealAHint(isNew);
        } else {
            mHintStrategy.setMealAHint(mFoodsUsageEntity.getMeal() == null);
        }
        mHintStrategy.setValueAHint(isNew);
        mHintStrategy.initialize(getActivity().getApplicationContext(), mFoodsUsageEntity, mAddMode);

        setupScreen();
        refreshScreen();

        builder.setTitle(getTitle());
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                fromScreenToEntity();
                if (StringUtils.isEmpty(mEdtDescription.getText())) {
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
        LOG.trace("Inside onSaveInstanceState: saving state to outState");
        fromScreenToEntity();
        outState.putString(STATE_KEY_TITLE, getTitle());
        outState.putInt(STATE_KEY_ADD_MODE, getAddMode());
        outState.putParcelable(STATE_KEY_FOODS_USAGE, getFoodsUsageEntity());
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

                LOG.trace("Entering mSpnMeal.onItemSelected: position={}", position);
                LOG.trace("Inside mSpnMeal.onItemSelected: mHintStrategy={} isMealAProgrammaticSelection()={}",
                        mHintStrategy, isMealAProgrammaticSelection(position));

                mFoodsUsageEntity.setMeal(Meals.getMealFromPosition(position));

                if (!isMealAProgrammaticSelection(position)) {
                    if (mHintStrategy.onMealChanged(getActivity().getApplicationContext(), mFoodsUsageEntity)) {
                        fromEntityToScreen();
                    }
                }

                // Reset programmatic selection control variable:
                setIsMealAProgrammaticSelection(position);

                if (StringUtils.isEmpty(mEdtDescription.getText())) {
                    final String mealName = getString(Meals.getMealResourceNameIdFromMealId(
                            Meals.getMealFromPosition(position)));
                    final String hint = String.format(getString(R.string.quick_usage_description), mealName.toLowerCase());
                    mEdtDescription.setHint(hint);
                }

                LOG.trace("Inside mSpnMeal.onItemSelected: mHintStrategy{}", mHintStrategy);
                LOG.trace("Leaving mSpnMeal.onItemSelected: position={}", position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing...
            }
        });

        mPickerInteger.setOnValueChangedListener(this);
        mPickerDecimal.setOnValueChangedListener(this);
        mPickerInteger.setMinValue(0);
        mPickerInteger.setMaxValue(99);
        PointUtils.setPickerDecimal(mPickerDecimal);
    }

    private void setIsMealAProgrammaticSelection(int position) {
        mSpnMeal.setTag(position);
    }

    private boolean isMealAProgrammaticSelection(int selection) {
        return selection == mSpnMeal.getTag();
    }

    private void setIsPointAProgrammaticSelection(int integerValue, int decimalValue) {
        mPickerInteger.setTag(integerValue);
        mPickerDecimal.setTag(decimalValue);
    }

    private boolean isPointAProgrammaticSelection(int integerValue, int decimalValue) {
        return integerValue == mPickerInteger.getTag() && decimalValue == mPickerDecimal.getTag();
    }

    private void refreshScreen() {
        fromEntityToScreen();
    }

    private void fromEntityToScreen() {
        LOG.trace("Entering fromEntityToScreen");
        LOG.trace("Inside fromEntityToScreen: mHintStrategy={}", mHintStrategy);

        mBtnDate.setText(DateUtils.dateIdToFormattedString(mFoodsUsageEntity.getDateId()));
        doMealProgrammaticSelection(mFoodsUsageEntity.getMeal());
        mBtnTime.setText(DateUtils.timeToFormattedString(mFoodsUsageEntity.getTime()));
        mEdtDescription.setText(mFoodsUsageEntity.getDescription());
        doPointProgrammaticSelection();

        LOG.trace("Leaving fromEntityToScreen");
    }

    private void doPointProgrammaticSelection() {
        setIsPointAProgrammaticSelection(PointUtils.integerPositionFromPoint(mFoodsUsageEntity.getValue()),
                PointUtils.decimalPositionFromPoint(mFoodsUsageEntity.getValue()));
        PointUtils.valueToPickers(mFoodsUsageEntity.getValue(), mPickerInteger, mPickerDecimal);
    }

    private void doMealProgrammaticSelection(Integer position) {
        setIsMealAProgrammaticSelection(position);
        mSpnMeal.setSelection(position);
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

    public int getAddMode() {
        return mAddMode;
    }

    public void setAddMode(int addMode) {
        mAddMode = addMode;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mFoodsUsageEntity.setDateId(DateUtils.datePickerToDateId(view));
        mHintStrategy.onDateChanged(getActivity().getApplicationContext(), mFoodsUsageEntity);
        fromEntityToScreen();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mFoodsUsageEntity.setTime(DateUtils.timePickerToTimeAsInt(view));
        mHintStrategy.onTimeChanged(getActivity().getApplicationContext(), mFoodsUsageEntity);
        fromEntityToScreen();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        mFoodsUsageEntity.setValue(PointUtils.pickersToValue(mPickerInteger, mPickerDecimal));
        if (!isPointAProgrammaticSelection(mPickerInteger.getValue(), mPickerDecimal.getValue())) {
            mHintStrategy.onValueChanged(getActivity().getApplicationContext(), mFoodsUsageEntity);
            fromEntityToScreen();
        }
    }
}
