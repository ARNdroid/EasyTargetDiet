package br.com.arndroid.etdiet.utils;

import android.util.Log;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import br.com.arndroid.etdiet.provider.weights.WeightsEntity;

public class WeightUtils {

    // Utility:
    protected WeightUtils() {
    }

    public static void weightToPickers(float weight, NumberPicker integerPicker,
                                             NumberPicker decimalPicker1, NumberPicker decimalPicker2) {
        integerPicker.setValue((int) Math.floor(weight));
        final String decimalPart = decimalPartFromWeight(weight);
        decimalPicker1.setValue(Integer.parseInt(decimalPart.substring(0,1)));
        decimalPicker2.setValue(Integer.parseInt(decimalPart.substring(1,2)));
    }

    public static float PickersToWeight(NumberPicker integerPicker, NumberPicker decimalPicker1,
                                             NumberPicker decimalPicker2) {
        return integerPicker.getValue() + decimalPicker1.getValue() * 0.1f
                + decimalPicker2.getValue() * 0.01f;
    }

    public static String decimalPartFromWeight(float weight) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('-');

        String pattern = "##0.00";
        DecimalFormat formatter = new DecimalFormat(pattern, symbols);
        final String[] split = formatter.format(weight).split("-");
        return split[1];
    }
}
