package br.com.arndroid.etdiet.weights;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.MenuUtils;
import br.com.arndroid.etdiet.utils.NavigationUtils;

public class WeightsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.weights_activity);

        final ActionBar actionBar = getActionBar();
        actionBar.setTitle(getString(R.string.weight));
        actionBar.setDisplayHomeAsUpEnabled(true);

        final WeightsListFragment fragment = (WeightsListFragment) getFragmentManager()
                .findFragmentById(R.id.weights_list_fragment);
        fragment.onReplyActionFromOtherFragment(null, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();

        switch (itemId) {
            case R.id.add:
                MenuUtils.callMenuInFragmentByMethod(getFragmentManager(),
                        R.id.weights_list_fragment, itemId);
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
        inflater.inflate(R.menu.weights, menu);
        return true;
    }
}