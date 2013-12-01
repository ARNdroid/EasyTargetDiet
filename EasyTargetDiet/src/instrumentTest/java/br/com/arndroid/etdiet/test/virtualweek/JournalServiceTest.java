package br.com.arndroid.etdiet.test.virtualweek;

import android.content.Intent;
import android.test.ServiceTestCase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.provider.days.DaysManager;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageManager;
import br.com.arndroid.etdiet.provider.parametershistory.ParametersHistoryManager;
import br.com.arndroid.etdiet.util.DateUtil;
import br.com.arndroid.etdiet.virtualweek.DaySummary;
import br.com.arndroid.etdiet.virtualweek.JournalApi;
import br.com.arndroid.etdiet.virtualweek.JournalService;
import br.com.arndroid.etdiet.virtualweek.ViewObserver;
import br.com.arndroid.etdiet.virtualweek.WeekPeriod;

public class JournalServiceTest extends ServiceTestCase<JournalService> {

    public JournalServiceTest() {
        super(JournalService.class);
    }

    /*
    The framework deals with bind and ServiceConnection plus unbind (inside tearDown())
     */

    public void testWithoutDataChangeInPeriodMustNotCallObserver() {
        final Intent intent = new Intent(getContext(), JournalService.class);
        final JournalService.LocalBinder localBinder = (JournalService.LocalBinder) bindService(intent);
        final JournalApi serviceApi = localBinder.serviceApi();
        final DaysManager daysManager = new DaysManager(getContext());
        final FoodsUsageManager foodsUsageManager = new FoodsUsageManager(getContext());

        final ViewObserverMonitor observer = new ViewObserverMonitor();
        serviceApi.registerViewObserver(observer);
        final WeekPeriod weekPeriod = new WeekPeriod(getContext(), new Date());
        Calendar cal = Calendar.getInstance();

        cal.setTime(weekPeriod.getInitialDate());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        DaysEntity daysEntity = daysManager.dayFromDate(cal.getTime());
        daysManager.refresh(daysEntity);
        FoodsUsageEntity foodsUsageEntity = new FoodsUsageEntity(null, DateUtil.dateToDateId(cal.getTime()),
                Meals.BREAKFAST, 1, "food to test", 10.0f);
        foodsUsageManager.refresh(foodsUsageEntity);

        cal.setTime(weekPeriod.getFinalDate());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        daysEntity = daysManager.dayFromDate(cal.getTime());
        daysManager.refresh(daysEntity);
        foodsUsageEntity = new FoodsUsageEntity(null, DateUtil.dateToDateId(cal.getTime()),
                Meals.BREAKFAST, 1, "food to test", 10.0f);
        foodsUsageManager.refresh(foodsUsageEntity);

        serviceApi.unregisterViewObserver(observer);

        assertEquals(0, observer.summariesOnDayChanged.size());
        assertEquals(0, observer.summariesOnFoodsUsageChanged.size());
        assertEquals(0, observer.summariesOnSummaryRequested.size());
        assertEquals(0, observer.parametersChangedCount);
    }

    public void testWithDataChangeInPeriodMustCallObserver() {
        final Intent intent = new Intent(getContext(), JournalService.class);
        final JournalService.LocalBinder localBinder = (JournalService.LocalBinder) bindService(intent);
        final JournalApi serviceApi = localBinder.serviceApi();
        final DaysManager daysManager = new DaysManager(getContext());
        final FoodsUsageManager foodsUsageManager = new FoodsUsageManager(getContext());
        final ParametersHistoryManager parametersHistoryManager = new ParametersHistoryManager(getContext());

        final ViewObserverMonitor observer = new ViewObserverMonitor();
        serviceApi.registerViewObserver(observer);

        final WeekPeriod weekPeriod = new WeekPeriod(getContext(), new Date());
        final Calendar cal = Calendar.getInstance();
        cal.setTime(weekPeriod.getInitialDate());
        final Date date1 = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        final Date date2 = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        final Date date3 = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        final Date date4 = cal.getTime();

        DaysEntity daysEntity = daysManager.dayFromDate(date1);
        daysManager.refresh(daysEntity);
        daysEntity = daysManager.dayFromDate(date2);
        daysManager.refresh(daysEntity);
        daysEntity = daysManager.dayFromDate(date3);
        daysManager.refresh(daysEntity);
        daysEntity = daysManager.dayFromDate(date4);
        daysManager.refresh(daysEntity);

        FoodsUsageEntity foodsUsageEntity = new FoodsUsageEntity(null, DateUtil.dateToDateId(date1),
                Meals.BREAKFAST, 1, "food to test 1", 10.0f);
        foodsUsageManager.refresh(foodsUsageEntity);
        foodsUsageEntity = new FoodsUsageEntity(null, DateUtil.dateToDateId(date2),
                Meals.BREAKFAST, 1, "food to test 2", 20.0f);
        foodsUsageManager.refresh(foodsUsageEntity);
        foodsUsageEntity = new FoodsUsageEntity(null, DateUtil.dateToDateId(date3),
                Meals.BREAKFAST, 1, "food to test 3", 30.0f);
        foodsUsageManager.refresh(foodsUsageEntity);

        serviceApi.requestSummaryForDateId(observer, DateUtil.dateToDateId(date1));
        serviceApi.requestSummaryForDateId(observer, DateUtil.dateToDateId(date2));

        parametersHistoryManager.setTrackingWeekday(Calendar.SATURDAY);

        try {
            assertEquals(4, observer.summariesOnDayChanged.size());
            assertEquals(3, observer.summariesOnFoodsUsageChanged.size());
            assertEquals(2, observer.summariesOnSummaryRequested.size());
            assertEquals(1, observer.parametersChangedCount);
        } finally {
            serviceApi.unregisterViewObserver(observer);
        }

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
