package br.com.arndroid.etdiet.meals;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.provider.days.DaysManager;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageManager;
import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.virtualweek.UsageSummary;

public final class Meals {

    // Utility class
    private Meals() {

    }

    // The value of this constants can NOT
    // be changed since their values are stored
    // in database
    public static final int BREAKFAST = 0;
    public static final int BRUNCH = 1;
    public static final int LUNCH = 2;
    public static final int SNACK = 3;
    public static final int DINNER = 4;
    public static final int SUPPER = 5;
    public static final int EXERCISE = 6;
    private static final int MEAL_TOTAL_ITEMS = 7;

    public static int getMealFromPosition(int position) {
        // This is intended to be used by adapters (based in a list, with an actual position, etc...)

        switch (position) {
            // We know... We could return position here but is better to explicit the relationship!
            case 0:
                return BREAKFAST;
            case 1:
                return BRUNCH;
            case 2:
                return LUNCH;
            case 3:
                return SNACK;
            case 4:
                return DINNER;
            case 5:
                return SUPPER;
            case 6:
                return EXERCISE;
            default:
                throw new IllegalArgumentException("Invalid position=" + position);
        }
    }

    public static int getMealsCount() {
        return MEAL_TOTAL_ITEMS;
    }

    public static int getMealResourceNameIdFromMealId(int mealId) {
        switch (mealId) {
            case BREAKFAST:
                return R.string.breakfast;
            case BRUNCH:
                return R.string.brunch;
            case LUNCH:
                return R.string.lunch;
            case SNACK:
                return R.string.snack;
            case DINNER:
                return R.string.dinner;
            case SUPPER:
                return R.string.supper;
            case EXERCISE:
                return R.string.exercise;
            default:
                throw new IllegalArgumentException("Invalid mealId=" + mealId);
        }
    }

    public static int preferredMealForTimeInDate(Context context, int time, Date date, boolean fillMode) {
        return fillMode ? adviceMealFillMode(context, date) : adviceMeal(context, time, date);
    }

    private static int adviceMealFillMode(Context context, Date date) {
        final DaysEntity daysEntity = new DaysManager(context).dayFromDate(date);
        final UsageSummary usageSummary = new FoodsUsageManager(context).usageSummaryForDateId(
                DateUtils.dateToDateId(date));

        final MealsAdvisorHelper advisorHelper = new MealsAdvisorHelper();

        final int onlyMeals = getMealsCount() - 1;
        List<Integer> found = new ArrayList<>(onlyMeals);
        List<Integer> candidates = new ArrayList<>(onlyMeals);
        List<Integer> temp;
        for (int candidate = 0; candidate < onlyMeals; candidate++) {
            candidates.add(candidate);
        }

        // First of all, find meals with points < goal.
        advisorHelper.findMealsWithPointsMinorGoal(daysEntity, usageSummary, candidates, found);
        if (found.size() == 1) { return found.get(0); }
        if (found.isEmpty()) {
            // No meals with points < goal. Try to find meals with NO points.
            advisorHelper.findMealsWithNoPoints(usageSummary, candidates, found);
            if (found.size() == 1) { return found.get(0); }
            if (found.isEmpty()) { return candidates.get(candidates.size() - 1); }
            if (found.size() > 1) { return found.get(0); }
        }
        if (found.size() > 1) {
            // Many meals with points < goal. Try to find meals with NO points.
            temp = candidates; candidates = found; found = temp;
            advisorHelper.findMealsWithNoPoints(usageSummary, candidates, found);
            if (found.size() == 1) { return found.get(0); }
            if (found.isEmpty()) { return candidates.get(0); }
            if (found.size() > 1) { return found.get(0); }
        }

        throw new IllegalStateException("here we must have returned smt before.");
    }

