package br.com.arndroid.etdiet.foodsusage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.FragmentActionReplier;
import br.com.arndroid.etdiet.action.FragmentMenuReplier;
import br.com.arndroid.etdiet.dialog.QuickInsertAutoDialog;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageManager;
import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.utils.ExposedObservable;

public class FoodsUsageListFragment extends ListFragment implements FragmentMenuReplier,
        LoaderManager.LoaderCallbacks<Cursor>,
        FragmentActionReplier {

    public interface FoodsUsageListFragmentListener {
        public void onListValuesChanged(Cursor data);
    }

    public static final String MEAL_SELECTED_ACTION_TAG = FoodsUsageListFragment.class.getSimpleName() +
            ".MEAL_SELECTED_ACTION_TAG";
    public static final String DATE_ID_ACTION_KEY = FoodsUsageListFragment.class.getSimpleName() +
            ".DATE_ID_ACTION_KEY";
    public static final String MEAL_ACTION_KEY = FoodsUsageListFragment.class.getSimpleName() +
            ".MEAL_ACTION_KEY";

    private static final int FOODS_USAGE_LOADER_ID = 1;

    final private ExposedObservable<FoodsUsageListFragmentListener> mListeners =
            new ExposedObservable<FoodsUsageListFragmentListener>();

    public void registerListener(FoodsUsageListFragmentListener listener) {
        mListeners.registerObserver(listener);
    }

    public void unregisterListener(FoodsUsageListFragmentListener listener) {
        mListeners.unregisterObserver(listener);
    }

    private String mDateId;
    private int mMeal;
    private FoodsUsageListAdapter mAdapter;
    private TextView mTxtEmpty;
    private ListView mLstList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.foods_usage_list_fragment, container, false);
        bindScreen(rootView);
        setupScreen(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new FoodsUsageListAdapter(getActivity());
        setListAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        final FoodsUsageManager manager = new FoodsUsageManager(
                getActivity().getApplicationContext());
        final FoodsUsageEntity entity = manager.foodUsageFromId(id);
        final QuickInsertAutoDialog dialog = new QuickInsertAutoDialog();
        dialog.setFoodsUsageEntity(entity);
        dialog.show(getFragmentManager(), null);
    }

    @Override
    public void onReplyMenuFromHolderActivity(int menuItemId) {
        switch (menuItemId) {
            case R.id.quick_add:
                final FoodsUsageEntity entity = new FoodsUsageEntity(null, mDateId,
                        Meals.getMealFromPosition(mMeal), getDefaultTime(), null,
                        getDefaultValue());
                final QuickInsertAutoDialog dialog = new QuickInsertAutoDialog();
                dialog.setFoodsUsageEntity(entity);
                dialog.show(getFragmentManager(), null);
                break;
            default:
                throw new IllegalArgumentException("Invalid menuItemId=" + menuItemId);
        }
    }

    private void bindScreen(View rootView) {
        mTxtEmpty = (TextView) rootView.findViewById(android.R.id.empty);
        mLstList = (ListView) rootView.findViewById(android.R.id.list);
    }

    private void setupScreen(View rootView) {
        final ListView list = (ListView) rootView.findViewById(android.R.id.list);
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
        mTxtEmpty.setText(getResources().getText(R.string.list_empty_foods_usage));
    }

    private void refreshScreen() {
        if (mLstList.getAdapter().getCount() == 0) {
            mTxtEmpty.setVisibility(View.VISIBLE);
            mLstList.setVisibility(View.GONE);
        } else {
            mTxtEmpty.setVisibility(View.GONE);
            mLstList.setVisibility(View.VISIBLE);
        }
    }

    private float getDefaultValue() {
        return Meals.preferredUsageForMealInDate(getActivity().getApplicationContext(),
                Meals.getMealFromPosition(mMeal),
                DateUtils.dateIdToDate(mDateId));
    }

    private int getDefaultTime() {
        return DateUtils.dateToTimeAsInt(new Date());
    }

    public void onDataChangedFromHolderActivity(String dateId, int meal) {
        mDateId = dateId;
        mMeal = meal;
        // If not loaded, load the first instance,
        // otherwise closes current loader e start a new one:
        getLoaderManager().restartLoader(FOODS_USAGE_LOADER_ID, null, this);
    }

    @Override
    public void onReplyActionFromOtherFragment(String actionTag, Bundle actionData) {
        onDataChangedFromHolderActivity(actionData.getString(DATE_ID_ACTION_KEY),
                actionData.getInt(MEAL_ACTION_KEY));
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
                throw new IllegalArgumentException("Invalid loader id=" + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor data) {
        mAdapter.swapCursor(data);
        refreshScreen();
        for (FoodsUsageListFragmentListener listener : mListeners.getAllObservers()) {
            listener.onListValuesChanged(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }
}