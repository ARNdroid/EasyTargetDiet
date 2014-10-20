package br.com.arndroid.etdiet.foodsusage;

import java.util.Date;

import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.virtualweek.DaySummary;
import br.com.arndroid.etdiet.virtualweek.UsageSummary;
import br.com.arndroid.etdiet.virtualweek.VirtualWeek;

public class FoodsUsageForecaster {

    private static FoodsUsageForecaster mInstance;

    public static FoodsUsageForecaster getInstance() {
        /*
         We don't worry about lost of one or two instances in a multi-threaded scenario (which will
         be very rare).
         */
        if (mInstance == null) {
            mInstance = new FoodsUsageForecaster();
        }
        return mInstance;
    }

    protected FoodsUsageForecaster() {
    }

    public FoodsUsageForecast forecast(Date referenceDate, DaySummary summary) {
        final int time = DateUtils.dateToTimeAsInt(referenceDate);
        final DaysEntity daysEntity = summary.getEntity();
        final UsageSummary usageSummary = summary.getUsage();
        float used = 0.0f;
        float toUse = 0.0f;

        for (int i = 0; i < Meals.getMealsCount() - 1; i++) {
            used += getMealUsageInSummaryForMeal(usageSummary, i);
            if (time < getEndTimeInDaysEntityForMeal(daysEntity, i)
                    && getMealUsageInSummaryForMeal(usageSummary, i) < getGoalInDaysEntityForMeal(daysEntity, i)) {
                toUse += getGoalInDaysEntityForMeal(daysEntity, i) - getMealUsageInSummaryForMeal(usageSummary, i);
            }
        }
        final float forecast = used + toUse;

        final FoodsUsageForecast result = new FoodsUsageForecast();
        if (forecast <= daysEntity.getAllowed()) {
            result.setForecastType(FoodsUsageForecast.STRAIGHT_TO_GOAL);
        } else if (forecast <= daysEntity.getAllowed() + summary.getWeeklyAllowanceBeforeUsage() + summary.getTotalExercise()) {
            result.setForecastType(FoodsUsageForecast.GOING_TO_GOAL_WITH_HELP);
        } else if (used <= daysEntity.getAllowed() + summary.getWeeklyAllowanceBeforeUsage() + summary.getTotalExercise()) {
            result.setForecastType(FoodsUsageForecast.OUT_OF_GOAL_BUT_CAN_RETURN);
        } else {
            result.setForecastType(FoodsUsageForecast.OUT_OF_GOAL);
        }
        result.setReferenceDate(referenceDate);
        result.setUsed(used);
        result.setToUse(toUse);
        result.setForecastUsed(forecast);

        return result;
    }

    private float getMealUsageInSummaryForMeal(UsageSummary summary, Integer meal) {
        switch (meal) {
            case Meals.BREAKFAST:
                return summary.getBreakfastUsed();
            case Meals.BRUNCH:
                return summary.getBrunchUsed();
            case Meals.LUNCH:
                return summary.getLunchUsed();
            case Meals.SNACK:
                return summary.getSnackUsed();
            case Meals.DINNER:
                return summary.getDinnerUsed();
            case Meals.SUPPER:
                return summary.getSupperUsed();
            default:
                throw new IllegalArgumentException("Invalid meal=" + meal + ".");
        }
    }

    private int getEndTimeInDaysEntityForMeal(DaysEntity entity, int meal) {
        switch (meal) {
            case Meals.BREAKFAST:
                return entity.getBreakfastEndTime();
            case Meals.BRUNCH:
                return entity.getBrunchEndTime();
            case Meals.LUNCH:
                return entity.getLunchEndTime();
            case Meals.SNACK:
                return entity.getSnackEndTime();
            case Meals.DINNER:
                return entity.getDinnerEndTime();
            case Meals.SUPPER:
                return entity.getSupperEndTime();
            default:
                throw new IllegalArgumentException("Invalid meal=" + meal + ".");
        }
    }

    private float getGoalInDaysEntityForMeal(DaysEntity entity, int meal) {
        switch (meal) {
            case Meals.BREAKFAST:
                return entity.getBreakfastGoal();
            case Meals.BRUNCH:
                return entity.getBrunchGoal();
            case Meals.LUNCH:
                return entity.getLunchGoal();
            case Meals.SNACK:
                return entity.getSnackGoal();
            case Meals.DINNER:
                return entity.getDinnerGoal();
            case Meals.SUPPER:
                return entity.getSupperGoal();
            default:
                throw new IllegalArgumentException("Invalid meal=" + meal + ".");
        }
    }
}