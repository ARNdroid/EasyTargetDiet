package br.com.arndroid.etdiet.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;

public class NavigationUtils {

    // Utility
    protected NavigationUtils() {}

    public static void navigateUpFromSameTaskPreservingScreenState(Activity sourceActivity) {
        Intent upIntent = NavUtils.getParentActivityIntent(sourceActivity);

        if (upIntent == null) {
            throw new IllegalArgumentException("Activity " +
                    sourceActivity.getClass().getSimpleName() +
                    " does not have a parent activity name specified." +
                    " (Did you forget to add the android.support.PARENT_ACTIVITY <meta-data> " +
                    " element in your manifest?)");
        }

        upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        upIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        NavUtils.navigateUpTo(sourceActivity, upIntent);
    }
}
