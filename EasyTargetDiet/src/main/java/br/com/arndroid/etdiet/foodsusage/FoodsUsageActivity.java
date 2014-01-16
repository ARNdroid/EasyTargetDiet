package br.com.arndroid.etdiet.foodsusage;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.FragmentActionReplier;
import br.com.arndroid.etdiet.action.MenuUtils;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.meals.MealsAdapter;
import br.com.arndroid.etdiet.provider.Contract;

public class FoodsUsageActivity extends ActionBarActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        ActionBar.OnNavigationListener {

    public static final String DATE_ID_ACTION_KEY = FoodsUsageActivity.class.getSimpleName() +
            ".DATE_ID_ACTION_KEY";
    public static final String MEAL_ACTION_KEY = FoodsUsageActivity.class.getSimpleName() +
            ".MEAL_ACTION_KEY";
    private static final int FOODS_USAGE_LOADER_ID = 1;
    
    private String mDateId;
    private FoodsUsageHeaderFragment mHeaderFragment;
    private FoodsUsageListFragment mListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.foods_usage_activity);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        MealsAdapter adapter = new MealsAdapter(this);
        actionBar.setListNavigationCallbacks(adapter, this);

        if(savedInstanceState == null) {
            Bundle data = getIntent().getExtras().getBundle(FragmentActionReplier.ACTION_DATA_KEY);
            mDateId = data.getString(DATE_ID_ACTION_KEY);
            actionBar.setSelectedNavigationItem(data.getInt(MEAL_ACTION_KEY));
        } else {
            mDateId = savedInstanceState.getString(DATE_ID_ACTION_KEY);
            actionBar.setSelectedNavigationItem(savedInstanceState.getInt(MEAL_ACTION_KEY));
        }

        bindScreen();
        // This will initialize the screen:
        getSupportLoaderManager().restartLoader(FOODS_USAGE_LOADER_ID, null, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(DATE_ID_ACTION_KEY, mDateId);
        outState.putInt(MEAL_ACTION_KEY, getSupportActionBar().getSelectedNavigationIndex());

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();

        switch (itemId) {
            case R.id.quick_add:
                MenuUtils.callMenuInFragmentByMethod(getSupportFragmentManager(),
                        R.id.foods_usage_list_fragment, itemId);
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.foods_usage, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(int position, long itemId) {
        // This will reload the screen:
        getSupportLoaderManager().restartLoader(FOODS_USAGE_LOADER_ID, null, this);
        return true;
    }

    private void bindScreen() {
        mHeaderFragment = (FoodsUsageHeaderFragment) getSupportFragmentManager()
                .findFragmentById(R.id.foods_usage_header_fragment);
        mListFragment = (FoodsUsageListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.foods_usage_list_fragment);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case FOODS_USAGE_LOADER_ID:
                final int meal = Meals.getMealFromPosition(getActionBar().getSelectedNavigationIndex());
                return new CursorLoader(this, Contract.FoodsUsage.CONTENT_URI,
                        Contract.FoodsUsage.SIMPLE_LIST_PROJECTION,
                        Contract.FoodsUsage.DATE_ID_AND_MEAL_SELECTION,
                        new String[] {String.valueOf(mDateId), String.valueOf(meal)}, null);
            default:
                throw new IllegalArgumentException("Invalid loader id=" + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor data) {
        mHeaderFragment.onDataChangedFromHolderActivity(mDateId,
                Meals.getMealFromPosition(getActionBar().getSelectedNavigationIndex()), data);
        mListFragment.onDataChangedFromHolderActivity(mDateId,
                Meals.getMealFromPosition(getActionBar().getSelectedNavigationIndex()), data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        // Header fragment doesn't need reset information...
        mListFragment.onDataChangedFromHolderActivity(mDateId,
                Meals.getMealFromPosition(getActionBar().getSelectedNavigationIndex()), null);
    }

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "==>ETD/" + FoodsUsageActivity.class.getSimpleName();

    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;
}