package br.com.arndroid.etdiet.foodsusage;

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

public class FoodsUsageListFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FOODS_USAGE_LOADER_ID = 1;

    private String mDateId;
    private int mMeal;
    private SimpleCursorAdapter mAdapter;

    public void refreshScreen(String dateId, int meal) {
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
        // TODO: I think the best choice is menu only in activities. We need to migrate this.
        setHasOptionsMenu(true);

        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.item_food_usage_list, null,
                new String[] {Contract.FoodsUsage.DESCRIPTION, Contract.FoodsUsage.VALUE},
                new int[] {R.id.lblFoodDescription, R.id.lblFoodValue}, 0);
        setListAdapter(mAdapter);

        ListView list = getListView();
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ((FoodUsageListFragListener) getActivity()).onFoodUsageLongSelected(id);
                return true;
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof FoodUsageListFragListener)) {
            throw new ClassCastException(activity.toString() + " must implement " +
                    FoodUsageListFragListener.class.getSimpleName());
        }
    }

    // TODO: I think the best choice is menu only in activities. We need to migrate this.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.foods_usage_list, menu);
    }

    // TODO: I think the best choice is menu only in activities. We need to migrate this.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quick_add:
                ((FoodUsageListFragListener) getActivity()).onQuickAddMenuSelected(mDateId,
                        getDefaultTime(), mMeal, null, getDefaultValue());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // TODO: I think the best choice is menu only in activities. We need to migrate this.
    private float getDefaultValue() {
        return Meals.preferredUsageForMealInDate(this.getActivity().getApplicationContext(),
                mMeal, DateUtil.dateIdToDate(mDateId));
    }

    // TODO: I think the best choice is menu only in activities. We need to migrate this.
    private int getDefaultTime() {
        return DateUtil.dateToTimeAsInt(new Date());
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        // Send the event and id to the host activity
        ((FoodUsageListFragListener) getActivity()).onFoodUsageSelected(id);
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
        ((FoodUsageListFragListener) getActivity()).onDataLoadFinished(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

    public interface FoodUsageListFragListener {
        public void onDataLoadFinished(Cursor data);

        public void onFoodUsageSelected(long foodUsageId);

        public void onFoodUsageLongSelected(long foodUsageId);

        public void onQuickAddMenuSelected(String dayId, int time, int meal, String description,
                                           float value);
    }
}
