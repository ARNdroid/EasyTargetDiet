package br.com.arndroid.etdiet.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.app.ActionBar;
import android.app.Activity;
import android.view.MenuItem;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.FragmentActionReplier;
import br.com.arndroid.etdiet.dialog.IntegerDialog;
import br.com.arndroid.etdiet.dialog.PointDialog;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.dialog.MealIdealValuesDialog;

public class SettingsListActivity extends Activity implements
        MealIdealValuesDialog.OnMealIdealValuesSetListener,
        PointDialog.OnPointSetListener,
        IntegerDialog.OnIntegerSetListener {

    public static final String SETTINGS_TYPE_PARAMETER = SettingsListActivity.class.getSimpleName()
            + ".SETTINGS_TYPE_PARAMETER";
    private String mSettingsColumnName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_list_activity);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null) {
            Intent intent = getIntent();
            mSettingsColumnName = intent.getExtras().getString(FragmentActionReplier.ACTION_TAG_KEY);
        } else {
            mSettingsColumnName = savedInstanceState.getString(SETTINGS_TYPE_PARAMETER);
        }
        actionBar.setTitle(getTitleFromSettingsType(mSettingsColumnName));
        SettingsListFragment fragment = (SettingsListFragment) getFragmentManager()
                .findFragmentById(R.id.settings_list_fragment);
        fragment.onReplyActionFromOtherFragment(mSettingsColumnName, null);
    }

    private String getTitleFromSettingsType(String settingsColumnName) {
        if (Contract.WeekdayParameters.EXERCISE_GOAL.equals(settingsColumnName)) {
            return getResources().getString(R.string.exercise_goal);
        } else if (Contract.WeekdayParameters.LIQUID_GOAL.equals(settingsColumnName)) {
            return getResources().getString(R.string.liquid_goal);
        } else if (Contract.WeekdayParameters.OIL_GOAL.equals(settingsColumnName)) {
            return getResources().getString(R.string.oil_goal);
        } else if (Contract.WeekdayParameters.SUPPLEMENT_GOAL.equals(settingsColumnName)) {
            return getResources().getString(R.string.supplement_goal);
        } else if (Contract.WeekdayParameters.BREAKFAST_GOAL.equals(settingsColumnName)) {
            return getResources().getString(R.string.breakfast_ideal_values);
        } else if (Contract.WeekdayParameters.BRUNCH_GOAL.equals(settingsColumnName)) {
            return getResources().getString(R.string.brunch_ideal_values);
        } else if (Contract.WeekdayParameters.LUNCH_GOAL.equals(settingsColumnName)) {
            return getResources().getString(R.string.lunch_ideal_values);
        } else if (Contract.WeekdayParameters.SNACK_GOAL.equals(settingsColumnName)) {
            return getResources().getString(R.string.snack_ideal_values);
        } else if (Contract.WeekdayParameters.DINNER_GOAL.equals(settingsColumnName)) {
            return getResources().getString(R.string.dinner_ideal_values);
        } else if (Contract.WeekdayParameters.SUPPER_GOAL.equals(settingsColumnName)) {
            return getResources().getString(R.string.supper_ideal_values);
        } else {
            throw new IllegalArgumentException("Invalid settingsColumnName=" + settingsColumnName);
        }
    }

    @Override
    protected void onSaveInstanceState(@SuppressWarnings("NullableProblems") Bundle outState) {
        outState.putString(SETTINGS_TYPE_PARAMETER, mSettingsColumnName);
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
    public void onPointSet(String tag, float actualValue) {
        // We are here against our will...
        if (tag.startsWith(SettingsListFragment.OWNER_TAG)) {
            ((PointDialog.OnPointSetListener)getFragmentManager().findFragmentById(
                    R.id.settings_list_fragment)).onPointSet(tag, actualValue);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
    }

    @Override
    public void onIntegerSet(String tag, int actualValue) {
        // We are here against our will...
        if (tag.startsWith(SettingsListFragment.OWNER_TAG)) {
            ((IntegerDialog.OnIntegerSetListener)getFragmentManager().findFragmentById(
                    R.id.settings_list_fragment)).onIntegerSet(tag, actualValue);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
    }

    @Override
    public void onMealIdealValuesSet(String tag, int actualStartTime, int actualEndTime, float actualValue) {
        // We are here against our will...
        if (tag.startsWith(SettingsListFragment.OWNER_TAG)) {
            ((MealIdealValuesDialog.OnMealIdealValuesSetListener)getFragmentManager()
                    .findFragmentById(R.id.settings_list_fragment)).onMealIdealValuesSet(
                    tag, actualStartTime, actualEndTime, actualValue);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
    }
}
