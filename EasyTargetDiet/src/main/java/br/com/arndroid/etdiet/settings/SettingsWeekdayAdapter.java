package br.com.arndroid.etdiet.settings;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.utils.DateUtils;

public class SettingsWeekdayAdapter extends CursorAdapter {

    private final String mSettingsColumnName;
    private final LayoutInflater mInflater;
    private ViewHolder mHolder = null;

    public SettingsWeekdayAdapter(Context context, String settingsColumnName) {
        super(context, null, false);
        mSettingsColumnName = settingsColumnName;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.settings_list_item_2, viewGroup, false);

        mHolder = new ViewHolder((TextView) view.findViewById(R.id.txtLine_1),
                (TextView) view.findViewById(R.id.txtLine_2));

        view.setTag(mHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        mHolder = (ViewHolder) view.getTag();
        int weekday = (int) cursor.getLong(cursor.getColumnIndex(Contract.WeekdayParameters._ID));
        final Resources resources = context.getResources();
        mHolder.text1.setText(resources.getStringArray(R.array.weekdays_name_list)[weekday - 1]);

        final String formattedValue;
        if (Contract.WeekdayParameters.EXERCISE_GOAL.equals(mSettingsColumnName)) {
            formattedValue = getFormattedUnitsActualValueFromColumnName(cursor, resources,
                    Contract.WeekdayParameters.EXERCISE_GOAL);
        } else if (Contract.WeekdayParameters.LIQUID_GOAL.equals(mSettingsColumnName)) {
            formattedValue = getFormattedTimesActualValueFromColumnName(cursor, resources,
                    Contract.WeekdayParameters.LIQUID_GOAL);
        } else if (Contract.WeekdayParameters.OIL_GOAL.equals(mSettingsColumnName)) {
            formattedValue = getFormattedTimesActualValueFromColumnName(cursor, resources,
                    Contract.WeekdayParameters.OIL_GOAL);
        } else if (Contract.WeekdayParameters.SUPPLEMENT_GOAL.equals(mSettingsColumnName)) {
            formattedValue = getFormattedTimesActualValueFromColumnName(cursor, resources,
                    Contract.WeekdayParameters.SUPPLEMENT_GOAL);
        } else if (Contract.WeekdayParameters.BREAKFAST_GOAL.equals(mSettingsColumnName)) {
            formattedValue = getFormattedMealIdealValueFromColumnName(
                    cursor, resources,
                    Contract.WeekdayParameters.BREAKFAST_START_TIME,
                    Contract.WeekdayParameters.BREAKFAST_END_TIME,
                    Contract.WeekdayParameters.BREAKFAST_GOAL);
        } else if (Contract.WeekdayParameters.BRUNCH_GOAL.equals(mSettingsColumnName)) {
            formattedValue = getFormattedMealIdealValueFromColumnName(
                    cursor, resources,
                    Contract.WeekdayParameters.BRUNCH_START_TIME,
                    Contract.WeekdayParameters.BRUNCH_END_TIME,
                    Contract.WeekdayParameters.BRUNCH_GOAL);
        } else if (Contract.WeekdayParameters.LUNCH_GOAL.equals(mSettingsColumnName)) {
            formattedValue = getFormattedMealIdealValueFromColumnName(
                    cursor, resources,
                    Contract.WeekdayParameters.LUNCH_START_TIME,
                    Contract.WeekdayParameters.LUNCH_END_TIME,
                    Contract.WeekdayParameters.LUNCH_GOAL);
        } else if (Contract.WeekdayParameters.SNACK_GOAL.equals(mSettingsColumnName)) {
            formattedValue = getFormattedMealIdealValueFromColumnName(
                    cursor, resources,
                    Contract.WeekdayParameters.SNACK_START_TIME,
                    Contract.WeekdayParameters.SNACK_END_TIME,
                    Contract.WeekdayParameters.SNACK_GOAL);
        } else if (Contract.WeekdayParameters.DINNER_GOAL.equals(mSettingsColumnName)) {
            formattedValue = getFormattedMealIdealValueFromColumnName(
                    cursor, resources,
                    Contract.WeekdayParameters.DINNER_START_TIME,
                    Contract.WeekdayParameters.DINNER_END_TIME,
                    Contract.WeekdayParameters.DINNER_GOAL);
        } else if (Contract.WeekdayParameters.SUPPER_GOAL.equals(mSettingsColumnName)) {
            formattedValue = getFormattedMealIdealValueFromColumnName(
                    cursor, resources,
                    Contract.WeekdayParameters.SUPPER_START_TIME,
                    Contract.WeekdayParameters.SUPPER_END_TIME,
                    Contract.WeekdayParameters.SUPPER_GOAL);
        } else {
            throw new IllegalStateException("Invalid mSettingsColumnName=" + mSettingsColumnName);
        }
        mHolder.text2.setText(formattedValue);
    }

    @SuppressWarnings("SameParameterValue")
    private String getFormattedUnitsActualValueFromColumnName(Cursor cursor, Resources resources,
                                                              String goalColumnName) {
        return String.format(resources.getString(R.string.units_actual_value),
                cursor.getFloat(cursor.getColumnIndex(goalColumnName)));
    }

    private String getFormattedTimesActualValueFromColumnName(Cursor cursor, Resources resources,
                                                              String goalColumnName) {
        return String.format(resources.getString(R.string.times_actual_value),
                cursor.getInt(cursor.getColumnIndex(goalColumnName)));
    }

    private String getFormattedMealIdealValueFromColumnName(Cursor cursor, Resources resources,
                                                            String startTimeColumnName,
                                                            String endTimeColumnName,
                                                            String goalColumnName) {
        return String.format(resources.getString(R.string.meal_ideal_actual_values),
                cursor.getFloat(cursor.getColumnIndex(goalColumnName)),
                DateUtils.timeToFormattedString(cursor.getInt(cursor.getColumnIndex(
                        startTimeColumnName))),
                DateUtils.timeToFormattedString(cursor.getInt(cursor.getColumnIndex(
                        endTimeColumnName))));
    }

    private static class ViewHolder {

        public final TextView text1;
        public final TextView text2;

        public ViewHolder(TextView text1, TextView text2) {
            this.text1 = text1;
            this.text2 = text2;
        }
    }
}
