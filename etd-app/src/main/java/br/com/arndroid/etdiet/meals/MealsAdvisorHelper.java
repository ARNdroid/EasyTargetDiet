package br.com.arndroid.etdiet.meals;

import java.util.ArrayList;
import java.util.List;

import br.com.arndroid.etdapi.data.Meal;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.virtualweek.UsageSummary;

public class MealsAdvisorHelper {

    public void findMealsInPeriod(int time, DaysEntity daysEntity, List<Meal> candidates, List<Meal> result) {
        validateListParametersOrThrow(candidates, result);
        result.clear();

        for (Meal meal : candidates) {
            if (time >= daysEntity.getStartTimeForMeal(meal) &&
                    time <= daysEntity.getEndTimeForMeal(meal)) {
                result.add(meal);
            }
        }
    }

    public void findMealsWithNoPoints(UsageSummary usageSummary, List<Meal> candidates, List<Meal> result) {

        validateListParametersOrThrow(candidates, result);
        result.clear();

        for (Meal meal : candidates) {
            if (usageSummary.getUsageForMeal(meal) == 0) {
                result.add(meal);
            }
        }
    }

    public void findMealsWithPointsMinorGoal(DaysEntity daysEntity, UsageSummary usageSummary, List<Meal> candidates, List<Meal> result) {
        validateListParametersOrThrow(candidates, result);
        result.clear();

        for (Meal meal : candidates) {
            if (usageSummary.getUsageForMeal(meal)
                    < daysEntity.getGoalForMeal(meal)) {
                result.add(meal);
            }
        }
    }

    public void findMealsWithMinorInterval(DaysEntity daysEntity, List<Meal> candidates,
                                           List<Meal> result) {

        validateListParametersOrThrow(candidates, result);
        result.clear();

        if (!candidates.isEmpty()) {
            result.add(candidates.get(0));
            int minorInterval = daysEntity.getEndTimeForMeal(candidates.get(0)) -
                    daysEntity.getStartTimeForMeal(candidates.get(0));
            for (int i = 1; i < candidates.size(); i++) {
                final int currentInterval = daysEntity.getEndTimeForMeal(candidates.get(i)) -
                        daysEntity.getStartTimeForMeal(candidates.get(i));
                if (currentInterval < minorInterval) {
                    result.clear();
                    result.add(candidates.get(i));
                    minorInterval = currentInterval;
                } else if (currentInterval == minorInterval) {
                    result.add(candidates.get(i));
                }
            }
        }
    }

    public void findMealsStartingEarly(DaysEntity daysEntity, List<Meal> candidates, List<Meal> result) {
        validateListParametersOrThrow(candidates, result);
        result.clear();

        if (!candidates.isEmpty()) {
            result.add(candidates.get(0));
            for (int i = 1; i < candidates.size(); i++) {
                if (daysEntity.getStartTimeForMeal(candidates.get(i))
                        < daysEntity.getStartTimeForMeal(result.get(0))) {
                    result.clear();
                    result.add(candidates.get(i));
                } else if (daysEntity.getStartTimeForMeal(candidates.get(i))
                        == daysEntity.getStartTimeForMeal(result.get(0))) {
                    result.add(candidates.get(i));
                }
            }
        }
    }

    public void findMealsEndingBeforeTime(int time, DaysEntity daysEntity, List<Meal> candidates, List<Meal> result) {
        validateListParametersOrThrow(candidates, result);
        result.clear();

        for (Meal meal : candidates) {
            if (daysEntity.getEndTimeForMeal(meal) < time) {
                result.add(meal);
            }
        }
    }

    public void findClosestMealsEndingBeforeTime(int time, DaysEntity daysEntity, List<Meal> candidates, List<Meal> result) {
        validateListParametersOrThrow(candidates, result);
        List<Meal> tempResult = new ArrayList<>(Meal.sizeOfOnlyMeals);
        findMealsEndingBeforeTime(time, daysEntity, candidates, tempResult);

        result.clear();
        if (!tempResult.isEmpty()) {
            result.add(tempResult.get(0));
            for (int i = 1; i < tempResult.size(); i++) {
                if (daysEntity.getEndTimeForMeal(tempResult.get(i))
                        > daysEntity.getEndTimeForMeal(result.get(0))) {
                    result.clear();
                    result.add(tempResult.get(i));
                } else if (daysEntity.getEndTimeForMeal(tempResult.get(i))
                        == daysEntity.getEndTimeForMeal(result.get(0))) {
                    result.add(tempResult.get(i));
                }
            }
        }
    }

    public void findMealsStartingAfterTime(int time, DaysEntity daysEntity, List<Meal> candidates, List<Meal> result) {
        validateListParametersOrThrow(candidates, result);
        result.clear();

        for (Meal meal : candidates) {
            if (daysEntity.getStartTimeForMeal(meal) > time) {
                result.add(meal);
            }
        }
    }

    public void findClosestMealsStartingAfterTime(int time, DaysEntity daysEntity, List<Meal> candidates, List<Meal> result) {
        validateListParametersOrThrow(candidates, result);
        List<Meal> tempResult = new ArrayList<>(Meal.sizeOfOnlyMeals);
        findMealsStartingAfterTime(time, daysEntity, candidates, tempResult);

        result.clear();
        if (!tempResult.isEmpty()) {
            result.add(tempResult.get(0));
            for (int i = 1; i < tempResult.size(); i++) {
                if (daysEntity.getStartTimeForMeal(tempResult.get(i))
                        < daysEntity.getStartTimeForMeal(result.get(0))) {
                    result.clear();
                    result.add(tempResult.get(i));
                } else if (daysEntity.getStartTimeForMeal(tempResult.get(i))
                        == daysEntity.getStartTimeForMeal(result.get(0))) {
                    result.add(tempResult.get(i));
                }
            }
        }
    }

    public void findClosestNeighborsForTime(int time, DaysEntity daysEntity, List<Meal> candidates, List<Meal> result) {
        /*
            Neighbors are only meals NOT in period with time (ie, meal end time < time or meal
            start time > time. PS: meal start time < meal end time is assured by WeekDayParametersManager)
         */

        validateListParametersOrThrow(candidates, result);
        result.clear();

        int minorDelta = Integer.MAX_VALUE;
        for (Meal meal : candidates) {
            final int startTime = daysEntity.getStartTimeForMeal(meal);
            final int endTime = daysEntity.getEndTimeForMeal(meal);
            boolean isNeighbor = false;
            int currentDelta = -1;
            if (endTime < time) {
                isNeighbor = true;
                currentDelta = time - endTime;
            } else if (startTime > time) {
                isNeighbor = true;
                currentDelta = startTime - time;
            }
            if (isNeighbor) {
                if (currentDelta < minorDelta) {
                    result.clear();
                    result.add(meal);
                    minorDelta = currentDelta;
                } else if (currentDelta == minorDelta) {
                    result.add(meal);
                }
            }
        }
    }

    private void validateListParametersOrThrow(List<Meal> candidates, List<Meal> result) {
        if (candidates == null || result == null) {
            throw new IllegalArgumentException("candidates and result must not null.");
        }
        if (candidates == result) {
            throw new IllegalArgumentException("candidates and result must be different lists.");
        }
    }
}
