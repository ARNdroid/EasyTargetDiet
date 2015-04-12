package br.com.arndroid.etdiet.meals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MealsAdapter extends BaseAdapter {
    private final Context mContext;
    private final LayoutInflater mInflater;

    public MealsAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Meals.getMealsCount();
    }

    @Override
    public Object getItem(int position) {
        final int mealId = Meals.getMealFromPosition(position);
        return mContext.getString(Meals.getMealResourceNameIdFromMealId(mealId));
    }

    @Override
    public long getItemId(int position) {
        return Meals.getMealFromPosition(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_list_item_1, null,
                    true);
            holder = new ViewHolder((TextView) convertView.findViewById(android.R.id.text1));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final String mealName = mContext.getString(Meals.getMealResourceNameIdFromMealId(
                Meals.getMealFromPosition(position)));
        holder.text1.setText(mealName);
        return convertView;
    }

    private static class ViewHolder {

        public final TextView text1;

        public ViewHolder(TextView text1) {
            this.text1 = text1;
        }
    }

}
