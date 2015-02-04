package br.com.arndroid.etdiet.settings;

import android.database.Cursor;
import android.os.Bundle;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.FragmentActionReplier;
import br.com.arndroid.etdiet.dialog.IntegerDialog;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersEntity;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersManager;
import br.com.arndroid.etdiet.dialog.MealIdealValuesDialog;
import br.com.arndroid.etdiet.dialog.PointDialog;

public class SettingsListFragment extends ListFragment implements
        MealIdealValuesDialog.OnMealIdealValuesSetListener,
        PointDialog.OnPointSetListener,
        IntegerDialog.OnIntegerSetListener,
        FragmentActionReplier,
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String OWNER_TAG = SettingsListFragment.class.getSimpleName();
    public static final String EXERCISE_GOAL_SETTINGS_TAG = OWNER_TAG + ".EXERCISE_GOAL_SETTINGS";
    public static final String LIQUID_GOAL_SETTINGS_TAG = OWNER_TAG + ".LIQUID_GOAL_SETTINGS";
    public static final String OIL_GOAL_SETTINGS_TAG = OWNER_TAG + ".OIL_GOAL_SETTINGS";
    public static final String SUPPLEMENT_GOAL_SETTINGS_TAG = OWNER_TAG + ".SUPPLEMENT_GOAL_SETTINGS";
    public static final String BREAKFAST_GOAL_SETTINGS_TAG = OWNER_TAG + ".BREAKFAST_GOAL_SETTINGS";
    public static final String BRUNCH_GOAL_SETTINGS_TAG = OWNER_TAG + ".BRUNCH_GOAL_SETTINGS";
    public static final String LUNCH_GOAL_SETTINGS_TAG = OWNER_TAG + ".LUNCH_GOAL_SETTINGS";
    public static final String SNACK_GOAL_SETTINGS_TAG = OWNER_TAG + ".SNACK_GOAL_SETTINGS";
    public static final String DINNER_GOAL_SETTINGS_TAG = OWNER_TAG + ".DINNER_GOAL_SETTINGS";
    public static final String SUPPER_GOAL_SETTINGS_TAG = OWNER_TAG + ".SUPPER_GOAL_SETTINGS";

    private static final int WEEKDAY_PARAMETERS_SETTINGS_LOADER_ID = 1;
    private static final String SELECTED_WEEKDAY_KEY = "SELECTED_WEEKDAY_KEY";

    /*
     We don't need to restore mSettingsColumnName from a saved instance state because the host
     activity is doing this and calling us with refresh(settingsColumn).
     */
    private String mSettingsColumnName;

    private int mSelectedWeekday;

    private SettingsWeekdayAdapter mAdapter;

    @Override
    public View onCreateView(@SuppressWarnings("NullableProblems") LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mSelectedWeekday = savedInstanceState.getInt(SELECTED_WEEKDAY_KEY);
        }

        mAdapter = new SettingsWeekdayAdapter(getActivity(), mSettingsColumnName);
        setListAdapter(mAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_WEEKDAY_KEY, mSelectedWeekday);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        mSelectedWeekday = (int) id;
        WeekdayParametersEntity entity = new WeekdayParametersManager(getActivity()
                .getApplicationContext()).weekdayParametersFromWeekday(mSelectedWeekday);

        if (Contract.WeekdayParameters.EXERCISE_GOAL.equals(mSettingsColumnName)) {
            createAndShowPointDialogWith(getString(R.string.exercise_goal),
                    0, 99, entity.getExerciseGoal(), EXERCISE_GOAL_SETTINGS_TAG);

        } else if (Contract.WeekdayParameters.LIQUID_GOAL.equals(mSettingsColumnName)) {
            createAndShowIntegerDialogWith(getString(R.string.liquid_goal),
                    0, 99, entity.getLiquidGoal(), LIQUID_GOAL_SETTINGS_TAG);

        } else if (Contract.WeekdayParameters.OIL_GOAL.equals(mSettingsColumnName)) {
            createAndShowIntegerDialogWith(getString(R.string.oil_goal), 0, 99,
                    entity.getOilGoal(), OIL_GOAL_SETTINGS_TAG);

        } else if (Contract.WeekdayParameters.SUPPLEMENT_GOAL.equals(mSettingsColumnName)) {
            createAndShowIntegerDialogWith(getString(R.string.supplement_goal), 0, 99,
                    entity.getSupplementGoal(), SUPPLEMENT_GOAL_SETTINGS_TAG);

        } else if (Contract.WeekdayParameters.BREAKFAST_GOAL.equals(mSettingsColumnName)) {
            createAndShowMealIdealValuesDialogWith(
                    getString(R.string.breakfast_ideal_values), 0, 99,
                    entity.getBreakfastStartTime(), entity.getBreakfastEndTime(),
                    entity.getBreakfastGoal(), BREAKFAST_GOAL_SETTINGS_TAG);

        } else if (Contract.WeekdayParameters.BRUNCH_GOAL.equals(mSettingsColumnName)) {
            createAndShowMealIdealValuesDialogWith(
                    getString(R.string.brunch_ideal_values), 0, 99,
                    entity.getBrunchStartTime(), entity.getBrunchEndTime(),
                    entity.getBrunchGoal(), BRUNCH_GOAL_SETTINGS_TAG);

        } else if (Contract.WeekdayParameters.LUNCH_GOAL.equals(mSettingsColumnName)) {
            createAndShowMealIdealValuesDialogWith(
                    getString(R.string.lunch_ideal_values), 0, 99,
                    entity.getLunchStartTime(), entity.getLunchEndTime(),
                    entity.getLunchGoal(), LUNCH_GOAL_SETTINGS_TAG);

        } else if (Contract.WeekdayParameters.SNACK_GOAL.equals(mSettingsColumnName)) {
            createAndShowMealIdealValuesDialogWith(
                    getString(R.string.snack_ideal_values), 0, 99,
                    entity.getSnackStartTime(), entity.getSnackEndTime(),
                    entity.getSnackGoal(), SNACK_GOAL_SETTINGS_TAG);

        } else if (Contract.WeekdayParameters.DINNER_GOAL.equals(mSettingsColumnName)) {
            createAndShowMealIdealValuesDialogWith(
                    getString(R.string.dinner_ideal_values), 0, 99,
                    entity.getDinnerStartTime(), entity.getDinnerEndTime(),
                    entity.getDinnerGoal(), DINNER_GOAL_SETTINGS_TAG);

        } else if (Contract.WeekdayParameters.SUPPER_GOAL.equals(mSettingsColumnName)) {
            createAndShowMealIdealValuesDialogWith(
                    getString(R.string.supper_ideal_values), 0, 99,
                    entity.getSupperStartTime(), entity.getSupperEndTime(),
                    entity.getSupperGoal(), SUPPER_GOAL_SETTINGS_TAG);

        } else {
            throw new IllegalStateException("Invalid mSettingsColumnName=" + mSettingsColumnName);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void createAndShowMealIdealValuesDialogWith(String title, int minIntegerValue,
                                                        int maxIntegerValue, Integer initialStartTime,
                                                        Integer initialEndTime, Float initialIdealValue,
                                                        String tag) {
        final MealIdealValuesDialog dialog = new MealIdealValuesDialog();
        dialog.setTitle(title);
        dialog.setMinIntegerValue(minIntegerValue);
        dialog.setMaxIntegerValue(maxIntegerValue);
        dialog.setInitialStartTime(initialStartTime);
        dialog.setInitialEndTime(initialEndTime);
        dialog.setInitialIdealValue(initialIdealValue);
        dialog.show(getFragmentManager(), tag);
    }

    @SuppressWarnings("SameParameterValue")
    private void createAndShowIntegerDialogWith(String title, int minValue, int maxValue,
                                                Integer initialValue, String tag) {
        final IntegerDialog dialog = new IntegerDialog();
        dialog.setTitle(title);
        dialog.setMinValue(minValue);
        dialog.setMaxValue(maxValue);
        dialog.setInitialValue(initialValue);
        dialog.show(getFragmentManager(), tag);
    }

    @SuppressWarnings("SameParameterValue")
    private void createAndShowPointDialogWith(String title, int minIntegerValue, int maxIntegerValue,
                                              Float initialValue, String tag) {
        final PointDialog dialog = new PointDialog();
        dialog.setTitle(title);
        dialog.setMinIntegerValue(minIntegerValue);
        dialog.setMaxIntegerValue(maxIntegerValue);
        dialog.setInitialValue(initialValue);
        dialog.show(getFragmentManager(), tag);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case WEEKDAY_PARAMETERS_SETTINGS_LOADER_ID:
                return new CursorLoader(getActivity(), Contract.WeekdayParameters.CONTENT_URI,
                        null, null, null, null);
            default:
                throw new IllegalArgumentException("Invalid loader id=" + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onPointSet(String tag, float actualValue) {
        if (EXERCISE_GOAL_SETTINGS_TAG.equals(tag)) {
            final WeekdayParametersManager manager = new WeekdayParametersManager(getActivity()
                    .getApplicationContext());
            final WeekdayParametersEntity entity =
                    manager.weekdayParametersFromWeekday(mSelectedWeekday);
            entity.setExerciseGoal(actualValue);
            manager.refresh(entity);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
    }

    @Override
    public void onIntegerSet(String tag, int actualValue) {
        final WeekdayParametersManager manager = new WeekdayParametersManager(getActivity()
                .getApplicationContext());
        final WeekdayParametersEntity entity =
                manager.weekdayParametersFromWeekday(mSelectedWeekday);

        if (LIQUID_GOAL_SETTINGS_TAG.equals(tag)) {
            entity.setLiquidGoal(actualValue);
        } else if (OIL_GOAL_SETTINGS_TAG.equals(tag)) {
            entity.setOilGoal(actualValue);
        } else if (SUPPLEMENT_GOAL_SETTINGS_TAG.equals(tag)) {
            entity.setSupplementGoal(actualValue);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }

        manager.refresh(entity);
    }

    @Override
    public void onMealIdealValuesSet(String tag, int actualStartTime, int actualEndTime,
                                     float actualValue) {
        final WeekdayParametersManager manager = new WeekdayParametersManager(getActivity()
                .getApplicationContext());
        final WeekdayParametersEntity entity =
                manager.weekdayParametersFromWeekday(mSelectedWeekday);

        if (BREAKFAST_GOAL_SETTINGS_TAG.equals(tag)) {
            entity.setBreakfastStartTime(actualStartTime);
            entity.setBreakfastEndTime(actualEndTime);
            entity.setBreakfastGoal(actualValue);
        } else if (BRUNCH_GOAL_SETTINGS_TAG.equals(tag)) {
            entity.setBrunchStartTime(actualStartTime);
            entity.setBrunchEndTime(actualEndTime);
            entity.setBrunchGoal(actualValue);
        } else if (LUNCH_GOAL_SETTINGS_TAG.equals(tag)) {
            entity.setLunchStartTime(actualStartTime);
            entity.setLunchEndTime(actualEndTime);
            entity.setLunchGoal(actualValue);
        } else if (SNACK_GOAL_SETTINGS_TAG.equals(tag)) {
            entity.setSnackStartTime(actualStartTime);
            entity.setSnackEndTime(actualEndTime);
            entity.setSnackGoal(actualValue);
        } else if (DINNER_GOAL_SETTINGS_TAG.equals(tag)) {
            entity.setDinnerStartTime(actualStartTime);
            entity.setDinnerEndTime(actualEndTime);
            entity.setDinnerGoal(actualValue);
        } else if (SUPPER_GOAL_SETTINGS_TAG.equals(tag)) {
            entity.setSupperStartTime(actualStartTime);
            entity.setSupperEndTime(actualEndTime);
            entity.setSupperGoal(actualValue);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }

        manager.refresh(entity);
    }

    @Override
    public void onReplyActionFromOtherFragment(String actionTag, Bundle actionData) {
        mSettingsColumnName = actionTag;
        // If not loaded, load the first instance,
        // otherwise closes current loader e start a new one:
        getLoaderManager().restartLoader(WEEKDAY_PARAMETERS_SETTINGS_LOADER_ID, null, this);
    }
}
