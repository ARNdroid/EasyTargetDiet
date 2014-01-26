package br.com.arndroid.etdiet.journal;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.ActivityActionCaller;
import br.com.arndroid.etdiet.action.FragmentMenuReplier;
import br.com.arndroid.etdiet.dialog.DateDialog;
import br.com.arndroid.etdiet.dialog.IntegerDialog;
import br.com.arndroid.etdiet.dialog.TextDialog;
import br.com.arndroid.etdiet.foodsusage.FoodsUsageActivity;
import br.com.arndroid.etdiet.foodsusage.FoodsUsageListFragment;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.provider.days.DaysManager;
import br.com.arndroid.etdiet.quickinsert.QuickInsertFrag;
import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.virtualweek.DaySummary;

public class JournalMyMealsFragment extends Fragment {

    private String mCurrentDateId;

    private TextView mTxtBreakfastPts;
    private TextView mTxtBreakfastTime;
    private TextView mTxtBreakfastIdeal;
    private TextView mTxtBrunchPts;
    private TextView mTxtBrunchTime;
    private TextView mTxtBrunchIdeal;
    private TextView mTxtLunchPts;
    private TextView mTxtLunchTime;
    private TextView mTxtLunchIdeal;
    private TextView mTxtSneakPts;
    private TextView mTxtSneakTime;
    private TextView mTxtSneakIdeal;
    private TextView mTxtDinnerPts;
    private TextView mTxtDinnerTime;
    private TextView mTxtDinnerIdeal;
    private TextView mTxtSupperPts;
    private TextView mTxtSupperTime;
    private TextView mTxtSupperIdeal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.journal_my_meals_fragment, container, false);

        bindScreen(rootView);
        setupScreen(rootView);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof ActivityActionCaller)) {
            throw new ClassCastException(activity.toString() + " must implement " +
                    ActivityActionCaller.class.getSimpleName());
        }

    }

    private void bindScreen(View rootView) {
        mTxtBreakfastPts = (TextView) rootView.findViewById(R.id.txtBreakfastPts);
        mTxtBreakfastTime = (TextView) rootView.findViewById(R.id.txtBreakfastTime);
        mTxtBreakfastIdeal = (TextView) rootView.findViewById(R.id.txtBreakfastIdeal);
        mTxtBrunchPts = (TextView) rootView.findViewById(R.id.txtBrunchPts);
        mTxtBrunchTime = (TextView) rootView.findViewById(R.id.txtBrunchTime);
        mTxtBrunchIdeal = (TextView) rootView.findViewById(R.id.txtBrunchIdeal);
        mTxtLunchPts = (TextView) rootView.findViewById(R.id.txtLunchPts);
        mTxtLunchTime = (TextView) rootView.findViewById(R.id.txtLunchTime);
        mTxtLunchIdeal = (TextView) rootView.findViewById(R.id.txtLunchIdeal);
        mTxtSneakPts = (TextView) rootView.findViewById(R.id.txtSneakPts);
        mTxtSneakTime = (TextView) rootView.findViewById(R.id.txtSneakTime);
        mTxtSneakIdeal = (TextView) rootView.findViewById(R.id.txtSneakIdeal);
        mTxtDinnerPts = (TextView) rootView.findViewById(R.id.txtDinnerPts);
        mTxtDinnerTime = (TextView) rootView.findViewById(R.id.txtDinnerTime);
        mTxtDinnerIdeal = (TextView) rootView.findViewById(R.id.txtDinnerIdeal);
        mTxtSupperPts = (TextView) rootView.findViewById(R.id.txtSupperPts);
        mTxtSupperTime = (TextView) rootView.findViewById(R.id.txtSupperTime);
        mTxtSupperIdeal = (TextView) rootView.findViewById(R.id.txtSupperIdeal);
    }

    private void setupScreen(View rootView) {
        final RelativeLayout layBreakfast = (RelativeLayout) rootView.findViewById(R.id.layBreakfast);
        layBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layMealAction(layBreakfast);
            }
        });

        final RelativeLayout layBrunch = (RelativeLayout) rootView.findViewById(R.id.layBrunch);
        layBrunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layMealAction(layBrunch);
            }
        });

        final RelativeLayout layLunch = (RelativeLayout) rootView.findViewById(R.id.layLunch);
        layLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layMealAction(layLunch);
            }
        });

        final RelativeLayout laySnack = (RelativeLayout) rootView.findViewById(R.id.laySnack);
        laySnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layMealAction(laySnack);
            }
        });

        final RelativeLayout layDinner = (RelativeLayout) rootView.findViewById(R.id.layDinner);
        layDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layMealAction(layDinner);
            }
        });

        final RelativeLayout laySupper = (RelativeLayout) rootView.findViewById(R.id.laySupper);
        laySupper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layMealAction(laySupper);
            }
        });
    }

    public void refreshScreen(DaySummary daySummary) {
        mCurrentDateId = daySummary.getEntity().getDateId();
        mTxtBreakfastPts.setText(String.valueOf(daySummary.getUsage().getBreakfastUsed()));
        mTxtBreakfastTime.setText(DateUtils.timeToFormattedString(
                daySummary.getEntity().getBreakfastStartTime()) + " - " +
                DateUtils.timeToFormattedString(daySummary.getEntity().getBreakfastEndTime()));
        final String ideal = getResources().getString(R.string.ideal_values) + " ";
        mTxtBreakfastIdeal.setText(ideal + String.valueOf(daySummary.getEntity().getBreakfastGoal()));
        mTxtBrunchPts.setText(String.valueOf(daySummary.getUsage().getBrunchUsed()));
        mTxtBrunchTime.setText(DateUtils.timeToFormattedString(daySummary.getEntity().getBrunchStartTime())
                + " - " + DateUtils.timeToFormattedString(daySummary.getEntity().getBrunchEndTime()));
        mTxtBrunchIdeal.setText(ideal + String.valueOf(daySummary.getEntity().getBrunchGoal()));
        mTxtLunchPts.setText(String.valueOf(daySummary.getUsage().getLunchUsed()));
        mTxtLunchTime.setText(DateUtils.timeToFormattedString(daySummary.getEntity().getLunchStartTime())
                + " - " + DateUtils.timeToFormattedString(daySummary.getEntity().getLunchEndTime()));
        mTxtLunchIdeal.setText(ideal + String.valueOf(daySummary.getEntity().getLunchGoal()));
        mTxtSneakPts.setText(String.valueOf(daySummary.getUsage().getSneakUsed()));
        mTxtSneakTime.setText(DateUtils.timeToFormattedString(daySummary.getEntity().getSnackStartTime())
                + " - " + DateUtils.timeToFormattedString(daySummary.getEntity().getSnackEndTime()));
        mTxtSneakIdeal.setText(ideal + String.valueOf(daySummary.getEntity().getSnackGoal()));
        mTxtDinnerPts.setText(String.valueOf(daySummary.getUsage().getDinnerUsed()));
        mTxtDinnerTime.setText(DateUtils.timeToFormattedString(daySummary.getEntity().getDinnerStartTime())
                + " - " + DateUtils.timeToFormattedString(daySummary.getEntity().getDinnerEndTime()));
        mTxtDinnerIdeal.setText(ideal + String.valueOf(daySummary.getEntity().getDinnerGoal()));
        mTxtSupperPts.setText(String.valueOf(daySummary.getUsage().getSupperUsed()));
        mTxtSupperTime.setText(DateUtils.timeToFormattedString(daySummary.getEntity().getSupperStartTime())
                + " - " + DateUtils.timeToFormattedString(daySummary.getEntity().getSupperEndTime()));
        mTxtSupperIdeal.setText(ideal + String.valueOf(daySummary.getEntity().getSupperGoal()));
    }

    public void layMealAction(View view) {
        int meal;
        switch (view.getId()) {
            case R.id.layBreakfast:
                meal = Meals.BREAKFAST;
                break;
            case R.id.layBrunch:
                meal = Meals.BRUNCH;
                break;
            case R.id.layLunch:
                meal = Meals.LUNCH;
                break;
            case R.id.laySnack:
                meal = Meals.SNACK;
                break;
            case R.id.layDinner:
                meal = Meals.DINNER;
                break;
            case R.id.laySupper:
                meal = Meals.SUPPER;
                break;
            default:
                throw new IllegalStateException("Invalid View.id " + view.getId());
        }

        final Bundle data = new Bundle();
        data.putString(FoodsUsageListFragment.DATE_ID_ACTION_KEY, mCurrentDateId);
        data.putInt(FoodsUsageListFragment.MEAL_ACTION_KEY, meal);

        ((ActivityActionCaller)getActivity()).onCallAction(R.id.foods_usage_list_fragment,
                FoodsUsageActivity.class, FoodsUsageListFragment.MEAL_SELECTED_ACTION_TAG, data);
    }
}