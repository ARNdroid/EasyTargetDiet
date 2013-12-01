package br.com.arndroid.etdiet.virtualweek;

public interface JournalApi {

    public void registerViewObserver(ViewObserver observer);

    public void unregisterViewObserver(ViewObserver observer);

    public void requestSummaryForDateId(ViewObserver observer, String dateId);
}
