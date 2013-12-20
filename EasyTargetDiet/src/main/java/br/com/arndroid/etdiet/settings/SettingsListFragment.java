package br.com.arndroid.etdiet.settings;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.util.DateUtil;

public class SettingsListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int WEEKDAY_PARAMETERS_SETTINGS_LOADER_ID = 1;
    private int mSettingsType;
    private SimpleCursorAdapter mAdapter;

    public void refresh(int settingsType) {
        mSettingsType = settingsType;
        // If not loaded, load the first instance,
        // otherwise closes current loader e start a new one:
        getLoaderManager().restartLoader(WEEKDAY_PARAMETERS_SETTINGS_LOADER_ID, null, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*
         We don't need to restore from a saved instance state because the host activity
         is doing this and calling us with refresh(settingsType).
         */

        // TODO: change to custom adapter:
        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_2, null,
                new String[] {Contract.WeekdayParameters._ID, Contract.WeekdayParameters.EXERCISE_GOAL},
                new int[] {android.R.id.text1, android.R.id.text2}, 0);
        setListAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        switch (mSettingsType) {
            // TODO: show dialog.
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
}
