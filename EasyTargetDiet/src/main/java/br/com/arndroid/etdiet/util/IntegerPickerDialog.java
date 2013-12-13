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
public class IntegerPickerDialog extends AlertDialog implements DialogInterface.OnClickListener {
    private static final String CURRENT_VALUE_KEY = "CURRENT_VALUE_KEY";
    private final NumberPicker mNumberPicker;
    private final OnNumberSetListener mCallBack;

    /**
     * The callback used to indicate the user is done filling in the date.
     */
    public interface OnNumberSetListener {
        void onDateSet(NumberPicker view, int value);
    }

    public IntegerPickerDialog(Context context,
                               OnNumberSetListener callBack,
                               String title,
                               int minValue,
                               int maxValue,
                               int value) {
        this(context, 0, callBack, title, minValue, maxValue, value);
    }

    public IntegerPickerDialog(Context context,
                               int theme,
                               OnNumberSetListener callBack,
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
        View view = inflater.inflate(R.layout.number_picker_dialog, null);
        setView(view);
        setTitle(title);
        mNumberPicker = (NumberPicker) view.findViewById(R.id.numberPicker);
        mNumberPicker.setMinValue(minValue);
        mNumberPicker.setMaxValue(maxValue);
        mNumberPicker.setValue(value);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        tryNotifyDateSet();
    }

    public NumberPicker getNumberPicker() {
        return mNumberPicker;
    }

    private void tryNotifyDateSet() {
        if (mCallBack != null) {
            mNumberPicker.clearFocus();
            mCallBack.onDateSet(mNumberPicker, mNumberPicker.getValue());
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
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mNumberPicker.setValue(savedInstanceState.getInt(CURRENT_VALUE_KEY));
    }
}
