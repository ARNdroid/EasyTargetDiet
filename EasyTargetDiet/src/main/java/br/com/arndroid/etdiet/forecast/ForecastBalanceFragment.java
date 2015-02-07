package br.com.arndroid.etdiet.forecast;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.arndroid.etdiet.R;

public class ForecastBalanceFragment extends Fragment {

    public static final String FORECAST_METER_SELECTED_ACTION_TAG = ForecastBalanceFragment.class +
            ".FORECAST_METER_SELECTED_ACTION_TAG";

    private TextView mTxtUsed;
    private TextView mTxtToUse;
    private TextView mTxtForecast;
    private TextView mTxtBalance;
    private TextView mTxtForecastDescription;

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
        mTxtForecastDescription = (TextView) rootView.findViewById(R.id.txtForecastDescription);
    }

    public void refreshScreen(ForecastEntity forecastEntity) {
        mTxtUsed.setText(String.valueOf(forecastEntity.getUsed()));
        mTxtToUse.setText(String.valueOf(forecastEntity.getToUse()));
        mTxtForecast.setText(String.valueOf(forecastEntity.getForecastUsed()));
        mTxtBalance.setText(String.valueOf(forecastEntity.getBalance()));
        mTxtForecastDescription.setText(getString(forecastEntity.getStringResourceIdForForecastType()));
    }
}
