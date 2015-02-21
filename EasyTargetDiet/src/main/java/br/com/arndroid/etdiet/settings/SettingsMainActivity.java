package br.com.arndroid.etdiet.settings;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.ActionUtils;
import br.com.arndroid.etdiet.action.ActivityActionCaller;
import br.com.arndroid.etdiet.dialog.PointDialog;
import br.com.arndroid.etdiet.dialog.StringListDialog;
import br.com.arndroid.etdiet.dialog.TextDialog;
import br.com.arndroid.etdiet.utils.NavigationUtils;

public class SettingsMainActivity extends Activity implements
        PointDialog.OnPointSetListener,
        StringListDialog.OnStringSelectedListener,
        TextDialog.OnTextSetListener,
        ActivityActionCaller {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_main_activity);
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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
        /*
         We are here against our will!
         The recommended approach to deal with Dialog Fragments is: use of host activity
         to listen the dialog confirmation.
         This leave us with an event generation inside a Fragment and event handling inside an
         Activity.
         The following code send the event to original Fragment again.
         */
        if (tag.startsWith(SettingsMainFragment.OWNER_TAG)) {
            ((PointDialog.OnPointSetListener) getFragmentManager()
                    .findFragmentById(R.id.settings_main_fragment)).onPointSet(tag, actualValue);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
    }

    @Override
    public void onStringSelected(String tag, int chosenIndex) {
        // We are here against our will...
        if (tag.startsWith(SettingsMainFragment.OWNER_TAG)) {
            ((StringListDialog.OnStringSelectedListener) getFragmentManager()
                    .findFragmentById(R.id.settings_main_fragment)).onStringSelected(tag, chosenIndex);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
    }

    @Override
    public void onCallAction(int fragmentId, Class holderActivityClass, String actionTag,
                             Bundle actionData) {

        ActionUtils.callActionInFragment(this, getFragmentManager(), fragmentId,
                holderActivityClass, actionTag, actionData);
    }

    @Override
    public void onTextSet(String tag, String actualText) {
        // We are here against our will...
        if (tag.startsWith(SettingsMainFragment.OWNER_TAG)) {
            ((TextDialog.OnTextSetListener) getFragmentManager()
                    .findFragmentById(R.id.settings_main_fragment)).onTextSet(tag, actualText);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
    }
}
