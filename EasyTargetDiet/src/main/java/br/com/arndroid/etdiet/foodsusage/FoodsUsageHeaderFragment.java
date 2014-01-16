package br.com.arndroid.etdiet.foodsusage;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.provider.days.DaysManager;
import br.com.arndroid.etdiet.utils.DateUtils;

public class FoodsUsageHeaderFragment extends Fragment {
    private TextView mTxtDate;
    private TextView mTxtUsed;
    private TextView mTxtGoal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.foods_usage_header_fragment, container, false);

        bindScreen(rootView);

        return rootView;
    }

    private void bindScreen(View rootView) {
        mTxtDate = (TextView) rootView.findViewById(R.id.txtDate);
        mTxtUsed = (TextView) rootView.findViewById(R.id.txtUsed);
        mTxtGoal = (TextView) rootView.findViewById(R.id.txtGoal);
    }

    private void refreshScreen(String dateId, int meal, float used) {
        mTxtDate.setText(DateUtils.dateIdToFormattedString(dateId));
        mTxtUsed.setText(String.format(getString(R.string.units_actual_value), used));

        DaysEntity entity = new DaysManager(getActivity().getApplicationContext())
                .dayFromDateId(dateId);
        float ideal;
        int startTime, endTime;
        switch (meal) {
            case Meals.BREAKFAST:
                ideal = entity.getBreakfastGoal();
                startTime = entity.getBreakfastStartTime();
                endTime = entity.getBreakfastEndTime();
                break;
            case Meals.BRUNCH:
                ideal = entity.getBrunchGoal();
                startTime = entity.getBrunchStartTime();
                endTime = entity.getBrunchEndTime();
                break;
            case Meals.LUNCH:
                ideal = entity.getLunchGoal();
                startTime = entity.getLunchStartTime();
                endTime = entity.getLunchEndTime();
                break;
            case Meals.SNACK:
                ideal = entity.getSnackGoal();
                startTime = entity.getSnackStartTime();
                endTime = entity.getSnackEndTime();
                break;
            case Meals.DINNER:
                ideal = entity.getDinnerGoal();
                startTime = entity.getDinnerStartTime();
                endTime = entity.getDinnerEndTime();
                break;
            case Meals.SUPPER:
                ideal = entity.getSupperGoal();
                startTime = entity.getSupperStartTime();
                endTime = entity.getSupperEndTime();
                break;
            case Meals.EXERCISE:
                ideal = entity.getExerciseGoal();
                startTime = -1;
                endTime = -1;
                break;
            default:
                throw new IllegalArgumentException("Invalid meal=" + meal);
        }
        if (startTime >= 0) {
            mTxtGoal.setText(String.format(getResources().getString(R.string.meal_ideal_actual_values),
                    ideal, DateUtils.timeToFormattedString(startTime),
                    DateUtils.timeToFormattedString(endTime)));
        } else {
            mTxtGoal.setText(String.format(getResources().getString(R.string.units_actual_value), ideal));
        }
    }

    public void onDataChangedFromHolderActivity(String dateId, int meal, Cursor data) {
        float totalUsed = 0.0f;
        if (data.moveToFirst()) {
            do {
                totalUsed += data.getFloat(data.getColumnIndex(Contract.FoodsUsage.VALUE));
            } while (data.moveToNext());
        }
        refreshScreen(dateId, meal, totalUsed);
    }
}
