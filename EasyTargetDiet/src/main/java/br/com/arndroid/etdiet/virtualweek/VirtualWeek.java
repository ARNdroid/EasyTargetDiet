package br.com.arndroid.etdiet.virtualweek;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.util.DateUtil;
import br.com.arndroid.etdiet.util.sk.m217.tests.utils.MockContentResolver2;

public class VirtualWeek implements DatabaseChangeObserver.ChangeListener {

    final private MockContentResolver2 mMockContentResolver2;
    private static VirtualWeek mInstance = null;
    private final Context mContext;
    private VirtualWeekEngine mVirtualWeekEngine;
    final private DatabaseChangeObserver[] mDayObserverArray =
            new DatabaseChangeObserver[VirtualWeekEngine.DAYS_IN_A_WEEK];
    final private DatabaseChangeObserver[] mFoodUsageObserverArray =
            new DatabaseChangeObserver[VirtualWeekEngine.DAYS_IN_A_WEEK];
    private DatabaseChangeObserver mParametersHistoryObserver = null;
    private DatabaseChangeObserver mWeekdayParametersObserver = null;
    final private List<ViewObserver> viewObservers = new LinkedList<ViewObserver>();

    public static VirtualWeek getInstance(Context context) {
        /*
         We don't worry about lost of one or two instances in a multi-threaded scenario (which will
         be very rare).
         */
        if (mInstance == null) {
            mInstance = new VirtualWeek(context, null);
        }
        return mInstance;
    }

    public static VirtualWeek getInstanceForTests(Context context, MockContentResolver2 mockContentResolver2) {
        return new VirtualWeek(context, mockContentResolver2);
    }

    protected VirtualWeek(Context context, MockContentResolver2 mockContentResolver2) {
        mContext = context;
        mMockContentResolver2 = mockContentResolver2;
        createVirtualWeekEngineAndRegisterObservers(new Date());
    }

