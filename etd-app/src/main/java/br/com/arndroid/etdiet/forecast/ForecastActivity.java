package br.com.arndroid.etdiet.forecast;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.FragmentActionReplier;
import br.com.arndroid.etdiet.journal.JournalMyPointsFragment;
import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.utils.PreferencesUtils;
import br.com.arndroid.etdiet.virtualweek.DaySummary;
import br.com.arndroid.etdiet.virtualweek.VirtualWeekEngine;

public class ForecastActivity extends Activity {

    public static final String DAY_SUMMARY_ACTION_KEY = ForecastActivity.class.getSimpleName() +
            ".DAY_SUMMARY_ACTION_KEY";

    private DaySummary mDaySummary;
    private ForecastBalanceFragment mBalanceFragment;
    private JournalMyPointsFragment mJournalMyPointsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.forecast_activity);

        final ActionBar actionBar = getActionBar();
        actionBar.setTitle(getString(R.string.forecast));
        actionBar.setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null) {
            Bundle data = getIntent().getExtras().getBundle(FragmentActionReplier.ACTION_DATA_KEY);
            mDaySummary = data.getParcelable(DAY_SUMMARY_ACTION_KEY);
        } else {
            mDaySummary = savedInstanceState.getParcelable(DAY_SUMMARY_ACTION_KEY);
        }

        bindScreen();
        setupScreen();
        refreshScreen();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(DAY_SUMMARY_ACTION_KEY, mDaySummary);

        super.onSaveInstanceState(outState);
    }

    private void bindScreen() {
        mBalanceFragment = (ForecastBalanceFragment) getFragmentManager()
                .findFragmentById(R.id.forecast_balance_fragment);
        mJournalMyPointsFragment = (JournalMyPointsFragment) getFragmentManager()
                .findFragmentById(R.id.journal_my_points_fragment);
    }

    private void setupScreen() {
        mJournalMyPointsFragment.setTitle(String.format(getString(R.string.my_units_at_end_of_day),
                PreferencesUtils.getTrackingUnitNameMany(getApplicationContext()).toUpperCase()));
        mJournalMyPointsFragment.setForecastMeterCanTouch(false);
    }

    private void refreshScreen() {
        final ForecastEntity forecastEntity = ForecastEntity.getInstanceForDaySummary(mDaySummary);
        mBalanceFragment.refreshScreen(forecastEntity);

        // We are about to change daySummary value. Make a defensive copy and go ahead!
        final DaySummary changedDaySummary = new DaySummary(mDaySummary);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        changedDaySummary.getEntity().setDateId(DateUtils.dateToDateId(calendar.getTime()));
        changedDaySummary.getUsage().setSupperUsed(changedDaySummary.getUsage().getSupperUsed()
            + forecastEntity.getToUse());
        VirtualWeekEngine.computeSummary(changedDaySummary);
        mJournalMyPointsFragment.refreshScreen(changedDaySummary);
    }
}
