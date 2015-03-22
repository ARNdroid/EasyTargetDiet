package br.com.arndroid.etdiet.backups.custom;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.MenuUtils;
import br.com.arndroid.etdiet.utils.NavigationUtils;

public class BackupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.backups_activity);

        final ActionBar actionBar = getActionBar();
        actionBar.setTitle(getString(R.string.backup));
        actionBar.setDisplayHomeAsUpEnabled(true);

        final BackupListFragment fragment = (BackupListFragment) getFragmentManager()
                .findFragmentById(R.id.backups_list_fragment);
        fragment.onReplyActionFromOtherFragment(null, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();

        switch (itemId) {
            case R.id.add:
                MenuUtils.callMenuInFragmentByMethod(getFragmentManager(),
                        R.id.backups_list_fragment, itemId);
                return true;
            case android.R.id.home:
                NavigationUtils.navigateUpFromSameTaskPreservingScreenState(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.backups, menu);
        return true;
    }
}