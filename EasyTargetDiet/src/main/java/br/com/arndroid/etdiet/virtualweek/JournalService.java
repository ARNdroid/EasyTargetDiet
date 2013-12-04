package br.com.arndroid.etdiet.virtualweek;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import br.com.arndroid.etdiet.provider.Contract;

public class JournalService extends Service implements JournalApi, DatabaseChangeObserver.ChangeListener {

    private VirtualWeek mVirtualWeek;
    private DatabaseChangeObserver[] mDayObserverArray =
            new DatabaseChangeObserver[VirtualWeek.DAYS_IN_A_WEEK];
    private DatabaseChangeObserver[] mFoodUsageObserverArray =
            new DatabaseChangeObserver[VirtualWeek.DAYS_IN_A_WEEK];
    private DatabaseChangeObserver mParametersHistoryObserver = null;
    private DatabaseChangeObserver mWeekdayParametersObserver = null;
    private List<ViewObserver> viewObservers = new LinkedList<ViewObserver>();
    private LocalBinder mBinder = new LocalBinder();

    @Override
    public void onCreate() {
        createVirtualWeekAndRegisterObservers();
    }

    private void createVirtualWeekAndRegisterObservers() {
        if (isLogEnabled) {
            Log.d(TAG, "***Creating a NEW virtual week***");
        }
        mVirtualWeek = new VirtualWeek(this, new Date());
        registerObservers();
    }

    @Override
    public void onDestroy() {
        unregisterObservers();
    }

    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void registerObservers() {
        for(int i = VirtualWeek.FIRST_WEEKDAY_INDEX; i < VirtualWeek.DAYS_IN_A_WEEK; i++) {
            createAndRegisterDayObserverForIndex(i);
            createAndRegisterFoodUsageObserverForIndex(i);
        }
        createAndRegisterParametersObservers();
    }

    private void createAndRegisterParametersObservers() {
        ContentResolver resolver = getContentResolver();

        mParametersHistoryObserver = new DatabaseChangeObserver(DatabaseChangeObserver.PARAMETERS_HISTORY_CHANGE_TYPE,
                VirtualWeek.NO_INDEX, this);
        resolver.registerContentObserver(Contract.ParametersHistory.CONTENT_URI, true, mParametersHistoryObserver);

        mWeekdayParametersObserver = new DatabaseChangeObserver(DatabaseChangeObserver.WEEKDAY_PARAMETERS_CHANGE_TYPE,
                VirtualWeek.NO_INDEX, this);
        resolver.registerContentObserver(Contract.WeekdayParameters.CONTENT_URI, true, mWeekdayParametersObserver);
    }

    private void createAndRegisterFoodUsageObserverForIndex(int index) {
        mFoodUsageObserverArray[index] = new DatabaseChangeObserver(DatabaseChangeObserver.FOODS_USAGE_CHANGE_TYPE,
                index, this);
        final Uri foodUri = Uri.withAppendedPath(Contract.FoodsUsage.DATE_ID_CONTENT_URI,
                mVirtualWeek.getDateIdForIndex(index));
        if (isLogEnabled) {
            Log.d(TAG, "createAndRegisterFoodObserverForIndex with index=" + index + " and foodUri=" + foodUri);
        }
        getContentResolver().registerContentObserver(foodUri, true, mFoodUsageObserverArray[index]);
    }

    private void createAndRegisterDayObserverForIndex(int index) {
        mDayObserverArray[index] = new DatabaseChangeObserver(DatabaseChangeObserver.DAY_CHANGE_TYPE,
                index, this);
        final Uri dayUri;
        if(mVirtualWeek.getIdForIndex(index) == null) {
            dayUri = Uri.withAppendedPath(Contract.Days.DATE_ID_CONTENT_URI,
                    mVirtualWeek.getDateIdForIndex(index));
        } else {
            dayUri = ContentUris.withAppendedId(Contract.Days.CONTENT_URI,
                    mVirtualWeek.getIdForIndex(index));
        }
        if (isLogEnabled) {
            Log.d(TAG, "createAndRegisterDayObserverForIndex with index=" + index + " and dayUri=" + dayUri);
            Log.d(TAG, "createAndRegisterDayObserverForIndex with local context=" + this);
            Log.d(TAG, "createAndRegisterDayObserverForIndex with app context=" + getApplicationContext());
        }
        getContentResolver().registerContentObserver(dayUri, true, mDayObserverArray[index]);
    }

