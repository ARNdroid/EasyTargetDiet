package br.com.arndroid.etdiet.foodsusage;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.FragmentActionReplier;
import br.com.arndroid.etdiet.action.SimpleActivityMenuCaller;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.meals.MealsAdapter;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.provider.days.DaysManager;
import br.com.arndroid.etdiet.quickinsert.QuickInsertFrag;
import br.com.arndroid.etdiet.util.DateUtil;

public class FoodsUsageActivity extends ActionBarActivity implements
        FoodsUsageListFragment.FoodUsageListFragmentListener,
        ActionBar.OnNavigationListener {

    private FoodsUsageListFragment mFragment;
    private String mDateId;
    private TextView mTxtDate;
    private TextView mTxtUsed;
    private TextView mTxtGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.foods_usage_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        MealsAdapter adapter = new MealsAdapter(this);
        actionBar.setListNavigationCallbacks(adapter, this);

        if(savedInstanceState == null) {
            Bundle data = getIntent().getExtras().getBundle(FragmentActionReplier.ACTION_DATA_KEY);
            mDateId = data.getString(FoodsUsageListFragment.DATE_ID_ACTION_KEY);
            actionBar.setSelectedNavigationItem(data.getInt(FoodsUsageListFragment.MEAL_ACTION_KEY));
        } else {
            mDateId = savedInstanceState.getString(FoodsUsageListFragment.DATE_ID_ACTION_KEY);
            actionBar.setSelectedNavigationItem(savedInstanceState.getInt(
                    FoodsUsageListFragment.MEAL_ACTION_KEY));
        }

        bindScreen();

        mFragment = (FoodsUsageListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.foods_usage_list_fragment);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(FoodsUsageListFragment.DATE_ID_ACTION_KEY, mDateId);
        outState.putInt(FoodsUsageListFragment.MEAL_ACTION_KEY,
                getSupportActionBar().getSelectedNavigationIndex());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFragment.registerListener(this);
        callFoodsUsageListFragmentToReplyAction(mDateId,
                getSupportActionBar().getSelectedNavigationIndex());
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFragment.unregisterListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        final Class holderActivityClass;
        final int fragmentId;

        switch (itemId) {
            case R.id.quick_add:
                holderActivityClass = FoodsUsageActivity.class;
                fragmentId = R.id.foods_usage_list_fragment;
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        new SimpleActivityMenuCaller().onCallMenu(this, getSupportFragmentManager(), fragmentId,
                holderActivityClass, itemId);
        return true;
    }

    @Override
    public void onListValuesChanged(Cursor data) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.foods_usage, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(int position, long itemId) {
        final int meal = (int) itemId;
        refreshScreen(meal);
        callFoodsUsageListFragmentToReplyAction(mDateId, meal);
        return true;
    }

    private void callFoodsUsageListFragmentToReplyAction(String dateId, int meal) {
        final Bundle data = new Bundle();
        data.putString(FoodsUsageListFragment.DATE_ID_ACTION_KEY, dateId);
        data.putInt(FoodsUsageListFragment.MEAL_ACTION_KEY, meal);
        mFragment.onReplyActionFromOtherFragment(null, data);
    }

    private void bindScreen() {
        mTxtDate = (TextView) findViewById(R.id.txtDate);
        mTxtUsed = (TextView) findViewById(R.id.txtUsed);
        mTxtGoal = (TextView) findViewById(R.id.txtGoal);
    }

    private void refreshScreen(int meal) {
        mTxtDate.setText(DateUtil.dateIdToFormattedString(mDateId));
        DaysEntity entity = new DaysManager(this).dayFromDateId(mDateId);
        float ideal;
        int startTime, endTime;
        switch (meal) {
            case Meals.BREAKFAST:
                ideal = entity.getBreakfastGoal();
                startTime = entity.getBreakfastStartTime();
                endTime = entity.getBreakfastEndTime();
                break;
            case Meals.BRUNCH:
                ideal = entity.getBrunchGoal();
                startTime = entity.getBrunchStartTime();
                endTime = entity.getBrunchEndTime();
                break;
            case Meals.LUNCH:
                ideal = entity.getLunchGoal();
                startTime = entity.getLunchStartTime();
                endTime = entity.getLunchEndTime();
                break;
            case Meals.SNACK:
                ideal = entity.getSnackGoal();
                startTime = entity.getSnackStartTime();
                endTime = entity.getSnackEndTime();
                break;
            case Meals.DINNER:
                ideal = entity.getDinnerGoal();
                startTime = entity.getDinnerStartTime();
                endTime = entity.getDinnerEndTime();
                break;
            case Meals.SUPPER:
                ideal = entity.getSupperGoal();
                startTime = entity.getSupperStartTime();
                endTime = entity.getSupperEndTime();
                break;
            case Meals.EXERCISE:
                ideal = entity.getExerciseGoal();
                startTime = -1;
                endTime = -1;
                break;
            default:
                throw new IllegalArgumentException("Invalid meal=" + meal);
        }
        if (startTime >= 0) {
            mTxtGoal.setText(String.format(getResources().getString(R.string.meal_ideal_actual_values),
                    ideal, DateUtil.timeToFormattedString(startTime),
                    DateUtil.timeToFormattedString(endTime)));
        } else {
            mTxtGoal.setText(String.format(getResources().getString(R.string.units_actual_value), ideal));
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "==>ETD/" + FoodsUsageActivity.class.getSimpleName();

    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;
}