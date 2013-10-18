package br.com.arndroid.etdiet.test.virtualweek;

import android.content.ContentResolver;
import android.test.ProviderTestCase2;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.Provider;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageManager;
import br.com.arndroid.etdiet.util.DateUtil;
import br.com.arndroid.etdiet.virtualweek.VirtualWeek;
import br.com.arndroid.etdiet.virtualweek.WeekPeriod;

public class VirtualWeekTest extends ProviderTestCase2<Provider> {

    public VirtualWeekTest() {
        super(Provider.class, Contract.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        // Important: calling the base class implementation of this method
        // where the "magic" of isolation is set up:
        super.setUp();
    }

    public void testWithoutDaysInDatabaseMustReturnCorrectValue() {
        VirtualWeek virtualWeek = new VirtualWeek(getMockContext(), new Date());

        for(int i = VirtualWeek.FIRST_WEEKDAY_INDEX; i < VirtualWeek.DAYS_IN_A_WEEK; i++) {
            assertEquals(26.0f, virtualWeek.getAllowedForIndex(i));
        }
        for(int i = VirtualWeek.FIRST_WEEKDAY_INDEX; i < VirtualWeek.DAYS_IN_A_WEEK; i++) {
            assertEquals(0.0f, virtualWeek.getTotalUsedForIndex(i));
        }
        for(int i = VirtualWeek.FIRST_WEEKDAY_INDEX; i < VirtualWeek.DAYS_IN_A_WEEK; i++) {
            assertEquals(0.0f, virtualWeek.getTotalExerciseForIndex(i));
        }
        for(int i = VirtualWeek.FIRST_WEEKDAY_INDEX; i < VirtualWeek.DAYS_IN_A_WEEK; i++) {
            assertEquals(49.0f, virtualWeek.getWeeklyAllowanceBeforeUsageForIndex(i));
        }
        for(int i = VirtualWeek.FIRST_WEEKDAY_INDEX; i < VirtualWeek.DAYS_IN_A_WEEK; i++) {
            assertEquals(26.0f, virtualWeek.getDiaryAllowanceAfterUsageForIndex(i));
        }
        for(int i = VirtualWeek.FIRST_WEEKDAY_INDEX; i < VirtualWeek.DAYS_IN_A_WEEK; i++) {
            assertEquals(0.0f, virtualWeek.getExerciseAfterUsageForIndex(i));
        }
        for(int i = VirtualWeek.FIRST_WEEKDAY_INDEX; i < VirtualWeek.DAYS_IN_A_WEEK; i++) {
            assertEquals(49.0f, virtualWeek.getWeeklyAllowanceAfterUsageForIndex(i));
        }
    }

    public void testWithDaysInDatabaseMustReturnCorrectValue() {
        final FoodsUsageManager manager = new FoodsUsageManager(getMockContext());
        WeekPeriod period = new WeekPeriod(getMockContext(), new Date());
        Calendar calendar = Calendar.getInstance();
        FoodsUsageEntity food = new FoodsUsageEntity(null, null, Meals.BREAKFAST, 0, "food for test", null);
        FoodsUsageEntity exercise = new FoodsUsageEntity(null, null, Meals.EXERCISE, 0, "exercise for test", null);

        calendar.setTime(period.getInitialDate());
        food.setDateId(DateUtil.dateToDateId(calendar.getTime()));
        food.setValue(30.0f);
        manager.refresh(food);
        exercise.setDateId(DateUtil.dateToDateId(calendar.getTime()));
        exercise.setValue(2.0f);
        manager.refresh(exercise);

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        food.setId(null);
        food.setDateId(DateUtil.dateToDateId(calendar.getTime()));
        food.setValue(25.0f);
        manager.refresh(food);

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        food.setId(null);
        food.setDateId(DateUtil.dateToDateId(calendar.getTime()));
        food.setValue(126.0f);
        manager.refresh(food);

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        food.setId(null);
        food.setDateId(DateUtil.dateToDateId(calendar.getTime()));
        food.setValue(6.0f);
        manager.refresh(food);
        exercise.setId(null);
        exercise.setDateId(DateUtil.dateToDateId(calendar.getTime()));
        exercise.setValue(5.0f);
        manager.refresh(exercise);

        calendar.add(Calendar.DAY_OF_MONTH, 2);
        food.setId(null);
        food.setDateId(DateUtil.dateToDateId(calendar.getTime()));
        food.setValue(27.0f);
        manager.refresh(food);

        VirtualWeek virtualWeek = new VirtualWeek(getMockContext(), new Date());

        assertComputedValuesForIndex(virtualWeek, 0, 26.0f, 30.0f, 2.0f, 49.0f, 0.0f, 0.0f, 47.0f);
        assertComputedValuesForIndex(virtualWeek, 1, 26.0f, 25.0f, 0.0f, 47.0f, 1.0f, 0.0f, 47.0f);
        assertComputedValuesForIndex(virtualWeek, 2, 26.0f, 126.0f, 0.0f, 47.0f, -53.0f, 0.0f, 0.0f);
        assertComputedValuesForIndex(virtualWeek, 3, 26.0f, 6.0f, 5.0f, 0.0f, 20.0f, 5.0f, 0.0f);
        assertComputedValuesForIndex(virtualWeek, 4, 26.0f, 0.0f, 0.0f, 0.0f, 26.0f, 0.0f, 0.0f);
        assertComputedValuesForIndex(virtualWeek, 5, 26.0f, 27.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f);
        assertComputedValuesForIndex(virtualWeek, 6, 26.0f, 0.0f, 0.0f, 0.0f, 26.0f, 0.0f, 0.0f);
    }

    private void assertComputedValuesForIndex(VirtualWeek virtualWeek, int index, float allowed,
                                              float totalUsed, float totalExercise, float weeklyAllowanceBeforeUsage,
                                              float diaryAllowanceAfterUsage, float exerciseAfterUsage,
                                              float weeklyAllowanceAfterUsage) {

        assertEquals(allowed, virtualWeek.getAllowedForIndex(index));
        assertEquals(totalUsed, virtualWeek.getTotalUsedForIndex(index));
        assertEquals(totalExercise, virtualWeek.getTotalExerciseForIndex(index));
        assertEquals(weeklyAllowanceBeforeUsage, virtualWeek.getWeeklyAllowanceBeforeUsageForIndex(index));
        assertEquals(diaryAllowanceAfterUsage, virtualWeek.getDiaryAllowanceAfterUsageForIndex(index));
        assertEquals(exerciseAfterUsage, virtualWeek.getExerciseAfterUsageForIndex(index));
        assertEquals(weeklyAllowanceAfterUsage, virtualWeek.getWeeklyAllowanceAfterUsageForIndex(index));
    }

}
