package br.com.arndroid.etdiet.utils;

import android.text.TextUtils;

public class StringUtils {

    // Utility
    private StringUtils() {
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || TextUtils.isEmpty(str.toString().trim());
    }
}
