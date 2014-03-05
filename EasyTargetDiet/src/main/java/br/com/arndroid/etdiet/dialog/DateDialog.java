package br.com.arndroid.etdiet.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.utils.DateUtils;

public class DateDialog extends DialogFragment {
    /**
     * The callback used to indicate the user is done filling in the date.
     */
    public interface OnDateSetListener {
        void onDateSet(String tag, Date actualDate);
    }

    private static final String TITLE_KEY = "TITLE_KEY";
    private static final String INITIAL_KEY = "INITIAL_KEY";
    private static final String ACTUAL_KEY = "ACTUAL_KEY";
    private static final String CALENDAR_VIEW_SHOWN = "CALENDAR_VIEW_SHOWN";
    private static final String SPINNER_SHOWN = "SPINNER_SHOWN";

    private String mTitle;
    private Date mInitialValue;
    private DatePicker mPickerDate;
    private boolean mCalendarViewShown = true;
    private boolean mSpinnerShown = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.date_dialog, null);
        builder.setView(view);

        bindScreen(view);

        Date actualValue = getInitialValue();
        if (savedInstanceState != null) {
            setTitle(savedInstanceState.getString(TITLE_KEY));
            setInitialValue(DateUtils.dateIdToDate(savedInstanceState.getString(INITIAL_KEY)));
            setCalendarViewShown(savedInstanceState.getBoolean(CALENDAR_VIEW_SHOWN));
            setSpinnerShown(savedInstanceState.getBoolean(SPINNER_SHOWN));
            actualValue = DateUtils.dateIdToDate(savedInstanceState.getString(ACTUAL_KEY));
        }

        setupScreen();

        refreshScreen(actualValue);

        builder.setTitle(getTitle());
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                ((OnDateSetListener)getActivity()).onDateSet(DateDialog.this.getTag(),
                        DateUtils.dateIdToDate(DateUtils.datePickerToDateId(mPickerDate)));
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

    private void setupScreen() {
        mPickerDate.setCalendarViewShown(mCalendarViewShown);
        mPickerDate.setSpinnersShown(mSpinnerShown);
    }

    private void refreshScreen(Date actualValue) {
        DateUtils.initDatePickerWithDateId(mPickerDate, DateUtils.dateToDateId(actualValue));
    }

    private void bindScreen(View rootView) {
        mPickerDate = (DatePicker) rootView.findViewById(R.id.datePicker);
    }

    @Override
    public void onSaveInstanceState(@SuppressWarnings("NullableProblems") Bundle outState) {
        outState.putString(TITLE_KEY, getTitle());
        outState.putString(INITIAL_KEY, DateUtils.dateToDateId(getInitialValue()));
        outState.putBoolean(CALENDAR_VIEW_SHOWN, getCalendarViewShown());
        outState.putBoolean(SPINNER_SHOWN, getSpinnerShown());
        outState.putString(ACTUAL_KEY, DateUtils.datePickerToDateId(mPickerDate));
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
        if (!(activity instanceof OnDateSetListener)) {
            throw new ClassCastException(activity.toString() +
                    " must implement IntegerDialog.OnDateSetListener");
        }
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public Date getInitialValue() {
        return mInitialValue;
    }

    public void setInitialValue(Date currentValue) {
        mInitialValue = currentValue;
    }

    public boolean getCalendarViewShown() {
        return mCalendarViewShown;
    }

    public void setCalendarViewShown(boolean calendarViewShown) {
        mCalendarViewShown = calendarViewShown;
    }

    public boolean getSpinnerShown() {
        return mSpinnerShown;
    }

    public void setSpinnerShown(boolean spinnerShown) {
        mSpinnerShown = spinnerShown;
    }
}
