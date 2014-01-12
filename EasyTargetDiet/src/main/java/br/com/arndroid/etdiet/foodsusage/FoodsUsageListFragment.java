package br.com.arndroid.etdiet.foodsusage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.FragmentReplier;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageManager;
import br.com.arndroid.etdiet.quickinsert.QuickInsertFrag;
import br.com.arndroid.etdiet.util.DateUtil;
import br.com.arndroid.etdiet.util.ExposedObservable;

public class FoodsUsageListFragment extends ListFragment implements
        FragmentReplier,
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String DATE_ID_ACTION_KEY = FoodsUsageListFragment.class.getSimpleName() +
            ".DATE_ID_ACTION_KEY";
    public static final String MEAL_ACTION_KEY = FoodsUsageListFragment.class.getSimpleName() +
            ".MEAL_ACTION_KEY";
    private static final int FOODS_USAGE_LOADER_ID = 1;

    private String mDateId;
    private int mMeal;
    private FoodsUsageListAdapter mAdapter;
    private ExposedObservable<FoodUsageListFragmentListener> mListeners =
            new ExposedObservable<FoodUsageListFragmentListener>();

    public void registerListener(FoodUsageListFragmentListener listener) {
        mListeners.registerObserver(listener);
    }

    public void unregisterListener(FoodUsageListFragmentListener listener) {
        mListeners.unregisterObserver(listener);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText(getResources().getText(R.string.list_empty_foods_usage));

        mAdapter = new FoodsUsageListAdapter(getActivity());
        setListAdapter(mAdapter);

        ListView list = getListView();
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // We need a final variable to use inside alert onClick event:
                final long foodId = id;
                final FoodsUsageManager manager = new FoodsUsageManager(
                        getActivity().getApplicationContext());
                final FoodsUsageEntity entity = manager.foodUsageFromId(foodId);
                final String mealName = FoodsUsageListFragment.this.getString(
                        Meals.getMealResourceNameIdFromMealId(entity.getMeal()));

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        manager.remove(foodId);
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);
                builder.setMessage(String.format(getResources().getString(R.string.delete_food_usage_msg),
                        entity.getDescription(), mealName));
                builder.create().show();
                return true;
            }
        });
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        QuickInsertFrag dialog = new QuickInsertFrag();
        dialog.setId(id);
        dialog.show(getActivity().getSupportFragmentManager(), QuickInsertFrag.UPDATE_TAG);
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
        for (FoodUsageListFragmentListener listener : mListeners.getAllObservers()) {
            listener.onListValuesChanged(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onReplyAction(String actionTag, Bundle actionData) {
        mDateId = actionData.getString(DATE_ID_ACTION_KEY);
        mMeal = actionData.getInt(MEAL_ACTION_KEY);
        // If not loaded, load the first instance,
        // otherwise closes current loader e start a new one:
        getLoaderManager().restartLoader(FOODS_USAGE_LOADER_ID, null, this);
    }

    public interface FoodUsageListFragmentListener {
        public void onListValuesChanged(Cursor data);
    }
}
