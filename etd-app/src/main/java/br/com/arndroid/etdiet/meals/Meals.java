package br.com.arndroid.etdiet.meals;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.arndroid.etdapi.data.Meal;
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

    public static Meal getMealFromPosition(int position) {
        // This is intended to be used by adapters (based in a list, with an actual position, etc...)
        return Meal.fromInteger(position);
    }

    public static int getPositionFromMeal(Meal meal) {
        // Counter part of getMealFromPosition
        return meal.getCorrelationId();
    }

    @SuppressWarnings("SameReturnValue")
    public static int getMealsCount() {
        return Meal.sizeOfAll;
    }

    public static int getMealResourceNameIdFromMeal(Meal meal) {
        switch (meal) {
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
                throw new IllegalArgumentException("Invalid meal=" + meal);
        }
    }

    public static Meal preferredMealForTimeInDate(Context context, int time, Date date, boolean fillMode) {
        return fillMode ? adviceMealFillMode(context, date) : adviceMeal(context, time, date);
    }

    private static Meal adviceMealFillMode(Context context, Date date) {
        final DaysEntity daysEntity = new DaysManager(context).dayFromDate(date);
        final UsageSummary usageSummary = new FoodsUsageManager(context).usageSummaryForDateId(
                DateUtils.dateToDateId(date));

        final MealsAdvisorHelper advisorHelper = new MealsAdvisorHelper();

        final Set<Meal> onlyMeals = Meal.getOnlyMealsUnmodifiableSet();
        List<Meal> found = new ArrayList<>(Meal.sizeOfOnlyMeals);
        List<Meal> candidates = new ArrayList<>(Meal.sizeOfOnlyMeals);
        List<Meal> temp;
        for (Meal candidate : onlyMeals) {
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

    private static Meal adviceMeal(Context context, int time, Date date) {
        final DaysEntity daysEntity = new DaysManager(context).dayFromDate(date);
        final UsageSummary usageSummary = new FoodsUsageManager(context).usageSummaryForDateId(
                DateUtils.dateToDateId(date));

        final MealsAdvisorHelper advisorHelper = new MealsAdvisorHelper();

        final Set<Meal> onlyMeals = Meal.getOnlyMealsUnmodifiableSet();
        final int sizeOfOnlyMeals = onlyMeals.size();
        List<Meal> found = new ArrayList<>(Meal.sizeOfOnlyMeals);
        List<Meal> candidates = new ArrayList<>(Meal.sizeOfOnlyMeals);
        List<Meal> temp;
        for (Meal candidate : onlyMeals) {
            candidates.add(candidate);
        }

        // First of all, find meals in period
        advisorHelper.findMealsInPeriod(time, daysEntity, candidates, found);
        if (found.size() == 1) { return found.get(0); }
        if (found.isEmpty()) {
            // No meals in period.

            // Try to find a union of closest meals ending before and closest meals starting after that
            final List<Meal> closestUnion = new ArrayList<>(sizeOfOnlyMeals);
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

    public static float preferredUsageForMealInDate(Context context, Meal meal, Date date) {
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