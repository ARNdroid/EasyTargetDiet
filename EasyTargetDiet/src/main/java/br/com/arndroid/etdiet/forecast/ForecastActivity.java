package br.com.arndroid.etdiet.forecast;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.FragmentActionReplier;

public class ForecastActivity extends Activity {

    public static final String FORECAST_ENTITY_ACTION_KEY = ForecastActivity.class.getSimpleName() +
            ".FORECAST_ENTITY_ACTION_KEY";

    private ForecastEntity mForecastEntity;
    private ForecastBalanceFragment mBalanceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.forecast_activity);

        final ActionBar actionBar = getActionBar();
        actionBar.setTitle(getString(R.string.forecast));
        actionBar.setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null) {
            Bundle data = getIntent().getExtras().getBundle(FragmentActionReplier.ACTION_DATA_KEY);
            mForecastEntity = data.getParcelable(FORECAST_ENTITY_ACTION_KEY);
        } else {
            mForecastEntity = savedInstanceState.getParcelable(FORECAST_ENTITY_ACTION_KEY);
        }

        bindScreen();
        refreshScreen();
    }

    @Override
    protected void onSaveInstanceState(@SuppressWarnings("NullableProblems") Bundle outState) {
        outState.putParcelable(FORECAST_ENTITY_ACTION_KEY, mForecastEntity);

        super.onSaveInstanceState(outState);
    }

    private void bindScreen() {
        mBalanceFragment = (ForecastBalanceFragment) getFragmentManager()
                .findFragmentById(R.id.forecast_balance_fragment);
    }

    private void refreshScreen() {
        mBalanceFragment.refreshScreen(mForecastEntity);
    }
}
