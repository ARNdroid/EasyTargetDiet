package br.com.arndroid.etdiet.test.util;

import junit.framework.TestCase;

import java.util.Calendar;

import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.utils.WeightUtils;

public class WeightUtilsTest extends TestCase {

    public void testDecimalPartFromWeight() {
        float weight = 0.0f;
        String result = "00";
        assertEquals(result, WeightUtils.decimalPartFromWeight(weight));

        weight = 1110.0011f;
        result = "00";
        assertEquals(result, WeightUtils.decimalPartFromWeight(weight));

        weight = 1110.0111f;
        result = "01";
        assertEquals(result, WeightUtils.decimalPartFromWeight(weight));

        weight = 1110.0711f;
        result = "07";
        assertEquals(result, WeightUtils.decimalPartFromWeight(weight));

        weight = 1110.9911f;
        result = "99";
        assertEquals(result, WeightUtils.decimalPartFromWeight(weight));

        weight = 12345.6789f;
        result = "68";
        assertEquals(result, WeightUtils.decimalPartFromWeight(weight));

    }
}
