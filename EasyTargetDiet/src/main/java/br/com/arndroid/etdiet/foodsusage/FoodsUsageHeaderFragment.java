package br.com.arndroid.etdiet.foodsusage;

import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import javax.crypto.spec.DESedeKeySpec;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.dialog.MealIdealValuesDialog;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.provider.days.DaysManager;
import br.com.arndroid.etdiet.utils.DateUtils;

public class FoodsUsageHeaderFragment extends Fragment implements MealIdealValuesDialog.OnMealIdealValuesSetListener {
    public static final String OWNER_TAG = FoodsUsageHeaderFragment.class.getSimpleName();

    private String mDateId;
    private int mMeal;
    private float mTotalUsed;
    private TextView mTxtDate;
    private TextView mTxtUsed;
    private TextView mTxtGoal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.foods_usage_header_fragment, container, false);

        bindScreen(rootView);

        setupScreen(rootView);

        return rootView;
    }

    private void bindScreen(View rootView) {
        mTxtDate = (TextView) rootView.findViewById(R.id.txtDate);
        mTxtUsed = (TextView) rootView.findViewById(R.id.txtUsed);
        mTxtGoal = (TextView) rootView.findViewById(R.id.txtGoal);
    }

    private void setupScreen(View rootView) {
        final ImageButton btnEdit = (ImageButton) rootView.findViewById(R.id.btnEditMeal);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DaysManager manager = new DaysManager(getActivity().getApplicationContext());
                DaysEntity daysEntity = manager.dayFromDateId(mDateId);

                final MealIdealValuesDialog dialog = new MealIdealValuesDialog();
                dialog.setTitle(getString(R.string.edit_ideal_values));
                dialog.setMinIntegerValue(0);
                dialog.setMaxIntegerValue(99);
                dialog.setInitialStartTime(daysEntity.getStartTimeForMeal(mMeal));
                dialog.setInitialEndTime(daysEntity.getEndTimeForMeal(mMeal));
                dialog.setInitialIdealValue(daysEntity.getGoalForMeal(mMeal));
                dialog.show(getFragmentManager(), OWNER_TAG);
            }
        });
    }

    private void refreshScreen(String dateId, int meal, float used) {
        mTxtDate.setText(DateUtils.dateIdToFormattedString(dateId));
        mTxtUsed.setText(String.valueOf(used));

        DaysEntity entity = new DaysManager(getActivity().getApplicationContext())
                .dayFromDateId(dateId);

        final float goal = entity.getGoalForMeal(meal);
        final int startTime = entity.getStartTimeForMeal(meal);
        final int endTime = entity.getEndTimeForMeal(meal);

        if (startTime >= 0) {
            mTxtGoal.setText(String.format(getString(R.string.meal_ideal_actual_values),
                    goal, DateUtils.timeToFormattedString(startTime),
                    DateUtils.timeToFormattedString(endTime)));
        } else {
            mTxtGoal.setText(String.format(getString(R.string.units_actual_value), goal));
        }
    }

    public void onDataChangedFromHolderActivity(String dateId, int meal, Cursor data) {
        mDateId = dateId;
        mMeal = meal;
        float totalUsed = 0.0f;
        if (data.moveToFirst()) {
            do {
                totalUsed += data.getFloat(data.getColumnIndex(Contract.FoodsUsage.VALUE));
            } while (data.moveToNext());
        }
        mTotalUsed = totalUsed;
        refreshScreen(mDateId, mMeal, mTotalUsed);
    }

    @Override
    public void onMealIdealValuesSet(String tag, int actualStartTime, int actualEndTime, float actualValue) {
        final DaysManager manager = new DaysManager(getActivity().getApplicationContext());

        DaysEntity daysEntity = manager.dayFromDateId(mDateId);
        if (mMeal != Meals.EXERCISE) {
            daysEntity.setStartTimeForMeal(actualStartTime, mMeal);
            daysEntity.setEndTimeForMeal(actualEndTime, mMeal);
        }
        daysEntity.setGoalForMeal(actualValue, mMeal);

        manager.refresh(daysEntity);
        refreshScreen(mDateId, mMeal, mTotalUsed);
    }
}
