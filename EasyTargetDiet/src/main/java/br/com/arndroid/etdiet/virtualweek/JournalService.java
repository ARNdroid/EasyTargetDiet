package br.com.arndroid.etdiet.virtualweek;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import br.com.arndroid.etdiet.provider.Contract;

public class JournalService extends Service implements DatabaseChangeObserver.ChangeListener {

    // TODO: decide initialization for following object.
    private VirtualWeek mVirtualWeek;
    private DatabaseChangeObserver[] mDayObserverArray =
            new DatabaseChangeObserver[VirtualWeek.DAYS_IN_A_WEEK];
    private DatabaseChangeObserver[] mFoodUsageObserverArray =
            new DatabaseChangeObserver[VirtualWeek.DAYS_IN_A_WEEK];
    private DatabaseChangeObserver mParametersHistoryObserver = null;
    private DatabaseChangeObserver mWeekdayParametersObserver = null;
    
    public IBinder onBind(Intent intent) {
        // TODO: implement.
        return null;
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

        mParametersHistoryObserver = new DatabaseChangeObserver(DatabaseChangeObserver.PARAMETERS_HISTORY_CHANGED_TYPE,
                VirtualWeek.NO_INDEX, this);
        resolver.registerContentObserver(Contract.ParametersHistory.CONTENT_URI, true, mParametersHistoryObserver);

        mWeekdayParametersObserver = new DatabaseChangeObserver(DatabaseChangeObserver.WEEKDAY_PARAMETERS_CHANGED_TYPE,
                VirtualWeek.NO_INDEX, this);
        resolver.registerContentObserver(Contract.WeekdayParameters.CONTENT_URI, true, mWeekdayParametersObserver);
    }

    private void createAndRegisterFoodUsageObserverForIndex(int index) {
        mFoodUsageObserverArray[index] = new DatabaseChangeObserver(DatabaseChangeObserver.FOODS_USAGE_CHANGED_TYPE,
                index, this);
        getContentResolver().registerContentObserver(Uri.withAppendedPath(
                Contract.FoodsUsage.DATE_ID_CONTENT_URI, mVirtualWeek.getDateIdForIndex(index)),
                true, mFoodUsageObserverArray[index]);
    }

    private void createAndRegisterDayObserverForIndex(int index) {
        mDayObserverArray[index] = new DatabaseChangeObserver(DatabaseChangeObserver.DAY_CHANGED_TYPE,
                index, this);
        final Uri dayUri;
        if(mVirtualWeek.getIdForIndex(index) == null) {
            dayUri = Uri.withAppendedPath(Contract.Days.DATE_ID_CONTENT_URI,
                    mVirtualWeek.getDateIdForIndex(index));
        } else {
            dayUri = ContentUris.withAppendedId(Contract.Days.CONTENT_URI,
                    mVirtualWeek.getIdForIndex(index));
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
    public void onContentChanged(int changedType, int index) {
        switch (changedType) {
            case DatabaseChangeObserver.DAY_CHANGED_TYPE:
                boolean changeObserver = mVirtualWeek.getIdForIndex(index) == null;
                mVirtualWeek.rebuildAndCalculateDayForIndex(index);
                if(changeObserver && mDayObserverArray[index] != null) {
                    unregisterAndReleaseDayObserverForIndex(index);
                    createAndRegisterDayObserverForIndex(index);
                }
                // TODO: implement observers (UI) notification
                break;
            case DatabaseChangeObserver.FOODS_USAGE_CHANGED_TYPE:
                mVirtualWeek.calculateDayAndNextIfNecessary(index);
                // TODO: implement observers (UI) notification
                break;
            case DatabaseChangeObserver.PARAMETERS_HISTORY_CHANGED_TYPE:
                // TODO: needs a complete VirtualWeek reconstruction
                // TODO: implement observers (UI) notification
                break;
            case DatabaseChangeObserver.WEEKDAY_PARAMETERS_CHANGED_TYPE:
                // TODO: needs a complete VirtualWeek reconstruction
                // TODO: implement observers (UI) notification
                break;
            default:
                throw new IllegalStateException("Invalid changedType = " + changedType);
        }
    }
}
