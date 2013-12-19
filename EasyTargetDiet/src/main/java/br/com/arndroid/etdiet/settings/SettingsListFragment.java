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

    static private final int FOODS_USAGE_LOADER_ID = 1;
    private String mDateId;
    private int mMeal;
    private SimpleCursorAdapter mAdapter;
    private OnFoodUsageListFragListener mListener;

    public void refresh(String dateId, int meal) {
        mDateId = dateId;
        mMeal = meal;
        // If not loaded, load the first instance,
        // otherwise closes current loader e start a new one:
        getLoaderManager().restartLoader(FOODS_USAGE_LOADER_ID, null, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText(getResources().getText(R.string.list_empty_foods_usage));
        setHasOptionsMenu(true);

        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_2, null,
                new String[] {Contract.FoodsUsage.DESCRIPTION, Contract.FoodsUsage.VALUE},
                new int[] {android.R.id.text1, android.R.id.text2}, 0);
        setListAdapter(mAdapter);

        ListView list = getListView();
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onFoodUsageLongSelected(id);
                return true;
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnFoodUsageListFragListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " +
                    OnFoodUsageListFragListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.foods_usage_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quick_add:
                mListener.onQuickAddMenuSelected(mDateId, getDefaultTime(), mMeal,
                        null, getDefaultValue());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private float getDefaultValue() {
        return Meals.preferredUsageForMealInDate(this.getActivity().getApplicationContext(),
                mMeal, DateUtil.dateIdToDate(mDateId));
    }

    private int getDefaultTime() {
        return DateUtil.dateToTimeAsInt(new Date());
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        // Send the event and id to the host activity
        mListener.onFoodUsageSelected(id);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case FOODS_USAGE_LOADER_ID:
                return new CursorLoader(getActivity(), Contract.FoodsUsage.CONTENT_URI,
                        Contract.FoodsUsage.SIMPLE_LIST_PROJECTION,
                        Contract.FoodsUsage.DATE_ID_AND_MEAL_SELECTION,
                        new String[] {String.valueOf(mDateId), String.valueOf(mMeal)}, null);
            default:
                throw new IllegalArgumentException("Invalid loader id '" + id + "'");
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

    public interface OnFoodUsageListFragListener {
        public void onFoodUsageSelected(long foodUsageId);

        public void onFoodUsageLongSelected(long foodUsageId);

        public void onQuickAddMenuSelected(String dayId, int time, int meal, String description,
                                           float value);
    }
}
