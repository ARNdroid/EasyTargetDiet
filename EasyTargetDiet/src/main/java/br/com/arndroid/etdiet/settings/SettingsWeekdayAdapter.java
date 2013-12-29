package br.com.arndroid.etdiet.settings;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.util.DateUtil;

public class SettingsWeekdayAdapter extends CursorAdapter {

    private final String mSettingsColumnName;

    public SettingsWeekdayAdapter(Context context, String settingsColumnName) {
        super(context, null, false);
        mSettingsColumnName = settingsColumnName;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater =  (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(android.R.layout.simple_list_item_2, null, true);

        ViewHolder holder = new ViewHolder((TextView) view.findViewById(android.R.id.text1),
                (TextView) view.findViewById(android.R.id.text2));

        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        int weekday = (int) cursor.getLong(cursor.getColumnIndex(Contract.WeekdayParameters._ID));
        final Resources resources = context.getResources();
        holder.text1.setText(resources.getStringArray(R.array.weekdays_name_list)[weekday - 1]);

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
        holder.text2.setText(formattedValue);
    }

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
                DateUtil.timeToFormattedString(cursor.getInt(cursor.getColumnIndex(
                        startTimeColumnName))),
                DateUtil.timeToFormattedString(cursor.getInt(cursor.getColumnIndex(
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
