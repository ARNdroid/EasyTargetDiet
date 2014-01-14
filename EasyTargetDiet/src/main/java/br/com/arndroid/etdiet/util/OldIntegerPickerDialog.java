package br.com.arndroid.etdiet.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import br.com.arndroid.etdiet.R;

/*
 Build by example of Android DatePickerDialog class
 */
public class OldIntegerPickerDialog extends AlertDialog implements DialogInterface.OnClickListener {

    private static final String CURRENT_VALUE_KEY = "CURRENT_VALUE_KEY";
    private NumberPicker mNumberPicker = null;
    private final OnIntegerSetListener mCallBack;

    /**
     * The callback used to indicate the user is done filling in the integer.
     */
    public interface OnIntegerSetListener {
        void onNumberSet(NumberPicker view, int value);
    }

    public OldIntegerPickerDialog(Context context,
                                  OnIntegerSetListener callBack,
                                  String title,
                                  int minValue,
                                  int maxValue,
                                  int value) {
        this(context, 0, callBack, title, minValue, maxValue, value);
    }

    public OldIntegerPickerDialog(Context context,
                                  int theme,
                                  OnIntegerSetListener callBack,
                                  String title,
                                  int minValue,
                                  int maxValue,
                                  int value) {
        super(context, theme);

        mCallBack = callBack;

        Context themeContext = getContext();
        setButton(BUTTON_POSITIVE, themeContext.getText(R.string.done), this);
        setIcon(0);

        LayoutInflater inflater =
                (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.integer_dialog, null);
        setView(view);
        setTitle(title);

        bindScreen(view);
        setupScreen(minValue, maxValue);
        refreshScreen(value);
    }

    private void refreshScreen(int value) {
        mNumberPicker.setValue(value);
    }

    private void setupScreen(int minValue, int maxValue) {
        mNumberPicker.setMinValue(minValue);
        mNumberPicker.setMaxValue(maxValue);
    }

    private void bindScreen(View rootView) {
        mNumberPicker = (NumberPicker) rootView.findViewById(R.id.integerPicker);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        tryNotifyDateSet();
    }

    private void tryNotifyDateSet() {
        if (mCallBack != null) {
            mNumberPicker.clearFocus();
            mCallBack.onNumberSet(mNumberPicker, mNumberPicker.getValue());
        }
    }

    @Override
    protected void onStop() {
        tryNotifyDateSet();
        super.onStop();
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(CURRENT_VALUE_KEY, mNumberPicker.getValue());
        return state;
    }

    @Override
    public void onRestoreInstanceState(@SuppressWarnings("NullableProblems") Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mNumberPicker.setValue(savedInstanceState.getInt(CURRENT_VALUE_KEY));
    }
}
