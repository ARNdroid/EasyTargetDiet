package br.com.arndroid.etdiet.settings;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.ActivityActionCaller;
import br.com.arndroid.etdiet.action.SimpleActivityActionCaller;
import br.com.arndroid.etdiet.dialog.PointDialog;
import br.com.arndroid.etdiet.dialog.StringListDialog;

public class SettingsMainActivity extends ActionBarActivity implements
        PointDialog.OnPointSetListener,
        StringListDialog.OnStringSelectedListener,
        ActivityActionCaller {

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
            ((PointDialog.OnPointSetListener) getSupportFragmentManager()
                    .findFragmentById(R.id.settings_main_fragment)).onPointSet(tag, actualValue);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
    }

    @Override
    public void onStringSelected(String tag, int chosenIndex) {
        // We are here against our will...
        if (tag.startsWith(SettingsMainFragment.OWNER_TAG)) {
            ((StringListDialog.OnStringSelectedListener) getSupportFragmentManager()
                    .findFragmentById(R.id.settings_main_fragment)).onStringSelected(tag, chosenIndex);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
    }

    @Override
    public void onCallAction(int fragmentId, Class holderActivityClass, String actionTag,
                             Bundle actionData) {

        new SimpleActivityActionCaller().onCallAction(this, getSupportFragmentManager(), fragmentId,
                holderActivityClass, actionTag, actionData);
    }

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "==>ETD/" + SettingsMainActivity.class.getSimpleName();

    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;
}
