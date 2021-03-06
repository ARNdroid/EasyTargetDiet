package br.com.arndroid.etdiet.forecast;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.virtualweek.DaySummary;

public class ForecastBalanceFragment extends Fragment {

    public static final String FORECAST_METER_SELECTED_ACTION_TAG = ForecastBalanceFragment.class +
            ".FORECAST_METER_SELECTED_ACTION_TAG";

    private TextView mTxtUsed;
    private TextView mTxtToUse;
    private TextView mTxtForecast;
    private TextView mTxtBalance;
    private TextView mTxtForecastDescription;
    private TextView mTxtBalanceDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.forecast_balance_fragment, container, false);

        bindScreen(rootView);

        return rootView;
    }

    private void bindScreen(View rootView) {
        mTxtUsed = (TextView) rootView.findViewById(R.id.txtUsed);
        mTxtToUse = (TextView) rootView.findViewById(R.id.txtToUse);
        mTxtForecast = (TextView) rootView.findViewById(R.id.txtForecast);
        mTxtBalance = (TextView) rootView.findViewById(R.id.txtBalance);
        mTxtBalanceDescription = (TextView) rootView.findViewById(R.id.txtBalanceDescription);
        mTxtForecastDescription = (TextView) rootView.findViewById(R.id.txtForecastDescription);
    }

    public void refreshScreen(ForecastEntity forecastEntity) {
        mTxtUsed.setText(String.valueOf(forecastEntity.getUsed()));
        mTxtToUse.setText(String.valueOf(forecastEntity.getToUse()));
        mTxtForecast.setText(String.valueOf(forecastEntity.getForecastUsed()));
        mTxtBalance.setText(String.valueOf(forecastEntity.getBalance()));
        final String goalType = getString(
                forecastEntity.getGoalType() == DaySummary.GOAL_TYPE_PLANNED ? R.string.planned : R.string.left);
        mTxtBalanceDescription.setText(String.format(getString(R.string.balance), goalType));
        mTxtForecastDescription.setText(getString(forecastEntity.getStringResourceIdForForecastType()));
    }
}
