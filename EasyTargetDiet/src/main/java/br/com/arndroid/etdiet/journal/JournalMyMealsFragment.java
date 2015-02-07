package br.com.arndroid.etdiet.journal;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.ActivityActionCaller;
import br.com.arndroid.etdiet.foodsusage.FoodsUsageActivity;
import br.com.arndroid.etdiet.foodsusage.FoodsUsageListFragment;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.virtualweek.DaySummary;

public class JournalMyMealsFragment extends Fragment {

    private String mCurrentDateId;

    private TextView mTxtBreakfastPts;
    private TextView mTxtBreakfastIdealValues;
    private TextView mTxtBrunchPts;
    private TextView mTxtBrunchIdealValues;
    private TextView mTxtLunchPts;
    private TextView mTxtLunchIdealValues;
    private TextView mTxtSnackPts;
    private TextView mTxtSnackIdealValues;
    private TextView mTxtDinnerPts;
    private TextView mTxtDinnerIdealValues;
    private TextView mTxtSupperPts;
    private TextView mTxtSupperIdealValues;
    private TextView mTxtTotalPts;

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
        mTxtBreakfastIdealValues = (TextView) rootView.findViewById(R.id.txtBreakfastIdealValues);
        mTxtBrunchPts = (TextView) rootView.findViewById(R.id.txtBrunchPts);
        mTxtBrunchIdealValues = (TextView) rootView.findViewById(R.id.txtBrunchIdealValues);
        mTxtLunchPts = (TextView) rootView.findViewById(R.id.txtLunchPts);
        mTxtLunchIdealValues = (TextView) rootView.findViewById(R.id.txtLunchIdealValues);
        mTxtSnackPts = (TextView) rootView.findViewById(R.id.txtSneakPts);
        mTxtSnackIdealValues = (TextView) rootView.findViewById(R.id.txtSneakIdealValues);
        mTxtDinnerPts = (TextView) rootView.findViewById(R.id.txtDinnerPts);
        mTxtDinnerIdealValues = (TextView) rootView.findViewById(R.id.txtDinnerIdealValues);
        mTxtSupperPts = (TextView) rootView.findViewById(R.id.txtSupperPts);
        mTxtSupperIdealValues = (TextView) rootView.findViewById(R.id.txtSupperIdealValues);
        mTxtTotalPts = (TextView) rootView.findViewById(R.id.txtTotalPts);
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
        final String idealFormat = getString(R.string.meal_ideal_actual_values);
        mTxtBreakfastPts.setText(String.valueOf(daySummary.getUsage().getBreakfastUsed()));
        mTxtBreakfastIdealValues.setText(String.format(
                idealFormat,
                daySummary.getEntity().getBreakfastGoal(),
                DateUtils.timeToFormattedString(daySummary.getEntity().getBreakfastStartTime()),
                DateUtils.timeToFormattedString(daySummary.getEntity().getBreakfastEndTime())));
        mTxtBrunchPts.setText(String.valueOf(daySummary.getUsage().getBrunchUsed()));
        mTxtBrunchIdealValues.setText(String.format(
                idealFormat,
                daySummary.getEntity().getBrunchGoal(),
                DateUtils.timeToFormattedString(daySummary.getEntity().getBrunchStartTime()),
                DateUtils.timeToFormattedString(daySummary.getEntity().getBrunchEndTime())));
        mTxtLunchPts.setText(String.valueOf(daySummary.getUsage().getLunchUsed()));
        mTxtLunchIdealValues.setText(String.format(
                idealFormat,
                daySummary.getEntity().getLunchGoal(),
                DateUtils.timeToFormattedString(daySummary.getEntity().getLunchStartTime()),
                DateUtils.timeToFormattedString(daySummary.getEntity().getLunchEndTime())));
        mTxtSnackPts.setText(String.valueOf(daySummary.getUsage().getSnackUsed()));
        mTxtSnackIdealValues.setText(String.format(
                idealFormat,
                daySummary.getEntity().getSnackGoal(),
                DateUtils.timeToFormattedString(daySummary.getEntity().getSnackStartTime()),
                DateUtils.timeToFormattedString(daySummary.getEntity().getSnackEndTime())));
        mTxtDinnerPts.setText(String.valueOf(daySummary.getUsage().getDinnerUsed()));
        mTxtDinnerIdealValues.setText(String.format(
                idealFormat,
                daySummary.getEntity().getDinnerGoal(),
                DateUtils.timeToFormattedString(daySummary.getEntity().getDinnerStartTime()),
                DateUtils.timeToFormattedString(daySummary.getEntity().getDinnerEndTime())));
        mTxtSupperPts.setText(String.valueOf(daySummary.getUsage().getSupperUsed()));
        mTxtSupperIdealValues.setText(String.format(
                idealFormat,
                daySummary.getEntity().getSupperGoal(),
                DateUtils.timeToFormattedString(daySummary.getEntity().getSupperStartTime()),
                DateUtils.timeToFormattedString(daySummary.getEntity().getSupperEndTime())));
        mTxtTotalPts.setText(String.valueOf(daySummary.getPlannedBeforeUsage()));
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