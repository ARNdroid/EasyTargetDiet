package br.com.arndroid.etdiet.journal;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.ActivityActionCaller;
import br.com.arndroid.etdiet.action.FragmentMenuReplier;
import br.com.arndroid.etdiet.dialog.quickinsert.QuickInsertAutoDialog;
import br.com.arndroid.etdiet.forecast.ForecastActivity;
import br.com.arndroid.etdiet.forecast.ForecastBalanceFragment;
import br.com.arndroid.etdiet.forecast.ForecastEntity;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersEntity;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersManager;
import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.views.ForecastMeterView;
import br.com.arndroid.etdiet.virtualweek.DaySummary;

public class JournalMyPointsFragment extends Fragment implements FragmentMenuReplier {

    private String mCurrentDateId;

    private TextView mTxtTitle;
    private TextView mTxtPtsDay;
    private TextView mTxtPtsWeek;
    private TextView mTxtPtsExercise;
    private ForecastMeterView mForecastMeter;
    private boolean mForecastMeterCanTouch;

    // We need to save the last DaySummary used in refreshScreen() to use
    // with ForecastActivity.
    private DaySummary mDaySummary;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.journal_my_points_fragment, container, false);

        bindScreen(rootView);
        setupScreen();

        return rootView;
    }

    private void bindScreen(View rootView) {
        mTxtTitle = (TextView) rootView.findViewById(R.id.txtTitle);
        mTxtPtsDay = (TextView) rootView.findViewById(R.id.txtPtsDay);
        mTxtPtsWeek = (TextView) rootView.findViewById(R.id.txtPtsWeek);
        mTxtPtsExercise = (TextView) rootView.findViewById(R.id.txtPtsExercise);
        mForecastMeter = (ForecastMeterView) rootView.findViewById(R.id.forecastMeter);
    }

    private void setupScreen() {


        mForecastMeter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (mForecastMeterCanTouch) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            v.setBackgroundColor(getResources().getColor(R.color.card_pressed));
                            return true;
                        case MotionEvent.ACTION_UP:
                            v.setBackgroundColor(getResources().getColor(R.color.card_background));

                            final Bundle data = new Bundle();
                            data.putParcelable(ForecastActivity.DAY_SUMMARY_ACTION_KEY, mDaySummary);

                            ((ActivityActionCaller)getActivity()).onCallAction(R.id.forecast_balance_fragment,
                                    ForecastActivity.class, ForecastBalanceFragment.FORECAST_METER_SELECTED_ACTION_TAG, data);
                            return true;
                        default:
                            return false;
                    }

                } else {
                    return false;
                }
            }
        });
    }

    public void refreshScreen(DaySummary daySummary) {
        mDaySummary = daySummary;
        mCurrentDateId = mDaySummary.getEntity().getDateId();
        final ForecastEntity forecastEntity = ForecastEntity.getInstanceForDaySummary(mDaySummary);
        mForecastMeter.setForecastType(forecastEntity.getForecastType());
        final float percentage = 1.0f - forecastEntity.getToUse() / forecastEntity.getForecastUsed();
        mForecastMeter.setPercentage(percentage);

        mTxtPtsDay.setText(String.valueOf(mDaySummary.getDiaryAllowanceAfterUsage()));
        mTxtPtsWeek.setText(String.valueOf(mDaySummary.getWeeklyAllowanceAfterUsage()));
        mTxtPtsExercise.setText(String.valueOf(mDaySummary.getExerciseAfterUsage()));
    }

    @Override
    public void onReplyMenuFromHolderActivity(int menuItemId) {

        switch (menuItemId) {
            case R.id.quick_add:
                final FoodsUsageEntity entity = new FoodsUsageEntity(null, mCurrentDateId,
                        null, null, null, null);
                final QuickInsertAutoDialog dialog = new QuickInsertAutoDialog();
                dialog.setFoodsUsageEntity(entity);
                dialog.setAddMode(QuickInsertAutoDialog.ADD_MODE_JOURNAL);
                dialog.show(getFragmentManager(), null);
                break;
            default:
                throw new IllegalArgumentException("Invalid menuItemId=" + menuItemId);
        }
    }

    public void setTitle(String title) {
        mTxtTitle.setText(title);
    }

    public void setForecastMeterCanTouch(boolean canTouch) {
        mForecastMeterCanTouch = canTouch;
    }
}