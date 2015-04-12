package br.com.arndroid.etdiet.utils;

import android.widget.NumberPicker;

public class PointUtils {

    // Utility:
    protected PointUtils() {
    }

    public static void setPickerDecimal(NumberPicker pickerDecimal) {
        pickerDecimal.setDisplayedValues(new String[]{"0", "5"});
        pickerDecimal.setMinValue(0);
        pickerDecimal.setMaxValue(1);
    }

    public static float pickersToValue(NumberPicker pickerInteger, NumberPicker pickerDecimal) {
        return pickerDecimal.getValue() == 0 ?
                pickerInteger.getValue() : pickerInteger.getValue() + 0.5f;
    }

    public static void valueToPickers(float value, NumberPicker pickerInteger,
                                      NumberPicker pickerDecimal) {
        pickerInteger.setValue(integerPositionFromPoint(value));
        pickerDecimal.setValue(decimalPositionFromPoint(value));
    }

    public static int integerPositionFromPoint(float point) {
        return (int) Math.floor(point);
    }

    public static int decimalPositionFromPoint(float point) {
        return point % 1 == 0 ? 0 : 1;
    }
}
