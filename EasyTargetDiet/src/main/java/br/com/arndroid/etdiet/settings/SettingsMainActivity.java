package br.com.arndroid.etdiet.settings;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.NumberPicker;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.util.OldIntegerPickerDialog;
import br.com.arndroid.etdiet.util.PointPickerDialog;

public class SettingsMainActivity extends ActionBarActivity implements
        PointPickerDialog.OnPointSetListener,
        OldIntegerPickerDialog.OnIntegerSetListener,
        SettingsMainFragment.SettingsMainFragmentListener {

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
            final SettingsMainFragment fragment = (SettingsMainFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.settings_fragment);
            fragment.onPointSet(tag, actualValue);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
    }

    @Override
    public void onNumberSet(NumberPicker view, int value) {
        // TODO: waiting for OldIntegerPickerDialog refactoring.
        // We are here against our will, again...
//        if (tag.startsWith(SettingsMainFragment.OWNER_TAG)) {
//            final SettingsMainFragment fragment = (SettingsMainFragment) getSupportFragmentManager()
//                    .findFragmentById(R.id.settings_fragment);
//            fragment.onPointSet(tag, actualValue);
//        } else {
//            throw new IllegalArgumentException("Invalid tag=" + tag);
//        }
    }

    private void callListFragmentForSetting(String settingName) {
        SettingsListFragment settingsListFragment = (SettingsListFragment)
                getSupportFragmentManager().findFragmentById(R.id.settings_list_fragment);

        if (settingsListFragment != null) {
            settingsListFragment.refresh(settingName);
        } else {
            Intent intent = new Intent(this, SettingsListActivity.class);
            intent.putExtra(SettingsListActivity.SETTINGS_TYPE_PARAMETER,
                    settingName);
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

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "==>ETD/" + SettingsMainActivity.class.getSimpleName();

    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;
}
