package br.com.arndroid.etdiet.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.foodsusage.FoodsUsageListFrag;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.util.DateUtil;
import br.com.arndroid.etdiet.util.PointPickerDialog;

public class SettingsListActivity extends ActionBarActivity implements PointPickerDialog.OnPointSetListener {

    public static final String SETTINGS_TYPE_PARAMETER = SettingsListActivity.class.getSimpleName()
            + ".SETTINGS_TYPE_PARAMETER";
    private String mSettingsColumnName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_list_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Tells fragment to show foods usage filtered by date and meal inside the intent/bundle
        // that started this activity:
        if(savedInstanceState == null) {
            Intent intent = getIntent();
            mSettingsColumnName = intent.getExtras().getString(SETTINGS_TYPE_PARAMETER);
        } else {
            mSettingsColumnName = savedInstanceState.getString(SETTINGS_TYPE_PARAMETER);
        }
        actionBar.setTitle(getTitleFromSettingsType(mSettingsColumnName));
        SettingsListFragment fragment = (SettingsListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.settings_list_fragment);
        fragment.refresh(mSettingsColumnName);
    }

    private String getTitleFromSettingsType(String settingsColumnName) {
        if (Contract.WeekdayParameters.EXERCISE_GOAL.equals(settingsColumnName)) {
            return getResources().getString(R.string.exercise_goal);
        } else {
            throw new IllegalArgumentException("Invalid settingsColumnName=" + settingsColumnName);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
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

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "==>ETD/" + SettingsListActivity.class.getSimpleName();
    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;

    @Override
    public void onPointSet(String tag, float actualValue) {
        // We are here against our will...
        if (tag.startsWith(SettingsListFragment.OWNER_TAG)) {
            ((PointPickerDialog.OnPointSetListener)getSupportFragmentManager().findFragmentById(
                    R.id.settings_list_fragment)).onPointSet(tag, actualValue);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
    }
}
