package br.com.arndroid.etdiet.forecast;

import java.util.Date;

import br.com.arndroid.etdapi.data.Meal;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.virtualweek.DaySummary;
import br.com.arndroid.etdiet.virtualweek.UsageSummary;

public class Forecaster {

    private static Forecaster mInstance;

    public static Forecaster getInstance() {
        /*
         We don't worry about lost of one or two instances in a multi-threaded scenario (which will
         be very rare).
         */
        if (mInstance == null) {
            mInstance = new Forecaster();
        }
        return mInstance;
    }

    protected Forecaster() {
    }

    public ForecastEntity forecast(Date referenceDate, DaySummary summary) {
        final int time = DateUtils.dateToTimeAsInt(referenceDate);
        final DaysEntity daysEntity = summary.getEntity();
        final UsageSummary usageSummary = summary.getUsage();
        float toUse = 0.0f;
        for (Meal meal : Meal.getOnlyMealsUnmodifiableSet()) {
            if (time < daysEntity.getEndTimeForMeal(meal)
                    && usageSummary.getUsageForMeal(meal) < daysEntity.getGoalForMeal(meal)) {
                toUse += daysEntity.getGoalForMeal(meal) - usageSummary.getUsageForMeal(meal);
            }
        }

        final float used = usageSummary.getTotalUsed();
        final float forecast = used + toUse;

        float reserves = summary.getWeeklyAllowanceBeforeUsage();
        if (summary.getSettingsValues().getExerciseUseMode() != Contract.ParametersHistory.EXERCISE_USE_MODE_DONT_USE) {
            reserves += summary.getTotalExercise();
        }

        final ForecastEntity result = new ForecastEntity();
        final float allowed = daysEntity.getAllowed();
        final float goal = summary.getGoalBeforeUsage();

        if (forecast <= goal) {
            result.setForecastType(ForecastEntity.STRAIGHT_TO_GOAL);
        } else if (forecast <= allowed + reserves) {
            result.setForecastType(ForecastEntity.OUT_OF_GOAL_WITH_ENOUGH_RESERVES);
        } else if (used <= allowed + reserves) {
            result.setForecastType(ForecastEntity.OUT_OF_GOAL_BUT_CAN_RETURN);
        } else {
            result.setForecastType(ForecastEntity.OUT_OF_GOAL);
        }
        result.setReferenceDate(referenceDate);
        result.setUsed(used);
        result.setToUse(toUse);
        result.setForecastUsed(forecast);
        result.setBalance(goal - forecast);
        result.setGoalType(summary.getGoalType());

        return result;
    }
}