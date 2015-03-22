package br.com.arndroid.etdiet.utils;

import android.app.backup.BackupManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import br.com.arndroid.etdiet.R;

public class PreferencesUtils {
    public static final String PREFERENCES_FILE_NAME = "etd_preferences";

    private static final String TRACKING_UNIT_NAME_ZERO_PREFERENCE_KEY = "TRACKING_UNIT_NAME_ZERO_PREFERENCE_KEY";
    private static final String TRACKING_UNIT_NAME_ONE_PREFERENCE_KEY = "TRACKING_UNIT_NAME_ONE_PREFERENCE_KEY";
    private static final String TRACKING_UNIT_NAME_MANY_PREFERENCE_KEY = "TRACKING_UNIT_NAME_MANY_PREFERENCE_KEY";

    // Utility:
    private PreferencesUtils() {
    }

    public static String getTrackingUnitNameZero(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(TRACKING_UNIT_NAME_ZERO_PREFERENCE_KEY, context.getString(R.string.default_tracking_unit_name_zero));
    }

    public static void setTrackingUnitNameZero(Context context, String name) {
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Tracking unit name for zero quantity must be NOT empty.");
        }

        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TRACKING_UNIT_NAME_ZERO_PREFERENCE_KEY, name.toLowerCase().trim());
        editor.apply();
        new BackupManager(context).dataChanged();
    }

    public static String getTrackingUnitNameOne(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(TRACKING_UNIT_NAME_ONE_PREFERENCE_KEY, context.getString(R.string.default_tracking_unit_name_one));
    }

    public static void setTrackingUnitNameOne(Context context, String name) {
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Tracking unit name for one quantity must be NOT empty.");
        }

        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TRACKING_UNIT_NAME_ONE_PREFERENCE_KEY, name.toLowerCase().trim());
        editor.apply();
        new BackupManager(context).dataChanged();
    }

    public static String getTrackingUnitNameMany(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(TRACKING_UNIT_NAME_MANY_PREFERENCE_KEY, context.getString(R.string.default_tracking_unit_name_many));
    }

    public static void setTrackingUnitNameMany(Context context, String name) {
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Tracking unit name for 'many' quantity must be NOT empty.");
        }

        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TRACKING_UNIT_NAME_MANY_PREFERENCE_KEY, name.toLowerCase().trim());
        editor.apply();
        new BackupManager(context).dataChanged();
    }

    public static String getTrackingUnitNameForQuantity(Context context, float quantity) {
        if (quantity < 0.0f) {
            throw new IllegalArgumentException("Quantity must be >= 0.");
        }

        if (quantity == 0.0f) {
            return getTrackingUnitNameZero(context);
        } else if (quantity == 1.0f) {
            return getTrackingUnitNameOne(context);
        } else {
            return getTrackingUnitNameMany(context);
        }
    }
}
