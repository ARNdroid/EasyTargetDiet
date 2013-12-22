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

public class PointPickerDialog extends DialogFragment {
    /**
     * The callback used to indicate the user is done filling in the point number.
     */
    public interface OnPointSetListener {
        void onPointSet(String tag, float actualValue);
    }

    private static final String TITLE_KEY = "TITLE_KEY";
    private static final String MIN_KEY = "MIN_KEY";
    private static final String MAX_KEY = "MAX_KEY";
    private static final String INITIAL_KEY = "INITIAL_KEY";
    private static final String ACTUAL_KEY = "ACTUAL_KEY";

    private String mTitle;
    private int mMinIntegerValue;
    private int mMaxIntegerValue;
    private float mInitialValue;
    private NumberPicker mPickerInteger;
    private NumberPicker mPickerDecimal;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.point_picker_dialog, null);
        builder.setView(view);

        attachScreen(view);

        float actualValue = mInitialValue;
        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getString(TITLE_KEY);
            mMinIntegerValue = savedInstanceState.getInt(MIN_KEY);
            mMaxIntegerValue = savedInstanceState.getInt(MAX_KEY);
            mInitialValue = savedInstanceState.getFloat(INITIAL_KEY);
            actualValue = savedInstanceState.getFloat(ACTUAL_KEY);
        }

        setupScreen();
        refreshScreen(actualValue);

        builder.setTitle(mTitle);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                final float actualValue = getActualValueFromPickers();
                ((OnPointSetListener)getActivity()).onPointSet(PointPickerDialog.this.getTag(), actualValue);
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

    private float getActualValueFromPickers() {
        return mPickerDecimal.getValue() == 0 ?
                mPickerInteger.getValue() : mPickerInteger.getValue() + 0.5f;
    }

    private void refreshScreen(float actualValue) {
        mPickerInteger.setValue((int) Math.floor(actualValue));
        mPickerDecimal.setValue(actualValue % 1 == 0 ? 0 : 1);
    }

    private void setupScreen() {
        mPickerInteger.setMinValue(mMinIntegerValue);
        mPickerInteger.setMaxValue(mMaxIntegerValue);
        mPickerDecimal.setDisplayedValues(new String[]{"0", "5"});
        mPickerDecimal.setMinValue(0);
        mPickerDecimal.setMaxValue(1);
        mPickerDecimal.setWrapSelectorWheel(true);
    }

    private void attachScreen(View rootView) {
        mPickerInteger = (NumberPicker) rootView.findViewById(R.id.pickerInteger);
        mPickerDecimal = (NumberPicker) rootView.findViewById(R.id.pickerDecimal);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(TITLE_KEY, mTitle);
        outState.putInt(MIN_KEY, mMinIntegerValue);
        outState.putInt(MAX_KEY, mMaxIntegerValue);
        outState.putFloat(INITIAL_KEY, mInitialValue);
        outState.putFloat(ACTUAL_KEY, getActualValueFromPickers());
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
        if (!(activity instanceof OnPointSetListener)) {
            throw new ClassCastException(activity.toString() + " must implement PointPickerDialog.OnIntegerSetListener");
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

    public float getInitialValue() {
        return mInitialValue;
    }

    public void setInitialValue(float currentValue) {
        this.mInitialValue = currentValue;
    }

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "==>ETD/" + PointPickerDialog.class.getSimpleName();
    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;
}
