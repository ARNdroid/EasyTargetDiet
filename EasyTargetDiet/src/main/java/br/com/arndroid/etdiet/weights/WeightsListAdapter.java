package br.com.arndroid.etdiet.weights;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.utils.DateUtils;

public class WeightsListAdapter extends CursorAdapter {

    public WeightsListAdapter(Context context) {
        super(context, null, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater =  (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.weights_list_item, null, true);

        ViewHolder holder = new ViewHolder(
                (TextView) view.findViewById(R.id.lblDate),
                (TextView) view.findViewById(R.id.lblTime),
                (TextView) view.findViewById(R.id.lblNotes),
                (TextView) view.findViewById(R.id.lblWeight)
        );

        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.date.setText(DateUtils.dateIdToFormattedString(cursor.getString(
                cursor.getColumnIndex(Contract.Weights.DATE_ID))));
        holder.time.setText(DateUtils.timeToFormattedString(cursor.getInt(
                cursor.getColumnIndex(Contract.Weights.TIME))));
        holder.note.setText(cursor.getString(cursor.getColumnIndex(Contract.Weights.NOTE)));
        if (TextUtils.isEmpty(holder.note.getText().toString())) {
            holder.note.setVisibility(View.GONE);
        } else {
            holder.note.setVisibility(View.VISIBLE);
        }
        holder.weight.setText(String.format("%.2f", cursor.getFloat(
                cursor.getColumnIndex(Contract.Weights.WEIGHT))));
    }

    private static class ViewHolder {

        public final TextView date;
        public final TextView time;
        public final TextView note;
        public final TextView weight;

        public ViewHolder(TextView date, TextView time, TextView note, TextView weight) {
            this.date = date;
            this.time = time;
            this.note = note;
            this.weight = weight;
        }
    }
}
