package br.com.arndroid.etdiet.test.virtualweek;

import android.test.ProviderTestCase2;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdapi.data.Meal;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.Provider;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageManager;
import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.virtualweek.VirtualWeekEngine;
import br.com.arndroid.etdiet.virtualweek.WeekPeriod;

public class VirtualWeekEngineTest extends ProviderTestCase2<Provider> {

    public VirtualWeekEngineTest() {
        super(Provider.class, Contract.AUTHORITY);
    }

    public void testWithoutDaysInDatabaseMustReturnCorrectValue() {
        VirtualWeekEngine virtualWeekEngine = new VirtualWeekEngine(getMockContext(), new Date());

        for(int i = VirtualWeekEngine.FIRST_WEEKDAY_INDEX; i < VirtualWeekEngine.DAYS_IN_A_WEEK; i++) {
            assertEquals(26.0f, virtualWeekEngine.getAllowedForIndex(i));
        }
        for(int i = VirtualWeekEngine.FIRST_WEEKDAY_INDEX; i < VirtualWeekEngine.DAYS_IN_A_WEEK; i++) {
            assertEquals(0.0f, virtualWeekEngine.getTotalUsedForIndex(i));
        }
        for(int i = VirtualWeekEngine.FIRST_WEEKDAY_INDEX; i < VirtualWeekEngine.DAYS_IN_A_WEEK; i++) {
            assertEquals(0.0f, virtualWeekEngine.getTotalExerciseForIndex(i));
        }
        for(int i = VirtualWeekEngine.FIRST_WEEKDAY_INDEX; i < VirtualWeekEngine.DAYS_IN_A_WEEK; i++) {
            assertEquals(49.0f, virtualWeekEngine.getWeeklyAllowanceBeforeUsageForIndex(i));
        }
        for(int i = VirtualWeekEngine.FIRST_WEEKDAY_INDEX; i < VirtualWeekEngine.DAYS_IN_A_WEEK; i++) {
            assertEquals(26.0f, virtualWeekEngine.getDiaryAllowanceAfterUsageForIndex(i));
        }
        for(int i = VirtualWeekEngine.FIRST_WEEKDAY_INDEX; i < VirtualWeekEngine.DAYS_IN_A_WEEK; i++) {
            assertEquals(0.0f, virtualWeekEngine.getExerciseAfterUsageForIndex(i));
        }
        for(int i = VirtualWeekEngine.FIRST_WEEKDAY_INDEX; i < VirtualWeekEngine.DAYS_IN_A_WEEK; i++) {
            assertEquals(49.0f, virtualWeekEngine.getWeeklyAllowanceAfterUsageForIndex(i));
        }
    }

    public void testWithDaysInDatabaseMustReturnCorrectValue() {
        final FoodsUsageManager manager = new FoodsUsageManager(getMockContext());
        WeekPeriod period = new WeekPeriod(getMockContext(), new Date());
        Calendar calendar = Calendar.getInstance();
        FoodsUsageEntity food = new FoodsUsageEntity(null, null, Meal.BREAKFAST, 0, "food for test", null);
        FoodsUsageEntity exercise = new FoodsUsageEntity(null, null, Meal.EXERCISE, 0, "exercise for test", null);

        calendar.setTime(period.getInitialDate());
        food.setDateId(DateUtils.dateToDateId(calendar.getTime()));
        food.setValue(30.0f);
        manager.refresh(food);
        exercise.setDateId(DateUtils.dateToDateId(calendar.getTime()));
        exercise.setValue(2.0f);
        manager.refresh(exercise);

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        food.setId(null);
        food.setDateId(DateUtils.dateToDateId(calendar.getTime()));
        food.setValue(25.0f);
        manager.refresh(food);

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        food.setId(null);
        food.setDateId(DateUtils.dateToDateId(calendar.getTime()));
        food.setValue(126.0f);
        manager.refresh(food);

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        food.setId(null);
        food.setDateId(DateUtils.dateToDateId(calendar.getTime()));
        food.setValue(6.0f);
        manager.refresh(food);
        exercise.setId(null);
        exercise.setDateId(DateUtils.dateToDateId(calendar.getTime()));
        exercise.setValue(5.0f);
        manager.refresh(exercise);

        calendar.add(Calendar.DAY_OF_MONTH, 2);
        food.setId(null);
        food.setDateId(DateUtils.dateToDateId(calendar.getTime()));
        food.setValue(27.0f);
        manager.refresh(food);

        VirtualWeekEngine virtualWeekEngine = new VirtualWeekEngine(getMockContext(), new Date());

        assertComputedValuesForIndex(virtualWeekEngine, 0, 26.0f, 30.0f, 2.0f, 49.0f, 0.0f, 0.0f, 47.0f);
        assertComputedValuesForIndex(virtualWeekEngine, 1, 26.0f, 25.0f, 0.0f, 47.0f, 1.0f, 0.0f, 47.0f);
        assertComputedValuesForIndex(virtualWeekEngine, 2, 26.0f, 126.0f, 0.0f, 47.0f, -53.0f, 0.0f, 0.0f);
        assertComputedValuesForIndex(virtualWeekEngine, 3, 26.0f, 6.0f, 5.0f, 0.0f, 20.0f, 5.0f, 0.0f);
        assertComputedValuesForIndex(virtualWeekEngine, 4, 26.0f, 0.0f, 0.0f, 0.0f, 26.0f, 0.0f, 0.0f);
        assertComputedValuesForIndex(virtualWeekEngine, 5, 26.0f, 27.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f);
        assertComputedValuesForIndex(virtualWeekEngine, 6, 26.0f, 0.0f, 0.0f, 0.0f, 26.0f, 0.0f, 0.0f);
    }

    @SuppressWarnings("SameParameterValue")
    private void assertComputedValuesForIndex(VirtualWeekEngine virtualWeekEngine, int index, float allowed,
                                              float totalUsed, float totalExercise, float weeklyAllowanceBeforeUsage,
                                              float diaryAllowanceAfterUsage, float exerciseAfterUsage,
                                              float weeklyAllowanceAfterUsage) {

        assertEquals(allowed, virtualWeekEngine.getAllowedForIndex(index));
        assertEquals(totalUsed, virtualWeekEngine.getTotalUsedForIndex(index));
        assertEquals(totalExercise, virtualWeekEngine.getTotalExerciseForIndex(index));
        assertEquals(weeklyAllowanceBeforeUsage, virtualWeekEngine.getWeeklyAllowanceBeforeUsageForIndex(index));
        assertEquals(diaryAllowanceAfterUsage, virtualWeekEngine.getDiaryAllowanceAfterUsageForIndex(index));
        assertEquals(exerciseAfterUsage, virtualWeekEngine.getExerciseAfterUsageForIndex(index));
        assertEquals(weeklyAllowanceAfterUsage, virtualWeekEngine.getWeeklyAllowanceAfterUsageForIndex(index));
    }

}
