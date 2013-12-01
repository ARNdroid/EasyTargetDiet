package br.com.arndroid.etdiet.virtualweek;

public interface ViewObserver {

    public void onDayChanged(DaySummary summary);

    public void onFoodsUsageChanged(DaySummary summary);

    public void onParametersChanged();

    public void onSummaryRequested(DaySummary daySummary);
}