    private static int adviceMeal(Context context, int time, Date date) {
        final DaysEntity daysEntity = new DaysManager(context).dayFromDate(date);
        final UsageSummary usageSummary = new FoodsUsageManager(context).usageSummaryForDateId(
                DateUtils.dateToDateId(date));

        final MealsAdvisorHelper advisorHelper = new MealsAdvisorHelper();

        final int onlyMeals = getMealsCount() - 1;
        List<Integer> found = new ArrayList<>(onlyMeals);
        List<Integer> candidates = new ArrayList<>(onlyMeals);
        List<Integer> temp;
        for (int candidate = 0; candidate < onlyMeals; candidate++) {
            candidates.add(candidate);
        }

        // First of all, find meals in period
        advisorHelper.findMealsInPeriod(time, daysEntity, candidates, found);
        if (found.size() == 1) { return found.get(0); }
        if (found.isEmpty()) {
            // No meals in period.

            // Try to find a union of closest meals ending before and closest meals starting after that
            final List<Integer> closestUnion = new ArrayList<>(onlyMeals);
            advisorHelper.findClosestMealsEndingBeforeTime(time, daysEntity, candidates, found);
            closestUnion.addAll(found);
            advisorHelper.findClosestMealsStartingAfterTime(time, daysEntity, candidates, found);
            closestUnion.addAll(found);

            // From union of closest meals, try to find meals with no points
            advisorHelper.findMealsWithNoPoints(usageSummary, closestUnion, found);
            if (found.size() == 1) { return found.get(0); }
            if (found.isEmpty()) {
                // Meals from union of closest only with points. Find the closest one from that union
                advisorHelper.findClosestNeighborsForTime(time, daysEntity, closestUnion, found);
                if (found.size() == 1) { return found.get(0); }
                if (found.size() > 1) { return found.get(found.size() - 1); }
                if (found.isEmpty()) { throw new IllegalStateException("here we must have found smt."); }
            } else {
                // Some meals from union of closest with no points. Find the closest one from this set
                temp = candidates; candidates = found; found = temp;
                advisorHelper.findClosestNeighborsForTime(time, daysEntity, candidates, found);
                if (found.size() == 1) { return found.get(0); }
                if (found.size() > 1) { return found.get(0); }
                if (found.isEmpty()) { throw new IllegalStateException("here we must have found smt."); }
            }
        } else {
            // Some meals in period. Try to find meals without points.
            temp = candidates; candidates = found; found = temp;
            advisorHelper.findMealsWithNoPoints(usageSummary, candidates, found);
            if (found.size() == 1) { return found.get(0); }
            if (found.isEmpty()) {
                // Meals in period only with points. Find meals with minor interval
                advisorHelper.findMealsWithMinorInterval(daysEntity, candidates, found);
                if (found.size() == 1) { return found.get(0); }
                if (found.size() > 1) { return found.get(found.size() - 1); }
                if (found.isEmpty()) { throw new IllegalStateException("here we must have found smt."); }
            } else {
                // Some meals in period without points. Find meals starting early.
                temp = candidates; candidates = found; found = temp;
                advisorHelper.findMealsStartingEarly(daysEntity, candidates, found);
                if (found.size() == 1) { return found.get(0); }
                if (found.size() > 1) {
                    // Some meal in period without points and starting at same time.
                    // Find meals with minor interval.
                    temp = candidates; candidates = found; found = temp;
                    advisorHelper.findMealsWithMinorInterval(daysEntity, candidates, found);
                    if (found.size() == 1) { return found.get(0); }
                    if (found.size() > 1) { return found.get(0); }
                    if (found.isEmpty()) { throw new IllegalStateException("here we must have found smt."); }
                }
                if (found.isEmpty()) { throw new IllegalStateException("here we must have found smt."); }
            }
        }

        throw new IllegalStateException("here we must have returned smt before.");
    }

    public static float preferredUsageForMealInDate(Context context, int meal, Date date) {
        DaysEntity entity = new DaysManager(context).dayFromDate(date);
        UsageSummary summary = new FoodsUsageManager(context).usageSummaryForDateId(DateUtils.dateToDateId(date));

        float mealGoal, mealUsed;
        switch (meal) {
            case BREAKFAST:
                mealGoal = entity.getBreakfastGoal();
                mealUsed = summary.getBreakfastUsed();
                break;
            case BRUNCH:
                mealGoal = entity.getBrunchGoal();
                mealUsed = summary.getBrunchUsed();
                break;
            case LUNCH:
                mealGoal = entity.getLunchGoal();
                mealUsed = summary.getLunchUsed();
                break;
            case SNACK:
                mealGoal = entity.getSnackGoal();
                mealUsed = summary.getSnackUsed();
                break;
            case DINNER:
                mealGoal = entity.getDinnerGoal();
                mealUsed = summary.getDinnerUsed();
                break;
            case SUPPER:
                mealGoal = entity.getSupperGoal();
                mealUsed = summary.getSupperUsed();
                break;
            case EXERCISE:
                mealGoal = entity.getExerciseGoal();
                mealUsed = summary.getExerciseDone();
                break;
            default:
                throw new IllegalArgumentException("Invalid meal=" + meal);
        }

        final float preferredUsage = mealGoal - mealUsed;
        return preferredUsage < 0 ? 0 : preferredUsage;
    }
}