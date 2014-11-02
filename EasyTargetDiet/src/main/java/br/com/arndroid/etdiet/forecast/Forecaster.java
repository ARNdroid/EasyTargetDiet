package br.com.arndroid.etdiet.forecast;

import java.util.Date;

import br.com.arndroid.etdiet.meals.Meals;
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
        float used = 0.0f;
        float toUse = 0.0f;

        for (int i = 0; i < Meals.getMealsCount() - 1; i++) {
            used += usageSummary.getUsageForMeal(i);
            if (time < daysEntity.getEndTimeForMeal(i)
                    && usageSummary.getUsageForMeal(i)  < daysEntity.getGoalForMeal(i)) {
                toUse += daysEntity.getGoalForMeal(i) - usageSummary.getUsageForMeal(i);
            }
        }
        final float forecast = used + toUse;

        float reserve = summary.getWeeklyAllowanceBeforeUsage();
        if (summary.getSettingsValues().getExerciseUseMode() != Contract.ParametersHistory.EXERCISE_USE_MODE_DONT_USE) {
            reserve += summary.getTotalExercise();
        }

        final ForecastEntity result = new ForecastEntity();
        if (forecast <= daysEntity.getAllowed()) {
            result.setForecastType(ForecastEntity.STRAIGHT_TO_GOAL);
        } else if (forecast <= daysEntity.getAllowed() + reserve) {
            result.setForecastType(ForecastEntity.GOING_TO_GOAL_WITH_HELP);
        } else if (used <= daysEntity.getAllowed() + reserve) {
            result.setForecastType(ForecastEntity.OUT_OF_GOAL_BUT_CAN_RETURN);
        } else {
            result.setForecastType(ForecastEntity.OUT_OF_GOAL);
        }
        result.setReferenceDate(referenceDate);
        result.setUsed(used);
        result.setToUse(toUse);
        result.setForecastUsed(forecast);
        result.setBalanceFromDailyAllowance(daysEntity.getAllowed() - forecast);

        return result;
    }
}