    private void createVirtualWeekEngineAndRegisterObservers(Date referenceDate) {
        mVirtualWeekEngine = new VirtualWeekEngine(mContext, referenceDate);
        registerObservers();
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            /*
            We are calling this here because Android doesn't have a hook to intercept global app
            shutdown.
             */
            unregisterObservers();
        } finally {
            super.finalize();
        }
    }

    private void registerObservers() {
        for(int i = VirtualWeekEngine.FIRST_WEEKDAY_INDEX; i < VirtualWeekEngine.DAYS_IN_A_WEEK; i++) {
            createAndRegisterDayObserverForIndex(i);
            createAndRegisterFoodUsageObserverForIndex(i);
        }
        createAndRegisterParametersObservers();
    }

    private void createAndRegisterParametersObservers() {
        ContentResolver resolver = mContext.getContentResolver();

        mParametersHistoryObserver = new DatabaseChangeObserver(DatabaseChangeObserver.PARAMETERS_HISTORY_CHANGE_TYPE,
                VirtualWeekEngine.NO_INDEX, this);
        resolver.registerContentObserver(Contract.ParametersHistory.CONTENT_URI, true,
                mParametersHistoryObserver);
        mWeekdayParametersObserver = new DatabaseChangeObserver(DatabaseChangeObserver.WEEKDAY_PARAMETERS_CHANGE_TYPE,
                VirtualWeekEngine.NO_INDEX, this);
        resolver.registerContentObserver(Contract.WeekdayParameters.CONTENT_URI, true, mWeekdayParametersObserver);
        if (mMockContentResolver2 != null) {
            mMockContentResolver2.safeRegisterContentObserver(Contract.ParametersHistory.CONTENT_URI,
                    true, mParametersHistoryObserver);
            mMockContentResolver2.safeRegisterContentObserver(Contract.WeekdayParameters.CONTENT_URI,
                    true, mWeekdayParametersObserver);
        }
    }

    private void createAndRegisterFoodUsageObserverForIndex(int index) {
        mFoodUsageObserverArray[index] = new DatabaseChangeObserver(DatabaseChangeObserver.FOODS_USAGE_CHANGE_TYPE,
                index, this);
        final Uri foodUri = Uri.withAppendedPath(Contract.FoodsUsage.DATE_ID_CONTENT_URI,
                mVirtualWeekEngine.getDateIdForIndex(index));
        mContext.getContentResolver().registerContentObserver(foodUri, true, mFoodUsageObserverArray[index]);
        if (mMockContentResolver2 != null) {
            mMockContentResolver2.safeRegisterContentObserver(foodUri, true, mFoodUsageObserverArray[index]);
        }
    }

    private void createAndRegisterDayObserverForIndex(int index) {
        mDayObserverArray[index] = new DatabaseChangeObserver(DatabaseChangeObserver.DAY_CHANGE_TYPE,
                index, this);
        final Uri dayUri;
        if(mVirtualWeekEngine.getIdForIndex(index) == null) {
            dayUri = Uri.withAppendedPath(Contract.Days.DATE_ID_CONTENT_URI,
                    mVirtualWeekEngine.getDateIdForIndex(index));
        } else {
            dayUri = ContentUris.withAppendedId(Contract.Days.CONTENT_URI,
                    mVirtualWeekEngine.getIdForIndex(index));
        }
        mContext.getContentResolver().registerContentObserver(dayUri, true, mDayObserverArray[index]);
        if (mMockContentResolver2 != null) {
            mMockContentResolver2.safeRegisterContentObserver(dayUri, true, mDayObserverArray[index]);
        }
    }

    private void unregisterObservers() {
        for(int i = VirtualWeekEngine.FIRST_WEEKDAY_INDEX; i < VirtualWeekEngine.DAYS_IN_A_WEEK; i++) {
            unregisterAndReleaseDayObserverForIndex(i);
            unregisterAndReleaseFoodUsageObserverForIndex(i);
        }
        unregisterAndReleaseParametersObservers();
    }

    private void unregisterAndReleaseParametersObservers() {
        if (mMockContentResolver2 != null) {
            mMockContentResolver2.safeUnregisterContentObserver(mParametersHistoryObserver);
            mMockContentResolver2.safeUnregisterContentObserver(mWeekdayParametersObserver);
        }
        ContentResolver resolver = mContext.getContentResolver();
        resolver.unregisterContentObserver(mParametersHistoryObserver);
        mParametersHistoryObserver = null;
        resolver.unregisterContentObserver(mWeekdayParametersObserver);
        mWeekdayParametersObserver = null;
    }

    private void unregisterAndReleaseFoodUsageObserverForIndex(int index) {
        if (mMockContentResolver2 != null) {
            mMockContentResolver2.safeUnregisterContentObserver(mFoodUsageObserverArray[index]);
        }
        mContext.getContentResolver().unregisterContentObserver(mFoodUsageObserverArray[index]);
        mFoodUsageObserverArray[index] = null;
    }

    private void unregisterAndReleaseDayObserverForIndex(int index) {
        if (mMockContentResolver2 != null) {
            mMockContentResolver2.safeUnregisterContentObserver(mDayObserverArray[index]);
        }
        mContext.getContentResolver().unregisterContentObserver(mDayObserverArray[index]);
        mDayObserverArray[index] = null;
    }

    @Override
    public void onContentChanged(int changeType, int index) {
        switch (changeType) {
            case DatabaseChangeObserver.DAY_CHANGE_TYPE:
                boolean modifyObserver = mVirtualWeekEngine.getIdForIndex(index) == null;
                mVirtualWeekEngine.rebuildAndCalculateDayForIndex(index);
                if(modifyObserver && mDayObserverArray[index] != null) {
                    unregisterAndReleaseDayObserverForIndex(index);
                    createAndRegisterDayObserverForIndex(index);
                }
                break;
            case DatabaseChangeObserver.FOODS_USAGE_CHANGE_TYPE:
                mVirtualWeekEngine.calculateDayAndNextIfNecessary(index);
                break;
            case DatabaseChangeObserver.PARAMETERS_HISTORY_CHANGE_TYPE:
            case DatabaseChangeObserver.WEEKDAY_PARAMETERS_CHANGE_TYPE:
                unregisterObservers();
                createVirtualWeekEngineAndRegisterObservers(new Date());
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
                    daySummary = mVirtualWeekEngine.daySummaryForIndex(index);
                    viewObserver.onDayChanged(daySummary);
                    break;
                case DatabaseChangeObserver.FOODS_USAGE_CHANGE_TYPE:
                    daySummary = mVirtualWeekEngine.daySummaryForIndex(index);
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

    public void registerViewObserver(ViewObserver observer) {
        viewObservers.add(observer);
    }

    public void unregisterViewObserver(ViewObserver observer) {
        viewObservers.remove(observer);
    }

    public void requestSummaryForDateId(ViewObserver observer, String dateId) {
        final DaySummary summary = mVirtualWeekEngine.daySummaryForDateId(dateId);
        if (summary != null) {
            observer.onSummaryRequested(summary);
        } else {
            swapVirtualWeek(dateId);
            requestSummaryForDateId(observer, dateId);
        }
    }

    private void swapVirtualWeek(String referenceDateId) {
        unregisterObservers();
        createVirtualWeekEngineAndRegisterObservers(DateUtil.dateIdToDate(referenceDateId));
    }

    public static interface ViewObserver {

        public void onDayChanged(DaySummary summary);

        public void onFoodsUsageChanged(DaySummary summary);

        public void onParametersChanged();

        public void onSummaryRequested(DaySummary daySummary);
    }


    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "==>ETD/" + VirtualWeek.class.getSimpleName();
    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;
}