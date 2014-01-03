package br.com.arndroid.etdiet.settings;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.ActivityCaller;
import br.com.arndroid.etdiet.action.SimpleActivityCaller;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.dialog.PointPickerDialog;
import br.com.arndroid.etdiet.dialog.StringListDialog;

public class SettingsMainActivity extends ActionBarActivity implements
        PointPickerDialog.OnPointSetListener,
        StringListDialog.OnStringSelectedListener,
        SettingsMainFragment.SettingsMainFragmentListener,
        ActivityCaller {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_main_activity);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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
        /*
         We are here against our will!
         The recommended approach to deal with Dialog Fragments is: use of host activity
         to listen the dialog confirmation.
         This leave us with an event generation inside a Fragment and event handling inside an
         Activity.
         The following code send the event to original Fragment again.
         */
        if (tag.startsWith(SettingsMainFragment.OWNER_TAG)) {
            ((PointPickerDialog.OnPointSetListener) getSupportFragmentManager()
                    .findFragmentById(R.id.settings_fragment)).onPointSet(tag, actualValue);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
    }

    @Override
    public void onStringSelected(String tag, int chosenIndex) {
        // We are here against our will...
        if (tag.startsWith(SettingsMainFragment.OWNER_TAG)) {
            ((StringListDialog.OnStringSelectedListener) getSupportFragmentManager()
                    .findFragmentById(R.id.settings_fragment)).onStringSelected(tag, chosenIndex);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
    }

    private void callListFragmentForSetting(String settingName) {
        SettingsListFragment settingsListFragment = (SettingsListFragment)
                getSupportFragmentManager().findFragmentById(R.id.settings_list_fragment);

        if (settingsListFragment != null) {
            settingsListFragment.refresh(settingName);
        } else {
            Intent intent = new Intent(this, SettingsListActivity.class);
            intent.putExtra(SettingsListActivity.SETTINGS_TYPE_PARAMETER, settingName);
            startActivity(intent);
        }
    }

    @Override
    public void onExerciseGoalSettingsSelected() {
        callListFragmentForSetting(Contract.WeekdayParameters.EXERCISE_GOAL);
    }

    @Override
    public void onLiquidGoalSettingsSelected() {
        callListFragmentForSetting(Contract.WeekdayParameters.LIQUID_GOAL);
    }

    @Override
    public void onOilGoalSettingsSelected() {
        callListFragmentForSetting(Contract.WeekdayParameters.OIL_GOAL);
    }

    @Override
    public void onSupplementGoalSettingsSelected() {
        callListFragmentForSetting(Contract.WeekdayParameters.SUPPLEMENT_GOAL);
    }

    @Override
    public void onBreakfastIdealValuesSelected() {
        callListFragmentForSetting(Contract.WeekdayParameters.BREAKFAST_GOAL);
    }

    @Override
    public void onBrunchIdealValuesSelected() {
        callListFragmentForSetting(Contract.WeekdayParameters.BRUNCH_GOAL);
    }

    @Override
    public void onLunchIdealValuesSelected() {
        callListFragmentForSetting(Contract.WeekdayParameters.LUNCH_GOAL);
    }

    @Override
    public void onSnackIdealValuesSelected() {
        callListFragmentForSetting(Contract.WeekdayParameters.SNACK_GOAL);
    }

    @Override
    public void onDinnerIdealValuesSelected() {
        callListFragmentForSetting(Contract.WeekdayParameters.DINNER_GOAL);
    }

    @Override
    public void onSupperIdealValuesSelected() {
        callListFragmentForSetting(Contract.WeekdayParameters.SUPPER_GOAL);
    }

    @Override
    public void onCallAction(int fragmentId, Class holderActivityClass, String actionTag,
                             Bundle actionData) {

        new SimpleActivityCaller().onCallAction(this, getSupportFragmentManager(), fragmentId,
                holderActivityClass, actionTag, actionData);
    }

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "==>ETD/" + SettingsMainActivity.class.getSimpleName();

    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;
}
