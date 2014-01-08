package br.com.arndroid.etdiet.foodsusage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageManager;
import br.com.arndroid.etdiet.quickinsert.QuickInsertFrag;
import br.com.arndroid.etdiet.util.DateUtil;

public class FoodsUsageActivity extends ActionBarActivity implements
        FoodsUsageListFragment.FoodUsageListFragListener,
        ActionBar.OnNavigationListener {

    public static final String DATE_ID_PARAMETER = FoodsUsageActivity.class.getSimpleName()
            + ".SETTINGS_TYPE_PARAMETER";
    public static final String MEAL_PARAMETER = FoodsUsageActivity.class.getSimpleName()
            + ".MEAL_PARAMETER";

    private FoodsUsageListFragment mFragment;
    private String mDateId;
    private String[] mealsNameList;
    private TextView mTxtDate;
    private TextView mTxtUsed;

    // TODO: refactoring: migration to FoodsUsageListFragment.
    @Override
    public void onFoodUsageSelected(long foodUsageId) {
        QuickInsertFrag dialog = new QuickInsertFrag();
        dialog.setId(foodUsageId);
        dialog.show(getSupportFragmentManager(), QuickInsertFrag.UPDATE_TAG);
    }

    // TODO: refactoring: migration to FoodsUsageListFragment.
    @Override
    public void onFoodUsageLongSelected(final long foodUsageId) {
        final FoodsUsageManager manager = new FoodsUsageManager(getApplicationContext());
        final FoodsUsageEntity entity = manager.foodUsageFromId(foodUsageId);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                manager.remove(foodUsageId);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setMessage(String.format(getResources().getString(R.string.delete_food_usage_msg),
                entity.getDescription(), mealsNameList[entity.getMeal()]));
        builder.create().show();
    }

    // TODO: I think the best choice is menu only in activities. We need to migrate this (to menu response).
    @Override
    public void onQuickAddMenuSelected(String dateId, int time, int meal, String description, float value) {
        FragmentManager manager = getSupportFragmentManager();
        QuickInsertFrag dialog = new QuickInsertFrag();
        dialog.setDateId(dateId);
        dialog.setTime(time);
        dialog.setMeal(meal);
        dialog.setDescription(description);
        dialog.setValue(value);
        dialog.show(manager, QuickInsertFrag.INSERT_TAG);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.foods_usage_activity);

        mealsNameList = getResources().getStringArray(R.array.meals_name_list);

        SpinnerAdapter adapter = ArrayAdapter.createFromResource(this, R.array.meals_name_list,
                android.R.layout.simple_spinner_dropdown_item);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(adapter, this);

        if(savedInstanceState == null) {
            Intent intent = getIntent();
            mDateId = intent.getExtras().getString(DATE_ID_PARAMETER);
            actionBar.setSelectedNavigationItem(intent.getExtras().getInt(MEAL_PARAMETER));
        } else {
            mDateId = savedInstanceState.getString(DATE_ID_PARAMETER);
            actionBar.setSelectedNavigationItem(savedInstanceState.getInt(MEAL_PARAMETER));
        }

        bindScreen();
        refreshScreen();

        mFragment = (FoodsUsageListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.foods_usage_list_frag);
        mFragment.refreshScreen(mDateId, actionBar.getSelectedNavigationIndex());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(DATE_ID_PARAMETER, mDateId);
        outState.putInt(MEAL_PARAMETER, getSupportActionBar().getSelectedNavigationIndex());

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDataLoadFinished(Cursor data) {
        // Data from origin has been changed. Update fields:
        float totalUsed = 0.0f;
        if (data.moveToFirst()) {
            do {
                totalUsed += data.getFloat(data.getColumnIndex(Contract.FoodsUsage.VALUE));
            } while (data.moveToNext());
        }
        mTxtUsed.setText(String.format(getString(R.string.units_actual_value), totalUsed));
    }

    @Override
    public boolean onNavigationItemSelected(int position, long itemId) {
        // TODO: the following logic needs change to use itemId
        mFragment.refreshScreen(mDateId, position);
        return true;
    }

    private void bindScreen() {
        mTxtDate = (TextView) findViewById(R.id.txtDate);
        mTxtUsed = (TextView) findViewById(R.id.txtUsed);
    }

    private void refreshScreen() {
        mTxtDate.setText(DateUtil.dateIdToFormattedString(mDateId));
    }

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "==>ETD/" + FoodsUsageActivity.class.getSimpleName();

    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;
}
