package br.com.arndroid.etdiet.test.virtualweek;

import android.os.Build;
import android.test.IsolatedContext;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.arndroid.etdapi.data.Meal;
import br.com.arndroid.etdiet.compat.Compatibility;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.Provider;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.provider.days.DaysManager;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageManager;
import br.com.arndroid.etdiet.provider.parametershistory.ParametersHistoryManager;
import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.utils.sk.m217.tests.utils.MockContentResolver2;
import br.com.arndroid.etdiet.virtualweek.DaySummary;
import br.com.arndroid.etdiet.virtualweek.VirtualWeek;
import br.com.arndroid.etdiet.virtualweek.WeekPeriod;
import br.com.arndroid.etdiet.utils.sk.m217.tests.utils.ProviderTestCase3;

public class VirtualWeekTest extends ProviderTestCase3<Provider> {

    public VirtualWeekTest() {
        super(Provider.class, Contract.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        // Important: calling the base class implementation of this method
        // where the "magic" of isolation is set up:
        super.setUp();
    }

    public void testWithoutDataChangeInPeriodMustNotCallObserver() {
        // This test is NOT compatible with API > 20. For more information see #162.
        if (Compatibility.getInstance().compatibilityLevel() > Build.VERSION_CODES.KITKAT_WATCH) {
            // OK... JUnit 3 doesn't have a good way to warn the runner. We will assume a pass here.
            assertTrue(true);
            return;
        }

        final IsolatedContext mockContext = getMockContext();
        final MockContentResolver2 contentResolver2 = getMockContentResolver();
        final VirtualWeek virtualWeek = VirtualWeek.getInstanceForTests(mockContext, contentResolver2);

        final ViewObserverMonitor observer = new ViewObserverMonitor();
        virtualWeek.registerViewObserver(observer);
        final WeekPeriod weekPeriod = new WeekPeriod(mockContext, new Date());

        Calendar cal = Calendar.getInstance();

        cal.setTime(weekPeriod.getInitialDate());
        cal.add(Calendar.DAY_OF_MONTH, -1);


        final DaysManager daysManager = new DaysManager(mockContext);
        DaysEntity daysEntity = daysManager.dayFromDate(cal.getTime());
        daysManager.refresh(daysEntity);
        // Refreshing again to updated:
        daysManager.refresh(daysEntity);

        final FoodsUsageManager foodsUsageManager = new FoodsUsageManager(mockContext);
        FoodsUsageEntity foodsUsageEntity = new FoodsUsageEntity(null, DateUtils.dateToDateId(cal.getTime()),
                Meal.BREAKFAST, 1, "food to test", 10.0f);
        foodsUsageManager.refresh(foodsUsageEntity);

        cal.setTime(weekPeriod.getFinalDate());
        cal.add(Calendar.DAY_OF_MONTH, 1);

        daysEntity = daysManager.dayFromDate(cal.getTime());
        daysManager.refresh(daysEntity);

        foodsUsageEntity = new FoodsUsageEntity(null, DateUtils.dateToDateId(cal.getTime()),
                Meal.BREAKFAST, 1, "food to test", 10.0f);
        foodsUsageManager.refresh(foodsUsageEntity);
        // Refreshing again to update:
        foodsUsageManager.refresh(foodsUsageEntity);
        // Deleting:
        foodsUsageManager.remove(foodsUsageEntity.getId());

        virtualWeek.unregisterViewObserver(observer);

        assertEquals(0, observer.summariesOnDayChanged.size());
        assertEquals(0, observer.summariesOnFoodsUsageChanged.size());
        assertEquals(0, observer.summariesOnSummaryRequested.size());
        assertEquals(0, observer.parametersChangedCount);
    }

    public void testWithDataChangeInPeriodMustCallObserver() {
        // This test is NOT compatible with API > 20. For more information see #162.
        if (Compatibility.getInstance().compatibilityLevel() > Build.VERSION_CODES.KITKAT_WATCH) {
            // OK... JUnit 3 doesn't have a good way to warn the runner. We will assume a pass here.
            assertTrue(true);
            return;
        }

        final IsolatedContext mockContext = getMockContext();
        final MockContentResolver2 contentResolver2 = getMockContentResolver();
        final VirtualWeek virtualWeek = VirtualWeek.getInstanceForTests(mockContext, contentResolver2);

        final ViewObserverMonitor observer = new ViewObserverMonitor();
        virtualWeek.registerViewObserver(observer);

        final WeekPeriod weekPeriod = new WeekPeriod(mockContext, new Date());
        final Calendar cal = Calendar.getInstance();
        cal.setTime(weekPeriod.getInitialDate());
        final Date date0 = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        final Date date1 = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        final Date date2 = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        final Date date3 = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        final Date date4 = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        final Date date5 = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        final Date date6 = cal.getTime();

        final DaysManager daysManager = new DaysManager(mockContext);
        DaysEntity daysEntity = daysManager.dayFromDate(date0);
        daysManager.refresh(daysEntity);
        daysEntity = daysManager.dayFromDate(date1);
        daysManager.refresh(daysEntity);
        daysEntity = daysManager.dayFromDate(date2);
        daysManager.refresh(daysEntity);
        daysEntity = daysManager.dayFromDate(date3);
        daysManager.refresh(daysEntity);
        daysEntity = daysManager.dayFromDate(date4);
        daysManager.refresh(daysEntity);
        daysEntity = daysManager.dayFromDate(date5);
        daysManager.refresh(daysEntity);
        daysEntity = daysManager.dayFromDate(date6);
        daysManager.refresh(daysEntity);
        // Refreshing again to update:
        daysManager.refresh(daysEntity);

        final FoodsUsageManager foodsUsageManager = new FoodsUsageManager(mockContext);
        FoodsUsageEntity foodsUsageEntity = new FoodsUsageEntity(null, DateUtils.dateToDateId(date0),
                Meal.BREAKFAST, 1, "food to test 1", 10.0f);
        foodsUsageManager.refresh(foodsUsageEntity);
        foodsUsageEntity = new FoodsUsageEntity(null, DateUtils.dateToDateId(date1),
                Meal.BREAKFAST, 1, "food to test 2", 20.0f);
        foodsUsageManager.refresh(foodsUsageEntity);
        foodsUsageEntity = new FoodsUsageEntity(null, DateUtils.dateToDateId(date2),
                Meal.BREAKFAST, 1, "food to test 3", 30.0f);
        foodsUsageManager.refresh(foodsUsageEntity);
        // Refreshing again to update:
        foodsUsageManager.refresh(foodsUsageEntity);
        // Deleting:
        foodsUsageManager.remove(foodsUsageEntity.getId());

        virtualWeek.requestSummaryForObserverAndDateId(observer, DateUtils.dateToDateId(date0));
        virtualWeek.requestSummaryForObserverAndDateId(observer, DateUtils.dateToDateId(date1));

        final ParametersHistoryManager parametersHistoryManager = new ParametersHistoryManager(mockContext);
        parametersHistoryManager.setTrackingWeekday(Calendar.MONDAY);

        virtualWeek.unregisterViewObserver(observer);

        assertEquals(8, observer.summariesOnDayChanged.size());
        assertEquals(observer.summariesOnDayChanged.get(0).getEntity().getDateId(), DateUtils.dateToDateId(date0));
        assertEquals(observer.summariesOnDayChanged.get(1).getEntity().getDateId(), DateUtils.dateToDateId(date1));
        assertEquals(observer.summariesOnDayChanged.get(2).getEntity().getDateId(), DateUtils.dateToDateId(date2));
        assertEquals(observer.summariesOnDayChanged.get(3).getEntity().getDateId(), DateUtils.dateToDateId(date3));
        assertEquals(observer.summariesOnDayChanged.get(4).getEntity().getDateId(), DateUtils.dateToDateId(date4));
        assertEquals(observer.summariesOnDayChanged.get(5).getEntity().getDateId(), DateUtils.dateToDateId(date5));
        assertEquals(observer.summariesOnDayChanged.get(6).getEntity().getDateId(), DateUtils.dateToDateId(date6));
        assertEquals(observer.summariesOnDayChanged.get(7).getEntity().getDateId(), DateUtils.dateToDateId(date6));

        assertEquals(5, observer.summariesOnFoodsUsageChanged.size());
        assertEquals(observer.summariesOnFoodsUsageChanged.get(0).getEntity().getDateId(), DateUtils.dateToDateId(date0));
        assertEquals(observer.summariesOnFoodsUsageChanged.get(1).getEntity().getDateId(), DateUtils.dateToDateId(date1));
        assertEquals(observer.summariesOnFoodsUsageChanged.get(2).getEntity().getDateId(), DateUtils.dateToDateId(date2));
        assertEquals(observer.summariesOnFoodsUsageChanged.get(3).getEntity().getDateId(), DateUtils.dateToDateId(date2));
        assertEquals(observer.summariesOnFoodsUsageChanged.get(4).getEntity().getDateId(), DateUtils.dateToDateId(date2));

        assertEquals(2, observer.summariesOnSummaryRequested.size());
        assertEquals(observer.summariesOnSummaryRequested.get(0).getEntity().getDateId(), DateUtils.dateToDateId(date0));
        assertEquals(observer.summariesOnSummaryRequested.get(1).getEntity().getDateId(), DateUtils.dateToDateId(date1));

        assertEquals(1, observer.parametersChangedCount);
    }

    public class ViewObserverMonitor implements VirtualWeek.ViewObserver {
        final public List<DaySummary> summariesOnDayChanged = new ArrayList<>();
        final public List<DaySummary> summariesOnFoodsUsageChanged = new ArrayList<>();
        final public List<DaySummary> summariesOnSummaryRequested = new ArrayList<>();
        public int parametersChangedCount = 0;
        public int databaseRestoredCount = 0;

        @Override
        public void onDayChanged(DaySummary summary) {
            summariesOnDayChanged.add(summary);
        }

        @Override
        public void onFoodsUsageChanged(DaySummary summary) {
            summariesOnFoodsUsageChanged.add(summary);
        }

        @Override
        public void onParametersChanged() {
            parametersChangedCount++;
        }

        @Override
        public void onSummaryRequested(DaySummary summary) {
            summariesOnSummaryRequested.add(summary);
        }

        @Override
        public void onDatabaseRestored() {
            databaseRestoredCount++;
        }
    }
}
