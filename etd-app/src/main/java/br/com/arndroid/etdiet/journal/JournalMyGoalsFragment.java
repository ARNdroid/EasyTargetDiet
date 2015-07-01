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
import br.com.arndroid.etdiet.dialog.IntegerDialog;
import br.com.arndroid.etdiet.dialog.quickinsert.QuickInsertAutoDialog;
import br.com.arndroid.etdiet.foodsusage.FoodsUsageActivity;
import br.com.arndroid.etdiet.foodsusage.FoodsUsageListFragment;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.provider.days.DaysManager;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.virtualweek.DaySummary;

public class JournalMyGoalsFragment extends Fragment implements
        IntegerDialog.OnIntegerSetListener {

    public static final String OWNER_TAG = JournalMyGoalsFragment.class.getSimpleName();
    public static final String LIQUID_DONE_TAG = OWNER_TAG + ".LIQUID_DONE_TAG";
    public static final String OIL_DONE_TAG = OWNER_TAG + ".OIL_DONE_TAG";
    public static final String SUPPLEMENT_DONE_TAG = OWNER_TAG + ".SUPPLEMENT_DONE_TAG";

    private String mCurrentDateId;

    private TextView mTxtExerciseGoal;
    private TextView mTxtLiquidGoal;
    private TextView mTxtOilGoal;
    private TextView mTxtSupplementGoal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.journal_my_goals_fragment, container, false);

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
        mTxtExerciseGoal = (TextView) rootView.findViewById(R.id.txtExerciseGoal);
        mTxtLiquidGoal = (TextView) rootView.findViewById(R.id.txtLiquidGoal);
        mTxtOilGoal = (TextView) rootView.findViewById(R.id.txtOilGoal);
        mTxtSupplementGoal = (TextView) rootView.findViewById(R.id.txtSupplementGoal);
    }

    private void setupScreen(View rootView) {
        final RelativeLayout layExerciseGoal = (RelativeLayout) rootView.findViewById(
                R.id.layExerciseGoal);
        layExerciseGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FoodsUsageEntity entity = new FoodsUsageEntity(null, mCurrentDateId,
                        Meals.EXERCISE, null, null, null);
                final QuickInsertAutoDialog dialog = new QuickInsertAutoDialog();
                dialog.setFoodsUsageEntity(entity);
                dialog.setAddMode(QuickInsertAutoDialog.ADD_MODE_USAGE_LIST);
                dialog.show(getFragmentManager(), null);
            }
        });
        layExerciseGoal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Bundle data = new Bundle();
                data.putString(FoodsUsageListFragment.DATE_ID_ACTION_KEY, mCurrentDateId);
                data.putInt(FoodsUsageListFragment.MEAL_POSITION_ACTION_KEY, Meals.EXERCISE);

                ((ActivityActionCaller)getActivity()).onCallAction(R.id.foods_usage_list_fragment,
                        FoodsUsageActivity.class, FoodsUsageListFragment.MEAL_SELECTED_ACTION_TAG,
                        data);
                return true;
            }
        });

        final RelativeLayout layLiquidGoal = (RelativeLayout) rootView.findViewById(
                R.id.layLiquidGoal);
        layLiquidGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaysManager manager = new DaysManager(getActivity().getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtils.dateIdToDate(mCurrentDateId));
                entity.setLiquidDone(entity.getLiquidDone() + 1);
                manager.refresh(entity);

            }
        });
        layLiquidGoal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DaysManager manager = new DaysManager(getActivity().getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtils.dateIdToDate(mCurrentDateId));
                IntegerDialog dialog = new IntegerDialog();
                dialog.setTitle(getString(R.string.liquid));
                dialog.setMinValue(0);
                dialog.setMaxValue(99);
                dialog.setInitialValue(entity.getLiquidDone());
                dialog.show(getFragmentManager(), LIQUID_DONE_TAG);
                return true;
            }
        });

        final RelativeLayout layOilGoal = (RelativeLayout) rootView.findViewById(R.id.layOilGoal);
        layOilGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaysManager manager = new DaysManager(getActivity().getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtils.dateIdToDate(mCurrentDateId));
                entity.setOilDone(entity.getOilDone() + 1);
                manager.refresh(entity);

            }
        });
        layOilGoal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DaysManager manager = new DaysManager(getActivity().getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtils.dateIdToDate(mCurrentDateId));
                IntegerDialog dialog = new IntegerDialog();
                dialog.setTitle(getString(R.string.oil));
                dialog.setMinValue(0);
                dialog.setMaxValue(99);
                dialog.setInitialValue(entity.getOilDone());
                dialog.show(getFragmentManager(), OIL_DONE_TAG);
                return true;
            }
        });

        final RelativeLayout laySupplementGoal = (RelativeLayout) rootView.findViewById(
                R.id.laySupplementGoal);
        laySupplementGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaysManager manager = new DaysManager(getActivity().getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtils.dateIdToDate(mCurrentDateId));
                entity.setSupplementDone(entity.getSupplementDone() + 1);
                manager.refresh(entity);

            }
        });
        laySupplementGoal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DaysManager manager = new DaysManager(getActivity().getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtils.dateIdToDate(mCurrentDateId));
                IntegerDialog dialog = new IntegerDialog();
                dialog.setTitle(getString(R.string.supplement));
                dialog.setMinValue(0);
                dialog.setMaxValue(99);
                dialog.setInitialValue(entity.getSupplementDone());
                dialog.show(getFragmentManager(), SUPPLEMENT_DONE_TAG);
                return true;
            }
        });
    }

    public void refreshScreen(DaySummary daySummary) {
        mCurrentDateId = daySummary.getEntity().getDateId();
        mTxtExerciseGoal.setText(String.valueOf(daySummary.getUsage().getExerciseDone()) + "/"
                + String.valueOf(daySummary.getEntity().getExerciseGoal()));
        mTxtLiquidGoal.setText(String.valueOf(daySummary.getEntity().getLiquidDone()) + "/"
                + String.valueOf(daySummary.getEntity().getLiquidGoal()));
        mTxtOilGoal.setText(String.valueOf(daySummary.getEntity().getOilDone()) + "/"
                + String.valueOf(daySummary.getEntity().getOilGoal()));
        mTxtSupplementGoal.setText(String.valueOf(daySummary.getEntity().getSupplementDone()) + "/"
                + String.valueOf(daySummary.getEntity().getSupplementGoal()));
    }

    @Override
    public void onIntegerSet(String tag, int actualValue) {
        final DaysManager manager = new DaysManager(getActivity().getApplicationContext());
        final DaysEntity entity = manager.dayFromDate(DateUtils.dateIdToDate(mCurrentDateId));
        if (LIQUID_DONE_TAG.equals(tag)) {
            entity.setLiquidDone(actualValue);
        } else if (OIL_DONE_TAG.equals(tag)) {
            entity.setOilDone(actualValue);
        } else if (SUPPLEMENT_DONE_TAG.equals(tag)) {
            entity.setSupplementDone(actualValue);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
        manager.refresh(entity);
    }
}