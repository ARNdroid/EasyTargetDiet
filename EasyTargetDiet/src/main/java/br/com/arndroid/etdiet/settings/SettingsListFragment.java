package br.com.arndroid.etdiet.settings;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersEntity;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersManager;
import br.com.arndroid.etdiet.util.PointPickerDialog;

public class SettingsListFragment extends ListFragment implements PointPickerDialog.OnPointSetListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String OWNER_TAG = SettingsListFragment.class.getSimpleName();
    public static final String EXERCISE_GOAL_SETTINGS_TAG = OWNER_TAG + ".EXERCISE_GOAL_SETTINGS";

    private static final int WEEKDAY_PARAMETERS_SETTINGS_LOADER_ID = 1;
    private static final String SELECTED_WEEKDAY_KEY = "SELECTED_WEEKDAY_KEY";

    /*
     We don't need to restore mSettingsColumnName from a saved instance state because the host activity
     is doing this and calling us with refresh(settingsColumn).
     */
    private String mSettingsColumnName;

    private int mSelectedWeekday;

    private SettingsWeekdayAdapter mAdapter;

    public void refresh(String settingsColumn) {
        mSettingsColumnName = settingsColumn;
        // If not loaded, load the first instance,
        // otherwise closes current loader e start a new one:
        getLoaderManager().restartLoader(WEEKDAY_PARAMETERS_SETTINGS_LOADER_ID, null, this);
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
            final PointPickerDialog dialog = new PointPickerDialog();
            dialog.setTitle(getResources().getString(R.string.exercise_goal));
            dialog.setMinIntegerValue(0);
            dialog.setMaxIntegerValue(99);
            dialog.setInitialValue(entity.getExerciseGoal());
            dialog.show(getFragmentManager(), EXERCISE_GOAL_SETTINGS_TAG);
        } else {
            throw new IllegalStateException("Invalid mSettingsColumnName=" + mSettingsColumnName);
        }
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
            final WeekdayParametersEntity entity = manager.weekdayParametersFromWeekday(mSelectedWeekday);
            entity.setExerciseGoal(actualValue);
            manager.refresh(entity);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
    }
}
