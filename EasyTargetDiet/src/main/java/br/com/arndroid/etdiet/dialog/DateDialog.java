package br.com.arndroid.etdiet.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.util.DateUtil;

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

    private String mTitle;
    private Date mInitialValue;
    private DatePicker mPickerDate;

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
            setInitialValue(DateUtil.dateIdToDate(savedInstanceState.getString(INITIAL_KEY)));
            actualValue = DateUtil.dateIdToDate(savedInstanceState.getString(ACTUAL_KEY));
        }

        refreshScreen(actualValue);

        builder.setTitle(getTitle());
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                ((OnDateSetListener)getActivity()).onDateSet(DateDialog.this.getTag(),
                        DateUtil.dateIdToDate(DateUtil.datePickerToDateId(mPickerDate)));
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

    private void refreshScreen(Date actualValue) {
        DateUtil.initDatePickerWithDateId(mPickerDate, DateUtil.dateToDateId(actualValue));
    }

    private void bindScreen(View rootView) {
        mPickerDate = (DatePicker) rootView.findViewById(R.id.datePicker);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(TITLE_KEY, getTitle());
        outState.putString(INITIAL_KEY, DateUtil.dateToDateId(getInitialValue()));
        outState.putString(ACTUAL_KEY, DateUtil.datePickerToDateId(mPickerDate));
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

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "==>ETD/" + DateDialog.class.getSimpleName();
    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;
}
