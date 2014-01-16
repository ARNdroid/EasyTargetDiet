package br.com.arndroid.etdiet.journal;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.ActivityActionCaller;
import br.com.arndroid.etdiet.action.MenuUtils;
import br.com.arndroid.etdiet.action.SimpleActivityActionCaller;
import br.com.arndroid.etdiet.dialog.DateDialog;
import br.com.arndroid.etdiet.dialog.IntegerDialog;
import br.com.arndroid.etdiet.dialog.TextDialog;
import br.com.arndroid.etdiet.settings.SettingsListActivity;
import br.com.arndroid.etdiet.settings.SettingsMainActivity;

public class JournalActivity extends ActionBarActivity implements
        DateDialog.OnDateSetListener,
        IntegerDialog.OnIntegerSetListener,
        TextDialog.OnTextSetListener,
        ActivityActionCaller {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_activity);
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
                        R.id.journal_fragment, itemId);
                return true;
            case R.id.settings:
                MenuUtils.callMenuInFragmentByIntent(this, SettingsMainActivity.class, itemId);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        if (tag.startsWith(JournalFragment.OWNER_TAG)) {
            ((DateDialog.OnDateSetListener) getSupportFragmentManager()
                    .findFragmentById(R.id.journal_fragment)).onDateSet(tag, actualDate);
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
        if (tag.startsWith(JournalFragment.OWNER_TAG)) {
            ((IntegerDialog.OnIntegerSetListener) getSupportFragmentManager()
                    .findFragmentById(R.id.journal_fragment)).onIntegerSet(tag, actualValue);
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
        if (tag.startsWith(JournalFragment.OWNER_TAG)) {
            ((TextDialog.OnTextSetListener) getSupportFragmentManager()
                    .findFragmentById(R.id.journal_fragment)).onTextSet(tag, actualText);
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
}
