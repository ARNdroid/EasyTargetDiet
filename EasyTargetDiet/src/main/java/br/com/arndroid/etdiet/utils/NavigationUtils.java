package br.com.arndroid.etdiet.utils;

import android.app.Activity;
import android.support.v4.app.NavUtils;

public class NavigationUtils {

    // Utility
    protected NavigationUtils() {}

    public static void navigateUpFromSameTask(Activity sourceActivity) {
        NavUtils.navigateUpFromSameTask(sourceActivity);
    }
}
