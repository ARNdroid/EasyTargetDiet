package br.com.arndroid.etdiet.settings;

import android.content.Intent;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.FragmentActionReplier;
import br.com.arndroid.etdiet.dialog.IntegerDialog;
import br.com.arndroid.etdiet.dialog.PointDialog;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.dialog.MealIdealValuesDialog;
import br.com.arndroid.etdiet.utils.NavigationUtils;

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
        switch (settingsColumnName) {
            case Contract.WeekdayParameters.EXERCISE_GOAL:
                return getString(R.string.exercise_goal);
            case Contract.WeekdayParameters.LIQUID_GOAL:
                return getString(R.string.liquid_goal);
            case Contract.WeekdayParameters.OIL_GOAL:
                return getString(R.string.oil_goal);
            case Contract.WeekdayParameters.SUPPLEMENT_GOAL:
                return getString(R.string.supplement_goal);
            case Contract.WeekdayParameters.BREAKFAST_GOAL:
                return getString(R.string.breakfast_ideal_values);
            case Contract.WeekdayParameters.BRUNCH_GOAL:
                return getString(R.string.brunch_ideal_values);
            case Contract.WeekdayParameters.LUNCH_GOAL:
                return getString(R.string.lunch_ideal_values);
            case Contract.WeekdayParameters.SNACK_GOAL:
                return getString(R.string.snack_ideal_values);
            case Contract.WeekdayParameters.DINNER_GOAL:
                return getString(R.string.dinner_ideal_values);
            case Contract.WeekdayParameters.SUPPER_GOAL:
                return getString(R.string.supper_ideal_values);
            default:
                throw new IllegalArgumentException("Invalid settingsColumnName=" + settingsColumnName);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(SETTINGS_TYPE_PARAMETER, mSettingsColumnName);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavigationUtils.navigateUpFromSameTaskPreservingScreenState(this);
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
