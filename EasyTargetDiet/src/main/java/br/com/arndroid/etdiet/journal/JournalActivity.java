package br.com.arndroid.etdiet.journal;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.ActionUtils;
import br.com.arndroid.etdiet.action.ActivityActionCaller;
import br.com.arndroid.etdiet.action.MenuUtils;
import br.com.arndroid.etdiet.dialog.DateDialog;
import br.com.arndroid.etdiet.dialog.IntegerDialog;
import br.com.arndroid.etdiet.dialog.TextDialog;
import br.com.arndroid.etdiet.foodsusage.FoodsUsageActivity;
import br.com.arndroid.etdiet.foodsusage.FoodsUsageListFragment;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.settings.SettingsMainActivity;
import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.virtualweek.DaySummary;
import br.com.arndroid.etdiet.virtualweek.VirtualWeek;
import br.com.arndroid.etdiet.weights.WeightsActivity;

public class JournalActivity extends ActionBarActivity implements
        JournalMyPointsFragment.JournalFragmentListener,
        VirtualWeek.ViewObserver,
        DateDialog.OnDateSetListener,
        IntegerDialog.OnIntegerSetListener,
        TextDialog.OnTextSetListener,
        ActivityActionCaller {

    private VirtualWeek mVirtualWeek;
    private String mCurrentDateId;
    private int mCurrentMeal;
    private JournalMyPointsFragment mJournalMyPointsFragment;
    private JournalMyGoalsFragment mJournalMyGoalsFragment;
    private JournalMyMealsFragment mJournalMyMealsFragment;
    private JournalNotesFragment mJournalNotesFragment;
    private FoodsUsageListFragment mFoodsUsageListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.journal_activity);

        if (savedInstanceState != null) {
            mCurrentDateId = savedInstanceState.getString(Contract.Days.DATE_ID);
            mCurrentMeal = savedInstanceState.getInt(Contract.FoodsUsage.MEAL);
        } else {
            final Date currentDate = new Date();
            mCurrentDateId = DateUtils.dateToDateId(currentDate);
            mCurrentMeal = Meals.preferredMealForTimeInDate(this,
                    DateUtils.dateToTimeAsInt(currentDate), currentDate);
        }

        bindScreen();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(Contract.Days.DATE_ID, mCurrentDateId);
        outState.putInt(Contract.FoodsUsage.MEAL, mCurrentMeal);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mVirtualWeek = VirtualWeek.getInstance(getApplicationContext());
        mVirtualWeek.registerViewObserver(this);
        mVirtualWeek.requestSummaryForDateId(this, mCurrentDateId);
    }

    @Override
    public void onPause() {
        mVirtualWeek.unregisterViewObserver(this);
        mVirtualWeek = null;
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.journal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();

        switch (itemId) {
            case R.id.quick_add:
                MenuUtils.callMenuInFragmentByMethod(getSupportFragmentManager(),
                        R.id.journal_my_points_fragment, itemId);
                return true;
            case R.id.weight:
                MenuUtils.callMenuInFragmentByIntent(this, WeightsActivity.class, itemId);
                return true;
            case R.id.settings:
                MenuUtils.callMenuInFragmentByIntent(this, SettingsMainActivity.class, itemId);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void bindScreen() {
        mJournalMyPointsFragment = (JournalMyPointsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.journal_my_points_fragment);
        mJournalMyGoalsFragment = (JournalMyGoalsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.journal_my_goals_fragment);
        mJournalMyMealsFragment = (JournalMyMealsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.journal_my_meals_fragment);
        mJournalNotesFragment = (JournalNotesFragment) getSupportFragmentManager()
                .findFragmentById(R.id.journal_notes_fragment);
        mFoodsUsageListFragment = (FoodsUsageListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.foods_usage_list_fragment);
    }

    private void refreshScreen(DaySummary daySummary) {
        mJournalMyPointsFragment.refreshScreen(daySummary);
        mJournalMyGoalsFragment.refreshScreen(daySummary);
        mJournalMyMealsFragment.refreshScreen(daySummary);
        mJournalNotesFragment.refreshScreen(daySummary);
        // mFoodsUsageListFragment doesn't need refresh (it's listener by CursorLoader)
    }

    @Override
    public void onDayChanged(DaySummary summary) {
        mVirtualWeek.requestSummaryForDateId(this, mCurrentDateId);
    }

    @Override
    public void onFoodsUsageChanged(DaySummary summary) {
        mVirtualWeek.requestSummaryForDateId(this, mCurrentDateId);
    }

    @Override
    public void onParametersChanged() {
        mVirtualWeek.requestSummaryForDateId(this, mCurrentDateId);
    }

    @Override
    public void onSummaryRequested(DaySummary daySummary) {
        refreshScreen(daySummary);
    }

    @Override
    public void onDateSet(String tag, Date actualDate) {
        /*
         We are here against our will!
         The recommended approach to deal with Dialog Fragments is: use of host activity
         to listen the dialog confirmation.
         This leave us with an event generation inside a Fragment and event handling inside an
         Activity.
         The following code send the event to original Fragment again.
         */
        if (tag.startsWith(JournalMyPointsFragment.OWNER_TAG)) {
            ((DateDialog.OnDateSetListener) getSupportFragmentManager()
                    .findFragmentById(R.id.journal_my_points_fragment)).onDateSet(tag, actualDate);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
    }

    @Override
    public void onIntegerSet(String tag, int actualValue) {
        /*
         We are here against our will!
         The recommended approach to deal with Dialog Fragments is: use of host activity
         to listen the dialog confirmation.
         This leave us with an event generation inside a Fragment and event handling inside an
         Activity.
         The following code send the event to original Fragment again.
         */
        if (tag.startsWith(JournalMyGoalsFragment.OWNER_TAG)) {
            ((IntegerDialog.OnIntegerSetListener) getSupportFragmentManager()
                    .findFragmentById(R.id.journal_my_goals_fragment)).onIntegerSet(tag, actualValue);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
    }

    @Override
    public void onTextSet(String tag, String actualText) {
        /*
         We are here against our will!
         The recommended approach to deal with Dialog Fragments is: use of host activity
         to listen the dialog confirmation.
         This leave us with an event generation inside a Fragment and event handling inside an
         Activity.
         The following code send the event to original Fragment again.
         */
        if (tag.startsWith(JournalNotesFragment.OWNER_TAG)) {
            ((TextDialog.OnTextSetListener) getSupportFragmentManager()
                    .findFragmentById(R.id.journal_notes_fragment)).onTextSet(tag, actualText);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
    }

    @Override
    public void onCallAction(int fragmentId, Class holderActivityClass, String actionTag,
                             Bundle actionData) {
        if (FoodsUsageListFragment.MEAL_SELECTED_ACTION_TAG.equals(actionTag)) {
            mCurrentMeal = actionData.getInt(FoodsUsageListFragment.MEAL_ACTION_KEY);
            /*
              We had problems checking only if mFoodsUsageListFragment != null.
              If the orientation change to landscape in a 7'' device the layout journal_activity.xml
              has this fragment but if orientation change again, the layout doesn't have BUT
              mFoodsUsageFragment != null wherever you check it. The addition check for isInLayout()
              solved the problem.
              At this point we have a 7'' layout only to proof our design for Activities and Fragments
              and we believe in final design for tablets we need don't remove content (fragment) if
              the orientation changes (It's a design philosophy by Android Developers).
             */
            if (mFoodsUsageListFragment != null && mFoodsUsageListFragment.isInLayout()) {
                mFoodsUsageListFragment.onDataChangedFromHolderActivity(mCurrentDateId, mCurrentMeal);
            } else {
                ActionUtils.callActionInFragmentByIntent(this, holderActivityClass, actionTag,
                        actionData);
            }
        } else {
            // For all other tags that's the best we can do...
            ActionUtils.callActionInFragment(this, getSupportFragmentManager(), fragmentId,
                    holderActivityClass, actionTag, actionData);
        }
    }

    @Override
    public void onDateChanged(Date newDate) {
        mCurrentDateId = DateUtils.dateToDateId(newDate);
        mVirtualWeek.requestSummaryForDateId(this, mCurrentDateId);
    }
}