    private void unregisterObservers() {
        for(int i = VirtualWeek.FIRST_WEEKDAY_INDEX; i < VirtualWeek.DAYS_IN_A_WEEK; i++) {
            unregisterAndReleaseDayObserverForIndex(i);
            unregisterAndReleaseFoodUsageObserverForIndex(i);
        }
        unregisterAndReleaseParametersObservers();
    }

    private void unregisterAndReleaseParametersObservers() {
        ContentResolver resolver = getContentResolver();
        resolver.unregisterContentObserver(mParametersHistoryObserver);
        mParametersHistoryObserver = null;
        resolver.unregisterContentObserver(mWeekdayParametersObserver);
        mWeekdayParametersObserver = null;
    }

    private void unregisterAndReleaseFoodUsageObserverForIndex(int index) {
        getContentResolver().unregisterContentObserver(mFoodUsageObserverArray[index]);
        mFoodUsageObserverArray[index] = null;
    }

    private void unregisterAndReleaseDayObserverForIndex(int index) {
        getContentResolver().unregisterContentObserver(mDayObserverArray[index]);
        mDayObserverArray[index] = null;
    }

    @Override
    public void onContentChanged(int changeType, int index) {
        switch (changeType) {
            case DatabaseChangeObserver.DAY_CHANGE_TYPE:
                boolean modifyObserver = mVirtualWeek.getIdForIndex(index) == null;
                mVirtualWeek.rebuildAndCalculateDayForIndex(index);
                if(modifyObserver && mDayObserverArray[index] != null) {
                    unregisterAndReleaseDayObserverForIndex(index);
                    createAndRegisterDayObserverForIndex(index);
                }
                break;
            case DatabaseChangeObserver.FOODS_USAGE_CHANGE_TYPE:
                mVirtualWeek.calculateDayAndNextIfNecessary(index);
                break;
            case DatabaseChangeObserver.PARAMETERS_HISTORY_CHANGE_TYPE:
            case DatabaseChangeObserver.WEEKDAY_PARAMETERS_CHANGE_TYPE:
                unregisterObservers();
                createVirtualWeekAndRegisterObservers();
                break;
            default:
                throw new IllegalStateException("Invalid changeType = " + changeType);
        }

        notifyAllViewObservers(changeType, index);
    }

    private void notifyAllViewObservers(int changeType, int index) {
        DaySummary daySummary;
        for (ViewObserver viewObserver : viewObservers) {
            switch (changeType) {
                case DatabaseChangeObserver.DAY_CHANGE_TYPE:
                    daySummary = mVirtualWeek.daySummaryForIndex(index);
                    viewObserver.onDayChanged(daySummary);
                    break;
                case DatabaseChangeObserver.FOODS_USAGE_CHANGE_TYPE:
                    daySummary = mVirtualWeek.daySummaryForIndex(index);
                    viewObserver.onFoodsUsageChanged(daySummary);
                    break;
                case DatabaseChangeObserver.PARAMETERS_HISTORY_CHANGE_TYPE:
                case DatabaseChangeObserver.WEEKDAY_PARAMETERS_CHANGE_TYPE:
                    viewObserver.onParametersChanged();
                    break;
                default:
                    throw new IllegalStateException("Invalid changeType = " + changeType);
            }
        }
    }

    @Override
    public void registerViewObserver(ViewObserver observer) {
        viewObservers.add(observer);
    }

    @Override
    public void unregisterViewObserver(ViewObserver observer) {
        viewObservers.remove(observer);
    }

    @Override
    public void requestSummaryForDateId(ViewObserver observer, String dateId) {
        observer.onSummaryRequested(mVirtualWeek.daySummaryForDateId(dateId));
    }

    public class LocalBinder extends Binder {
        public JournalApi serviceApi() {
            return JournalService.this;
        }
    }

    private static final String TAG = "==>ETD/" + JournalService.class.getSimpleName();
    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;
}