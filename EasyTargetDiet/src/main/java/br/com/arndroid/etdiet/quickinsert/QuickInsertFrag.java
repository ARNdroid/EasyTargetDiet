package br.com.arndroid.etdiet.quickinsert;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.meals.MealsAdapter;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageManager;
import br.com.arndroid.etdiet.utils.DateUtils;

/*
    We need something like a DialogFragment with a layout that references a Fragment. This fragment
    will have its own layout with all UI bellow.
    The challenge is create the best communication between
      i - The host activity and the Dialog - Host will create the dialog and need to pass parameters
      ii - The dialog and the fragment - Dialog must pass every parameters forward
    We need to see Android Dialogs from Android Developers: there is an option to create a activity
    with the following custom attribute in AndroidManifest.xml:
    <activity android:theme="@android:style/Theme.Holo.DialogWhenLarge" >
    Another refactor: the workflow for save/restore instance state plus fill* are unclear and have
    duplicated code.
 */
public class QuickInsertFrag extends DialogFragment implements DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    public static final String INSERT_TAG = QuickInsertFrag.class.getSimpleName() + ".INSERT_TAG";
    public static final String UPDATE_TAG = QuickInsertFrag.class.getSimpleName() + ".UPDATE_TAG";
    public static final String ID_PARAMETER = QuickInsertFrag.class.getSimpleName() + ".ID_PARAMETER";
    private static final String DATE_ID_PARAMETER = QuickInsertFrag.class.getSimpleName() + ".SETTINGS_TYPE_PARAMETER";
    private static final String TIME_PARAMETER = QuickInsertFrag.class.getSimpleName() + ".TIME_PARAMETER";
    private static final String VALUE_PARAMETER = QuickInsertFrag.class.getSimpleName() + ".VALUE_PARAMETER";

    private Long mId;
    private String mDateIdSet = null;
    private Integer mTimeSet = null;
    private Integer mMealSet = null;
    private String mDescriptionSet = null;
    private Float mValueSet = null;
    private Button btnDate;
    private Button btnTime;
    private Spinner spnMeal;
    private EditText edtDescription;
    private NumberPicker pickerInteger;
    private NumberPicker pickerDecimal;

    public QuickInsertFrag() {
        // This constructor is required for DialogFragment
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.quick_insert_frag, null);
        setFieldsReferenceFromForm(view);
        setupFields();

        if (savedInstanceState != null) {
            fillFormWithDataFromInstanceState(savedInstanceState);
        } else {
            if (UPDATE_TAG.equals(getTag())) {
                fillFormWithDataFromDB();
            }
            else {
                fillFormWithDataFromSetters();
            }
        }

        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // We will do nothing here because we are overriding onClick bellow...
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        final AlertDialog dlg = builder.create();

        dlg.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = dlg.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (insertOrUpdateFoodsUsage()) {
                            dlg.dismiss();
                        }
                    }
                });
            }
        });

        return dlg;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mId != null) {
            outState.putLong(ID_PARAMETER, mId);
        }
        outState.putString(DATE_ID_PARAMETER, mDateIdSet);
        outState.putInt(TIME_PARAMETER, mTimeSet);
        mValueSet = pickerDecimal.getValue() == 0 ? pickerInteger.getValue() : pickerInteger.getValue() + 0.5f;
        outState.putFloat(VALUE_PARAMETER, mValueSet);

        super.onSaveInstanceState(outState);
    }

    private void fillFormWithDataFromInstanceState(Bundle savedInstanceState) {
        final long returnedId = savedInstanceState.getLong(ID_PARAMETER, -1L);
        if (returnedId != -1L) {
            setId(returnedId);
        }
        mDateIdSet = savedInstanceState.getString(DATE_ID_PARAMETER);
        btnDate.setText(DateUtils.dateIdToFormattedString(mDateIdSet));
        mTimeSet = savedInstanceState.getInt(TIME_PARAMETER);
        btnTime.setText(DateUtils.timeToFormattedString(mTimeSet));
        mValueSet = savedInstanceState.getFloat(VALUE_PARAMETER);
        pickerInteger.setValue((int) Math.floor(mValueSet));
        pickerDecimal.setValue(mValueSet % 1 == 0 ? 0 : 1);
    }

    private void fillFormWithDataFromDB() {
        FoodsUsageEntity entity = new FoodsUsageManager(getActivity().getApplicationContext()).foodUsageFromId(mId);
        spnMeal.setSelection(entity.getMeal());
        btnDate.setText(DateUtils.dateIdToFormattedString(entity.getDateId()));
        mDateIdSet = entity.getDateId();
        btnTime.setText(DateUtils.timeToFormattedString(entity.getTime()));
        mTimeSet = entity.getTime();
        edtDescription.setText(entity.getDescription());
        mValueSet = entity.getValue();
        pickerInteger.setValue((int) Math.floor(entity.getValue()));
        pickerDecimal.setValue(entity.getValue() % 1 == 0 ? 0 : 1);
    }

    private void setupFields() {
        MealsAdapter adapter = new MealsAdapter(getActivity());
        spnMeal.setAdapter(adapter);
        spnMeal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (TextUtils.isEmpty(edtDescription.getText())) {
                    final String mealName = getString(Meals.getMealResourceNameIdFromMealId(
                            Meals.getMealFromPosition(position)));
                    final String hint = String.format(getResources().getString(R.string.quick_insert_description), mealName.toLowerCase());
                    edtDescription.setHint(hint);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing...
            }
        });
        pickerInteger.setMinValue(0);
        pickerInteger.setMaxValue(99);
        pickerDecimal.setMinValue(0);
        pickerDecimal.setMaxValue(1);
        pickerDecimal.setDisplayedValues(new String[]{"0", "5"});
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(QuickInsertFrag.this.getActivity(),
                        QuickInsertFrag.this, DateUtils.getYearFromDateId(mDateIdSet),
                        DateUtils.getMonthFromDateId(mDateIdSet) - 1,
                        DateUtils.getDayFromDateId(mDateIdSet));
                dialog.show();
            }
        });
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(QuickInsertFrag.this.getActivity(),
                        QuickInsertFrag.this, DateUtils.getHoursFromTimeAsInt(mTimeSet),
                        DateUtils.getMinutesFromTimeAsInt(mTimeSet), true);
                dialog.show();
            }
        });
    }

    private void setFieldsReferenceFromForm(View view) {
        spnMeal = (Spinner) view.findViewById(R.id.spnMeal);
        btnDate = (Button) view.findViewById(R.id.btnDate);
        btnTime = (Button) view.findViewById(R.id.btnTime);
        edtDescription = (EditText) view.findViewById(R.id.edtDescription);
        pickerInteger = (NumberPicker) view.findViewById(R.id.pickerInteger);
        pickerDecimal = (NumberPicker) view.findViewById(R.id.pickerDecimal);
    }

    private boolean insertOrUpdateFoodsUsage() {
        FoodsUsageEntity entity = new FoodsUsageEntity(
                mId,
                mDateIdSet,
                spnMeal.getSelectedItemPosition(),
                mTimeSet,
                TextUtils.isEmpty(edtDescription.getText().toString()) ? edtDescription.getHint().toString() : edtDescription.getText().toString(),
                pickerDecimal.getValue() == 0 ? pickerInteger.getValue() : pickerInteger.getValue() + 0.5f);

        // Validations:
        try {
            entity.validateOrThrow();
        } catch (Contract.TargetException e) {
            constructDialogForError(e);
            return false;
        }

        // Operation:
        new FoodsUsageManager(getActivity().getApplicationContext()).refresh(entity);
        setId(entity.getId());
        return true;
    }

    private void constructDialogForError(Contract.TargetException e) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.quick_add);
        /*
            Message here is NOT friendly but the UI are dealing with the data therefore errors are
            NOT expected.
         */
        builder.setMessage(e.getMessage());
        builder.setPositiveButton(android.R.string.ok, null);
        builder.create().show();
    }

    public void setId(Long id) {
        mId = id;
    }

    private void fillFormWithDataFromSetters() {
        spnMeal.setSelection(mMealSet);
        btnDate.setText(DateUtils.dateIdToFormattedString(mDateIdSet));
        btnTime.setText(DateUtils.timeToFormattedString(mTimeSet));
        edtDescription.setText(mDescriptionSet);
        pickerInteger.setValue((int) Math.floor(mValueSet));
        pickerDecimal.setValue(mValueSet % 1 == 0 ? 0 : 1);
    }

    public void setDateId(String dayId) {
        mDateIdSet = dayId;
    }

    public void setTime(Integer time) {
        mTimeSet = time;
    }

    public void setMeal(Integer meal) {
        mMealSet = meal;
    }

    public void setDescription(String description) {
        mDescriptionSet = description;
    }

    public void setValue(Float value) {
        mValueSet = value;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mDateIdSet = DateUtils.datePickerToDateId(view);
        btnDate.setText(DateUtils.dateIdToFormattedString(mDateIdSet));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mTimeSet = DateUtils.timePickerToTimeAsInt(view);
        btnTime.setText(DateUtils.timeToFormattedString(mTimeSet));
    }

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "==>ETD/" + QuickInsertFrag.class.getSimpleName();
    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;

}
