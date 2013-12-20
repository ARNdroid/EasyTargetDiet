package br.com.arndroid.etdiet.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.foodsusage.FoodsUsageListFrag;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageManager;
import br.com.arndroid.etdiet.quickinsert.QuickInsertFrag;
import br.com.arndroid.etdiet.util.DateUtil;

public class SettingsListActivity extends ActionBarActivity implements ActionBar.OnNavigationListener {

    public static final String SETTINGS_TYPE_PARAMETER = SettingsListActivity.class.getSimpleName()
            + ".SETTINGS_TYPE_PARAMETER";
    private SettingsListFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: changes from here...
        setContentView(R.layout.foods_usage_act);

        mealsNameList = getResources().getStringArray(R.array.meals_name_list);

        SpinnerAdapter adapter = ArrayAdapter.createFromResource(this, R.array.meals_name_list,
                android.R.layout.simple_spinner_dropdown_item);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(adapter, this);

        // Tells fragment to show foods usage filtered by date and meal inside the intent/bundle
        // that started this activity:
        if(savedInstanceState == null) {
            Intent intent = getIntent();
            mDateId = intent.getExtras().getString(SETTINGS_TYPE_PARAMETER);
            actionBar.setSelectedNavigationItem(intent.getExtras().getInt(MEAL_PARAMETER));
        } else {
            mDateId = savedInstanceState.getString(SETTINGS_TYPE_PARAMETER);
            actionBar.setSelectedNavigationItem(savedInstanceState.getInt(MEAL_PARAMETER));
        }
        mFragment = (FoodsUsageListFrag) getSupportFragmentManager().findFragmentById(R.id.foods_usage_list_frag);
        mFragment.refresh(mDateId, actionBar.getSelectedNavigationIndex());

        setFieldsReferenceFromForm();
        setupFields();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(SETTINGS_TYPE_PARAMETER, mDateId);
        outState.putInt(MEAL_PARAMETER, getSupportActionBar().getSelectedNavigationIndex());
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
    public boolean onNavigationItemSelected(int position, long itemId) {
        // TODO: the following logic needs change to use itemId
        if(isLogEnabled) {
            Log.d(TAG,
                  " ->onNavigationItemSelected()" +
                  " ->position = " + position +
                  " ->itemId = " + itemId +
                  " ->mDateId = " + mDateId
            );
        }
        mFragment.refresh(mDateId, position);
        return true;
    }

    private void setFieldsReferenceFromForm() {
        mTxtDate = (TextView) findViewById(R.id.txtDate);
    }

    private void setupFields() {
        mTxtDate.setText(DateUtil.dateIdToFormattedString(mDateId));
    }

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "==>ETD/" + SettingsListActivity.class.getSimpleName();
    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;
}
