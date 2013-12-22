package br.com.arndroid.etdiet.util.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import br.com.arndroid.etdiet.R;

public class IntegerPickerDialog extends DialogFragment {
    /**
     * The callback used to indicate the user is done filling in the point number.
     */
    public interface OnIntegerSetListener {
        void onIntegerSet(String tag, int actualValue);
    }

    private static final String TITLE_KEY = "TITLE_KEY";
    private static final String MIN_KEY = "MIN_KEY";
    private static final String MAX_KEY = "MAX_KEY";
    private static final String INITIAL_KEY = "INITIAL_KEY";
    private static final String ACTUAL_KEY = "ACTUAL_KEY";

    private String mTitle;
    private int mMinValue;
    private int mMaxValue;
    private int mInitialValue;
    private NumberPicker mPickerInteger;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.integer_picker_dialog, null);
        builder.setView(view);

        attachScreen(view);

        int actualValue = mInitialValue;
        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getString(TITLE_KEY);
            mMinValue = savedInstanceState.getInt(MIN_KEY);
            mMaxValue = savedInstanceState.getInt(MAX_KEY);
            mInitialValue = savedInstanceState.getInt(INITIAL_KEY);
            actualValue = savedInstanceState.getInt(ACTUAL_KEY);
        }

        setupScreen();
        refreshScreen(actualValue);

        builder.setTitle(mTitle);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                ((OnIntegerSetListener)getActivity()).onIntegerSet(IntegerPickerDialog.this.getTag(),
                        mPickerInteger.getValue());
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

    private void refreshScreen(float actualValue) {
        mPickerInteger.setValue((int) Math.floor(actualValue));
    }

    private void setupScreen() {
        mPickerInteger.setMinValue(mMinValue);
        mPickerInteger.setMaxValue(mMaxValue);
    }

    private void attachScreen(View rootView) {
        mPickerInteger = (NumberPicker) rootView.findViewById(R.id.integerPicker);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(TITLE_KEY, mTitle);
        outState.putInt(MIN_KEY, mMinValue);
        outState.putInt(MAX_KEY, mMaxValue);
        outState.putInt(INITIAL_KEY, mInitialValue);
        outState.putInt(ACTUAL_KEY, mPickerInteger.getValue());
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
        if (!(activity instanceof OnIntegerSetListener)) {
            throw new ClassCastException(activity.toString() + " must implement IntegerPickerDialog.OnIntegerSetListener");
        }
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public int getMinValue() {
        return mMinValue;
    }

    public void setMinValue(int minIntegerValue) {
        this.mMinValue = minIntegerValue;
    }

    public int getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(int maxIntegerValue) {
        this.mMaxValue = maxIntegerValue;
    }

    public int getInitialValue() {
        return mInitialValue;
    }

    public void setInitialValue(int currentValue) {
        this.mInitialValue = currentValue;
    }

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "==>ETD/" + IntegerPickerDialog.class.getSimpleName();
    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;
}
