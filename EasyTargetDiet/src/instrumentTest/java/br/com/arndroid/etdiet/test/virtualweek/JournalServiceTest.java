package br.com.arndroid.etdiet.test.virtualweek;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.test.ServiceTestCase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.provider.days.DaysManager;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageManager;
import br.com.arndroid.etdiet.provider.parametershistory.ParametersHistoryManager;
import br.com.arndroid.etdiet.util.DateUtil;
import br.com.arndroid.etdiet.virtualweek.DatabaseChangeObserver;
import br.com.arndroid.etdiet.virtualweek.DaySummary;
import br.com.arndroid.etdiet.virtualweek.JournalApi;
import br.com.arndroid.etdiet.virtualweek.JournalService;
import br.com.arndroid.etdiet.virtualweek.TestContentObserver;
import br.com.arndroid.etdiet.virtualweek.ViewObserver;
import br.com.arndroid.etdiet.virtualweek.WeekPeriod;

public class JournalServiceTest extends ServiceTestCase<JournalService> {

    public JournalServiceTest() {
        super(JournalService.class);
    }

    /*
    The framework deals with bind and ServiceConnection plus unbind (inside tearDown())
     */

//    public void testWithoutDataChangeInPeriodMustNotCallObserver() {
//        final Intent intent = new Intent(getContext(), JournalService.class);
//        final JournalService.LocalBinder localBinder = (JournalService.LocalBinder) bindService(intent);
//        final JournalApi serviceApi = localBinder.serviceApi();
//        final DaysManager daysManager = new DaysManager(getContext());
//        final FoodsUsageManager foodsUsageManager = new FoodsUsageManager(getContext());
//
//        final ViewObserverMonitor observer = new ViewObserverMonitor();
//        serviceApi.registerViewObserver(observer);
//        final WeekPeriod weekPeriod = new WeekPeriod(getContext(), new Date());
//        Calendar cal = Calendar.getInstance();
//
//        cal.setTime(weekPeriod.getInitialDate());
//        cal.add(Calendar.DAY_OF_MONTH, -1);
//        DaysEntity daysEntity = daysManager.dayFromDate(cal.getTime());
//        daysManager.refresh(daysEntity);
//        FoodsUsageEntity foodsUsageEntity = new FoodsUsageEntity(null, DateUtil.dateToDateId(cal.getTime()),
//                Meals.BREAKFAST, 1, "food to test", 10.0f);
//        foodsUsageManager.refresh(foodsUsageEntity);
//
//        cal.setTime(weekPeriod.getFinalDate());
//        cal.add(Calendar.DAY_OF_MONTH, 1);
//        daysEntity = daysManager.dayFromDate(cal.getTime());
//        daysManager.refresh(daysEntity);
//        foodsUsageEntity = new FoodsUsageEntity(null, DateUtil.dateToDateId(cal.getTime()),
//                Meals.BREAKFAST, 1, "food to test", 10.0f);
//        foodsUsageManager.refresh(foodsUsageEntity);
//
//        serviceApi.unregisterViewObserver(observer);
//
//        assertEquals(0, observer.summariesOnDayChanged.size());
//        assertEquals(0, observer.summariesOnFoodsUsageChanged.size());
//        assertEquals(0, observer.summariesOnSummaryRequested.size());
//        assertEquals(0, observer.parametersChangedCount);
//    }
//
//    public void testWithDataChangeInPeriodMustCallObserver() throws InterruptedException {
//        final CountDownLatch signal = new CountDownLatch(1);
//
//        final Intent intent = new Intent(getContext(), JournalService.class);
//        final JournalService.LocalBinder localBinder = (JournalService.LocalBinder) bindService(intent);
//        final JournalApi serviceApi = localBinder.serviceApi();
//        final DaysManager daysManager = new DaysManager(getContext());
//        final FoodsUsageManager foodsUsageManager = new FoodsUsageManager(getContext());
//        final ParametersHistoryManager parametersHistoryManager = new ParametersHistoryManager(getContext());
//
//        final ViewObserverMonitor observer = new ViewObserverMonitor();
//        serviceApi.registerViewObserver(observer);
//
//        final WeekPeriod weekPeriod = new WeekPeriod(getContext(), new Date());
//        final Calendar cal = Calendar.getInstance();
//        cal.setTime(weekPeriod.getInitialDate());
//        final Date date1 = cal.getTime();
//        cal.add(Calendar.DAY_OF_MONTH, 1);
//        final Date date2 = cal.getTime();
//        cal.add(Calendar.DAY_OF_MONTH, 1);
//        final Date date3 = cal.getTime();
//        cal.add(Calendar.DAY_OF_MONTH, 1);
//        final Date date4 = cal.getTime();
//
//        DaysEntity daysEntity = daysManager.dayFromDate(date1);
//        daysManager.refresh(daysEntity);
//        daysEntity = daysManager.dayFromDate(date2);
//        daysManager.refresh(daysEntity);
//        daysEntity = daysManager.dayFromDate(date3);
//        daysManager.refresh(daysEntity);
//        daysEntity = daysManager.dayFromDate(date4);
//        daysManager.refresh(daysEntity);
//
//        FoodsUsageEntity foodsUsageEntity = new FoodsUsageEntity(null, DateUtil.dateToDateId(date1),
//                Meals.BREAKFAST, 1, "food to test 1", 10.0f);
//        foodsUsageManager.refresh(foodsUsageEntity);
//        foodsUsageEntity = new FoodsUsageEntity(null, DateUtil.dateToDateId(date2),
//                Meals.BREAKFAST, 1, "food to test 2", 20.0f);
//        foodsUsageManager.refresh(foodsUsageEntity);
//        foodsUsageEntity = new FoodsUsageEntity(null, DateUtil.dateToDateId(date3),
//                Meals.BREAKFAST, 1, "food to test 3", 30.0f);
//        foodsUsageManager.refresh(foodsUsageEntity);
//
//        serviceApi.requestSummaryForDateId(observer, DateUtil.dateToDateId(date1));
//        serviceApi.requestSummaryForDateId(observer, DateUtil.dateToDateId(date2));
//
//        parametersHistoryManager.setTrackingWeekday(Calendar.SATURDAY);
//
//        Handler handler = new Handler(Looper.getMainLooper());
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                try {
//                    assertEquals(4, observer.summariesOnDayChanged.size());
//                    assertEquals(3, observer.summariesOnFoodsUsageChanged.size());
//                    assertEquals(2, observer.summariesOnSummaryRequested.size());
//                    assertEquals(0, observer.parametersChangedCount);
//                } finally {
//                    serviceApi.unregisterViewObserver(observer);
//                    signal.countDown();
//                }
//            }
//        }, 2000);
//
//        signal.await();
//    }

//    public void testMultiThreadV2() throws InterruptedException {
//        final CountDownLatch latch1 = new CountDownLatch(1);
//        final TestContentObserver mObserver = new TestContentObserver();
//        Handler handler = new Handler(Looper.getMainLooper());
//        Log.d(TAG, "*** Step I: starting ***");
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                Log.d(TAG, "*** Step I: Inside Runnable.run() ***");
//                final String dateId = DateUtil.dateToDateId(new Date());
//                getContext().getContentResolver().registerContentObserver(Uri.withAppendedPath(
//                        Contract.FoodsUsage.DATE_ID_CONTENT_URI, dateId), true, mObserver);
//
//                final FoodsUsageManager foodsManager = new FoodsUsageManager(getContext());
//                FoodsUsageEntity foodsEntity = null;
//
//                foodsEntity = new FoodsUsageEntity(null, dateId,
//                        Meals.BREAKFAST, 1, "food for test", 7.7f);
//                foodsManager.refresh(foodsEntity);
//
//                latch1.countDown();
//            }
//        }, 1);
//
//        latch1.await();
//        Log.d(TAG, "*** Step II: starting ***");
//        final CountDownLatch latch2 = new CountDownLatch(1);
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                Log.d(TAG, "*** Step II: Inside Runnable.run() ***");
//                latch2.countDown();
//            }
//        }, 1000);
//
//        Log.d(TAG, "*** mObserver.mCalled being asserted ***");
//        assertTrue(mObserver.mCalled);
//        getContext().getContentResolver().unregisterContentObserver(mObserver);
//    }

    public void testMultiThreadV3() throws InterruptedException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final TestContentObserver mObserver = new TestContentObserver();

        Log.d(TAG, "*** Step I: starting ***");
        new Thread(new Runnable() {
            public void run() {
                Log.d(TAG, "*** Step I: Inside Runnable.run() ***");
                final String dateId = DateUtil.dateToDateId(new Date());
                getContext().getContentResolver().registerContentObserver(Uri.withAppendedPath(
                        Contract.FoodsUsage.DATE_ID_CONTENT_URI, dateId), true, mObserver);

                final FoodsUsageManager foodsManager = new FoodsUsageManager(getContext());
                FoodsUsageEntity foodsEntity = null;

                foodsEntity = new FoodsUsageEntity(null, dateId,
                        Meals.BREAKFAST, 1, "food for test", 7.7f);
                foodsManager.refresh(foodsEntity);

                latch1.countDown();
            }
        }).start();

        latch1.await();
        Log.d(TAG, "*** Step II: starting ***");
        Handler handler = new Handler();
        final CountDownLatch latch2 = new CountDownLatch(1);
        handler.postDelayed(new Runnable() {
            public void run() {
                Log.d(TAG, "*** Step II: Inside Runnable.run() ***");
                latch2.countDown();
            }
        }, 2000);

        Log.d(TAG, "*** mObserver.mCalled being asserted ***");
        assertTrue(mObserver.mCalled);
        getContext().getContentResolver().unregisterContentObserver(mObserver);
    }

