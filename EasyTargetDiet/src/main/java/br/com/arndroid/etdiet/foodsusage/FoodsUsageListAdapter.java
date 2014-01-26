package br.com.arndroid.etdiet.foodsusage;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.utils.DateUtils;

public class FoodsUsageListAdapter extends CursorAdapter {

    public FoodsUsageListAdapter(Context context) {
        super(context, null, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater =  (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.foods_usage_list_item, null, true);

        ViewHolder holder = new ViewHolder((TextView) view.findViewById(R.id.lblFoodValue),
                (TextView) view.findViewById(R.id.lblFoodDescription),
                (TextView) view.findViewById(R.id.lblTime));

        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.value.setText(String.valueOf(cursor.getFloat(
                cursor.getColumnIndex(Contract.FoodsUsage.VALUE))));
        holder.description.setText(cursor.getString(
                cursor.getColumnIndex(Contract.FoodsUsage.DESCRIPTION)));
        holder.time.setText(DateUtils.timeToFormattedString(cursor.getInt(
                cursor.getColumnIndex(Contract.FoodsUsage.TIME))));
    }

    private static class ViewHolder {

        public final TextView value;
        public final TextView description;
        public final TextView time;

        public ViewHolder(TextView value, TextView description, TextView time) {
            this.value = value;
            this.description = description;
            this.time = time;
        }
    }
}
