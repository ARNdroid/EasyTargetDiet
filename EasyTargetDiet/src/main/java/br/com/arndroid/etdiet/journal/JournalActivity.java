package br.com.arndroid.etdiet.journal;

import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
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
import br.com.arndroid.etdiet.foodsusage.FoodsUsageListFragment;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.settings.SettingsMainActivity;
import br.com.arndroid.etdiet.utils.CurrentDateId;
import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.virtualweek.DaySummary;
import br.com.arndroid.etdiet.virtualweek.VirtualWeek;
import br.com.arndroid.etdiet.weights.WeightsActivity;

public class JournalActivity extends Activity implements
        JournalMyPointsFragment.JournalFragmentListener,
        VirtualWeek.ViewObserver,
        DateDialog.OnDateSetListener,
        IntegerDialog.OnIntegerSetListener,
        TextDialog.OnTextSetListener,
        ActivityActionCaller {

    private VirtualWeek mVirtualWeek;
    private CurrentDateId mCurrentDateId = new CurrentDateId();
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

        mCurrentDateId.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentMeal = savedInstanceState.getInt(Contract.FoodsUsage.MEAL);
        } else {
            final Date currentDate = new Date();
            mCurrentMeal = Meals.preferredMealForTimeInDate(this,
                    DateUtils.dateToTimeAsInt(currentDate), currentDate, false);
        }

        bindScreen();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        mCurrentDateId.onSaveInstanceState(outState);
        outState.putInt(Contract.FoodsUsage.MEAL, mCurrentMeal);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mVirtualWeek = VirtualWeek.getInstance(getApplicationContext());
        mVirtualWeek.registerViewObserver(this);
        mVirtualWeek.requestSummaryForObserverAndDateId(this, mCurrentDateId.getCurrentDateId());
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
                MenuUtils.callMenuInFragmentByMethod(getFragmentManager(),
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
        mJournalMyPointsFragment = (JournalMyPointsFragment) getFragmentManager()
                .findFragmentById(R.id.journal_my_points_fragment);
        mJournalMyGoalsFragment = (JournalMyGoalsFragment) getFragmentManager()
                .findFragmentById(R.id.journal_my_goals_fragment);
        mJournalMyMealsFragment = (JournalMyMealsFragment) getFragmentManager()
                .findFragmentById(R.id.journal_my_meals_fragment);
        mJournalNotesFragment = (JournalNotesFragment) getFragmentManager()
                .findFragmentById(R.id.journal_notes_fragment);
        mFoodsUsageListFragment = (FoodsUsageListFragment) getFragmentManager()
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
        mVirtualWeek.requestSummaryForObserverAndDateId(this, mCurrentDateId.getCurrentDateId());
    }

    @Override
    public void onFoodsUsageChanged(DaySummary summary) {
        mVirtualWeek.requestSummaryForObserverAndDateId(this, mCurrentDateId.getCurrentDateId());
    }

    @Override
    public void onParametersChanged() {
        mVirtualWeek.requestSummaryForObserverAndDateId(this, mCurrentDateId.getCurrentDateId());
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
            ((DateDialog.OnDateSetListener) getFragmentManager()
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
            ((IntegerDialog.OnIntegerSetListener) getFragmentManager()
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
            ((TextDialog.OnTextSetListener) getFragmentManager()
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
                mFoodsUsageListFragment.onDataChangedFromHolderActivity(mCurrentDateId.getCurrentDateId(),
                        mCurrentMeal);
            } else {
                ActionUtils.callActionInFragmentByIntent(this, holderActivityClass, actionTag,
                        actionData);
            }
        } else {
            // For all other tags that's the best we can do...
            ActionUtils.callActionInFragment(this, getFragmentManager(), fragmentId,
                    holderActivityClass, actionTag, actionData);
        }
    }

    @Override
    public void onDateChanged(Date newDate) {
        mCurrentDateId.setCurrentDateId(DateUtils.dateToDateId(newDate));
        mVirtualWeek.requestSummaryForObserverAndDateId(this, mCurrentDateId.getCurrentDateId());
    }
}