    public class ViewObserverMonitor implements ViewObserver {
        public List<DaySummary> summariesOnDayChanged = new ArrayList<DaySummary>();
        public List<DaySummary> summariesOnFoodsUsageChanged = new ArrayList<DaySummary>();
        public List<DaySummary> summariesOnSummaryRequested = new ArrayList<DaySummary>();
        public int parametersChangedCount = 0;

        @Override
        public void onDayChanged(DaySummary summary) {
            if (isLogEnabled) {
                Log.d(TAG, "onDayChanged entered.");
            }
            summariesOnDayChanged.add(summary);
        }

        @Override
        public void onFoodsUsageChanged(DaySummary summary) {
            if (isLogEnabled) {
                Log.d(TAG, "onFoodsUsageChanged entered.");
            }
            summariesOnFoodsUsageChanged.add(summary);
        }

        @Override
        public void onParametersChanged() {
            if (isLogEnabled) {
                Log.d(TAG, "onParametersChanged entered.");
            }
            parametersChangedCount++;
        }

        @Override
        public void onSummaryRequested(DaySummary summary) {
            if (isLogEnabled) {
                Log.d(TAG, "onSummaryRequested entered.");
            }
            summariesOnSummaryRequested.add(summary);
        }
    }

    private static final String TAG = "==>ETD/" + JournalServiceTest.class.getSimpleName();
    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;

}
