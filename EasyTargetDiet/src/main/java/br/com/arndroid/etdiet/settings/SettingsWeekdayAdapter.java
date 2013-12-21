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

        final String actualValue;
        if (Contract.WeekdayParameters.EXERCISE_GOAL.equals(mSettingsColumnName)) {
            actualValue = String.format(resources.getString(R.string.units_actual_value),
                    cursor.getFloat(cursor.getColumnIndex(Contract.WeekdayParameters.EXERCISE_GOAL)));
        } else {
            throw new IllegalStateException("Invalid mSettingsColumnName=" + mSettingsColumnName);
        }
        holder.text2.setText(actualValue);
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
